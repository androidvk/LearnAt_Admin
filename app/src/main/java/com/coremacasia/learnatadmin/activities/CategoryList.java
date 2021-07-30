package com.coremacasia.learnatadmin.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.adapter.CategoryAdapter;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

/**
 * A fragment representing a list of Items.
 */
public class CategoryList extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryList() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryList newInstance(int columnCount) {
        CategoryList fragment = new CategoryList();
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
    private RecyclerView recyclerView;
    private CommonDataViewModel viewModel;
    DocumentReference commonListRef = Reference.superRef(RMAP.list);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        recyclerView=view.findViewById(R.id.list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            CategoryAdapter adapter = new CategoryAdapter(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);

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