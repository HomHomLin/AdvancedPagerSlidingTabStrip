# Android AdvancedPagerSlidingTabStrip

Android AdvancedPagerSlidingTabStrip是一种Android平台的导航控件，完美兼容Android自带库和兼容库的`ViewPager`组件，最低支持Android api v9。

## Feature
 * 支持Tab小圆点以及数量显示和隐藏
 * 支持自定义Tab View
 * 支持使用Bitmap、Drawable和本地resId来显示Tab图标
 * 支持对Tab图标替换成自定义View来加载网络图片
 * 支持自由设置小圆点、tab大小和位置等设置
 * 支持实现如：微博等不均匀Tab的APP风格，并支持滑动

Project site： <https://github.com/HomHomLin/AdvancedPagerSlidingTabStrip>.

最新版本:v1.4.0

效果图：

![p1](https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/Screen_20160216163404.png)

![p2](https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/Screen_20160216163428.png)

![p3](https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/Screen_20160216163452.png)

![p4](https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/Screen_20160411141706.png)

##导入项目

**Gradle dependency:**
``` groovy
compile 'homhomlin.lib:apsts:1.4.0'
```

or

**Maven dependency:**
``` xml
<dependency>
  <groupId>homhomlin.lib</groupId>
  <artifactId>apsts</artifactId>
  <version>1.4.0</version>
</dependency>
```


##用法

基本AdvancedPagerPagerSlidingTabStrip：

在需要添加的界面xml中添加组件和ViewPager
``` xml
    <com.lhh.apst.library.AdvancedPagerSlidingTabStrip
        android:id="@+id/tabs"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        style="@style/pagertab_icon_style"
        android:layout_alignParentBottom="true"
        android:fillViewport="false"/>

    <style name="pagertab_style">
        <item name="android:background">@drawable/tab_bg_normal</item>
        <item name="apTabBackground">@drawable/tab_bg_transparent</item>
        <item name="android:textSize">13sp</item>
        <item name="android:textAppearance">@style/CustomTabPageIndicator.Text</item>
        <item name="android:textColor">@drawable/tab_color_select</item>
        <item name="apTabIndicatorColor">@color/home_bar_text_push</item>
        <item name="apTabUnderlineColor">#1A000000</item>
        <item name="apTabDividerColor">#00000000</item>
        <item name="apTabShouldExpand">false</item>
        <item name="apTabPaddingLeftRight">8dp</item>
        <item name="apTabDrawMode">text</item>
        <item name="apTabTextSelectColor">@color/home_bar_text_push</item>
    </style>
```
在代码中find该组件，并且设置adapter和ViewPager。
``` java
    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new TestAdapter(getSupportFragmentManager()));


    AdvancedPagerSlidingTabStrip tabs = (AdvancedPagerSlidingTabStrip) findViewById(R.id.tabs);
    tabs.setViewPager(pager);
```

AdvancedPagerSlidingTabStrip支持绑定OnPageChangeListener，并且不影响使用效果。

``` java
    tabs.setOnPageChangeListener(mPageChangeListener);
```

通过调用AdvancedPagerSlidingTabStrip的showDot(int index)和hideDot（int index）来显示或者隐藏Tab上的小圆点，index代表需要显示和隐藏的tab序列位置（0 ~ N）。

通过调用showDot(int index,String txt)方法可以显示小圆点文字，并同样通过hideDot来隐藏。如：

``` java
    tabs.showDot(0, “99+”);
```

## Tab显示模式

