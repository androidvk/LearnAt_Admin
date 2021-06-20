package com.coremacasia.learnatadmin.menus.mentors;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.menus.mentors.placeholder.PlaceholderContent;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentor_list, container, false);
        bAddMentor=view.findViewById(R.id.button7);
        // Set the adapter
        if (view instanceof RecyclerView) {

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bAddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DF_Add_Mentor df_add_mentor=DF_Add_Mentor.newInstance();
                df_add_mentor.show(getActivity().getSupportFragmentManager(), DF_Add_Mentor.TAG);
            }
        });

    }
}