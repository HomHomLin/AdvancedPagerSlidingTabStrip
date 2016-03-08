package com.lhh.apst.advancedpagerslidingtabstrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private Button mNormalTab;
    private Button mIconTab;
    private Button mCustomTab;
    private Button mViewTab;
    private Button mWeiboTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setButton();
    }

    private void findViews(){
        mIconTab = (Button)findViewById(R.id.icontab);
        mNormalTab = (Button)findViewById(R.id.noramltab);
        mCustomTab = (Button)findViewById(R.id.customtab);
        mViewTab = (Button)findViewById(R.id.viewtab);
        mWeiboTab = (Button)findViewById(R.id.weibotab);
    }

    private void setButton(){
        mIconTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,IconTabActivity.class));
            }
        });
        mNormalTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,NormalTabActivity.class));
            }
        });
        mCustomTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,CustomTabActivity.class));
            }
        });
        mViewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,ViewTabActivity.class));

            }
        });
        mWeiboTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,WeiboTabActivity.class));
            }
        });
    }

}
