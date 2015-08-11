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
    }

}
