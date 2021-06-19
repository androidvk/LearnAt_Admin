package com.coremacasia.learnatadmin.menus.courses;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.menus.category.CategoryAdapter;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

/**
 * A fragment representing a list of Items.
 */
public class Courses extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";

    public Courses() {
    }
    public static Courses newInstance(int columnCount) {
        Courses fragment = new Courses();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private RecyclerView rvCourses;
    private Spinner spinner;
    private CommonDataViewModel viewModel;
    private DocumentReference commonListRef = Reference.superRef(RMAP.courses_all);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_list, container, false);
        rvCourses = view.findViewById(R.id.rvCourses);
        spinner = view.findViewById(R.id.spinner);
        // Set the adapter
        if (view instanceof RecyclerView) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            CoursesAdapter adapter = new CoursesAdapter(getActivity());
            rvCourses.setLayoutManager(linearLayoutManager);
            rvCourses.setAdapter(adapter);

            viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
            viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                    new Observer<CommonDataModel>() {
                        @Override
                        public void onChanged(CommonDataModel commonDataModel) {
                            adapter.setDataModel(commonDataModel);
                            adapter.notifyDataSetChanged();
                        }
                    });

        }
        return view;
    }
}