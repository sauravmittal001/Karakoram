package com.example.karakoram.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.karakoram.R;
import com.example.karakoram.parentFragment.EventFragment;
import com.example.karakoram.parentFragment.MyStuffFragment;
import com.example.karakoram.parentFragment.billFragment;
import com.example.karakoram.parentFragment.messFragment;
import com.example.karakoram.resource.User;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle toggle;
    private AHBottomNavigation navView;
    private NavigationView side_navview;
    private View header;
    private boolean editMode;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        initViews();
        setViews();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        openFragment(new EventFragment(false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu menu = side_navview.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_logout);
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "loggedOut");
        if (userId.equals("loggedOut"))
            menuItem.setTitle("login/signin");
        else
            menuItem.setTitle("logout");
        TextView nameView = header.findViewById(R.id.user_name);
        TextView entryNumberView = header.findViewById(R.id.user_entry_number);
        nameView.setText(sharedPreferences.getString("userName", ""));
        entryNumberView.setText(sharedPreferences.getString("entryNumber", ""));

        if (editMode) {
            navView.setVisibility(View.GONE);
            openFragment(new messFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mdrawer.isDrawerOpen(GravityCompat.START)) {
            mdrawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    private void initVariables() {
        editMode = getIntent().getBooleanExtra("editMode", false);
        sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
    }

    private void initViews() {
        navView = findViewById(R.id.bottom_navigation);
    }

    private void setViews() {
        setNewBottomNav();
        setSideBar();
        ImageView header_image = (ImageView) header.findViewById(R.id.user_image);
        header_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = sharedPreferences.getString("userId","loggedOut");
                if(!userId.equals("loggedOut"))
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
            }
        });

        mdrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);
        mdrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNewBottomNav() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.title_events, R.drawable.ic_input_get,R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.title_mess, R.drawable.ic_mess,R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.title_bill, R.drawable.ic_bill,R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.title_my_stuff, R.drawable.ic_dashboard_black_24dp,R.color.colorPrimary);

        navView.addItem(item1);
        navView.addItem(item2);
        navView.addItem(item3);
        navView.addItem(item4);

        navView.setBehaviorTranslationEnabled(true);
        navView.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        navView.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        navView.setInactiveColor(ContextCompat.getColor(this, R.color.black));
        navView.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        navView.setColored(false);
        navView.setCurrentItem(0);
        navView.setTitleTextSize(40,40);


        navView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        return openFragment(new EventFragment(false));

                    case 1:
                        return openFragment(new messFragment());

                    case 2:
                        return openFragment(new billFragment());

                    case 3:
                        return openFragment(new MyStuffFragment());
                }
                return false;
            }
        });
        navView.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {

            }
        });
    }

    private boolean openFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);

            // transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }

    private void setSideBar() {
        side_navview = findViewById(R.id.side_navview);
        header = side_navview.getHeaderView(0);
        side_navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.navigation_complaints) {
                    if (sharedPreferences.getString("type", "Student").equals("Admin"))
                        startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this, ComplaintFormActivity.class));
                } else if (id == R.id.navigation_about) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                } else if (id == R.id.navigation_logout) {
                    if (menuItem.getTitle().equals("logout")) {
                        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        menuItem.setTitle("login/signin");
                        TextView nameView = header.findViewById(R.id.user_name);
                        TextView entryNumberView = header.findViewById(R.id.user_entry_number);
                        nameView.setText("");
                        entryNumberView.setText("");
                        Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_SHORT).show();
                    } else
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
                return true;
            }
        });
    }

}




