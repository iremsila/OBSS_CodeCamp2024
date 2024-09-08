package com.iremsilayildirim.homework1.ui.login

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.iremsilayildirim.homework1.R
import kotlin.concurrent.thread

class SeamlessBackgroundSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var bitmap: Bitmap
    private var translateX: Float = 0f
    private var threadRunning = false
    private lateinit var drawThread: Thread

    init {
        holder.addCallback(this)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.paven)
    }

    private fun startAnimation() {
        threadRunning = true
        drawThread = thread(start = true) {
            while (threadRunning) {
                if (holder.surface.isValid) {
                    val canvas = holder.lockCanvas()
                    if (canvas != null) {
                        drawBackground(canvas)
                        holder.unlockCanvasAndPost(canvas)
                    }
                }

                // Move the background leftward continuously
                translateX += 0.75f
                if (translateX >= bitmap.width) {
                    translateX = 0f
                }

                try {
                    Thread.sleep(16) // ~60 FPS for smooth animation
                } catch (e: InterruptedException) {
                    threadRunning = false
                }
            }
        }
    }

    private fun drawBackground(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height

        // Calculate source and destination rectangles for seamless scrolling
        val srcRect1 = Rect(
            translateX.toInt(),
            0,
            (translateX + width).toInt(),
            height
        )
        val dstRect1 = Rect(0, 0, width, height)

        val srcRect2 = Rect(0, 0, translateX.toInt(), height)
        val dstRect2 = Rect((bitmap.width - translateX).toInt(), 0, width, height)

        // Draw first part of the bitmap
        canvas.drawBitmap(bitmap, srcRect1, dstRect1, paint)

        // Draw second part if needed to cover the entire surface view width
        if (translateX > 0) {
            canvas.drawBitmap(bitmap, srcRect2, dstRect2, paint)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startAnimation()
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        // Handle surface changes if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        threadRunning = false
        if (::drawThread.isInitialized && drawThread.isAlive) {
            drawThread.interrupt()
        }
    }
}
