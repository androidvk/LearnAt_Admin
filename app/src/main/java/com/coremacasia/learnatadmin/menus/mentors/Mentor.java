package com.coremacasia.learnatadmin.menus.mentors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.CommonDataViewModel;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

/**
 * A fragment representing a list of Items.
 */
public class Mentor extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Mentor() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Mentor newInstance(int columnCount) {
        Mentor fragment = new Mentor();
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

    private Button bAddMentor;
    private RecyclerView rvMentor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentor_list, container, false);
        bAddMentor = view.findViewById(R.id.button7);
        rvMentor = view.findViewById(R.id.rvMentor);
        // Set the adapter

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bAddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Add_Mentor df_add_mentor = DF_Add_Mentor.newInstance();
                df_add_mentor.show(getActivity().getSupportFragmentManager(), DF_Add_Mentor.TAG);
            }
        });
        setRecyclerViewMentor();

    }
    private CommonDataViewModel viewModel;
    private DocumentReference commonListRef;
    private void setRecyclerViewMentor() {
        commonListRef=Reference.superRef(RMAP.list);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        MentorsAdapter adapter = new MentorsAdapter(getActivity());
        rvMentor.setLayoutManager(linearLayoutManager);
        rvMentor.setAdapter(adapter);
        rvMentor.setNestedScrollingEnabled(false);

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
}