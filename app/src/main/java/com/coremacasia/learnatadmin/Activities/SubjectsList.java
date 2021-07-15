package com.coremacasia.learnatadmin.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.menus.adapter.SubjectsAdapter;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private static final String TAG = "Subjects";
    private CommonDataViewModel viewModel;
    private DocumentReference commonListRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;


            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
            SubjectsAdapter adapter = new SubjectsAdapter(getActivity(),2);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.getFilter().filter("");
            //recyclerView.setNestedScrollingEnabled(false);

            commonListRef = Reference.superRef(RMAP.list);
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