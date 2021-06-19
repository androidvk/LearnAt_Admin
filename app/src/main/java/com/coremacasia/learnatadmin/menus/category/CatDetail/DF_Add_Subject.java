package com.coremacasia.learnatadmin.menus.category.CatDetail;

import android.os.Bundle;
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
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DF_Add_Subject extends DialogFragment {
    public static final String TAG = "DF_Add_Subject";
    private static String CAT;
    private DialogAddSubjectBinding binding;
    private EditText eTitle,eDescription,eCode;
    private ImageView imageView;
    private Button bSubmit;
    public static DF_Add_Subject newInstance(String CAT) {
        DF_Add_Subject.CAT = CAT;

        Bundle args = new Bundle();

        DF_Add_Subject fragment = new DF_Add_Subject();
        fragment.setArguments(args);
        return fragment;

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
        eTitle=binding.editTextTextPersonName5;
        eDescription=binding.editTextTextPersonName6;
        eCode=binding.editTextTextPersonName7;
        imageView=binding.imageView8;
        bSubmit=binding.button6;
        return binding.getRoot();
    }
    private DocumentReference listRef= Reference.superRef(RMAP.list);
    private DocumentReference categoryRef=Reference.superRef(CAT);
    private String sTitle,sDescription,sCode;
    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTitle=eTitle.getText().toString().trim();
                sDescription=eDescription.getText().toString().trim();
                sCode=eCode.getText().toString().trim();
                if(!sTitle.equals("") && !sDescription.equals("") && !sCode.equals("")){

                    writeSubjects();
                }else Toast.makeText(getActivity(), "Input Values", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void writeSubjects() {
        // TODO: 20-06-2021 Admin ID
        DocumentReference documentReference= FirebaseFirestore.getInstance().collection("dd").document();
        String uid=documentReference.getId();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();;
        Map map =new HashMap();
        map.put(kMap.title,sTitle);
        map.put(kMap.desc,sDescription);
        map.put(kMap.subject_code,sCode);
        map.put(kMap.uid,uid);
        map.put(kMap.added_on, new Date());
        map.put(kMap.category,CAT);
        map.put(kMap.added_by,firebaseUser.getUid());
        map.put(kMap.icon,"");

        Map map1=new HashMap();
        map1.put(kMap.all_subjects,FieldValue.arrayUnion(map));

        WriteBatch batch=FirebaseFirestore.getInstance().batch();

        batch.set(listRef,map1, SetOptions.merge());
        batch.set(categoryRef,map1,SetOptions.merge());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    dismiss();
                }
            }
        });



    }
}
