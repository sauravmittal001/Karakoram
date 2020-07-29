package com.example.karakoram.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karakoram.R;
import com.example.karakoram.notificationFragment;
import com.example.karakoram.parentFragment.HomeFragment;
import com.example.karakoram.parentFragment.MyStuffFragment;
import com.example.karakoram.parentFragment.billFragment;
import com.example.karakoram.parentFragment.messFragment;
import com.example.karakoram.resource.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

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
        side_navview=findViewById(R.id.side_navview);
        header=side_navview.getHeaderView(0);
        side_navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==R.id.navigation_complaints){
                    startActivity(new Intent(MainActivity.this, ComplainActivity.class));
                }
                else if(id==R.id.navigation_about){
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
                else if(id==R.id.navigation_logout){
                    if(menuItem.getTitle().equals("logout")){
                        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        menuItem.setTitle("login/signin");
                        TextView nameView = header.findViewById(R.id.user_name);
                        TextView entryNumberView = header.findViewById(R.id.user_entry_number);
                        nameView.setText("");
                        entryNumberView.setText("");
                        Toast.makeText(getApplicationContext(),"logged out",Toast.LENGTH_SHORT).show();
                    }
                    else
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Set the adapter onto the view pager
        openfragment(new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu menu = side_navview.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_logout);
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(userId.equals("loggedOut"))
            menuItem.setTitle("login/signin");
        else
            menuItem.setTitle("logout");
        TextView nameView = header.findViewById(R.id.user_name);
        TextView entryNumberView = header.findViewById(R.id.user_entry_number);
        nameView.setText(sharedPreferences.getString("userName",""));
        entryNumberView.setText(sharedPreferences.getString("entryNumber",""));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private boolean openfragment(Fragment fragment) {
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
                    return openfragment(new HomeFragment());

                case R.id.navigation_mess:
                    return openfragment(new messFragment());

                case R.id.navigation_bill:
                    return openfragment(new billFragment());

                case R.id.navigation_notification:
                    return openfragment(new notificationFragment());

                case R.id.navigation_my_stuff:
                    return openfragment(new MyStuffFragment());
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




