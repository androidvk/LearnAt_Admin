package com.coremacasia.learnatadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.commons.all_courses.AllCoursesViewModel;
import com.coremacasia.learnatadmin.commons.all_courses.CourseModel;
import com.coremacasia.learnatadmin.databinding.ActivityMainBinding;
import com.coremacasia.learnatadmin.start_items.Splash;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import ro.alexmamo.firestore_document.FirestoreDocument;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_category, R.id.nav_courses, R.id.nav_mentors
                , R.id.nav_subjects)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(MainActivity.this, Splash.class));
            finish();
        } else {
            getData();
        }

    }

    private DocumentReference commonListRef;
    private CommonDataViewModel viewModel;
    private AllCoursesViewModel allCoursesViewModel;

    private static final String TAG = "MainActivity";
    private void getData() {
        commonListRef = Reference.superRef(RMAP.list);
        viewModel = new ViewModelProvider(this).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(MainActivity.this,
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        MyStore.setCommonData(commonDataModel);
                    }
                });

        allCoursesViewModel=new ViewModelProvider(this).get(AllCoursesViewModel.class);
        allCoursesViewModel.getCommonMutableLiveData(Reference.superRef(RMAP.all_courses))
                .observe(MainActivity.this, new Observer<CourseModel>() {
                    @Override
                    public void onChanged(CourseModel courseModel) {
                        MyStore.setCourseData(courseModel);
                    }
                });
    }
}