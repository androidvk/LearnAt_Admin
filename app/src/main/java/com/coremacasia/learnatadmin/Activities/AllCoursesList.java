package com.coremacasia.learnatadmin.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.adapter.CoursesAdapter;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

/**
 * A fragment representing a list of Items.
 */
public class AllCoursesList extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "Courses";

    public AllCoursesList() {
    }

    public static AllCoursesList newInstance(int columnCount) {
        AllCoursesList fragment = new AllCoursesList();
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
    private DocumentReference commonListRef = Reference.superRef(RMAP.courses_all);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_list, container, false);
        rvCourses = view.findViewById(R.id.rvCourses);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CoursesAdapter adapter = new CoursesAdapter(getActivity());
        rvCourses.setLayoutManager(linearLayoutManager);
        rvCourses.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCourseData());
        adapter.notifyDataSetChanged();
        return view;
    }
}