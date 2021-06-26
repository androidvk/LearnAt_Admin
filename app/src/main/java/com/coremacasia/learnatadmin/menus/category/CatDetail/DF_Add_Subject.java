package com.coremacasia.learnatadmin.menus.category.CatDetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogAddSubjectBinding;
import com.coremacasia.learnatadmin.menus.subjects.SubjectHelper;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DF_Add_Subject extends DialogFragment {
    public static final String TAG = "DF_Add_Subject";
    private String CAT;
    private DialogAddSubjectBinding binding;
    private EditText eTitle, eDescription, eCode,eImageLink;
    private ImageView imageView;
    private Button bSubmit;

    public DF_Add_Subject(String CAT, int i, SubjectHelper helper,
                          int position, ArrayList<SubjectHelper> list) {
        this.CAT = CAT;
        this.i = i;
        this.helper = helper;
        this.position = position;
        this.list = list;
        categoryRef=Reference.superRef(CAT);
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DialogAddSubjectBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eTitle = binding.editTextTextPersonName5;
        eDescription = binding.editTextTextPersonName6;
        eCode = binding.editTextTextPersonName7;
        imageView = binding.imageView8;
        bSubmit = binding.button6;
        eImageLink=binding.editTextTextPersonName11;
        return binding.getRoot();
    }

    private DocumentReference listRef = Reference.superRef(RMAP.list);
    private DocumentReference categoryRef;
    private int i;
    private SubjectHelper helper;
    private int position;
    private ArrayList<SubjectHelper> list;
    private String sTitle, sDescription, sCode,sImagelink;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated:I " + i + CAT);
        if (i == 10) {
            eCode.setText(helper.getSubject_code());
            eDescription.setText(helper.getDesc());
            eTitle.setText(helper.getTitle());
            eImageLink.setText(helper.getIcon());
        }

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTitle = eTitle.getText().toString().trim();
                sDescription = eDescription.getText().toString().trim();
                sCode = eCode.getText().toString().trim();
                sImagelink=eImageLink.getText().toString().trim();
                if (!sTitle.equals("") && !sDescription.equals("") && !sCode.equals("")) {
                    if (i == 5) {
                        writeSubjects();
                    } else if (i == 10) {
                        //update
                        updateList();
                    }

                } else Toast.makeText(getActivity(), "Input Values", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void updateList() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Map map = new HashMap();
        map.put(kMap.title, sTitle);
        map.put(kMap.desc, sDescription);
        map.put(kMap.subject_code, sCode);
        map.put(kMap.subject_id, helper.getSubject_id());
        map.put(kMap.updated_by, firebaseUser.getUid());
        map.put(kMap.update_time, new Date());
        map.put(kMap.added_on, new Date());
        map.put(kMap.category, CAT);
        map.put(kMap.added_by, helper.getAdded_by());
        map.put(kMap.icon, sImagelink);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(map);
        SubjectHelper helper = gson.fromJson(jsonElement, SubjectHelper.class);
        list.set(position,helper);



        Map map1 = new HashMap();
        map1.put(kMap.all_subjects, list);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        batch.set(listRef, map1, SetOptions.merge());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dismiss();
                }
            }
        });

    }

    private void writeSubjects() {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("dd").document();
        String subject_id = documentReference.getId();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Map map = new HashMap();
        map.put(kMap.title, sTitle);
        map.put(kMap.desc, sDescription);
        map.put(kMap.subject_code, sCode);
        map.put(kMap.subject_id, subject_id);
        map.put(kMap.added_on, new Date());
        map.put(kMap.category, CAT);
        map.put(kMap.added_by, firebaseUser.getUid());
        map.put(kMap.icon, "");

        Map map1 = new HashMap();
        map1.put(kMap.all_subjects, map);

        Map map2=new HashMap();
        map2.put(kMap.all_subjects,subject_id);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        batch.set(listRef, map1, SetOptions.merge());
        batch.set(categoryRef,map2,SetOptions.merge());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dismiss();
                }
            }
        });


    }
}
