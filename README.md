# Android AdvancedPagerSlidingTabStrip

Android AdvancedPagerSlidingTabStrip是一种Android平台的导航控件，完美兼容Android自带库和兼容库的`ViewPager`组件。

## Feature
 * 支持Tab小圆点以及数量显示和隐藏
 * 支持自定义Tab View
 * 支持使用Bitmap、Drawable和本地resId来显示Tab图标
 * 支持对Tab图标替换成自定义View来加载网络图片

Project site： <https://github.com/HomHomLin/AdvancedPagerSlidingTabStrip>.

#最新版本
v1.2.0


![p1](http://ww4.sinaimg.cn/mw1024/6e4e0c91gw1euym6rifr7j20810g2dgl.jpg)![p2](http://ww2.sinaimg.cn/bmiddle/6e4e0c91gw1euym6s3jw3j20810g2dgm.jpg)![p3](http://ww1.sinaimg.cn/bmiddle/6e4e0c91gw1euymy0xtn7j20810g2dgl.jpg)![p4](http://ww1.sinaimg.cn/bmiddle/6e4e0c91gw1ew6q3hxg7qj20k00zkdh8.jpg)![p5](http://ww4.sinaimg.cn/bmiddle/6e4e0c91gw1ew6q95gmllj20k00zk400.jpg)![p6](http://ww3.sinaimg.cn/bmiddle/6e4e0c91gw1ewb9a9y0kyj20k00zkmym.jpg)

##导入项目

**Gradle dependency:**
``` groovy
compile 'homhomlin.lib:apsts:1.2.0'
```

or

**Maven dependency:**
``` xml
<dependency>
  <groupId>homhomlin.lib</groupId>
  <artifactId>apsts</artifactId>
  <version>1.2.0</version>
</dependency>
```


#用法

基本AdvancedPagerPagerSlidingTabStrip：

在需要添加的界面xml中添加组件和ViewPager

    <com.lhh.apst.library.AdvancedPagerSlidingTabStrip
        android:id="@+id/tabs"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        style="@style/pagertab_icon_style"
        android:layout_alignParentBottom="true"
        android:fillViewport="false"/>

    <style name="pagertab_style">
        <item name="android:background">@drawable/tab_bg_normal</item>
        <item name="tabBackground">@drawable/tab_bg_transparent</item>
        <item name="android:textSize">13sp</item>
        <item name="android:textAppearance">@style/CustomTabPageIndicator.Text</item>
        <item name="android:textColor">@drawable/tab_color_select</item>
        <item name="tabIndicatorColor">@color/home_bar_text_push</item>
        <item name="tabUnderlineColor">#1A000000</item>
        <item name="tabDividerColor">#00000000</item>
        <item name="tabShouldExpand">false</item>
        <item name="tabPaddingLeftRight">8dp</item>
        <item name="tabDrawMode">text</item>
        <item name="tabTextSelectColor">@color/home_bar_text_push</item>
    </style>

在代码中find该组件，并且设置adapter和ViewPager。

    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new TestAdapter(getSupportFragmentManager()));


    AdvancedPagerSlidingTabStrip tabs = (AdvancedPagerSlidingTabStrip) findViewById(R.id.tabs);
    tabs.setViewPager(pager);


AdvancedPagerSlidingTabStrip支持绑定OnPageChangeListener，并且不影响使用效果。

    tabs.setOnPageChangeListener(mPageChangeListener);

通过调用AdvancedPagerSlidingTabStrip的showDot(int index)和hideDot（int index）来显示或者隐藏Tab上的小圆点，index代表需要显示和隐藏的tab序列位置（0 ~ N）。

通过调用showDot(int index,String txt)方法可以显示小圆点文字，并同样通过hideDot来隐藏。如：

    tabs.showDot(0, “99+”);

# Tab显示模式

一、基本Adapter显示

  1.纯文本显示

  通过实现Adapter内的getPageTitle()接口即可显示纯文本情况的效果。

  2.图文显示

  通过将Adapter实现AdvancedPagerSlidingTabStrip.IconTabProvider接口，并实现其中的getPageIcon（展示未选中的图片）、getPageSelectIcon（展示选中的图片）和getPageIconText（展示的文本）方法即可显示图文效果。

  其中getPageIcon（展示未选中的图片）和getPageSelectIcon方法可以通过改变方法返回值来显示不同类型的图片，可以选择的返回值为Bitmap、Drawable和ResId。

  3.自定义图片View显示

  通过将Adapter实现AdvancedPagerSlidingTabStrip.ViewTabProvider接口，并实现其中的onSelectIconView（选中的自定义图片View）、onIconView（未选中的自定义图片View）和getPageIconText（文本）方法即可。

  需要注意的是，onSelectIconView和onIconView两个方法的返回值均为View，并会回调回上一次使用的View缓存对象，你可以通过判断返回的View是否为null来决定是否新建View对象。（PS：不判断缓存将导致你每次调用都会创建新的View对象。）

  你可以直接创建并返回ImageView对象，也可以返回其他View子类，该模式可以用于显示网络图片，需要注意的是你需要手动给View添加LayoutParams来控制其大小，并只能使用RelativeLayout.LayoutParams，具体实现方式可以查看[Demo](https://github.com/HomHomLin/AdvancedPagerSlidingTabStrip/blob/master/app/src/main/java/com/lhh/apst/advancedpagerslidingtabstrip/ViewTabActivity.java)。

二、自定义Tab

  我知道以上模式可能并不能完全满足需求，有时候可能我们需要的是更复杂的Tab，所以添加自定义tab来满足各种各样的需求。

  当前自定义tab被封装到另一个tab类中，通过使用CustomPagerSlidingTabStrip控件来实现，该控件的所有使用方法和AdvancedPagerSlidingTabStrip一致。

  通过将Adapter实现CustomPagerSlidingTabStrip.CustomTabProvider并实现其中getSelectTabView（选中的View）和getDisSelectTabView（未选中的View）方法来实现自定义Tab，两个方法同样会回调上一次使用的View缓存对象。具体实现方式可以查看[Demo](https://github.com/HomHomLin/AdvancedPagerSlidingTabStrip/blob/master/app/src/main/java/com/lhh/apst/advancedpagerslidingtabstrip/CustomTabActivity.java)。

# XML样式参数

 * `tabIndicatorColor` 导航条的颜色
 * `tabUnderlineColor` Tab底部下划线的颜色
 * `tabDividerColor` 每个Tab之间的分割线颜色
 * `tabstabTextSelectColor` 被选中的Tab的文本字体颜色
 * `tabIndicatorHeight` 导航条的高度
 * `tabUnderlineHeight` Tab底部下划线的高度
 * `tabDividerPadding` Tab分割线的padding
 * `tabPaddingLeftRight` 每个Tab的左右padding
 * `tabPaddingTopBottom` 每个Tab的上下padding
 * `tabScrollOffset` 选中tab的滑动offset
 * `tabBackground` tab的背景
 * `tabShouldExpand` 伸缩情况，如果为真，每个tab都是相同的weight，默认是false
 * `tabTextAllCaps` Tab的文字是否为全部大写，如果是true就全部大写，默认为true
 * `tabTextSelectColor` 你所选择的那个tab的颜色
 * `tabDrawMode` 绘制模式，text或者normal，用于是否将下划线绘制为跟随TextView

# Developed By

 * Linhonghong - <linhh90@163.com>

 该组件基于Andreas Stuetz的PagerSlidingTabStrip[Github](https://github.com/astuetz/PagerSlidingTabStrip/)

