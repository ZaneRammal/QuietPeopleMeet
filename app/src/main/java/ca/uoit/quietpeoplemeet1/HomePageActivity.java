package ca.uoit.quietpeoplemeet1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.AlphabeticIndex;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import java.util.Map;

public class HomePageActivity extends AppCompatActivity implements RecordFragment.OnFragmentInteractionListener, StartFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public ListView drawerList;
    public String[] layers;
    private ActionBarDrawerToggle drawerToggle;
    private Map map;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Fragment fragment = null;
        Class fragmentClass = StartFragment.class;


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();


        /* Initialize toolbar, drawerlayout, and navigation view*/
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);


        /* DrawerToggle is the three lines (hamburger) icon that you
         *  can tap to open the navigation drawaer
         * */
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        /* addDrawerListener was added in Api 24, but at the same time,
         * setDrawerListener was deprecated
         */
        if (Build.VERSION.SDK_INT < 24) {
            drawerLayout.setDrawerListener(drawerToggle);
        } else {
            drawerLayout.addDrawerListener(drawerToggle);
        }

        setupDrawerContent(navigationView);

    }




    @Override
    public void onFragmentInteraction (Uri uri) {


    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }



    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = StartFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_main:
                fragmentClass = StartFragment.class;
                break;
            case R.id.nav_sound:
                fragmentClass = RecordFragment.class;
                break;
            case R.id.nav_send:
               // fragmentClass = ThirdFragment.class;
                break;
            default:
               // fragmentClass = FirstFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }




    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }


    /* Methods*/

    private void displayMessage(String message) {

        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


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
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Required because our application supports Android all the way down to Api Level 21. There was a change in
     * how permissions where granted and verified. So it is important to place all permission checking code in its own method to
     * avoid cluttering onCreate
     *
     * @param activity
     * @return
     */
    public void checkPermissions(Activity activity) {
        PackageManager packMan = activity.getPackageManager();
        int hasWritePermission = packMan.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, activity.getPackageName());
        int hasRecordPermission = packMan.checkPermission(Manifest.permission.RECORD_AUDIO, activity.getPackageName());


        /* If the API is lower than 23, we cannot use runtime permission statements, so we must check to see if permission has been granted. */
        if (Build.VERSION.SDK_INT < 23) {

            switch (hasWritePermission) {

                case ((PackageManager.PERMISSION_GRANTED)): {
                    Context context = getApplicationContext();
                    CharSequence text = "Write permission is granted.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    break;
                }

                case ((PackageManager.PERMISSION_DENIED)): {

                    Context context = getApplicationContext();
                    CharSequence text = "Write permission is denied. Application will not function correctly.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    break;
                }

                default:

                    Context context = getApplicationContext();
                    CharSequence text = "Default statement reached. Application may not function correctly.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    break;
            }

            switch (hasRecordPermission) {

                case ((PackageManager.PERMISSION_GRANTED)): {
                    Context context = getApplicationContext();
                    CharSequence text = "Record permission is granted.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    break;
                }
                case ((PackageManager.PERMISSION_DENIED)): {
                    Context context = getApplicationContext();
                    CharSequence text = "Record permission is denied. Application will not function correctly.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    break;
                }
                default:
                    // warn that default statement reached
                    break;
            }

        } /* If the API is lower than 23, we cannot use runtime permission statements, so we must check to see if permission has been granted. */

        /* If the API is  greater than 22, we can use runtime permission statements. */
        else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1000);

        } /* If the API is  greater than 22, we can use runtime permission statements. */

    }

}
