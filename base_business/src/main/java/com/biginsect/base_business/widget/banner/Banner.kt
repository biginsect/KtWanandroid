package com.biginsect.base_business.widget.banner

import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import android.widget.RelativeLayout
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.InvocationTargetException


/**
 *@author biginsect
 *Created at 2021/6/7 16:36
 */
class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_AUTO_TIME = 2500L
        const val DEFAULT_PAGER_DURATION = 800L
        const val NORMAL_COUNT = 2
    }

    private var changeCallback: ViewPager2.OnPageChangeCallback? = null
    private val compositePageTransformer = CompositePageTransformer()
    private var adapterWrapper: BannerAdapterWrapper? = null
    private var viewPager2: ViewPager2 = ViewPager2(context)
    private var indicator: Indicator? = null
    private var isAutoPlay = true
    private var isBeginPagerChange = true
    private var isTaskPostDelayed = false
    private var autoTurningTime = DEFAULT_AUTO_TIME
    private var pagerScrollDuration = DEFAULT_PAGER_DURATION
    private var needPage = NORMAL_COUNT
    private var sidePage = needPage / NORMAL_COUNT
    private var tempPosition = 0

    private var startX = 0f
    private var startY = 0f
    private var lastX = 0f
    private var lastY = 0f
    private val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop.shr(1)


    init {
        viewPager2.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewPager2.setPageTransformer(compositePageTransformer)
        viewPager2.registerOnPageChangeCallback(OnPageChangeCallback())
        adapterWrapper = BannerAdapterWrapper()
        viewPager2.adapter = adapterWrapper
        setOffscreenPageLimit(1)
        initViewPagerScrollProxy()
        addView(viewPager2)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isAutoPlay()) {
            startTurning()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isAutoPlay()) {
            stopTurning()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isAutoPlay() && viewPager2.isUserInputEnabled) {
            if (event?.action == MotionEvent.ACTION_UP or MotionEvent.ACTION_CANCEL or MotionEvent.ACTION_OUTSIDE) {
                startTurning()
            } else if (event?.action == MotionEvent.ACTION_DOWN) {
                stopTurning()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.rawX
                startX = lastX
                lastY = ev.rawY
                startY = lastY
            }
            MotionEvent.ACTION_MOVE -> {
                lastX = ev.rawX
                lastY = ev.rawY
                if (viewPager2.isUserInputEnabled) {
                    val distanceX = Math.abs(lastX - startX)
                    val distanceY = Math.abs(lastY - startY)
                    val disallowIntercept =
                        if (viewPager2.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                            distanceX > scaledTouchSlop && distanceX > distanceY
                        } else {
                            distanceY > scaledTouchSlop && distanceY > distanceX
                        }
                    parent.requestDisallowInterceptTouchEvent(disallowIntercept)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                return Math.abs(lastX - startX) > scaledTouchSlop || Math.abs(lastY - startY) > scaledTouchSlop
            }
            MotionEvent.ACTION_UP -> {
                return Math.abs(lastX - startX) > scaledTouchSlop || Math.abs(lastY - startY) > scaledTouchSlop
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun setPageMargin(multiWidth: Int, pageMargin: Int): Banner {
        return setPageMargin(multiWidth, multiWidth, pageMargin)
    }

    fun setPageMargin(tlWidth: Int, brWidth: Int, pageMargin: Int): Banner {
        val realMargin = if (pageMargin < 0) {
            0
        } else {
            pageMargin
        }
        addPageTransformer(MarginPageTransformer(realMargin))
        val recyclerView = viewPager2.getChildAt(0) as RecyclerView
        if (viewPager2.orientation == ViewPager2.ORIENTATION_VERTICAL) {
            recyclerView.setPadding(
                viewPager2.paddingLeft,
                tlWidth + Math.abs(realMargin),
                viewPager2.paddingRight,
                brWidth + Math.abs(realMargin)
            )
        } else {
            recyclerView.setPadding(
                tlWidth + Math.abs(realMargin),
                viewPager2.paddingTop,
                brWidth + Math.abs(realMargin),
                viewPager2.paddingBottom
            )
        }
        recyclerView.clipToPadding = false
        needPage = NORMAL_COUNT + NORMAL_COUNT
        sidePage = NORMAL_COUNT
        return this
    }

    fun addPageTransformer(transformer: ViewPager2.PageTransformer): Banner {
        compositePageTransformer.addTransformer(transformer)
        return this
    }

    fun setAutoTurningTime(autoTurningTime: Long): Banner {
        this.autoTurningTime = autoTurningTime
        return this
    }

    fun setOuterPageChangeListener(listener: ViewPager2.OnPageChangeCallback?): Banner {
        changeCallback = listener
        return this
    }

    fun setOffscreenPageLimit(limit: Int): Banner {
        viewPager2.offscreenPageLimit = limit
        return this
    }

    fun setPagerScrollDuration(pagerScrollDuration: Long): Banner {
        this.pagerScrollDuration = pagerScrollDuration
        return this
    }

    fun setOrientation(@ViewPager2.Orientation orientation: Int): Banner {
        viewPager2.orientation = orientation
        return this
    }

    fun addItemDecoration(decoration: RecyclerView.ItemDecoration): Banner {
        viewPager2.addItemDecoration(decoration)
        return this
    }

    fun addItemDecoration(decoration: RecyclerView.ItemDecoration, index: Int): Banner {
        viewPager2.addItemDecoration(decoration, index)
        return this
    }

    fun setAutoPlay(autoPlay: Boolean): Banner {
        isAutoPlay = autoPlay
        if (isAutoPlay && getRealCount() > 1) {
            startTurning()
        }

        return this
    }

    fun isAutoPlay(): Boolean {
        return isAutoPlay && getRealCount() > 1
    }

    fun setIndicator(indicator: Indicator?) {
        setIndicator(indicator, true)
    }

    fun setIndicator(indicator: Indicator?, attachToRoot: Boolean): Banner {
        if (this.indicator != null) {
            removeView(this.indicator?.getView())
        }
        if (indicator != null) {
            this.indicator = indicator
            if (attachToRoot) {
                addView(this.indicator?.getView(), this.indicator?.getParams())
            }
        }

        return this
    }

    fun setRoundCorners(radius: Float): Banner {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline?) {
                outline?.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
        clipToOutline = true
        return this
    }

    fun setCurrentItem(position: Int) {
        setCurrentItem(position, true)
    }

    fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        tempPosition = position + sidePage
        viewPager2.setCurrentItem(tempPosition, smoothScroll)
    }

    fun getCurrentPager(): Int {
        val position = toRealPosition(tempPosition)
        return Math.max(position, 0)
    }

    fun getViewPager2(): ViewPager2 {
        return viewPager2
    }

    fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>? {
        return adapterWrapper?.adapter
    }

    fun startTurning() {
        stopTurning()
        postDelayed(task, autoTurningTime)
        isTaskPostDelayed = true
    }

    fun stopTurning() {
        if (isTaskPostDelayed) {
            removeCallbacks(task)
            isTaskPostDelayed = false
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        setAdapter(adapter, 0)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, startPosition: Int) {
        adapterWrapper?.registerAdapter(adapter)
        startPage(startPosition)
    }

    private fun startPage(startPosition: Int) {
        if (sidePage == NORMAL_COUNT) {
            viewPager2.adapter = adapterWrapper
        } else {
            adapterWrapper?.notifyDataSetChanged()
        }
        setCurrentItem(startPosition, false);
        indicator?.initIndicatorCount(getRealCount(), getCurrentPager());
        if (isAutoPlay()) {
            startTurning()

        }
    }

    private fun getRealCount(): Int {
        return adapterWrapper?.getRealCount() ?: 0
    }

    private fun toRealPosition(position: Int): Int {
        var realPosition = 0
        if (getRealCount() > 1) {
            realPosition = (position - sidePage) % getRealCount()
        }
        if (realPosition < 0) {
            realPosition += getRealCount()
        }

        return realPosition
    }

    private val task = object : Runnable {
        override fun run() {
            if (isAutoPlay()) {
                tempPosition++
                if (tempPosition == getRealCount() + sidePage + 1) {
                    isBeginPagerChange = false
                    viewPager2.setCurrentItem(sidePage, false)
                    post(this)
                } else {
                    isBeginPagerChange = true
                    viewPager2.currentItem = tempPosition
                    postDelayed(this, autoTurningTime)
                }
            }
        }
    }

    inner class OnPageChangeCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val realPosition = toRealPosition(position)
            changeCallback?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
            indicator?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            if (getRealCount() > 1) {
                tempPosition = position
            }
            if (isBeginPagerChange) {
                val realPosition = toRealPosition(position)
                changeCallback?.onPageSelected(realPosition)
                indicator?.onPageSelected(realPosition)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                when (tempPosition) {
                    sidePage - 1 -> {
                        isBeginPagerChange = false
                        viewPager2.setCurrentItem(getRealCount() + tempPosition, false)
                    }
                    getRealCount() + sidePage -> {
                        isBeginPagerChange = false
                        viewPager2.setCurrentItem(sidePage, false)
                    }
                    else -> isBeginPagerChange = true
                }
            }
            changeCallback?.onPageScrollStateChanged(state)
            indicator?.onPageScrollStateChanged(state)
        }
    }

    inner class BannerAdapterWrapper : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return adapter!!.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            adapter!!.onBindViewHolder(holder, position)
        }

        override fun getItemViewType(position: Int): Int {
            return adapter!!.getItemViewType(position)
        }

        override fun getItemCount() = if (getRealCount() > 1) {
            getRealCount() + needPage
        } else {
            getRealCount()
        }


        override fun getItemId(position: Int): Long {
            return adapter!!.getItemId(position)
        }

        fun getRealCount(): Int {
            return adapter?.itemCount ?: 0
        }

        fun registerAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
            if (this.adapter != null) {
                adapter.unregisterAdapterDataObserver(mItemDataSetChangeObserver)
            }
            this.adapter = adapter
            adapter.registerAdapterDataObserver(mItemDataSetChangeObserver)
        }
    }

    private val mItemDataSetChangeObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart > 1) {
                onChanged()
            }
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }

        override fun onChanged() {
            startPage(getCurrentPager())
        }
    }

    inner class ProxyLayoutManager(
        context: Context,
        private val layoutManager: LinearLayoutManager
    ) : LinearLayoutManager(context, layoutManager.orientation, false) {

        override fun performAccessibilityAction(
            recycler: RecyclerView.Recycler,
            state: RecyclerView.State,
            action: Int,
            args: Bundle?
        ): Boolean {
            return layoutManager.performAccessibilityAction(recycler, state, action, args)
        }

        override fun onInitializeAccessibilityNodeInfo(
            recycler: RecyclerView.Recycler,
            state: RecyclerView.State,
            info: AccessibilityNodeInfoCompat
        ) {
            layoutManager.onInitializeAccessibilityNodeInfo(recycler, state, info)
        }

        override fun requestChildRectangleOnScreen(
            parent: RecyclerView,
            child: View,
            rect: Rect,
            immediate: Boolean,
            focusedChildVisible: Boolean
        ): Boolean {
            return layoutManager.requestChildRectangleOnScreen(
                parent,
                child,
                rect,
                immediate,
                focusedChildVisible
            )
        }

        override fun calculateExtraLayoutSpace(
            state: RecyclerView.State,
            extraLayoutSpace: IntArray
        ) {
            try {
                val method = layoutManager::class.java.getDeclaredMethod(
                    "calculateExtraLayoutSpace",
                    state::class.java,
                    extraLayoutSpace::class.java
                )
                method.isAccessible = true
                method.invoke(layoutManager, state, extraLayoutSpace)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }

        override fun smoothScrollToPosition(
            recyclerView: RecyclerView?,
            state: RecyclerView.State?,
            position: Int
        ) {
            val smoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
                override fun calculateTimeForDeceleration(dx: Int): Int {
                    return (pagerScrollDuration * (1 - 0.3356)).toInt()
                }
            }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }

    private fun initViewPagerScrollProxy() {
        try {
            val recyclerView = viewPager2.getChildAt(0) as RecyclerView
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val proxyLayoutManager = ProxyLayoutManager(context, layoutManager)
            recyclerView.layoutManager = proxyLayoutManager

            val mRecyclerView =
                RecyclerView.LayoutManager::class.java.getDeclaredField("mRecyclerView")
            mRecyclerView.isAccessible = true
            mRecyclerView.set(layoutManager, recyclerView)

            val mLayoutMangerField = ViewPager2::class.java.getDeclaredField("mLayoutManager")
            mLayoutMangerField.isAccessible = true
            mLayoutMangerField.set(viewPager2, proxyLayoutManager)

            val pageTransformerAdapterField =
                ViewPager2::class.java.getDeclaredField("mPageTransformerAdapter")
            pageTransformerAdapterField.isAccessible = true
            val mPageTransformerAdapter = pageTransformerAdapterField.get(viewPager2)
            if (mPageTransformerAdapter != null) {
                val aClass = mPageTransformerAdapter.javaClass
                val o = aClass.getDeclaredField("mLayoutManager")
                o.isAccessible = true
                o.set(mPageTransformerAdapter, proxyLayoutManager)
            }
            val scrollEventAdapterField =
                ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
            scrollEventAdapterField.isAccessible = true
            val mScrollEventAdapter = scrollEventAdapterField.get(viewPager2)
            if (mScrollEventAdapter != null) {
                val aClass = mScrollEventAdapter.javaClass
                val o = aClass.getDeclaredField("mLayoutManager")
                o.isAccessible = true
                o.set(mScrollEventAdapter, proxyLayoutManager)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}