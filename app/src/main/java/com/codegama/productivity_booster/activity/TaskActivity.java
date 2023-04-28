package com.codegama.productivity_booster.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.codegama.productivity_booster.R;
import com.codegama.productivity_booster.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.codegama.productivity_booster.bottomSheetFragment.ShowCalendarViewBottomSheet;
import com.codegama.productivity_booster.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.productivity_booster.fragment.TaskFragment;
import com.codegama.productivity_booster.fragment.planning;
import com.codegama.productivity_booster.fragment.profile;
import com.codegama.productivity_booster.fragment.settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class TaskActivity extends AppCompatActivity implements CreateTaskBottomSheetFragment.setRefreshListener {


    @BindView(R.id.addTask)
    FloatingActionButton addTask;

    /*
    @BindView(R.id.calendar)
    ImageView calendar;
    */

    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar Toolbar;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            // Set the status bar to be translucent
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


        addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, TaskActivity.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
            this.refresh();
        });


        /*
        calendar.setOnClickListener(view -> {
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });
        */

        setSupportActionBar(Toolbar);


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set the tasks fragment as the default fragment to show
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TaskFragment()).commit();


        MenuItem tasksItem = bottomNav.getMenu().findItem(R.id.tasks);
        tasksItem.setChecked(true);
    }



    @Override
    public void refresh() {
        // Get the current fragment in the fragment container
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // Check if the current fragment is an instance of TaskFragment
        if (currentFragment instanceof TaskFragment) {
            // Call the getSavedtask() method in TaskFragment
            ((TaskFragment) currentFragment).getSavedTasks();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.topbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id=item.getItemId();
        if(id==R.id.calendar){
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        }
        if(id==R.id.notifications){
            Toast.makeText(this,"Notifications",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.planning3:
                            selectedFragment = new settings();
                            break;
                        case R.id.tasks:
                            selectedFragment = new TaskFragment();
                            break;
                        case R.id.planning1:
                            selectedFragment = new planning();
                            break;
                        case R.id.planning2:
                            selectedFragment = new profile();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}