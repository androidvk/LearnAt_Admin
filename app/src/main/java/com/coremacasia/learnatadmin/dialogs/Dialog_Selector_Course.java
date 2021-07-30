package com.coremacasia.learnatadmin.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.adapter.CoursesAdapter;
import com.coremacasia.learnatadmin.databinding.FragmentCoursesListBinding;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.utility.MyStore;

import org.jetbrains.annotations.NotNull;

public class Dialog_Selector_Course extends DialogFragment {
    public static final String TAG = "DF_Selector_Course";

    private FragmentCoursesListBinding binding;

    public static Dialog_Selector_Course newInstance() {
        Bundle args = new Bundle();
        Dialog_Selector_Course fragment = new Dialog_Selector_Course();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rvCourses;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentCoursesListBinding.inflate(LayoutInflater.from(inflater.getContext()));
        rvCourses = binding.rvCourses;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CoursesAdapter adapter = new CoursesAdapter(getActivity());
        rvCourses.setLayoutManager(linearLayoutManager);
        rvCourses.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCourseData());
        adapter.notifyDataSetChanged();

        adapter.onCourseClick(new CoursesAdapter.OnCourseSelectedListener() {
            @Override
            public void onCourseClick(CourseHelper helper) {
                listener.onCourseSelected(helper);
                dismiss();
            }
        });

        return binding.getRoot();
    }

    public OnGetSelectedCourseListener listener;

    public interface OnGetSelectedCourseListener {
        void onCourseSelected(CourseHelper helper);
    }

    public void onCourseSelected(OnGetSelectedCourseListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
