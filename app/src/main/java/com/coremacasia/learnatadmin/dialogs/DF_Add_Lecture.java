package com.coremacasia.learnatadmin.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogLectureAdderBinding;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DF_Add_Lecture extends DialogFragment {
    private DialogLectureAdderBinding binding;
    public static final String TAG = "DF_Lecture_Adder";
    private EditText eTitle, eThumbnail, eDescription;
    private Button bAdd;
    private TextView tCourseName, tlTitle, tlDescription;
    private ImageView thumbnail,iBack;
    private CourseHelper courseHelper;

    public static DF_Add_Lecture newInstance() {
        Bundle args = new Bundle();
        DF_Add_Lecture fragment = new DF_Add_Lecture();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DialogLectureAdderBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eTitle = binding.editTextTextPersonName12;
        eThumbnail = binding.editTextTextPersonName13;
        eDescription = binding.editTextTextPersonName14;
        bAdd = binding.button10;
        tCourseName = binding.textView37;
        tlTitle = binding.textView45;
        iBack=binding.imageView14;
        tlDescription = binding.textView46;
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    private String sTitle, sDescription, sThumbnail;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTitle = eTitle.getText().toString().trim();
                sDescription = eDescription.getText().toString().trim();
                sThumbnail = eThumbnail.getText().toString().trim();

                if (!sTitle.equals("") && !sDescription.equals("")) {
                    writeData();
                }
            }
        });
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void writeData() {
        DocumentReference dr = FirebaseFirestore.getInstance().collection("Df").document();
        String lectureId = dr.getId();
        DocumentReference reference = Reference.superCourseRef(courseHelper.getCategory_id(),
                courseHelper.getCourse_id());
        Map map = new HashMap();
        map.put(kMap.title, sTitle);
        map.put(kMap.desc, sDescription);
        map.put(kMap.thumbnail, sThumbnail);
        map.put(kMap.timestamp, new Date());
        map.put(kMap.lecture_id, lectureId);

        Map map1 = new HashMap();
        map1.put(kMap.lectures, FieldValue.arrayUnion(map));

        reference.set(map1, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setCourseHelper(CourseHelper courseHelper) {
        this.courseHelper = courseHelper;
    }
}
