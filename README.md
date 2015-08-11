# Android AdvancedPagerSlidingTabStrip

该组件改造自Andreas Stuetz的PagerSlidingTabStrip[Github](https://github.com/astuetz/PagerSlidingTabStrip/)

原组件已经很久没有更新，有些地方有些许缺漏，这里进行重新升级和改造。

Android AdvancedPagerSlidingTabStrip是一种Android平台的导航控件，完美兼容Android自带库和兼容库的`ViewPager`组件。

![p1](http://ww4.sinaimg.cn/mw1024/6e4e0c91gw1euym6rifr7j20810g2dgl.jpg)![p2](http://ww2.sinaimg.cn/bmiddle/6e4e0c91gw1euym6s3jw3j20810g2dgm.jpg)

#用法

将Library导入工程，在需要添加的界面xml中添加组件和ViewPager

    <com.lhh.apst.library.AdvancedPagerSlidingTabStrip
        android:id="@+id/tabs"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        style="@style/pagertab_icon_style"
        android:layout_alignParentBottom="true"
        android:fillViewport="false"/>

在代码中find该组件，并且设置adapter和ViewPager。

    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new TestAdapter(getSupportFragmentManager()));


    AdvancedPagerSlidingTabStrip tabs = (AdvancedPagerSlidingTabStrip) findViewById(R.id.tabs);
    tabs.setViewPager(pager);


AdvancedPagerSlidingTabStrip支持绑定OnPageChangeListener，并且不影响使用效果。

    tabs.setOnPageChangeListener(mPageChangeListener);

# 特点

一、现在你可以使用带有文字和图片的Tab了，并且带有多种切换的效果，原组件的IconAdapter只能使用图片而不能同时使用文字和图片并且图片没有切换效果。

  1.带有文字的图片
  只需要将你的Adapter实现AdvancedPagerSlidingTabStrip.IconTabProvider即可。其中提供getPageIconText(int index)方法用于返回index位置的图片文字。

  2.切换效果
  AdvancedPagerSlidingTabStrip.IconTabProvider中提供了getPageIconSelectResId(int index)和getPageIconResId(int index)两个方法，前者用于实现选中时候的图片效果，后者用于实现默认情况下的图片效果。


二、带有提示小点的tab，原组件没有该功能。现在你可以通过AdvancedPagerSlidingTabStrip实现像iOS一样的红点提示功能了。

   1.带有提示点的tab
     只需要调用AdvancedPagerSlidingTabStrip的showDot(int index)或者hideDot(int index)即可实现红点的显示和隐藏两个方法，index代表需要显示和隐藏的tab序列位置（0 ~ N）。

三、BUG修复和参数增加

    默认情况下一些字体和颜色设置无效的问题也在AdvancedPagerSlidingTabStrip中得到了修复。

    增加了一个新参数pstsTabPaddingTopBottom用于设置上下padding。

# 参数

跟原组件的参数一致:

 * `indicatorColor` Color of the sliding indicator
 * `underlineColor` Color of the full-width line on the bottom of the view
 * `dividerColor` Color of the dividers between tabs
 * `indicatorHeight`Height of the sliding indicator
 * `underlineHeight` Height of the full-width line on the bottom of the view
 * `dividerPadding` Top and bottom padding of the dividers
 * `tabPaddingLeftRight` Left and right padding of each tab
 * `tabPaddingTopBottom` Top and bottom padding of each tab
 * `scrollOffset` Scroll offset of the selected tab
 * `tabBackground` Background drawable of each tab, should be a StateListDrawable
 * `shouldExpand` If set to true, each tab is given the same weight, default false
 * `textAllCaps` If true, all tab titles will be upper case, default true

# 更新日志

### 当前版本: 1.0.0

# Developed By

 * Andreas Stuetz - <andreas.stuetz@gmail.com> (原作者，向其致敬！)

 * Linhonghong - <linhh90@163.com> (本小弟，^_^)
