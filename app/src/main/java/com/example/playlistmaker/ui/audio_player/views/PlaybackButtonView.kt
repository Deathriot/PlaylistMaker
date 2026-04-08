package com.example.playlistmaker.ui.audio_player.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var isPlaying = false

    private val imagePlay: Bitmap?
    private val imagePause: Bitmap?
    private val minWidth: Int
    private val minHeight: Int

    private var rect = RectF(0f, 0f, 0f, 0f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePlay = getDrawable(R.styleable.PlaybackButtonView_imagePlay)?.toBitmap()
                imagePause = getDrawable(R.styleable.PlaybackButtonView_imagePause)?.toBitmap()

                minWidth = imagePlay?.width
                    ?: throw IllegalArgumentException("Для кнопки проигрывателя не указана картинка play")
                minHeight = imagePlay.height
            } finally {
                recycle()
            }
        }
    }

    fun changeState(newState: Boolean) {
        if (isPlaying != newState) {
            isPlaying = newState
            invalidate()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                isPlaying = !isPlaying
                performClick()
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        if (isPlaying) {
            imagePause?.let {
                canvas.drawBitmap(imagePause, null, rect, null)
            }
        } else {
            imagePlay?.let {
                canvas.drawBitmap(imagePlay, null, rect, null)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val contentWidth = when (widthMode) {
            MeasureSpec.UNSPECIFIED -> minWidth

            MeasureSpec.EXACTLY -> widthSize

            MeasureSpec.AT_MOST -> minWidth

            else -> error("Неизвестный режим ширины ($widthMode)")
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val contentHeight = when (heightMode) {

            MeasureSpec.UNSPECIFIED -> minHeight

            MeasureSpec.EXACTLY -> heightSize


            MeasureSpec.AT_MOST -> minWidth

            else -> error("Неизвестный режим высоты ($heightMode)")
        }

        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }
}