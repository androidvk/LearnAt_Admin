package com.coremacasia.learnatadmin.menus.category.CatDetail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.commons.comp_exam.CategoryViewModel;
import com.coremacasia.learnatadmin.databinding.DialogAddCourseBinding;
import com.coremacasia.learnatadmin.menus.mentors.MentorHelper;
import com.coremacasia.learnatadmin.menus.subjects.SubjectHelper;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DF_Add_Course extends DialogFragment {
    public static final String TAG = "DF_Add_Course";
    private static String CAT;
    private DialogAddCourseBinding binding;
    private EditText eTitle, eDescription, eThumbnail;
    private Spinner spSubject, spMentor;
    private SwitchMaterial sIndividual, sLive, sVisible;
    private ImageView imageView;
    private Button bSubmit;
    private String sTitle, sDescription, sThumbnail;


    private CommonDataViewModel viewModel;
    private DocumentReference commonListRef = Reference.superRef(RMAP.list);
    private ArrayAdapter adapter, adapter1;
    private List<MentorHelper> mentorList = new ArrayList<>();
    private List<String> mentorNames = new ArrayList<>();
    private DocumentReference categoryRef;
    private List<SubjectHelper> subjectList = MyStore.getCommonData().getAll_subjects();
    private List<String> subjectNames = new ArrayList<>();
    private CategoryViewModel categoryViewModel;

    private SubjectHelper selectedSubjectHelper;
    private MentorHelper selectedMentorHelper;


    public  DF_Add_Course newInstance(String CAT) {
        DF_Add_Course.CAT = CAT;
        Bundle args = new Bundle();
        DF_Add_Course fragment = new DF_Add_Course();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogAddCourseBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eTitle = binding.editTextTextPersonName2;
        eDescription = binding.editTextTextPersonName3;
        spSubject = binding.spinner2;
        spMentor = binding.spinner3;
        sLive = binding.switch1;
        sVisible = binding.switch2;
        sIndividual = binding.switch3;
        imageView = binding.imageView7;
        bSubmit = binding.button4;
        eThumbnail = binding.editTextTextPersonName10;
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mentorSpinnerConfig();
        subjectSpinnerConfig();

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTitle = eTitle.getText().toString().trim();
                sDescription = eDescription.getText().toString().trim();
                sThumbnail = eThumbnail.getText().toString().trim();

                if (!sThumbnail.equals("") && !sDescription.equals("") && !sTitle.equals("")) {
                    writeData();
                }

            }
        });

    }


    private void writeData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference ref = FirebaseFirestore.getInstance().collection("df").document();
        String course_id = ref.getId();

        Map map = new HashMap();
        map.put(kMap.added_by, user.getUid());
        map.put(kMap.added_on, new Date());
        map.put(kMap.thumbnail, sThumbnail);
        map.put(kMap.title, sTitle);
        map.put(kMap.course_id, course_id);
        map.put(kMap.mentor_id, selectedMentorHelper.getMentor_id());
        map.put(kMap.subject_id, selectedSubjectHelper.getSubject_id());
        map.put(kMap.is_live, sLive.isChecked());
        map.put(kMap.is_individual, sIndividual.isChecked());
        map.put(kMap.is_visible, sVisible.isChecked());

        Map map1=new HashMap();
        map1.put(RMAP.all_courses, FieldValue.arrayUnion(map));

        Map map2=new HashMap();
        map2.put(RMAP.course_id,FieldValue.arrayUnion(course_id));

        DocumentReference all_course=Reference.superRef(RMAP.all_courses);

        WriteBatch batch=FirebaseFirestore.getInstance().batch();
        batch.set(categoryRef,map2, SetOptions.merge());
        batch.set(all_course,map1, SetOptions.merge());
        batch.set(Reference.superCourseRef(CAT,course_id),map,SetOptions.merge());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    dismiss();
                }
            }
        });

    }


    private void mentorSpinnerConfig() {
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, mentorNames);
        adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spMentor.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        mentorList = commonDataModel.getMentors();
                        if(mentorList==null){
                            return;
                        }
                        Log.e(TAG, "onChanged: mentor " + mentorList.size());
                        for (MentorHelper helper : mentorList) {
                            mentorNames.add(helper.getName());
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

        spMentor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMentorHelper = mentorList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void subjectSpinnerConfig() {
        categoryRef = Reference.superRef(CAT);
        adapter1 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, subjectNames);
        adapter1.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spSubject.setAdapter(adapter1);
       int i=0;
        for (SubjectHelper helper : subjectList) {
            i++;
            subjectNames.add(helper.getTitle());
            if(i==subjectList.size()){
                adapter1.notifyDataSetChanged();
            }
        }

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubjectHelper = subjectList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mentorList=null;
        subjectList=null;
        categoryViewModel=null;
        viewModel=null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
