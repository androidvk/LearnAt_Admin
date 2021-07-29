package com.coremacasia.learnatadmin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.comp_exam.CategoryViewModel;
import com.coremacasia.learnatadmin.adapter.CATCourseAdapter;
import com.coremacasia.learnatadmin.adapter.CATSubjectAdapter;
import com.coremacasia.learnatadmin.dialogs.DF_Add_Course;
import com.coremacasia.learnatadmin.dialogs.DF_Add_Subject;
import com.coremacasia.learnatadmin.dialogs.DF_Selector_Course;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

public class CatDetails extends AppCompatActivity {
    private static final String TAG = "CatDetails";
    private RecyclerView rvSubjects,rvCourses;
    private Button bAddCourse,bAddSubject;
    private String CAT;
    private TextView tSeeAllCourses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CAT=getIntent().getStringExtra("cat");
        getSupportActionBar().setTitle(CAT);
        rvSubjects=findViewById(R.id.rvCourses);
        rvCourses=findViewById(R.id.recyclerViewcat);
        bAddCourse=findViewById(R.id.button3);
        bAddSubject=findViewById(R.id.button5);
        tSeeAllCourses=findViewById(R.id.textView48);
        bAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Add_Course df_add_course=DF_Add_Course.newInstance(CAT, null);
                df_add_course.show(getSupportFragmentManager(),DF_Add_Course.TAG);
            }
        });
        bAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Add_Subject df_add_subject=new DF_Add_Subject(CAT, 5, null, 0, null);
                df_add_subject.show(getSupportFragmentManager(),DF_Add_Subject.TAG);
            }
        });

        tSeeAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Selector_Course dialog=new DF_Selector_Course();
                dialog.show(getSupportFragmentManager(),DF_Selector_Course.TAG);
            }
        });

        setSubjectView();
        setCourseView();
    }

    private void setCourseView() {
        categoryRef = Reference.superRef(CAT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        CATCourseAdapter adapter=new CATCourseAdapter(CatDetails.this);
        rvCourses.setLayoutManager(linearLayoutManager);
        rvCourses.setAdapter(adapter);
        rvCourses.setNestedScrollingEnabled(false);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCommonMutableLiveData(categoryRef).observe(this,
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel);
                        adapter.notifyDataSetChanged();
                    }
                });


    }

    private CategoryViewModel categoryViewModel;
    DocumentReference categoryRef;
    private void setSubjectView() {
        categoryRef = Reference.superRef(CAT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        CATSubjectAdapter adapter = new CATSubjectAdapter(CatDetails.this);
        rvSubjects.setLayoutManager(linearLayoutManager);
        rvSubjects.setAdapter(adapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCommonMutableLiveData(categoryRef).observe(this,
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel,CAT);
                        adapter.notifyDataSetChanged();
                    }
                });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}