* 基本Adapter显示

  1.纯文本显示

  通过实现Adapter内的getPageTitle()接口即可显示纯文本情况的效果。

  2.图文显示

  通过将Adapter实现AdvancedPagerSlidingTabStrip.IconTabProvider接口，并实现其中的getPageIcon（展示未选中的图片）、getPageSelectIcon（展示选中的图片）和getPageIconText（展示的文本）方法即可显示图文效果。

  其中getPageIcon（展示未选中的图片）和getPageSelectIcon方法可以通过改变方法返回值来显示不同类型的图片，可以选择的返回值为Bitmap、Drawable和ResId。

  3.自定义图片View显示

  通过将Adapter实现AdvancedPagerSlidingTabStrip.ViewTabProvider接口，并实现其中的onSelectIconView（选中的自定义图片View）、onIconView（未选中的自定义图片View）和getPageIconText（文本）方法即可。

  需要注意的是，onSelectIconView和onIconView两个方法的返回值均为View，并会回调回上一次使用的View缓存对象，你可以通过判断返回的View是否为null来决定是否新建View对象。（PS：不判断缓存将导致你每次调用都会创建新的View对象。）

  你可以直接创建并返回ImageView对象，也可以返回其他View子类，该模式可以用于显示网络图片，需要注意的是你需要手动给View添加LayoutParams来控制其大小，并只能使用RelativeLayout.LayoutParams，具体实现方式可以查看[Demo](https://github.com/HomHomLin/AdvancedPagerSlidingTabStrip/blob/master/app/src/main/java/com/lhh/apst/advancedpagerslidingtabstrip/ViewTabActivity.java)。

* 自定义Tab

  我知道以上模式可能并不能完全满足需求，有时候可能我们需要的是更复杂的Tab，所以添加自定义tab来满足各种各样的需求。

  当前自定义tab被封装到另一个tab类中，通过使用CustomPagerSlidingTabStrip控件来实现，该控件的所有使用方法和AdvancedPagerSlidingTabStrip一致。

  通过将Adapter实现CustomPagerSlidingTabStrip.CustomTabProvider并实现其中getSelectTabView（选中的View）和getDisSelectTabView（未选中的View）方法来实现自定义Tab，两个方法同样会回调上一次使用的View缓存对象。具体实现方式可以查看[Demo](https://github.com/HomHomLin/AdvancedPagerSlidingTabStrip/blob/master/app/src/main/java/com/lhh/apst/advancedpagerslidingtabstrip/CustomTabActivity.java)。
  
## 拓展的Provider显示设置

  有时候我们不使用自定义View显示模式又想调整现有的Tab，比如设置小圆点位置、tab大小和间距等来满足原有就可以实现的需求。

  AdvancedPagerSlidingTabStrip提供了几个Provider来实现这些功能，你可以通过将你的Adapter实现定制的接口方法来实现，具体可以查看demo中的WeiboTabActivity.java。

* AdvancedPagerSlidingTabStrip.LayoutProvider

通过将Adapter实现AdvancedPagerSlidingTabStrip.LayoutProvider可以实现对Tab以及内容icon的Layout设置。

 ``` java
 public float getPageWeight(int position);
 ```

 该方法用于设置每个pageTab在整个tabs中的权重。

``` java
public int[] getPageRule(int position);
```

该方法用于设置每个tab的相对位置，如将tab设置为靠左：return new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};。

 ``` java
 public Margins getPageMargins(int position);
 ```

 该方法用于设置每个tab的间距大小，如将tab设置为距离左边距30px：return  new Margins(30,0,0,0);。

 * AdvancedPagerSlidingTabStrip.TipsProvider

通过将Adapter实现AdvancedPagerSlidingTabStrip.TipsProvider可以实现对小圆点的设置。

 ``` java
 public int[] getTipsRule(int position);
 ```

 该方法用于设置小圆点的相对位置。


 ``` java
public Margins getTipsMargins(int position);
 ```

 该方法用于设置小圆点在tab中的间距大小。

``` java
public Drawable getTipsDrawable(int position);
```
 该方法用于设置小圆点的背景，默认为红色圆角图。

## XML样式参数

 * `apTabIndicatorColor` 导航条的颜色
 * `apTabUnderlineColor` Tab底部下划线的颜色
 * `apTabDividerColor` 每个Tab之间的分割线颜色
 * `apTabstabTextSelectColor` 被选中的Tab的文本字体颜色
 * `apTabIndicatorHeight` 导航条的高度
 * `apTabUnderlineHeight` Tab底部下划线的高度
 * `apTabDividerPadding` Tab分割线的padding
 * `apTabPaddingLeftRight` 每个Tab的左右padding
 * `apTabPaddingTopBottom` 每个Tab的上下padding
 * `apTabScrollOffset` 选中tab的滑动offset
 * `apTabBackground` tab的背景
 * `apTabShouldExpand` 伸缩情况，如果为真，每个tab都是相同的weight，默认是false
 * `apTabTextAllCaps` Tab的文字是否为全部大写，如果是true就全部大写，默认为true
 * `apTabTextSelectColor` 你所选择的那个tab的颜色
 * `apTabDrawMode` 绘制模式，text或者normal，用于是否将下划线绘制为跟随TextView

## Developed By

 * Linhonghong - <linhh90@163.com>

##License
Copyright 2016 LinHongHong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.