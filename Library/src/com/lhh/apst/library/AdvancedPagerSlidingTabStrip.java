package com.lhh.apst.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by linhomhom on 2015/8/10.
 */
public class AdvancedPagerSlidingTabStrip extends HorizontalScrollView {

    public interface LayoutProvider {
        public float getPageWeight(int position);
        public int[] getPageRule(int position);
        public Margins getPageMargins(int position);
    }

    public  interface TipsProvider {
        public int[] getTipsRule(int position);
        public Margins getTipsMargins(int position);
        public Drawable getTipsDrawable(int position);
    }

    public interface IconTabProvider {
        public <T extends Object> T getPageIcon(int position);
        public <T extends Object> T getPageSelectIcon(int position);
        public Rect getPageIconBounds(int position);
    }

    public interface ViewTabProvider{
//        public View onCreateIconView(int position, View view, ViewGroup parent);
        public View onSelectIconView(int position, View view, ViewGroup parent);
        public View onIconView(int position, View view, ViewGroup parent);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on
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
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 3;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 24;
    private int tabPaddingTopBottom = 0;
    private int dividerWidth = 1;

    private int tabTextSize = 15;
    private int tabTextColor = 0xFF666666;
    private int tabTextSelectColor = 0xFF666666;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;
    private int tabDrawMode = DRAW_MODE_NORMAL;

    public static final int DRAW_MODE_NORMAL = 0;
    public static final int DRAW_MODE_TEXT = 1;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.psts_background_tab;
//    private int tabBackgroundResId;

    private Locale locale;

    public AdvancedPagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public AdvancedPagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvancedPagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
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
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

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
        textAllCaps = a.getBoolean(R.styleable.AdvancedPagerSlidingTabStrip_apTabTextAllCaps, textAllCaps);
        tabTextSelectColor = a.getColor(R.styleable.AdvancedPagerSlidingTabStrip_apTabTextSelectColor, dividerColor);
        tabDrawMode = a.getInteger(R.styleable.AdvancedPagerSlidingTabStrip_apTabDrawMode,DRAW_MODE_NORMAL);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

//        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        if(mViewTabCache == null){
            mViewTabCache = new SparseArray<>();
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

            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIcon(i), pager.getAdapter().getPageTitle(i).toString());
            } else if(pager.getAdapter() instanceof ViewTabProvider){
                addViewTab(i, ((ViewTabProvider) pager.getAdapter()),pager.getAdapter().getPageTitle(i).toString());
            } else {
//                addIconTab(i, R.drawable.home_categry_icon_n, pager.getAdapter().getPageTitle(i).toString());

                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }

        }

        updateTabStyles();

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

    private void addTextTab(final int position, String title) {

        RelativeLayout tab = new RelativeLayout(getContext());
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToChild(position, 0);
                pager.setCurrentItem(position);
            }
        });

        TextView txt = new TextView(getContext());
        txt.setText(title);
        txt.setFocusable(true);
        txt.setGravity(Gravity.CENTER);
        txt.setSingleLine();
        txt.setId(R.id.id_tab_txt);
        RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        if(pager.getAdapter() instanceof LayoutProvider){
            LayoutProvider layoutProvider = (LayoutProvider)pager.getAdapter();
            for(Integer rule : layoutProvider.getPageRule(position)){
                txtParams.addRule(rule);
            }
            Margins margins = layoutProvider.getPageMargins(position);
            if(margins != null) {
                txtParams.setMargins(margins.mLeft, margins.mTop, margins.mRight, margins.mBottom);
            }
        }
