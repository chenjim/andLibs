/*
 * Copyright (C) 2014 lafosca Studio, SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chenjim.andlibs.face

import android.content.Context
import android.graphics.*
import android.media.FaceDetector
import android.util.Log
import com.chenjim.andlibs.BuildConfig
import com.chenjim.andlibs.face.FaceCropperUtils.forceConfig565
import com.chenjim.andlibs.face.FaceCropperUtils.forceEvenBitmapSize

/**
 * An utility that crops faces from bitmaps.
 * It support multiple faces (max 8 by default) and crop them all, fitted in the same image.
 */
class FaceCropper {
    enum class SizeMode {
        FaceMarginPx, EyeDistanceFactorMargin
    }

    var faceMinSize = MIN_FACE_SIZE
    private var mFaceMarginPx = 100
    private var mEyeDistanceFactorMargin = 2f
    var maxFaces = MAX_FACES
    var sizeMode = SizeMode.EyeDistanceFactorMargin
        private set
    var isDebug = false
    private var mDebugPainter: Paint? = null
    private var mDebugAreaPainter: Paint? = null

    constructor() {
        initPaints()
    }

    constructor(faceMarginPx: Int) {
        mFaceMarginPx = faceMarginPx;
        initPaints()
    }

    constructor(eyesDistanceFactorMargin: Float) {
        eyeDistanceFactorMargin = eyesDistanceFactorMargin
        initPaints()
    }

    private fun initPaints() {
        mDebugPainter = Paint()
        mDebugPainter!!.color = Color.RED
        mDebugPainter!!.alpha = 80
        mDebugAreaPainter = Paint()
        mDebugAreaPainter!!.color = Color.GREEN
        mDebugAreaPainter!!.alpha = 80
    }

    var faceMarginPx: Int
        get() = mFaceMarginPx
        set(faceMarginPx) {
            mFaceMarginPx = faceMarginPx
            sizeMode = SizeMode.FaceMarginPx
        }

    var eyeDistanceFactorMargin: Float
        get() = mEyeDistanceFactorMargin
        set(eyeDistanceFactorMargin) {
            mEyeDistanceFactorMargin = eyeDistanceFactorMargin
            sizeMode = SizeMode.EyeDistanceFactorMargin
        }

    protected fun cropFace(original: Bitmap?, debug: Boolean): CropResult {
        var fixedBitmap = forceEvenBitmapSize(original!!)
        fixedBitmap = forceConfig565(fixedBitmap)
        val mutableBitmap = fixedBitmap.copy(Bitmap.Config.RGB_565, true)
        if (fixedBitmap != mutableBitmap) {
            fixedBitmap.recycle()
        }
        val faceDetector = FaceDetector(
                mutableBitmap.width, mutableBitmap.height,
                maxFaces)
        val faces = arrayOfNulls<FaceDetector.Face>(maxFaces)

        // The bitmap must be in 565 format (for now).
        val faceCount = faceDetector.findFaces(mutableBitmap, faces)
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "$faceCount faces found")
        }
        if (faceCount == 0) {
            return CropResult(mutableBitmap)
        }
        var initX = mutableBitmap.width
        var initY = mutableBitmap.height
        var endX = 0
        var endY = 0
        val centerFace = PointF()
        val canvas = Canvas(mutableBitmap)
        canvas.drawBitmap(mutableBitmap, Matrix(), null)

        // Calculates minimum box to fit all detected faces
        for (i in 0 until faceCount) {
            val face = faces[i]

            // Eyes distance * 3 usually fits an entire face
            var faceSize = (face!!.eyesDistance() * 3).toInt()
            if (SizeMode.FaceMarginPx == sizeMode) {
                faceSize += mFaceMarginPx * 2 // *2 for top and down/right and left effect
            } else if (SizeMode.EyeDistanceFactorMargin == sizeMode) {
                faceSize += (face.eyesDistance() * mEyeDistanceFactorMargin.toInt()).toInt()
            }
            faceSize = Math.max(faceSize, faceMinSize)
            face.getMidPoint(centerFace)
            if (debug) {
                canvas.drawPoint(centerFace.x, centerFace.y, mDebugPainter!!)
                canvas.drawCircle(centerFace.x, centerFace.y, face.eyesDistance() * 1.5f, mDebugPainter!!)
            }
            var tInitX = (centerFace.x - faceSize / 2).toInt()
            var tInitY = (centerFace.y - faceSize / 2).toInt()
            tInitX = Math.max(0, tInitX)
            tInitY = Math.max(0, tInitY)
            var tEndX = tInitX + faceSize
            var tEndY = tInitY + faceSize
            tEndX = Math.min(tEndX, mutableBitmap.width)
            tEndY = Math.min(tEndY, mutableBitmap.height)
            initX = Math.min(initX, tInitX)
            initY = Math.min(initY, tInitY)
            endX = Math.max(endX, tEndX)
            endY = Math.max(endY, tEndY)
        }
        var sizeX = endX - initX
        var sizeY = endY - initY
        if (sizeX + initX > mutableBitmap.width) {
            sizeX = mutableBitmap.width - initX
        }
        if (sizeY + initY > mutableBitmap.height) {
            sizeY = mutableBitmap.height - initY
        }
        val init = Point(initX, initY)
        val end = Point(initX + sizeX, initY + sizeY)
        return CropResult(mutableBitmap, init, end)
    }

    @Deprecated("")
    fun cropFace(ctx: Context, resDrawable: Int): Bitmap {
        return getCroppedImage(ctx, resDrawable)
    }

    @Deprecated("")
    fun cropFace(bitmap: Bitmap?): Bitmap {
        return getCroppedImage(bitmap)
    }

    fun getFullDebugImage(ctx: Context, resDrawable: Int): Bitmap {
        // Set internal configuration to RGB_565
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
        return getFullDebugImage(BitmapFactory.decodeResource(ctx.resources, resDrawable, bitmapOptions))
    }

    fun getFullDebugImage(bitmap: Bitmap?): Bitmap {
        val result = cropFace(bitmap, true)
        val canvas = Canvas(result.bitmap)
        canvas.drawBitmap(result.bitmap, Matrix(), null)
        canvas.drawRect(result.init.x.toFloat(),
                result.init.y.toFloat(),
                result.end.x.toFloat(),
                result.end.y.toFloat(),
                mDebugAreaPainter!!)
        return result.bitmap
    }

    fun getCroppedImage(ctx: Context, resDrawable: Int): Bitmap {
        // Set internal configuration to RGB_565
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
        return getCroppedImage(BitmapFactory.decodeResource(ctx.resources, resDrawable, bitmapOptions))
    }

    fun getCroppedImage(bitmap: Bitmap?): Bitmap {
        val result = cropFace(bitmap, isDebug)
        val croppedBitmap = Bitmap.createBitmap(result.bitmap,
                result.init.x,
                result.init.y,
                result.end.x - result.init.x,
                result.end.y - result.init.y)
        if (result.bitmap != croppedBitmap) {
            result.bitmap.recycle()
        }
        return croppedBitmap
    }

    protected inner class CropResult {
        var bitmap: Bitmap
        var init: Point
        var end: Point

        constructor(bitmap: Bitmap, init: Point, end: Point) {
            this.bitmap = bitmap
            this.init = init
            this.end = end
        }

        constructor(bitmap: Bitmap) {
            this.bitmap = bitmap
            init = Point(0, 0)
            end = Point(bitmap.width, bitmap.height)
        }

    }

    companion object {
        private val LOG_TAG = FaceCropper::class.java.simpleName
        private const val MAX_FACES = 8
        private const val MIN_FACE_SIZE = 200
    }
}