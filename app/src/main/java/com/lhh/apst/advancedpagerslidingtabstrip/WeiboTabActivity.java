package com.lhh.apst.advancedpagerslidingtabstrip;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lhh.apst.fragments.FirstFragment;
import com.lhh.apst.fragments.FourthFragment;
import com.lhh.apst.fragments.SecondFragment;
import com.lhh.apst.fragments.ThirdFragment;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

/**
 * Created by Linhh on 16/3/8.
 */
public class WeiboTabActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener{

    public AdvancedPagerSlidingTabStrip mAPSTS;
    public APSTSViewPager mVP;

    private static final int VIEW_FIRST 		= 0;
    private static final int VIEW_SECOND	    = 1;
    private static final int VIEW_THIRD       = 2;
    private static final int VIEW_FOURTH    = 3;

    private static final int VIEW_SIZE = 4;

    private FirstFragment mFirstFragment = null;
    private SecondFragment mSecondFragment = null;
    private ThirdFragment mThirdFragment = null;
    private FourthFragment mFourthFragment = null;
    private ImageView mIvCenterBtn = null;

    private int mSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_tab);
        findViews();
        init();
    }

    private void findViews(){
        mAPSTS = (AdvancedPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (APSTSViewPager)findViewById(R.id.vp_main);
        mIvCenterBtn = (ImageView)findViewById(R.id.ivCenterBtn);
    }

    private void init(){
        mIvCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeiboTabActivity.this, "Center Btn is Clicked.",Toast.LENGTH_SHORT).show();
            }
        });
        mSize = getResources().getDimensionPixelSize(R.dimen.weibo_tab_size);
        mVP.setOffscreenPageLimit(VIEW_SIZE);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        mVP.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
        mVP.setCurrentItem(VIEW_FIRST);
        mAPSTS.showDot(VIEW_FIRST,"99+");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.IconTabProvider,AdvancedPagerSlidingTabStrip.LayoutProvider{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        if(null == mFirstFragment)
                            mFirstFragment = FirstFragment.instance();
                        return mFirstFragment;

                    case VIEW_SECOND:
                        if(null == mSecondFragment)
                            mSecondFragment = SecondFragment.instance();
                        return mSecondFragment;

                    case VIEW_THIRD:
                        if(null == mThirdFragment)
                            mThirdFragment = ThirdFragment.instance();
                        return mThirdFragment;

                    case VIEW_FOURTH:
                        if(null == mFourthFragment)
                            mFourthFragment = FourthFragment.instance();
                        return mFourthFragment;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        return  "first";
                    case  VIEW_SECOND:
                        return  "second";
                    case  VIEW_THIRD:
                        return  "third";
                    case  VIEW_FOURTH:
                        return  "fourth";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public float getPageWeight(int position) {
            return 0.0f;
//            if(position >= 0 && position < VIEW_SIZE){
//                switch (position){
//                    case  VIEW_FIRST:
//                        return  1.0f;
//                    case  VIEW_SECOND:
//                        return  1.0f;
//                    case  VIEW_THIRD:
//                        return  1.0f;
//                    case  VIEW_FOURTH:
//                        return  1.0f;
//                    default:
//                        break;
//                }
//            }
//            return 1.0f;
        }

        @Override
        public int[] getPageRule(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        return  new int[0];
                    case  VIEW_SECOND:
                        return  new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};
                    case  VIEW_THIRD:
                        return  new int[]{
                                RelativeLayout.ALIGN_PARENT_RIGHT};
                    case  VIEW_FOURTH:
                        return  new int[0];
                    default:
                        break;
                }
            }
            return new int[0];
        }

        @Override
        public Integer getPageIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.tabbar_home;
                    case VIEW_SECOND:
                        return  R.mipmap.tabbar_message_center;
                    case VIEW_THIRD:
                        return  R.mipmap.tabbar_discover;
                    case VIEW_FOURTH:
                        return  R.mipmap.tabbar_profile;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Integer getPageSelectIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.tabbar_home_selected;
                    case VIEW_SECOND:
                        return  R.mipmap.tabbar_message_center_highlighted;
                    case VIEW_THIRD:
                        return  R.mipmap.tabbar_discover_highlighted;
                    case VIEW_FOURTH:
                        return  R.mipmap.tabbar_profile_highlighted;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Rect getPageIconBounds(int position) {
            return new Rect(0, 0, mSize, mSize);
        }
    }
}
