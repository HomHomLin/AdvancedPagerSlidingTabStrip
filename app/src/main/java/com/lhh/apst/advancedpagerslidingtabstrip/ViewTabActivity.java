package com.lhh.apst.advancedpagerslidingtabstrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lhh.apst.fragments.FirstFragment;
import com.lhh.apst.fragments.FourthFragment;
import com.lhh.apst.fragments.SecondFragment;
import com.lhh.apst.fragments.ThirdFragment;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

/**
 * Created by Linhh on 16/2/16.
 */
public class ViewTabActivity  extends ActionBarActivity implements ViewPager.OnPageChangeListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tab);
        findViews();
        init();
    }

    private void findViews(){
        mAPSTS = (AdvancedPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (APSTSViewPager)findViewById(R.id.vp_main);
    }

    private void init(){
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

    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.ViewTabProvider{

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
        public View onSelectIconView(int position, View view, ViewGroup parent) {
            FrescoThumbnailView draweeView;
            if(view == null){
                draweeView = new FrescoThumbnailView(ViewTabActivity.this);
                draweeView.setLayoutParams(new RelativeLayout.LayoutParams(50,50));
                view = draweeView;
            }
            draweeView = (FrescoThumbnailView)view;
            switch (position){
                case  VIEW_FIRST:
                    draweeView.loadView("",R.mipmap.home_categry_icon_f_n);
                    break;
                case  VIEW_SECOND:
                    draweeView.loadView("",R.mipmap.home_auther_icon_f_p);
                    break;
                case  VIEW_THIRD:
                    draweeView.loadView("",R.mipmap.home_live_icon_f_n);
                    break;
                case  VIEW_FOURTH:
                    draweeView.loadView("",R.mipmap.home_main_icon_f_n);
                    break;
                default:
                    break;
            }
            return draweeView;
        }

        @Override
        public View onIconView(int position, View view, ViewGroup parent) {
            FrescoThumbnailView draweeView;
            if(view == null){
                draweeView = new FrescoThumbnailView(ViewTabActivity.this);
                draweeView.setLayoutParams(new RelativeLayout.LayoutParams(50,50));
                view = draweeView;
            }
            draweeView = (FrescoThumbnailView)view;
            switch (position){
                case  VIEW_FIRST:
                    draweeView.loadView("",R.mipmap.home_categry_icon_n);
                    break;
                case  VIEW_SECOND:
                    draweeView.loadView("",R.mipmap.home_auther_icon_f_n);
                    break;
                case  VIEW_THIRD:
                    draweeView.loadView("",R.mipmap.home_live_icon_n);
                    break;
                case  VIEW_FOURTH:
                    draweeView.loadView("",R.mipmap.home_main_icon_n);
                    break;
                default:
                    break;
            }
            return draweeView;
        }

        @Override
        public String getPageIconText(int position) {
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

    }
}

