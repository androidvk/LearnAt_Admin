package com.coremacasia.learnatadmin.dialogs;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogSubjectSelectorBinding;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;
import com.coremacasia.learnatadmin.adapter.SubjectsCategoryAdapter;
import com.coremacasia.learnatadmin.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Dialog_Selector_Subject extends DialogFragment {
    public static final String TAG = "DialogSubjectSelector";
    private static String CAT;
    private static ArrayList<String> selectedSubjectList;
    DialogSubjectSelectorBinding binding;

    public static Dialog_Selector_Subject newInstance(String CAT, ArrayList<String> selectedSubjectList) {
        Dialog_Selector_Subject.CAT = CAT;
        Dialog_Selector_Subject.selectedSubjectList = selectedSubjectList;

        Bundle args = new Bundle();

        Dialog_Selector_Subject fragment = new Dialog_Selector_Subject();
        fragment.setArguments(args);
        return fragment;
    }

    private SearchView searchView;
    private RecyclerView recyclerView;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding= DialogSubjectSelectorBinding.inflate(LayoutInflater.from(inflater.getContext()));
        searchView=binding.searchView;
        recyclerView=binding.recyclerView;
        return binding.getRoot();
    }
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    private OnSubjectClickListener listener;
    public interface OnSubjectClickListener{
        void onSubjectClick(SubjectHelper helper);
    }
    public void onSubjectClick(OnSubjectClickListener listener){
        this.listener=listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable
                                      Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        SubjectsCategoryAdapter adapter=new SubjectsCategoryAdapter(getActivity(),1, selectedSubjectList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setCategory(CAT);
        adapter.setDataModel(MyStore.getCommonData());

        adapter.getFilter().filter("");
        adapter.notifyDataSetChanged();

        adapter.onSubjectClick(new SubjectsCategoryAdapter.OnSubjectClickListener() {
            @Override
            public void onSubjectClick(SubjectHelper helper) {
                listener.onSubjectClick(helper);
                dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit: " + query);
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }
}
