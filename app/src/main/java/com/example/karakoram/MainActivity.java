package com.example.karakoram;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.karakoram.activity.AboutActivity;
import com.example.karakoram.activity.ComplaintActivity;
import com.example.karakoram.activity.UserInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity  {

    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle toggle;
    private BottomNavigationView navView;
    private NavigationView side_navview;
    private View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bottom navigation
        navView =  findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        //sidebar
        side_navview=(NavigationView)findViewById(R.id.side_navview);
        header=side_navview.getHeaderView(0);
        side_navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==R.id.navigation_complaints){
                    startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
                }
                else if(id==R.id.navigation_about){
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
                else if(id==R.id.navigation_logout){

                }
                return true;
            }
        });
        ImageView header_image=(ImageView)header.findViewById(R.id.user_image);

        header_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
            }
        });



        mdrawer=(DrawerLayout)findViewById(R.id.drawer_layout);

        toggle=new ActionBarDrawerToggle(this,mdrawer,R.string.open,R.string.close);

        mdrawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Set the adapter onto the view pager

        openfragment(new homeFragment());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(toggle.onOptionsItemSelected(item))
        return true;
        return super.onOptionsItemSelected(item);
    }

    private boolean openfragment(Fragment fragment)
    {
        if(fragment!=null)
        {   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);

           // transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }


   BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    return openfragment(new homeFragment());

                case R.id.navigation_mess:
                    return openfragment(new messFragment());


                case R.id.navigation_bill:
                    return openfragment(new billFragment());

                case R.id.navigation_notification:
                    return openfragment(new notificationFragment());

            }

            return false;
        }

    };

    @Override
    public void onBackPressed() {
        if(mdrawer.isDrawerOpen(GravityCompat.START))
        {
            mdrawer.closeDrawer(GravityCompat.START);
        }
        else
        super.onBackPressed();
    }
}




