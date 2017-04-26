package com.nhatdear.sademo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nhatdear.sademo.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseUser;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nhatdear.sademo.StashAwayApp;
import com.nhatdear.sademo.components.MyCustomTextView;
import com.nhatdear.sademo.database.SA_FirebaseDatabase;
import com.nhatdear.sademo.fragments.SA_BarChartFragment;
import com.nhatdear.sademo.helpers.RoundImageTransform;
import com.nhatdear.sademo.helpers.SA_Helper;
import com.nhatdear.sademo.models.SA_Portfolio;
import com.nhatdear.sademo.models.SA_User;

import java.util.ArrayList;
import java.util.List;

import static com.nhatdear.sademo.activities.SA_MainActivity.MODE.DAILY;
import static com.nhatdear.sademo.activities.SA_MainActivity.MODE.MONTHLY;
import static com.nhatdear.sademo.activities.SA_MainActivity.MODE.QUARTERLY;

public class SA_MainActivity extends SA_BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = SA_MainActivity.class.getSimpleName();
    private RadioGroup radioGroup1;
    private ArrayList<SA_Portfolio> arrayList;
    private int currentSearchYear = 2017;
    private MyCustomTextView tv_chart_name;
    public enum MODE {
        DAILY,
        QUARTERLY,
        MONTHLY
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sa_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Vote me if you like this demo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SA_User currentUser = StashAwayApp.getInstance().getCurrentUser();
        ImageView imageView = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imv_person);

        if (!TextUtils.isEmpty(currentUser.photoUrl)) {
            Glide.with(this).load(Uri.parse(currentUser.photoUrl)).centerCrop().transform(new RoundImageTransform(this)).into(imageView);
        }

        MyCustomTextView tv_username = (MyCustomTextView)navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tv_username.setText(currentUser.name);

        TextView tv_user_email = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_user_email);
        tv_user_email.setText(currentUser.email);

        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId)
            {
                case R.id.rbtn_daily:
                    setDataToChart(arrayList,DAILY);
                    break;
                case R.id.rbtn_monthly:
                    setDataToChart(arrayList,MONTHLY);
                    break;
                case R.id.rbtn_quarterly:
                    setDataToChart(arrayList, QUARTERLY);
                    break;
                default:
                    break;
            }
        });
        tv_chart_name = (MyCustomTextView)findViewById(R.id.tv_chart_name);
        tv_chart_name.setText("REPORT OF ");
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("2017", "2016", "2015");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                currentSearchYear = Integer.parseInt(item);
                getDataFromFirebase();
            }
        });

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        try {
            showProgressDialog("Loading portfolio data");
            SA_FirebaseDatabase database = new SA_FirebaseDatabase();
            database.getPortfolios(currentSearchYear).subscribe(array->{
                arrayList = array;
                setDataToChart(arrayList, MONTHLY);
                hideProgressDialog();
            },throwable -> {
                hideProgressDialog();
                SA_Helper.showSnackbar(findViewById(R.id.main_view),throwable.getLocalizedMessage());
            });
        } catch (Exception e){
            hideProgressDialog();
            e.printStackTrace();
        }
    }

    private void setDataToChart(ArrayList<SA_Portfolio> arrayList, MODE mode) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, SA_BarChartFragment.newInstance(arrayList, mode)).addToBackStack("BAR");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sa__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            String[] fileList = this.fileList();
            for (String file : fileList) {
                this.deleteFile(file);
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                List<String> providers = user.getProviders();
                if (providers != null) {
                    if (providers.contains("facebook.com")) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }

            mAuth.signOut();

            startActivity((new Intent(SA_MainActivity.this, SA_LoginActivity.class)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_statement) {

        } else if (id == R.id.nav_withdraw) {

        } else if (id == R.id.nav_referrals) {

        } else if (id == R.id.nav_support) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
