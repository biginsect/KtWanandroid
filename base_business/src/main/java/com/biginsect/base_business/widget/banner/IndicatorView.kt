package com.biginsect.base_business.widget.banner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt


/**
 *@author biginsect
 *Created at 2021/6/4 14:41
 */
class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Indicator {

    enum class IndicatorStyle {
        INDICATOR_CIRCLE,
        INDICATOR_CIRCLE_RECT,
        INDICATOR_BEZIER,
        INDICATOR_DASH,
        INDICATOR_BIG_CIRCLE
    }

    private val mDecelerateInterpolator = DecelerateInterpolator()
    private val mAccelerateInterpolator = AccelerateInterpolator()

    private val mPath = Path()
    private var mOffset = 0.0f
    private var mSelectedPage = 0
    private var mPagerCount = 0
    private var mUnColor = Color.GRAY
    private var mSelectedColor = Color.WHITE

    private val mIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRectF: RectF = RectF()
    private var mParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    )
    private var mIndicatorStyle: IndicatorStyle = IndicatorStyle.INDICATOR_CIRCLE

    /**------------------------**/
    private var mIndicatorRadius = dp2px(3.5f)
    private var mIndicatorRatio = 1.0f
    private var mIndicatorSelectedRadius = dp2px(3.5f)
    private var mIndicatorSelectedRatio = 1.0f
    private var mIndicatorSpacing = dp2px(10.0f)

    /**------------------------**/

    init {
        mParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        mParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        mParams.bottomMargin = dp2px(10.0f).toInt()
    }

    override fun initIndicatorCount(pageCount: Int, currentPage: Int) {
        mPagerCount = pageCount
        visibility = if (pageCount > 1) {
            VISIBLE
        } else {
            GONE
        }
        requestLayout()
    }

    override fun getView(): View {
        return this
    }

    override fun getParams(): RelativeLayout.LayoutParams {
        return mParams
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mSelectedPage = position
        mOffset = positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var result = 0
        when (mode) {
            MeasureSpec.EXACTLY -> result = width
            MeasureSpec.AT_MOST -> {
                val ratioSelectedRadius = getRatioSelectedRadius()
                val ratioRadius = getRatioRadius()
                val diameterDistance =
                    ratioRadius.coerceAtLeast(ratioSelectedRadius) * 2 * mPagerCount
                val spacingDistance = (mPagerCount - 1) * mIndicatorSpacing
                val al = ratioSelectedRadius - ratioRadius
                result =
                    (diameterDistance + spacingDistance + al + paddingLeft + paddingRight).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                val ratioSelectedRadius = getRatioSelectedRadius()
                val ratioRadius = getRatioRadius()
                val diameterDistance =
                    ratioRadius.coerceAtLeast(ratioSelectedRadius) * 2 * mPagerCount
                val spacingDistance = (mPagerCount - 1) * mIndicatorSpacing
                val al = ratioSelectedRadius - ratioRadius
                result =
                    (diameterDistance + spacingDistance + al + paddingLeft + paddingRight).toInt()
            }
        }
        return result
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        var result = 0
        when (mode) {
            MeasureSpec.EXACTLY -> result = height
            MeasureSpec.AT_MOST -> {
                val ratioSelectedRadius = getRatioSelectedRadius()
                val ratioRadius = getRatioRadius()
                val diameterDistance = ratioSelectedRadius.coerceAtLeast(ratioRadius) * 2
                result = (diameterDistance + paddingTop + paddingBottom).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                val ratioSelectedRadius = getRatioSelectedRadius()
                val ratioRadius = getRatioRadius()
                val diameterDistance = ratioSelectedRadius.coerceAtLeast(ratioRadius) * 2
                result = (diameterDistance + paddingTop + paddingBottom).toInt()
            }
        }
        return result
    }

    fun setIndicatorRaius(indicatorRadius: Float): IndicatorView {
        val indicatorRadiusDp = dp2px(mIndicatorRadius)
        if (mIndicatorRadius == mIndicatorSelectedRadius) {
            mIndicatorSelectedRadius = indicatorRadiusDp
        }
        mIndicatorRadius = indicatorRadiusDp
        return this
    }

    fun setIndicatorRatio(indicatorRatio: Float): IndicatorView {
        if (mIndicatorRatio == mIndicatorSelectedRatio) {
            mIndicatorSelectedRatio = indicatorRatio
        }
        mIndicatorRatio = indicatorRatio
        return this
    }

    fun setIndicatorSelectedRadius(indicatorSelectedRadius: Float): IndicatorView {
        mIndicatorSelectedRadius = dp2px(indicatorSelectedRadius)
        return this
    }

    fun setIndicatorSelectedRatio(indicatorSelectedRatio: Float): IndicatorView {
        mIndicatorSelectedRatio = indicatorSelectedRatio
        return this
    }

    fun setIndicatorSpacing(indicatorSpacing: Float): IndicatorView? {
        mIndicatorSpacing = dp2px(indicatorSpacing)
        return this
    }

    fun setIndicatorStyle(indicatorStyle: IndicatorStyle): IndicatorView {
        mIndicatorStyle = indicatorStyle
        return this
    }

    fun setIndicatorColor(@ColorInt indicatorColor: Int): IndicatorView {
        mUnColor = indicatorColor
        return this
    }

    fun setIndicatorSelectorColor(@ColorInt indicatorSelectorColor: Int): IndicatorView {
        mSelectedColor = indicatorSelectorColor
        return this
    }

    fun setParams(params: RelativeLayout.LayoutParams): IndicatorView {
        mParams = params
        return this
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mPagerCount == 0) {
            return
        }
        val midY = height / 2.0f + 0.5f
        when (mIndicatorStyle) {
            IndicatorStyle.INDICATOR_CIRCLE -> drawCircle(canvas, midY)
            IndicatorStyle.INDICATOR_CIRCLE_RECT -> drawCircleRect(canvas, midY)
            IndicatorStyle.INDICATOR_BEZIER -> drawBezier(canvas, midY)
            IndicatorStyle.INDICATOR_DASH -> drawDash(canvas, midY)
            IndicatorStyle.INDICATOR_BIG_CIRCLE -> drawBigCircle(canvas, midY)
        }
    }

    private fun drawCircle(canvas: Canvas?, midY: Float) {
        drawPagerCountCircle(canvas, midY)
        val indicatorStartX = getIndicatorStartX(mSelectedPage)
        val nextIndicatorStartX = getIndicatorStartX((mSelectedPage + 1) % mPagerCount)
        val ratioRadius = getRatioSelectedRadius()
        val left = indicatorStartX - ratioRadius
        val right = indicatorStartX + ratioRadius
        val nextLeft = nextIndicatorStartX - ratioRadius
        val nextRight = nextIndicatorStartX + ratioRadius
        val leftX = left + (nextLeft - left * getInterpolatedOffset())
        val rightX = right + (nextRight - right) * getInterpolatedOffset()
        mRectF.set(leftX, midY - mIndicatorSelectedRadius, rightX, midY + mIndicatorSelectedRadius)
        mIndicatorPaint.color = mSelectedColor
        canvas?.drawRoundRect(
            mRectF,
            mIndicatorSelectedRadius,
            mIndicatorSelectedRadius,
            mIndicatorPaint
        )
    }

    private fun drawCircleRect(canvas: Canvas?, midY: Float) {
        drawPagerCountCircle(canvas, midY)
        val indicatorStartX = getIndicatorStartX(mSelectedPage)
        val ratioRadius = getRatioSelectedRadius()
        val left = indicatorStartX - ratioRadius
        val right = indicatorStartX + ratioRadius
        val offset = getInterpolatedOffset()
        var distance = mIndicatorSpacing + Math.max(getRatioRadius(), ratioRadius) * 2
        val leftX: Float
        val rightX: Float
        if ((mSelectedPage + 1) % mPagerCount == 0) {
            distance *= -mSelectedPage
            leftX = left + Math.max((distance * offset * 2), distance)
            rightX = right + Math.min(distance * (offset - 0.5f) * 2.0f, 0f)
        } else {
            leftX = left + Math.max(distance * (offset - 0.5f) * 2.0f, 0f)
            rightX = right + Math.min((distance * offset * 2), distance)
        }
        mRectF.set(leftX, midY - mIndicatorSelectedRadius, rightX, midY + mIndicatorSelectedRadius)
        mIndicatorPaint.color = mSelectedColor
        canvas?.drawRoundRect(
            mRectF,
            mIndicatorSelectedRadius,
            mIndicatorSelectedRadius,
            mIndicatorPaint
        )
    }

    private fun drawBezier(canvas: Canvas?, midY: Float) {
        drawPagerCountCircle(canvas, midY)
        val indicatorStartX = getIndicatorStartX(mSelectedPage)
        val nextIndicatorStartX = getIndicatorStartX((mSelectedPage + 1) % mPagerCount)
        val leftX =
            indicatorStartX + (nextIndicatorStartX - indicatorStartX) * mAccelerateInterpolator.getInterpolation(
                mOffset
            )
        val rightX =
            indicatorStartX + (nextIndicatorStartX - indicatorStartX) * getInterpolatedOffset()
        val ratioSelectedRadius = getRatioSelectedRadius()
        val minRadius = mIndicatorSelectedRadius * 0.57f
        val minRatioRadius = minRadius * mIndicatorSelectedRatio
        val leftRadius =
            ratioSelectedRadius + (minRatioRadius - ratioSelectedRadius) * getInterpolatedOffset()
        val rightRadius =
            minRatioRadius + (ratioSelectedRadius - minRatioRadius) * mAccelerateInterpolator.getInterpolation(
                mOffset
            )
        val leftTopOrBottomOffset = (mIndicatorSelectedRadius - minRadius) * getInterpolatedOffset()
        val rightTopOrBottomOffset =
            (mIndicatorSelectedRadius - minRadius) * mAccelerateInterpolator.getInterpolation(
                mOffset
            )
        mIndicatorPaint.color = mSelectedColor
        mRectF.set(
            leftX - leftRadius,
            midY - mIndicatorSelectedRadius + leftTopOrBottomOffset,
            leftX + leftRadius,
            midY + mIndicatorSelectedRadius - leftTopOrBottomOffset
        )
        canvas?.drawRoundRect(mRectF, leftRadius, leftRadius, mIndicatorPaint)
        mRectF.set(
            rightX - rightRadius,
            midY - minRadius - rightTopOrBottomOffset,
            rightX + rightRadius,
            midY + minRadius + rightTopOrBottomOffset
        )
        canvas?.drawRoundRect(mRectF, rightRadius, rightRadius, mIndicatorPaint)
        mPath.reset()
        mPath.moveTo(rightX, midY)
        mPath.lineTo(rightX, midY - minRadius - rightTopOrBottomOffset)
        mPath.quadTo(
            rightX + (leftX - rightX) / 2.0f,
            midY,
            leftX,
            midY - mIndicatorSelectedRadius + leftTopOrBottomOffset
        )
        mPath.lineTo(leftX, midY + mIndicatorSelectedRadius - leftTopOrBottomOffset)
        mPath.quadTo(
            rightX + (leftX - rightX) / 2.0f,
            midY,
            rightX,
            midY + minRadius + rightTopOrBottomOffset
        )
        mPath.close()
        canvas?.drawPath(mPath, mIndicatorPaint)
    }

    private fun drawDash(canvas: Canvas?, midY: Float) {
        val offset = getInterpolatedOffset()
        val ratioSelectedRadius = getRatioSelectedRadius()
        val ratioIndicatorRadius = getRatioRadius()
        val distance = ratioSelectedRadius - ratioIndicatorRadius
        val distanceOffset = distance * offset
        val nextPage = (mSelectedPage + 1) % mPagerCount
        val isNextFirst = nextPage == 0
        mIndicatorPaint.color = mUnColor
        for (i in 0 until mPagerCount) {
            var startCx: Float = getIndicatorStartX(i)
            if (isNextFirst) startCx += distanceOffset
            val left: Float = startCx - ratioIndicatorRadius
            val top: Float = midY - mIndicatorRadius
            val right: Float = startCx + ratioIndicatorRadius
            val bottom: Float = midY + mIndicatorRadius
            if (mSelectedPage + 1 <= i) {
                mRectF.set(left + distance, top, right + distance, bottom)
            } else {
                mRectF.set(left, top, right, bottom)
            }
            canvas!!.drawRoundRect(mRectF, mIndicatorRadius, mIndicatorRadius, mIndicatorPaint)
        }
        mIndicatorPaint.color = mSelectedColor

        if (offset < 0.99f) {
            var leftX: Float = getIndicatorStartX(mSelectedPage) - ratioSelectedRadius
            if (isNextFirst) leftX += distanceOffset
            val rightX: Float = leftX + ratioSelectedRadius * 2 + distance - distanceOffset
            mRectF.set(
                leftX,
                midY - mIndicatorSelectedRadius,
                rightX,
                midY + mIndicatorSelectedRadius
            )
            canvas?.drawRoundRect(
                mRectF,
                mIndicatorSelectedRadius,
                mIndicatorSelectedRadius,
                mIndicatorPaint
            )
        }
        if (offset > 0.1f) {
            val nextRightX: Float =
                getIndicatorStartX(nextPage) + ratioSelectedRadius + if (isNextFirst) distanceOffset else distance
            val nextLeftX: Float = nextRightX - ratioSelectedRadius * 2 - distanceOffset
            mRectF.set(
                nextLeftX,
                midY - mIndicatorSelectedRadius,
                nextRightX,
                midY + mIndicatorSelectedRadius
            )
            canvas?.drawRoundRect(
                mRectF,
                mIndicatorSelectedRadius,
                mIndicatorSelectedRadius,
                mIndicatorPaint
            )
        }
    }

    private fun drawBigCircle(canvas: Canvas?, midY: Float) {
        drawPagerCountCircle(canvas, midY)
        val offset = getInterpolatedOffset()
        val indicatorStartX = getIndicatorStartX(mSelectedPage)
        val nextIndicatorStartX = getIndicatorStartX((mSelectedPage + 1) % mPagerCount)
        val ratioRadius = getRatioRadius()
        val maxRadius = mIndicatorSelectedRadius
        val maxRatioRadius = maxRadius * mIndicatorSelectedRatio
        val leftRadius = maxRatioRadius - ((maxRatioRadius - ratioRadius) * offset)
        val rightRadius = ratioRadius - ((maxRatioRadius - ratioRadius) * offset)
        val topOrBottomOffset = (maxRadius - mIndicatorRadius) * offset
        mIndicatorPaint.color = mSelectedColor
        if (offset < 0.99f) {
            val top = midY - maxRadius + topOrBottomOffset
            val left = indicatorStartX - leftRadius
            val right = indicatorStartX + leftRadius
            val bottom = midY + maxRadius - topOrBottomOffset
            mRectF.set(left, top, right, bottom)
            canvas?.drawRoundRect(mRectF, leftRadius, leftRadius, mIndicatorPaint)
        }
        if (offset > 1.0f) {
            val top = midY - mIndicatorRadius - topOrBottomOffset
            val left = nextIndicatorStartX - rightRadius
            val right = nextIndicatorStartX + rightRadius
            val bottom = midY + mIndicatorRadius + topOrBottomOffset
            mRectF.set(left, top, right, bottom)
            canvas?.drawRoundRect(mRectF, rightRadius, rightRadius, mIndicatorPaint)
        }
    }

    private fun drawPagerCountCircle(canvas: Canvas?, midY: Float) {
        mIndicatorPaint.color = mUnColor
        for (i in 0..mPagerCount) {
            val startCx = getIndicatorStartX(i)
            val ratioIndicatorRadius = getRatioRadius()
            val left = startCx - ratioIndicatorRadius
            val top = midY - mIndicatorRadius
            val right = startCx + ratioIndicatorRadius
            val bottom = midY + mIndicatorRadius
            mRectF.set(left, top, right, bottom)
            canvas?.drawRoundRect(mRectF, mIndicatorRatio, mIndicatorRatio, mIndicatorPaint)
        }
    }

    private fun getIndicatorStartX(index: Int): Float {
        val ratioRadius = getRatioRadius()
        val rationSelectedRadius = getRatioSelectedRadius()
        val ratioIndicatorRadius = Math.max(ratioRadius, rationSelectedRadius)
        val centerSpacing = ratioIndicatorRadius * 2.0f + mIndicatorSpacing
        val centerX = ratioIndicatorRadius + paddingLeft + centerSpacing * index

        val otherOffset = if (mIndicatorStyle == IndicatorStyle.INDICATOR_DASH) {
            0f
        } else {
            (ratioIndicatorRadius - ratioRadius) / 2.0f
        }
        return centerX + otherOffset
    }

    private fun getRatioSelectedRadius() = mIndicatorSelectedRadius * mIndicatorSelectedRatio

    private fun getRatioRadius() = mIndicatorRadius * mIndicatorRatio

    private fun getInterpolatedOffset() = mDecelerateInterpolator.getInterpolation(mOffset)

    private fun dp2px(dp: Float): Float {
        return (dp * resources.displayMetrics.density)
    }
}