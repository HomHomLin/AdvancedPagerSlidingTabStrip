package com.lhh.apst.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by linhomhom on 2015/9/22.
 */
public class CustomPagerSlidingTabStrip extends HorizontalScrollView {

    public interface CustomTabProvider{
        public View getSelectTabView(int position, View convertView);
        public View getDisSelectTabView(int position, View convertView);
    }
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private boolean checkedTabWidths = false;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    private boolean shouldExpand = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 3;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 24;
    private int tabPaddingTopBottom = 0;
    private int dividerWidth = 1;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.psts_background_tab;
//    private int tabBackgroundResId;

    private Locale locale;

    public CustomPagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public CustomPagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = null;

        // get custom attrs


        a = context.obtainStyledAttributes(attrs, R.styleable.AdvancedPagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.AdvancedPagerSlidingTabStrip_apTabIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.AdvancedPagerSlidingTabStrip_apTabUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.AdvancedPagerSlidingTabStrip_apTabDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.AdvancedPagerSlidingTabStrip_apTabIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.AdvancedPagerSlidingTabStrip_apTabUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.AdvancedPagerSlidingTabStrip_apTabDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.AdvancedPagerSlidingTabStrip_apTabPaddingLeftRight, tabPadding);
        tabPaddingTopBottom = a.getDimensionPixelSize(R.styleable.AdvancedPagerSlidingTabStrip_apTabPaddingTopBottom, tabPaddingTopBottom);
        tabBackgroundResId = a.getResourceId(R.styleable.AdvancedPagerSlidingTabStrip_apTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.AdvancedPagerSlidingTabStrip_apTabShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.AdvancedPagerSlidingTabStrip_apTabScrollOffset, scrollOffset);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        if(mDisSelectTabViewCache == null){
            mDisSelectTabViewCache = new SparseArray<>();
        }
        if(mSelectTabViewCache == null){
            mSelectTabViewCache = new SparseArray<>();
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.addOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof CustomTabProvider) {
                View view = ((CustomTabProvider) pager.getAdapter()).getSelectTabView(i, getSelectTabView(i));
                setSelectTabView(i, view);
                updateViewStyle(view);
                addTab(i, view);
            }

        }

//        updateTabStyles();

        checkedTabWidths = false;

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

        pageListener.onPageSelected(0);//default

    }

    private PageOnClickListener mPageOnClickListener;

    private void addTab(final int position, View view){
        if(mPageOnClickListener == null){
            mPageOnClickListener = new PageOnClickListener();
        }
        view.setTag(R.id.tag_position, position);
        view.setOnClickListener(mPageOnClickListener);
        tabsContainer.addView(view);
    }

    public View getTabAt(int pos) {
        if (pos >= tabsContainer.getChildCount()) {
            throw new IllegalStateException("pos is too big.");
        }
        return tabsContainer.getChildAt(pos);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            //v.setLayoutParams(defaultTabLayoutParams);
            //my modify
            updateViewStyle(v);
        }

    }

    public void updateViewStyle(View v){
        v.setLayoutParams(expandedTabLayoutParams);
        v.setBackgroundResource(tabBackgroundResId);
        if (shouldExpand) {
            v.setPadding(0, 0, 0, 0);
        } else {
            v.setPadding(tabPadding, tabPaddingTopBottom, tabPadding, tabPaddingTopBottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!shouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    tabsContainer.getChildAt(i).setLayoutParams(expandedTabLayoutParams);
                }
            }

            checkedTabWidths = true;
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        drawTabNormalMode(canvas);
    }

    private void drawTabNormalMode(Canvas canvas){
        final int height = getHeight();

        // 设置提示下划线的颜色

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);

        }

        //绘制提示下划线
        canvas.drawRect(lineLeft , height - indicatorHeight, lineRight , height, rectPaint);

        // 绘制下划线

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // 绘制分割线

        dividerPaint.setColor(dividerColor);

        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {

            setSelectItem(position);

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    private SparseArray<View> mDisSelectTabViewCache;//缓存

    private View getDisSelectTabView(int position){
        return mDisSelectTabViewCache.get(position);
    }

    private void setDisSelectTabView(int position , View view){
        mDisSelectTabViewCache.put(position,view);
    }

    private SparseArray<View> mSelectTabViewCache;//缓存

    private View getSelectTabView(int position){
        return mSelectTabViewCache.get(position);
    }

    private void setSelectTabView( int position ,View view){
        mSelectTabViewCache.put(position,view);
    }

    class PageOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int tag = (int)view.getTag(R.id.tag_position);
            scrollToChild(tag, 0);
            pager.setCurrentItem(tag);
        }
    }

    public void setSelectItem(int position){
        if (!(pager.getAdapter() instanceof CustomTabProvider)) {
            return;
        }
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {

            tabsContainer.removeViewAt(i);

            View view = null;

            if (i == position) {
                view = ((CustomTabProvider) pager.getAdapter()).getSelectTabView(i, getSelectTabView(i));
                setSelectTabView(i, view);

            } else {
                view = ((CustomTabProvider) pager.getAdapter()).getDisSelectTabView(i, getDisSelectTabView(i));
                setDisSelectTabView(i, view);
            }

            view.setTag(R.id.tag_position, i);

            if(mPageOnClickListener == null){
                mPageOnClickListener = new PageOnClickListener();
            }

            view.setOnClickListener(mPageOnClickListener);

            tabsContainer.addView(view, i);

            updateViewStyle(view);
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
