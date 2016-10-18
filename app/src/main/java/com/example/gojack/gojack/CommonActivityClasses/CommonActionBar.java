package com.example.gojack.gojack.CommonActivityClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class CommonActionBar extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.common_actionbar);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.commonToolBar);
        // setSupportActionBar(toolbar);
    }

    public void setActionBar() {
        LayoutInflater inflater = LayoutInflater.from(CommonActionBar.this);
        View v = inflater.inflate(R.layout.common_actionbar, null, false);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
