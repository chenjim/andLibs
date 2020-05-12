package com.chenjim.andlibs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.RequiresApi;


public class GaussBlur {
    private static final String TAG = "GaussBlur";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void init(Context context) {
        Bitmap bitmapIn = Bitmap.createBitmap(10, 10, Config.ALPHA_8);
        Bitmap bitmapOut = Bitmap.createBitmap(10, 10, Config.ALPHA_8);
        GaussBlur.doBlur(bitmapIn, bitmapOut, 25, context);
        if (bitmapIn != null && !bitmapIn.isRecycled()) {
            bitmapIn.recycle();
            bitmapIn = null;
        }
        if (bitmapOut != null && !bitmapOut.isRecycled()) {
            bitmapOut.recycle();
            bitmapOut = null;
        }
    }

    /**
     * 通过调用系统RenderScript的api的模糊, minSdkVersion 17
     * 首次调用用RenderScript.create会慢，所以要提前初始化{@link #init(Context)}
     * 受到 radius的限制，效果不一定是最佳
     *
     * @param bitmap    source bitmap
     * @param outBitmap out bitmap
     * @param radius    0 < radius <= 25
     * @param context   context
     * @return out bitmap
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap doBlur(Bitmap bitmap, Bitmap outBitmap, float radius, Context context) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        //Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
//        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    /**
     * 先对原图缩放，然后用算法CPU计算模糊
     * 无{@param scaleFacto} 时，对大图的模糊速度会慢
     *
     * @param source      source bitmap
     * @param scaleFactor 大于1，可以加快模糊效果
     * @param radius      模糊半径，与 {@param scaleFactor}配合使用
     * @return 模糊后的bitmap
     */
    public static Bitmap doBlur(Bitmap source, float scaleFactor, int radius) {
        Bitmap overlay = compressBitmap(source, scaleFactor);

        return doBlur(overlay, radius, true);
    }

    /**
     * 对{@param srcBitmap} 模糊处理
     *
     * @param srcBitmap        source bitmap
     * @param radius           模糊半径，
     * @param canReuseInBitmap 传入的bitmap是否可以直接修改其内容
     * @return 模糊后的bitmap
     */
    public static Bitmap doBlur(Bitmap srcBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = srcBitmap;
        } else {
            bitmap = srcBitmap.copy(srcBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int[] vmin = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int[] dv = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * 比例压缩图片
     *
     * @param sourceBitmap 源bitmap
     * @param scaleFactor  大于1，将bitmap缩小
     * @return 缩小scaleFactor倍后的bitmap
     */
    public static Bitmap compressBitmap(Bitmap sourceBitmap, float scaleFactor) {
        //long time0 = System.currentTimeMillis();
        Bitmap overlay = Bitmap.createBitmap((int) (sourceBitmap.getWidth() / scaleFactor),
                (int) (sourceBitmap.getHeight() / scaleFactor), Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        //PTLog.d(TAG, "compress bitmap time00:" + (System.currentTimeMillis() - time0));
        canvas.translate(0, 0);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //PTLog.d(TAG, "compress bitmap time01:" + (System.currentTimeMillis() - time0));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        //PTLog.d(TAG, "compress bitmap time02:" + (System.currentTimeMillis() - time0));
        return overlay;
    }


    /*降低图片对比度达到使图片变暗的效果*/
    private Bitmap darkBitmap(Bitmap srcBitmap) {

        float contrast = (float) (20 / 100.0); //picture contrast
        float offset = (float) 0.0; //picture RGB offset

        int imgHeight, imgWidth;
        imgHeight = srcBitmap.getHeight();
        imgWidth = srcBitmap.getWidth();

        Bitmap dstBitmap = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
        Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{contrast, 0, 0, 0, offset,
                0, contrast, 0, 0, offset,
                0, 0, contrast, 0, offset,
                0, 0, 0, 1, 0});

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(srcBitmap, 0, 0, paint);

        dstBitmap.recycle();
        return bmp;
    }
}

