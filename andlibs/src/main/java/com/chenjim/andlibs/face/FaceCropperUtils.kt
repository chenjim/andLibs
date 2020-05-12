/*
 * Copyright 2014 ignasi
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

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Created by ignasi on 28/03/14.
 */
object FaceCropperUtils {
    @JvmStatic
    fun forceEvenBitmapSize(original: Bitmap): Bitmap {
        var width = original.width
        var height = original.height
        if (width % 2 == 1) {
            width++
        }
        if (height % 2 == 1) {
            height++
        }
        var fixedBitmap = original
        if (width != original.width || height != original.height) {
            fixedBitmap = Bitmap.createScaledBitmap(original, width, height, false)
        }
        if (fixedBitmap != original) {
            original.recycle()
        }
        return fixedBitmap
    }

    @JvmStatic
    fun forceConfig565(original: Bitmap): Bitmap {
        var convertedBitmap = original
        if (original.config != Bitmap.Config.RGB_565) {
            convertedBitmap = Bitmap.createBitmap(original.width, original.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(convertedBitmap)
            val paint = Paint()
            paint.color = Color.BLACK
            canvas.drawBitmap(original, 0f, 0f, paint)
            if (convertedBitmap != original) {
                original.recycle()
            }
        }
        return convertedBitmap
    }
}