//        txtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        txt.setLayoutParams(txtParams);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.psts_dot_wh), getResources().getDimensionPixelSize(R.dimen.psts_dot_wh));
        TextView dot = new TextView(getContext());
        dot.setTextColor(Color.WHITE);
        dot.setBackgroundColor(Color.BLUE);
        dot.setGravity(Gravity.CENTER);
        dot.setSingleLine();
        dot.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.psts_dot_txt_size));
        //16 new
        Drawable dot_drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.apsts_tips, null);


        boolean hasRule = false;
        layoutParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.psts_dot_m_right), 0);
        if(pager.getAdapter() instanceof TipsProvider){
            TipsProvider tipsProvider = (TipsProvider)pager.getAdapter();
            for(Integer rule : tipsProvider.getTipsRule(position)){
                layoutParams.addRule(rule);
                hasRule = true;
            }
            Margins margins = tipsProvider.getTipsMargins(position);
            if(margins != null) {
                layoutParams.setMargins(margins.mLeft, margins.mTop, margins.mRight, margins.mBottom);
            }
            Drawable drawable = tipsProvider.getTipsDrawable(position);
            if(drawable != null){
                dot_drawable = drawable;

            }
        }

        if(!hasRule){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dot.setBackground(dot_drawable);
        }else{
            dot.setBackgroundDrawable(dot_drawable);
        }
        dot.setLayoutParams(layoutParams);

        tab.addView(txt);
        tab.addView(dot);
        dot.setVisibility(View.GONE);
        tabsContainer.addView(tab);

    }

    public void showDot(int index) {
        RelativeLayout tab = (RelativeLayout) tabsContainer.getChildAt(index);
        TextView dot_layout = (TextView) tab.getChildAt(tab.getChildCount() - 1);
        dot_layout.setText("");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)dot_layout.getLayoutParams();
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.psts_dot_wh);
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.psts_dot_wh);

        dot_layout.setVisibility(View.VISIBLE);
    }

    public void showDot(int index,String dotTxt) {
        RelativeLayout tab = (RelativeLayout) tabsContainer.getChildAt(index);
        TextView dot_layout = (TextView) tab.getChildAt(tab.getChildCount() - 1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)dot_layout.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        dot_layout.setText(dotTxt);
        dot_layout.setVisibility(View.VISIBLE);
    }

    public void hideDot(int index) {
        RelativeLayout tab = (RelativeLayout) tabsContainer.getChildAt(index);
        TextView dot_layout = (TextView) tab.getChildAt(tab.getChildCount() - 1);
        dot_layout.setVisibility(View.GONE);
    }

    private SparseArray<View> mViewTabCache;//缓存

    private View getTabView(int position){
        return mViewTabCache.get(position);
    }

    private void setTabView( int position ,View view){
        mViewTabCache.put(position, view);
    }

    private void addViewTab(final int position, ViewTabProvider provider, String title) {

        RelativeLayout tab = new RelativeLayout(getContext());
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToChild(position, 0);
                pager.setCurrentItem(position);
            }
        });

        //文本和图
        TextView txt = new TextView(getContext());
        txt.setText(title);
        txt.setFocusable(true);
        txt.setGravity(Gravity.CENTER);
        txt.setSingleLine();
        txt.setId(R.id.id_tab_txt);
        RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txtParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if(pager.getAdapter() instanceof LayoutProvider){
            LayoutProvider layoutProvider = (LayoutProvider)pager.getAdapter();
            for(Integer rule : layoutProvider.getPageRule(position)){
                txtParams.addRule(rule);
            }
            Margins margins = layoutProvider.getPageMargins(position);
            if(margins != null) {
                txtParams.setMargins(margins.mLeft, margins.mTop, margins.mRight, margins.mBottom);
            }
        }
        txtParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txtParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        txt.setLayoutParams(txtParams);

        //使用缓存
        View view = provider.onIconView(position, getTabView(position), tab);
        setTabView(position, view);//保存view到缓存中
        RelativeLayout.LayoutParams viewLayoutParams
                = (RelativeLayout.LayoutParams)view.getLayoutParams();
        viewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        viewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.id_tab_txt);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.psts_dot_wh), getResources().getDimensionPixelSize(R.dimen.psts_dot_wh));
        TextView dot = new TextView(getContext());
        dot.setTextColor(Color.WHITE);
        dot.setBackgroundColor(Color.BLUE);
        dot.setGravity(Gravity.CENTER);
        dot.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.psts_dot_txt_size));
        dot.setSingleLine();

        Drawable dot_drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.apsts_tips, null);

        boolean hasRule = false;
        layoutParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.psts_dot_m_right), 0);
        if(pager.getAdapter() instanceof TipsProvider){
            TipsProvider tipsProvider = (TipsProvider)pager.getAdapter();
            for(Integer rule : tipsProvider.getTipsRule(position)){
                layoutParams.addRule(rule);
                hasRule = true;
            }
            Margins margins = tipsProvider.getTipsMargins(position);
            if(margins != null) {
                layoutParams.setMargins(margins.mLeft, margins.mTop, margins.mRight, margins.mBottom);
            }
            Drawable drawable = tipsProvider.getTipsDrawable(position);
            if(drawable != null){
                dot_drawable = drawable;

            }
        }

        if(!hasRule){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dot.setBackground(dot_drawable);
        }else{
            dot.setBackgroundDrawable(dot_drawable);
        }
        dot.setLayoutParams(layoutParams);

        tab.addView(txt);
        tab.addView(view);
        tab.addView(dot);
        dot.setVisibility(View.GONE);
        tabsContainer.addView(tab);

    }

    private void addIconTab(final int position, Object res, String text) {

        RelativeLayout tab = new RelativeLayout(getContext());
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToChild(position, 0);
                pager.setCurrentItem(position);
            }
        });

        //文本和图
        TextView txt = new TextView(getContext());
        txt.setText(text);
        txt.setFocusable(true);
        txt.setGravity(Gravity.CENTER);
        txt.setSingleLine();
        txt.setId(R.id.id_tab_txt);
        RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        if(pager.getAdapter() instanceof LayoutProvider){
            LayoutProvider layoutProvider = (LayoutProvider)pager.getAdapter();
            for(Integer rule : layoutProvider.getPageRule(position)){
                txtParams.addRule(rule);

            }
            Margins margins = layoutProvider.getPageMargins(position);
            if(margins != null) {
                txtParams.setMargins(margins.mLeft, margins.mTop, margins.mRight, margins.mBottom);
            }
        }
