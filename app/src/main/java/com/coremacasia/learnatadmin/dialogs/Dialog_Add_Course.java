package com.coremacasia.learnatadmin.dialogs;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.adapter.CoursePricingAdapter;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.commons.comp_exam.CategoryViewModel;
import com.coremacasia.learnatadmin.databinding.DialogAddCourseBinding;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.helpers.CoursePriceHelper;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Dialog_Add_Course extends DialogFragment {
    public static final String TAG = "DF_Add_Course";
    private static String CAT;
    private static CourseHelper helper;
    private DialogAddCourseBinding binding;
    private EditText eTitle, eDescription, eThumbnail;
    private Spinner spSubject;
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
    private TextView tDetails, tMentor, tSubject, tStartDate;
    private Spinner spLanguage;
    private SubjectHelper selectedSubjectHelper;
    private MentorHelper selectedMentorHelper;
    private View vBack;
    private Calendar myCalendar;
    private String selectedLanguage;
    private Date selectedDate;
    private RecyclerView recyclerViewPricing;
    private Button bAddPrice;

    public static Dialog_Add_Course newInstance(String CAT1, CourseHelper helper1) {
        CAT = CAT1;
        helper = helper1;
        Bundle args = new Bundle();
        Dialog_Add_Course fragment = new Dialog_Add_Course();
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
        tSubject = binding.textView20;
        tMentor = binding.textView25;
        sLive = binding.switch1;
        sVisible = binding.switch2;
        sIndividual = binding.switch3;
        bSubmit = binding.button4;
        eThumbnail = binding.editTextTextPersonName10;
        tDetails = binding.textView33;
        vBack = binding.imageView7;
        tStartDate = binding.textView49;
        spLanguage = binding.spinner;
        recyclerViewPricing = binding.recyclerView4;
        bAddPrice = binding.button19;
        myCalendar = Calendar.getInstance();
        return binding.getRoot();

    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    String subject = "";
    String mentor = "";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mentorSelector();
        subjectSpinnerConfig();
        setCoursePricingRecyclerView();
        categoryRef = Reference.superRef(CAT);
        //For Updating Course
        if (helper != null) {
            // TODO: 27-06-2021 Update course price
            // TODO: 27-06-2021 Update if mentor or not
            // TODO: 27-06-2021 write to mentor links
            // TODO: 27-06-2021 Subject and mentor chooser correct

            bSubmit.setText("Update");
            eTitle.setText(helper.getTitle());
            eThumbnail.setText(helper.getThumbnail());
            eDescription.setText(helper.getDesc());
            sLive.setChecked(helper.isIs_live());
            sIndividual.setChecked(helper.isIs_individual());
            sVisible.setChecked(helper.isIs_visible());
            if (helper.getStart_date() != null) {

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                selectedDate = myCalendar.getTime();
                tStartDate.setText("StartDate: "+sdf.format(selectedDate));
            }

            for (SubjectHelper subjectHelper : MyStore.getCommonData().getAll_subjects()) {
                if (helper.getSubject_id().equals(subjectHelper.getSubject_id())) {
                    subject = subjectHelper.getTitle() + " | " + subjectHelper.getSubject_code();
                    selectedSubjectHelper = subjectHelper;
                    tSubject.setText("Subject: " + subjectHelper.getTitle());

                    for (MentorHelper mentorHelper : MyStore.getCommonData().getMentors()) {
                        if (helper.getMentor_id().equals(mentorHelper.getMentor_id())) {

                            mentor = mentorHelper.getName();
                            selectedMentorHelper = mentorHelper;
                            tMentor.setText("Mentor: " + mentorHelper.getName());

                            tDetails.setText("Added By: " + helper.getAdded_by() +
                                    "\nCategoryID: " + helper.getCategory_id());
                            break;
                        }

                    }
                    break;

                }
            }

        }

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTitle = eTitle.getText().toString().trim();
                sDescription = eDescription.getText().toString().trim();
                sThumbnail = eThumbnail.getText().toString().trim();

                if (!sThumbnail.equals("") && !sDescription.equals("")
                        && !sTitle.equals("") ) {
                    if (helper == null) {
                        writeData();
                    } else {
                        updateCourse();
                    }

                }

            }
        });

        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        languageSpinner();
        datePickerDialog();

    }

    private void setCoursePricingRecyclerView() {

        ArrayList<CoursePriceHelper> list=new ArrayList<CoursePriceHelper>();
        //list=helper.getPrice_duration();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewPricing.setLayoutManager(layoutManager);
        CoursePricingAdapter adapter=new CoursePricingAdapter(list,bAddPrice,getActivity());
        recyclerViewPricing.setAdapter(adapter);

    }

    private void datePickerDialog() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                selectedDate = myCalendar.getTime();
                tStartDate.setText("StartDate: "+sdf.format(myCalendar.getTime()));
            }

        };

        tStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void languageSpinner() {
        ArrayList<String> lang_list = MyStore.getCommonData().getCourse_lang();
        ArrayAdapter ad
                = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                lang_list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(ad);
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = lang_list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateCourse() {
        Map map = new HashMap();
        map.put(kMap.added_by, helper.getAdded_by());
        map.put(kMap.added_on, new Date());
        map.put(kMap.thumbnail, sThumbnail);
        map.put(kMap.title, sTitle);
        map.put(kMap.desc, sDescription);
        map.put(kMap.category_id, CAT);
        map.put(kMap.course_id, helper.getCourse_id());
        map.put(kMap.mentor_id, selectedMentorHelper.getMentor_id());
        map.put(kMap.subject_id, selectedSubjectHelper.getSubject_id());
        map.put(kMap.is_live, sLive.isChecked());
        map.put(kMap.is_individual, sIndividual.isChecked());
        map.put(kMap.is_visible, sVisible.isChecked());
        map.put(kMap.start_date, selectedDate);
        map.put(kMap.course_lang, selectedLanguage);


        ArrayList<CourseHelper> list = MyStore.getCourseData().getAll_courses();
        int position = 0;
        for (CourseHelper courseHelper : list) {
            if (courseHelper.getCourse_id().equals(helper.getCourse_id())) {
                break;
            }
            position++;
        }
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(map);
        CourseHelper helper = gson.fromJson(jsonElement, CourseHelper.class);
        list.set(position, helper);


        Map map1 = new HashMap();
        map1.put(RMAP.all_courses, list);

        Map map2 = new HashMap();
        map2.put(RMAP.course_id, FieldValue.arrayUnion(helper.getCourse_id()));

        DocumentReference all_course = Reference.superRef(RMAP.all_courses);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        //batch.set(categoryRef, map2, SetOptions.merge());
        batch.set(all_course, map1, SetOptions.merge());

        batch.set(Reference.superCourseRef(CAT, helper.getCourse_id()), map,
                SetOptions.merge());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dismiss();
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
        map.put(kMap.desc, sDescription);
        map.put(kMap.category_id, CAT);
        map.put(kMap.duration, 1);
        map.put(kMap.course_id, course_id);
        map.put(kMap.mentor_id, selectedMentorHelper.getMentor_id());
        map.put(kMap.subject_id, selectedSubjectHelper.getSubject_id());
        map.put(kMap.is_live, sLive.isChecked());
        map.put(kMap.is_individual, sIndividual.isChecked());
        map.put(kMap.is_visible, sVisible.isChecked());
        map.put(kMap.start_date, selectedDate);
        map.put(kMap.course_lang, selectedLanguage);

        Map map1 = new HashMap();
        map1.put(RMAP.all_courses, FieldValue.arrayUnion(map));

        Map map2 = new HashMap();
        map2.put(RMAP.course_id, FieldValue.arrayUnion(course_id));

        DocumentReference all_course = Reference.superRef(RMAP.all_courses);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        batch.set(categoryRef, map2, SetOptions.merge());
        batch.set(all_course, map1, SetOptions.merge());
        batch.set(Reference.superCourseRef(CAT, course_id), map, SetOptions.merge());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dismiss();
                }
            }
        });

    }

    private void mentorSelector() {
        tMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Mentor_Search_list df = Dialog_Mentor_Search_list.newInstance();

                df.show(getActivity().getSupportFragmentManager(),
                        Dialog_Mentor_Search_list.TAG);

                df.onMentorSelected(new Dialog_Mentor_Search_list.GetSelectedMentorListener() {
                    @Override
                    public void onMentorSelected(MentorHelper helper) {
                        selectedMentorHelper = helper;
                        tMentor.setText(helper.getName());
                    }
                });
            }
        });


    }

    private void subjectSpinnerConfig() {
        tSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedSubjectList = new ArrayList<>();
                selectedSubjectList.add(selectedSubjectHelper.getSubject_id());
                Dialog_Selector_Subject dialog = Dialog_Selector_Subject.newInstance(CAT, selectedSubjectList);
                dialog.show(getActivity().getSupportFragmentManager(),
                        Dialog_Selector_Subject.TAG);
                dialog.onSubjectClick(new Dialog_Selector_Subject.OnSubjectClickListener() {
                    @Override
                    public void onSubjectClick(SubjectHelper helper) {
                        selectedSubjectHelper = helper;
                        tSubject.setText(helper.getTitle());
                    }
                });
            }
        });

    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mentorList = null;
        subjectList = null;
        categoryViewModel = null;
        viewModel = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
