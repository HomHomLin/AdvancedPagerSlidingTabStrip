package com.lhh.apst.advancedpagerslidingtabstrip;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lhh.apst.fragments.FirstFragment;
import com.lhh.apst.fragments.FourthFragment;
import com.lhh.apst.fragments.SecondFragment;
import com.lhh.apst.fragments.ThirdFragment;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lhh.apst.library.ViewHolder;

/**
 * Created by linhonghong on 2015/8/10.
 */
public class CustomTabActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener{

    public CustomPagerSlidingTabStrip mAPSTS;
    public APSTSViewPager mVP;

    private static final int VIEW_FIRST 		= 0;
    private static final int VIEW_SECOND	    = 1;
    private static final int VIEW_THIRD       = 2;
    private static final int VIEW_FOURTH    = 3;

    private static final int VIEW_SIZE = 4;

    private Context mContext;

    private FirstFragment mFirstFragment = null;
    private SecondFragment mSecondFragment = null;
    private ThirdFragment mThirdFragment = null;
    private FourthFragment mFourthFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_custom_tab);
        findViews();
        init();
    }

    private void findViews(){
        mAPSTS = (CustomPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (APSTSViewPager)findViewById(R.id.vp_main);
    }

    private void init(){
        mVP.setOffscreenPageLimit(VIEW_SIZE);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        mVP.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
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

    public class FragmentAdapter extends FragmentStatePagerAdapter implements CustomPagerSlidingTabStrip.CustomTabProvider{

        protected LayoutInflater mInflater;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            mInflater = LayoutInflater.from(mContext);
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
                        return  "FIRST";
                    case  VIEW_SECOND:
                        return  "SECOND";
                    case  VIEW_THIRD:
                        return  "THIRD";
                    case  VIEW_FOURTH:
                        return  "FOURTH";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public View getSelectTabView(int position, View convertView) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.custom_select_tab, null);
            }

            TextView tv = ViewHolder.get(convertView, R.id.tvTab);

            tv.setText(getPageTitle(position));

            return convertView;
        }

        @Override
        public View getDisSelectTabView(int position, View convertView) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.custom_disselect_tab, null);
            }

            TextView tv = ViewHolder.get(convertView, R.id.tvTab);

            tv.setText(getPageTitle(position));

            return convertView;
        }
    }
}

