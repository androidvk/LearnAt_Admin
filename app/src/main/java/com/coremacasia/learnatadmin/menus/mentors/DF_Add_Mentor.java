package com.coremacasia.learnatadmin.menus.mentors;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.databinding.DialogAddMentorBinding;
import com.coremacasia.learnatadmin.menus.category.CategoryHelper;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DF_Add_Mentor extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "DF_Add_Mentor";
    private EditText eName, eQualification, eImageLink;
    private DialogAddMentorBinding binding;
    private Spinner spinner;
    private Button bSubmit;

    public static DF_Add_Mentor newInstance() {
        Bundle args = new Bundle();
        DF_Add_Mentor fragment = new DF_Add_Mentor();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        binding = DialogAddMentorBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eName = binding.editTextTextPersonName4;
        eQualification = binding.editTextTextPersonName8;
        eImageLink = binding.editTextTextPersonName9;
        spinner = binding.spinner4;
        bSubmit = binding.button8;
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    private CommonDataViewModel viewModel;
    private DocumentReference commonListRef = Reference.superRef(RMAP.list);
    private ArrayAdapter adapter;
    private List<CategoryHelper> categoryList = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private String sName,sQualification,sCAT,sImagelink;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerConfig();
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName=eName.getText().toString().trim();
                sQualification=eQualification.getText().toString().trim();
                sImagelink=eImageLink.getText().toString().trim();

                if(!sName.equals("")&&!sQualification.equals("")&&!sImagelink.equals("")){
                    writeData();
                }else Toast.makeText(getActivity(), "Input Fields", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void writeData() {
        DocumentReference documentReference= FirebaseFirestore.
                getInstance().collection("df").document();
        String mentor_id=documentReference.getId();
        Map map=new HashMap();
        map.put(kMap.name,sName);
        map.put(kMap.mentor_id,mentor_id);
        map.put(kMap.image,sImagelink);
        map.put(kMap.category,sCAT);

        Map map1=new HashMap();
        map1.put(kMap.mentors, FieldValue.arrayUnion(map));
        Reference.superRef(kMap.list).set(map1, SetOptions.merge()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dismiss();
                        }
                    }
                }
        );
    }

    private void spinnerConfig() {

        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        categoryList = commonDataModel.getCategory();
                        Log.e(TAG, "onChanged: " + categoryList.size());
                        for (CategoryHelper helper : categoryList) {
                            names.add(helper.getName());
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sCAT=categoryList.get(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
