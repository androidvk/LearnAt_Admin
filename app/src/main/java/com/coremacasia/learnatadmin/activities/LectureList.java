package com.coremacasia.learnatadmin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.ActivityLectureListBinding;
import com.coremacasia.learnatadmin.adapter.LectureAdapter;
import com.coremacasia.learnatadmin.dialogs.Dialog_Add_Lecture;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

public class LectureList extends AppCompatActivity {
    private static final String TAG = "LectureList";
    private String sCourseId;
    public CourseHelper courseHelper;
    private ActivityLectureListBinding binding;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLectureListBinding.inflate(getLayoutInflater());
        recyclerView=binding.recyclerView;
        setContentView(binding.getRoot());
        Gson gson = new Gson();
        courseHelper = gson.fromJson(getIntent().getStringExtra("helper"),
                CourseHelper.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(courseHelper.getTitle());

        getCourseData();
        //setRecyclerView();


    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        LectureAdapter adapter=new LectureAdapter(this);
        if(courseHelper.getLectures().size()!=0){
            adapter.setLectureList(courseHelper.getLectures());
            recyclerView.setAdapter(adapter);
        }else Log.e(TAG, "setRecyclerView: No Data for Lectures" );
    }

    private void getCourseData() {
        DocumentReference reference = Reference.superCourseRef(courseHelper.getCategory_id()
                , courseHelper.getCourse_id());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                   courseHelper = value.toObject(CourseHelper.class);
                   setRecyclerView();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lecture_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            Dialog_Add_Lecture dialog= Dialog_Add_Lecture.newInstance();
            dialog.setCourseHelper(courseHelper);
            dialog.show(getSupportFragmentManager(), Dialog_Add_Lecture.TAG);
        }
        return super.onOptionsItemSelected(item);
    }
}