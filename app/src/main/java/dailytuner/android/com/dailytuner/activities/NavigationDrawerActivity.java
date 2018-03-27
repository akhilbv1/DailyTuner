package dailytuner.android.com.dailytuner.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.firebase.FirebaseUserTable;
import dailytuner.android.com.dailytuner.fragments.ScheduleFragment;
import dailytuner.android.com.dailytuner.fragments.ActivitiesListFragment;
import dailytuner.android.com.dailytuner.fragments.ChangePasswordFragment;
import dailytuner.android.com.dailytuner.fragments.DashboardFragment;
import dailytuner.android.com.dailytuner.fragments.EditProfileFragment;
import dailytuner.android.com.dailytuner.utils.CommonUtils;

/**Created by akhil on 2/2/18.
 */

/*
  navigation drawer
  respective layout:-sample_navigation_drawer
 */

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    String userid;
    FirebaseUserTable user;
    TextView TextViewuserName;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        setLoginPreference();
        toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        View header=navigationView.getHeaderView(0);
        TextViewuserName = header.findViewById(R.id.TextViewuserName);


        setSupportActionBar(toolbar);
        setupDrawerToggle();
        userid = getUid();
        defaultFragment();
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        getUserDetailsFromFirebase();
        //new Asyntask().execute();
        Log.i("signout",""+FirebaseAuth.getInstance().getCurrentUser());
        Log.i("userid",""+userid);
    }

    /**
     * sets shared preference islogin to true
     */
    private void setLoginPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    private String getUid(){
      CommonUtils.getPreferences(this);
      String uid = CommonUtils.currentUser;
      Log.i("current_user",""+uid);
      return uid;
    }

    /**
     * opens default Fragment.
     */
    private void defaultFragment(){
        setToolbarTitle(getString(R.string.toolbar_title_myactivities));
        Fragment fragment = new ActivitiesListFragment();
        Bundle args = new Bundle();
        args.putString("userid",userid);
        fragment.setArguments(args);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.container, fragment);
        tx.commit();
    }


    @Override
    public void onBackPressed() {
       exitApptoHome();
    }

    private void exitApptoHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * invoked when navigation item is clicked
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_acitvitieslist:
                setToolbarTitle(getString(R.string.toolbar_title_myactivities));
                fragment = new ActivitiesListFragment();
                break;

            case R.id.dashboard:
                setToolbarTitle("Dashboard");
                fragment = new DashboardFragment();
                break;

            case R.id.nav_schedule:
                setToolbarTitle("Schedule");
                fragment = new ScheduleFragment();
                break;

            case R.id.nav_edit_profile:
                setToolbarTitle("Edit Profile");

                fragment = new EditProfileFragment();
                break;

            case R.id.nav_chng_pass:
                setToolbarTitle(getString(R.string.toolbar_title_change_password));
               fragment = new ChangePasswordFragment();
                break;

            case R.id.nav_signout:
               Log.i("signout",""+FirebaseAuth.getInstance().getCurrentUser());
                setSignOutPreference();
                break;
            default:
                break;
        }
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putString("userid",userid);
            args.putString("email",user.getEmailid());
            args.putString("mobile",user.getMobileNum());
            args.putString("username",user.getUsername());
            args.putString("password",user.getPassword());
            fragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack("fragments");
            ft.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
//        invalidateOptionsMenu();
        return true;
    }



    /**
     * sets islogin in shared preference to false on user signout
     */
    private void setSignOutPreference(){

        Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        FirebaseAuth.getInstance().signOut();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isLogin",false);
        editor.putString("current",null);
        editor.clear();
        editor.commit();

        Toast.makeText(this,"User Logged out success",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * navigation drawertoggle button.
     */
    private void setupDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



    private void getUserDetailsFromFirebase(){
        dialog.show();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");


        mDatabase.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.cancel();
                 user = dataSnapshot.getValue(FirebaseUserTable.class);
                 TextViewuserName.setText(user.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.cancel();
                Log.i("datasbeError",""+databaseError.getMessage());
            }
        });
    }



    /**
     *
     * sets respective toolbar title for each activity
     * @param title
     */
    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }


  }