//        txtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        txt.setLayoutParams(txtParams);

        setViewResource(position,res, txt);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.psts_dot_wh), getResources().getDimensionPixelSize(R.dimen.psts_dot_wh));
        TextView dot = new TextView(getContext());
        dot.setTextColor(Color.WHITE);
        dot.setBackgroundColor(Color.BLUE);
        dot.setGravity(Gravity.CENTER);
        dot.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.psts_dot_txt_size));
        dot.setSingleLine();
        Drawable dot_drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.apsts_tips, null);

        boolean hasRule = false;
        layoutParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.psts_dot_m_right), 0);
        if(pager.getAdapter() instanceof TipsProvider){
            TipsProvider tipsProvider = (TipsProvider)pager.getAdapter();
            for(Integer rule : tipsProvider.getTipsRule(position)){
                layoutParams.addRule(rule);
                hasRule = true;
            }
            Margins margins = tipsProvider.getTipsMargins(position);
            if(margins != null) {
                layoutParams.setMargins(margins.mLeft, margins.mTop, margins.mRight, margins.mBottom);
            }
            Drawable drawable = tipsProvider.getTipsDrawable(position);
            if(drawable != null){
                dot_drawable = drawable;

            }
        }

        if(!hasRule){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dot.setBackground(dot_drawable);
        }else{
            dot.setBackgroundDrawable(dot_drawable);
        }
        dot.setLayoutParams(layoutParams);

        tab.addView(txt);
        tab.addView(dot);
        dot.setVisibility(View.GONE);
        tabsContainer.addView(tab);

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
            v.setLayoutParams(expandedTabLayoutParams);
            v.setBackgroundResource(tabBackgroundResId);
            if (shouldExpand) {
                v.setPadding(0, 0, 0, 0);
            } else {
                v.setPadding(tabPadding, tabPaddingTopBottom, tabPadding, tabPaddingTopBottom);
            }

            if (v instanceof RelativeLayout) {

                RelativeLayout tab = (RelativeLayout) v;
                TextView tv = (TextView)tab.getChildAt(0);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tv.setTypeface(tabTypeface, tabTypefaceStyle);
                tv.setTextColor(tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tv.setAllCaps(true);
                    } else {
                        tv.setText(tv.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }

    }

    public boolean setTabLayoutParams(){
        if(pager.getAdapter() instanceof LayoutProvider){
            LayoutProvider weightProvider = (LayoutProvider)pager.getAdapter();
            if(weightProvider == null){
                return false;
            }
            for (int i = 0; i < tabCount; i++) {
                float weight = weightProvider.getPageWeight(i);
                if(weight != 0.0f){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, weight == 0.0f ? 1.0f : weight);
                    tabsContainer.getChildAt(i).setLayoutParams(layoutParams);
                }else{
                    tabsContainer.getChildAt(i).setLayoutParams(expandedTabLayoutParams);
                }

            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!shouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            setTabLayoutParams();
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                if(!setTabLayoutParams()){
                    for (int i = 0; i < tabCount; i++) {
                        tabsContainer.getChildAt(i).setLayoutParams(expandedTabLayoutParams);
                    }
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

        if(tabDrawMode == DRAW_MODE_NORMAL) {
            drawTabNormalMode(canvas);
        }else {
            drawTabTextMode(canvas);
        }
    }

    private void drawTabNormalMode(Canvas canvas){
        final int height = getHeight();

        // 设置提示下划线的颜色

        rectPaint.setColor(indicatorColor);

        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

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

    private void drawTabTextMode(Canvas canvas){

        final int height = getHeight();

        // 设置提示下划线的颜色

        rectPaint.setColor(indicatorColor);

        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();

        View currentTextView = ((RelativeLayout)currentTab).getChildAt(0);
        float currentTextViewLeft = currentTextView.getLeft();
        float currentTextViewRight = currentTextView.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);

            View nextTextView = ((RelativeLayout)nextTab).getChildAt(0);
            float nextTextViewLeft = nextTextView.getLeft();
            float nextTextViewRight = nextTextView.getRight();

            currentTextViewLeft = (currentPositionOffset * nextTextViewLeft + (1f - currentPositionOffset) * currentTextViewLeft);
            currentTextViewRight = (currentPositionOffset * nextTextViewRight + (1f - currentPositionOffset) * currentTextViewRight);
        }

        //绘制提示下划线
        canvas.drawRect(lineLeft + currentTextViewLeft, height - indicatorHeight, lineLeft + currentTextViewRight , height, rectPaint);

        // 分割线paint

        dividerPaint.setColor(dividerColor);

        // 下划线paint
        rectPaint.setColor(underlineColor);

        for (int i = 0; i < tabCount; i++) {
            View tab = tabsContainer.getChildAt(i);
            //绘制分割线
            if(i < tabCount - 1) {
                canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
            }
            View tabTextView = ((RelativeLayout)tab).getChildAt(0);
            canvas.drawRect(tab.getLeft() + tabTextView.getLeft(), height - underlineHeight, tab.getLeft() + tabTextView.getRight(), height, rectPaint);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            if (tabsContainer != null && tabsContainer.getChildAt(position) != null) {
                scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            }

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

    private void setViewResource(int position ,Object obj , TextView view){
        IconTabProvider iconTabProvider = (IconTabProvider)pager.getAdapter();
        Rect rect = iconTabProvider.getPageIconBounds(position);
        Drawable drawable = null;
        if(obj instanceof Integer) {
            int resId = (int) obj;
            if (resId != 0) {
                drawable = ResourcesCompat.getDrawable(getResources(),resId,null);
            }
        }else if(obj instanceof Bitmap){
            Bitmap bitmap = (Bitmap) obj;
            if (bitmap != null) {
                drawable = new BitmapDrawable(getResources(),bitmap);
            }
        }else if(obj instanceof Drawable){
            drawable = (Drawable) obj;

        }

        if (drawable != null) {
            if(rect != null){
                drawable.setBounds(rect);
                view.setCompoundDrawables(null, drawable, null, null);
            }else{
                view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            }
        }
    }

    public void setSelectItem(int position){
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            RelativeLayout linearLayout = ((RelativeLayout) tabsContainer.getChildAt(i));
            if (i == position) {
                ((TextView) linearLayout.getChildAt(0)).setTextColor(tabTextSelectColor);
                if (pager.getAdapter() instanceof IconTabProvider) {
                    Object obj = ((IconTabProvider) pager.getAdapter()).getPageSelectIcon(i);
                    setViewResource(position, obj, (TextView) linearLayout.getChildAt(0));
//                    ().setCompoundDrawablesWithIntrinsicBounds(0, ((IconTabProvider) pager.getAdapter()).getPageIconSelectResId(i), 0, 0);
                }else if(pager.getAdapter() instanceof ViewTabProvider){
                    View view = ((ViewTabProvider) pager.getAdapter()).onSelectIconView(i,getTabView(i),linearLayout);
                    setTabView(i,view);
                }
//                    ((TextView) linearLayout.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_categry_icon_f_n, 0, 0);
            } else {
                ((TextView) linearLayout.getChildAt(0)).setTextColor(tabTextColor);
                if (pager.getAdapter() instanceof IconTabProvider) {
                    Object obj = ((IconTabProvider) pager.getAdapter()).getPageIcon(i);
                    setViewResource(position, obj, (TextView) linearLayout.getChildAt(0));
//                    ((TextView) linearLayout.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i), 0, 0);
                }else if(pager.getAdapter() instanceof ViewTabProvider){
                    View view = ((ViewTabProvider) pager.getAdapter()).onIconView(i, getTabView(i), linearLayout);
                    setTabView(i,view);
                }
//                    ((TextView) linearLayout.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_categry_icon_n, 0, 0);

            }
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

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
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
