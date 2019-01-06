package com.a12400.mish.mslfandom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

import com.a12400.mish.mslfandom.database.GemDatabase;
import com.a12400.mish.mslfandom.database.MonsterDatabase;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private boolean ifPlayVideo=true;
    private String MY_MESSAGE_NAME = "isDone";
    private String MY_PREFS_NAME = "name";
    private Intent intent;
    private boolean isDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ifPlayVideo = prefs.getBoolean("ifPlayVideo", true);
       // Log.i("message","Main " + ifPlayVideo);

        intent = new Intent(this, FullscreenActivity.class);
        isDone=getIntent().getBooleanExtra(MY_MESSAGE_NAME,false);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }
    void init(){

        MonsterDatabase monsterDB = MonsterDatabase.getINSTANCE(this);
        GemDatabase gemDB = GemDatabase.getINSTANCE(this);
        //Log.i("message","Fire time enter Main" );
        monsterDB.monsterDao().getAll();
        gemDB.gemDao().getAll();
       // Log.i("message","Second time enter Main" );

        Log.i("message","Main???ifPlayVideo " + ifPlayVideo);
        Log.i("message","Main???isDone " + isDone);
        if (ifPlayVideo){
            if(!isDone){
                startActivity(intent);
            }
        }

        if(isDone||!ifPlayVideo) {
            fragementGo();
            Log.i("message","why go home??" );
        }

    }
    private void fragementGo(){
    Fragment HomePageFragment = null;
        HomePageFragment = new HomePageFragment();
        if (HomePageFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(com.a12400.mish.mslfandom.R.id.main_screen, HomePageFragment);
            ft.commit();

    }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.a12400.mish.mslfandom.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.a12400.mish.mslfandom.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.a12400.mish.mslfandom.R.id.action_on) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("ifPlayVideo", true);
            editor.apply();
            return true;
        }
        if (id == com.a12400.mish.mslfandom.R.id.action_off) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("ifPlayVideo", false);
            editor.apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;


        if (id == com.a12400.mish.mslfandom.R.id.nav_camera) {
            fragment = new AddMonsterFragment();
        } else if (id == com.a12400.mish.mslfandom.R.id.nav_gallery) {
            fragment = new AddGemFragment();
        } else if (id == com.a12400.mish.mslfandom.R.id.nav_slideshow) {
            fragment = new CalculatorFragment();
        } else if (id == com.a12400.mish.mslfandom.R.id.nav_manage) {
            fragment = new HomePageFragment();
        } else if (id == com.a12400.mish.mslfandom.R.id.nav_share) {
            startActivity(intent);
        } else if (id == com.a12400.mish.mslfandom.R.id.nav_send) {

        } else if (id == com.a12400.mish.mslfandom.R.id.nav_signup){
            fragment = new SignupActivity();
        }


        if (fragment!=null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(com.a12400.mish.mslfandom.R.id.main_screen, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.a12400.mish.mslfandom.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
