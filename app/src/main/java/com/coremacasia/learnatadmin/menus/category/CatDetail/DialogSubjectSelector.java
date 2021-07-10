package com.coremacasia.learnatadmin.menus.category.CatDetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogMentorSearchListBinding;
import com.coremacasia.learnatadmin.databinding.DialogSubjectSelectorBinding;
import com.coremacasia.learnatadmin.menus.mentors.MentorHelper;
import com.coremacasia.learnatadmin.menus.mentors.MentorsAdapter;
import com.coremacasia.learnatadmin.menus.subjects.SubjectHelper;
import com.coremacasia.learnatadmin.menus.subjects.SubjectsAdapter;
import com.coremacasia.learnatadmin.utility.MyStore;

import org.jetbrains.annotations.NotNull;

public class DialogSubjectSelector extends DialogFragment {
    public static final String TAG = "DialogSubjectSelector";
    private static String CAT;
    DialogSubjectSelectorBinding binding;

    public static DialogSubjectSelector newInstance(String CAT) {
        DialogSubjectSelector.CAT = CAT;

        Bundle args = new Bundle();

        DialogSubjectSelector fragment = new DialogSubjectSelector();
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

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable
                                      Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        SubjectsAdapter adapter=new SubjectsAdapter(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setCategory(CAT);
        adapter.setDataModel(MyStore.getCommonData());

        adapter.getFilter().filter("");
        adapter.notifyDataSetChanged();

        adapter.onSubjectClick(new SubjectsAdapter.OnSubjectClickListener() {
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
