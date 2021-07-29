package com.coremacasia.learnatadmin.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogAddMentorBinding;
import com.coremacasia.learnatadmin.helpers.CategoryHelper;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DF_Add_Mentor extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "DF_Add_Mentor";
    private static int from;
    private static MentorHelper selectedMentorHelper;
    private EditText eName, eQualification, eImageLink;
    private DialogAddMentorBinding binding;
    private Button bSubmit;
    private ImageView iBack;
    private TextView tCategory;
    public static DF_Add_Mentor newInstance(int i, MentorHelper helper) {
        DF_Add_Mentor.from = i;
        selectedMentorHelper = helper;
        Bundle args = new Bundle();
        DF_Add_Mentor fragment = new DF_Add_Mentor();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogAddMentorBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eName = binding.editTextTextPersonName4;
        eQualification = binding.editTextTextPersonName8;
        eImageLink = binding.editTextTextPersonName9;

        bSubmit = binding.button8;
        iBack = binding.imageView13;
        tCategory=binding.textView26;
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    private DocumentReference commonListRef = Reference.superRef(RMAP.list);

    private List<CategoryHelper> categoryList = new ArrayList<>();

    private String sName, sQualification, sCAT, sImagelink;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(from==2){
            bSubmit.setText("Update");
            sCAT=selectedMentorHelper.getCategory();
            eImageLink.setText(selectedMentorHelper.getImage());
            //eQualification.setText(selectedMentorHelper.get);
            eName.setText(selectedMentorHelper.getName());
            tCategory.setText("Category: "+selectedMentorHelper.getCategory());
        }
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = eName.getText().toString().trim();
                sQualification = eQualification.getText().toString().trim();
                sImagelink = eImageLink.getText().toString().trim();

                if (!sName.equals("") /*&& !sQualification.equals("")*/ && !sImagelink.equals("")) {
                   if(from==1){
                       writeData();
                   }else {
                       updateData();
                   }

                } else Toast.makeText(getActivity(), "Input Fields", Toast.LENGTH_SHORT).show();
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Selector_CAT dialog= Dialog_Selector_CAT.newInstance();
                dialog.show(getActivity().getSupportFragmentManager(), Dialog_Selector_CAT.TAG);

                dialog.onCategoryClick(new Dialog_Selector_CAT.CategoryClickListener() {
                    @Override
                    public void OnCategoryClick(CategoryHelper helper) {
                        tCategory.setText("Category: "+helper.getName());
                        sCAT=helper.getId();
                    }

                    @Override
                    public void OnError() {

                    }
                });

            }
        });
    }

    private void updateData() {
        String mentor_id = selectedMentorHelper.getMentor_id();
        Map map = new HashMap();
        map.put(kMap.name, sName);
        map.put(kMap.mentor_id, mentor_id);
        map.put(kMap.image, sImagelink);
        map.put(kMap.category, sCAT);

        ArrayList<MentorHelper> list = MyStore.getCommonData().getMentors();
        int position = 0;
        for (MentorHelper mentorHelper : list) {
            if (mentorHelper.getMentor_id().equals(selectedMentorHelper.getMentor_id())) {
                break;
            }
            position++;
        }
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(map);
        MentorHelper helper = gson.fromJson(jsonElement, MentorHelper.class);
        list.set(position, helper);

        Map map1 = new HashMap();
        map1.put(kMap.mentors, list);

        Reference.superRef(kMap.list).set(map1, SetOptions.merge()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismiss();
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void writeData() {
        DocumentReference documentReference = FirebaseFirestore.
                getInstance().collection("df").document();
        String mentor_id = documentReference.getId();
        Map map = new HashMap();
        map.put(kMap.name, sName);
        map.put(kMap.mentor_id, mentor_id);
        map.put(kMap.image, sImagelink);
        map.put(kMap.category, sCAT);

        Map map1 = new HashMap();
        map1.put(kMap.mentors, FieldValue.arrayUnion(map));
        Reference.superRef(kMap.list).set(map1, SetOptions.merge()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismiss();
                        }
                    }
                }
        );
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        if(categoryList.size()!=0){
            sCAT = categoryList.get(position).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
