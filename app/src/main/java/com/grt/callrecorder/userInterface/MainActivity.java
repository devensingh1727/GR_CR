package com.grt.callrecorder.userInterface;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.grt.callrecorder.R;
import com.grt.callrecorder.logics.CallReceiverBroadcast;
import com.grt.callrecorder.storage.MyPreferences;
import com.grt.callrecorder.utilities.ConstantsValue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.recording_activator)
    SwitchCompat recordingActivator;
    private String tagFragment = "HomeFragment";
    private Handler handler = new Handler();
    private static final long DRAWER_CLOSE_DELAY = 250;
    private long sTime;
    private MyPreferences myPreferences;
    private ActionBarDrawerToggle mDrawerToggle;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeToolbar();
        if (checkPermissionGranted()) {
            createFolders();
            initializeComponents();
            initializeNavView();
            if (savedInstanceState == null) {
                displayFragment(0);
                navigationView.setCheckedItem(R.id.navigation_item_1);
            }
        }
    }


    private boolean checkPermissionGranted() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Call Phone");
        if (!addPermission(permissionsList, Manifest.permission.PROCESS_OUTGOING_CALLS))
            permissionsNeeded.add("Process OutGoing Call");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("Record Audio");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0)+" to work application properly.";
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        } else {

            return true;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this, R.style.IshiDialogTheme)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.PROCESS_OUTGOING_CALLS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    createFolders();
                    initializeComponents();
                    initializeNavView();
                    displayFragment(0);
                    navigationView.setCheckedItem(R.id.navigation_item_1);
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void createFolders() {
        File mainFolder = null;
        File recordingFolder = null;
        if (isExternalStorageWritable()) {
            mainFolder = new File(Environment.getExternalStorageDirectory(),
                    "/Global Call Recorder");
            if (!mainFolder.exists()) {
                mainFolder.mkdirs();
                File noMedia = new File(mainFolder.getAbsolutePath(), "/"
                        + ".nomedia");
                try {
                    noMedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                recordingFolder = new File(mainFolder.getAbsolutePath(),
                        "/.Recordings");
                if (!recordingFolder.exists()) {
                    recordingFolder.mkdirs();

                } else {
                }
            } else {
            }
        } else {
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void initializeComponents() {
        myPreferences = new MyPreferences(MainActivity.this);
        recordingActivator.setChecked(myPreferences.isRecordingEnable());
        recordingActivator.setOnCheckedChangeListener(this);
    }

    private void initializeNavView() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer,
                R.string.close_drawer);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        displayFragment(0);
                        return true;
                    case R.id.navigation_item_2:
                        displayFragment(1);
                        return true;
                    case R.id.navigation_item_3:
                        displayFragment(2);
                        return true;
                    case R.id.navigation_item_4:
                        displayFragment(3);
                        return true;
                    case R.id.navigation_item_5:
                        displayFragment(4);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void displayFragment(final int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RecordingsFragment();
                tagFragment = "RecordingsFragment";
                break;
            case 1:
                fragment = new SettingsFragment();
                tagFragment = "SettingsFragment";
                break;
            case 2:
                fragment = new FeedbackFragment();
                tagFragment = "FeedbackFragment";
                break;
            case 3:
                fragment = new HelpFragment();
                tagFragment = "HelpFragment";
                break;
            case 4:
                fragment = new AboutUsFragment();
                tagFragment = "AboutUsFragment";
                break;
        }
        drawerLayout.closeDrawer(navigationView);
        final Fragment finalFragment = fragment;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalFragment != null) {
                            toolbar.setTitle(ConstantsValue.NAV_FRAG_TITTLE[position]);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.mainContainer, finalFragment, tagFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                        }
                    }
                });
            }
        }, DRAWER_CLOSE_DELAY);
    }

    @Override
    public void onBackPressed() {
        RecordingsFragment myFragment = (RecordingsFragment) getSupportFragmentManager().findFragmentByTag("RecordingsFragment");
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            if (myFragment != null && myFragment.isVisible()) {
                if (sTime < System.currentTimeMillis()) {
                    sTime = System.currentTimeMillis() + 2000;
                    Toast.makeText(MainActivity.this, "Tap again to quit.", Toast.LENGTH_SHORT).show();
                } else {
                    super.onBackPressed();
                }
            } else {
                displayFragment(0);
                navigationView.setCheckedItem(R.id.navigation_item_1);
            }
        }
    }

    private void initializeToolbar() {
        if (toolbar != null)
            setSupportActionBar(toolbar);
        //dev changes
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ComponentName componentName = new ComponentName(MainActivity.this,
                CallReceiverBroadcast.class);
        PackageManager packageManager = getPackageManager();
        if (isChecked) {
            myPreferences.setRecordingEnable(true);
            packageManager.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            myPreferences.setRecordingEnable(false);
            packageManager.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        if (id == R.id.action_prize) {
            displayFragment(0);
            navigationView.setCheckedItem(R.id.navigation_item_1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
