package com.coremacasia.learnatadmin.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.adapter.SubjectsCategoryAdapter;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class SubjectsList extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SubjectsList() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SubjectsList newInstance(int columnCount) {
        SubjectsList fragment = new SubjectsList();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    private int from;
    private ArrayList<String> selectedSubjects = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            from = getArguments().getInt("from");
            if (from == 1) {
                Gson gson = new Gson();
                selectedSubjects = gson.fromJson(getArguments().getString("myjson"), ArrayList.class);
            }
        }
    }

    public static final String TAG = "SubjectsList";
    private CommonDataViewModel viewModel;
    private DocumentReference commonListRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects_list, container, false);
        Log.e(TAG, "onCreateView: ");
        // Set the adapter

        if (from == 0) {
            from = 2;
        }

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;


            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
            SubjectsCategoryAdapter adapter = new SubjectsCategoryAdapter(getActivity(), from, selectedSubjects);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.getFilter().filter("");
            commonListRef = Reference.superRef(RMAP.list);
            viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
            viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                    new Observer<CommonDataModel>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onChanged(CommonDataModel commonDataModel) {
                            adapter.setDataModel(commonDataModel);
                            adapter.notifyDataSetChanged();
                        }
                    });

            adapter.onSubjectClick(new SubjectsCategoryAdapter.OnSubjectClickListener() {
                @Override
                public void onSubjectClick(SubjectHelper helper) {
                    listener.onSubjectClick(helper);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }


        return view;
    }

    private OnSubjectClickListener listener;

    public interface OnSubjectClickListener {
        void onSubjectClick(SubjectHelper helper);
    }

    public void onSubjectClick(OnSubjectClickListener listener) {
        this.listener = listener;
    }
}