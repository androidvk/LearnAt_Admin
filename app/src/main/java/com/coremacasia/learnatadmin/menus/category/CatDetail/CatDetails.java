package com.coremacasia.learnatadmin.menus.category.CatDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.coremacasia.learnatadmin.R;

public class CatDetails extends AppCompatActivity {
    private static final String TAG = "CatDetails";
    private RecyclerView recyclerView;
    private Button bAddCourse,bAddSubject;
    private String CAT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_details);
        CAT=getIntent().getStringExtra("cat");
        recyclerView=findViewById(R.id.rvCourses);
        bAddCourse=findViewById(R.id.button3);
        bAddSubject=findViewById(R.id.button5);
        bAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Add_Course df_add_course=DF_Add_Course.newInstance(CAT);
                df_add_course.show(getSupportFragmentManager(),DF_Add_Course.TAG);
            }
        });
        bAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Add_Subject df_add_subject=DF_Add_Subject.newInstance(CAT);
                df_add_subject.show(getSupportFragmentManager(),DF_Add_Subject.TAG);
            }
        });
    }
}