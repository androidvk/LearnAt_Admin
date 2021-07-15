package com.coremacasia.learnatadmin.menus.dialogs;

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
import com.coremacasia.learnatadmin.menus.helpers.MentorHelper;
import com.coremacasia.learnatadmin.menus.adapter.MentorsAdapter;
import com.coremacasia.learnatadmin.utility.MyStore;

public class DF_Mentor_Search_list extends DialogFragment {
    public static final String TAG = "DF_Mentor_Search_list";
    private DialogMentorSearchListBinding binding;

    public static DF_Mentor_Search_list newInstance() {
        Bundle args = new Bundle();
        DF_Mentor_Search_list fragment = new DF_Mentor_Search_list();
        fragment.setArguments(args);
        return fragment;
    }

    private SearchView searchView;
    private RecyclerView recyclerView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,@NonNull ViewGroup container,
                             Bundle savedInstanceState) {
        binding=DialogMentorSearchListBinding.inflate(LayoutInflater.from(inflater.getContext()));
        searchView=binding.searchView;
        recyclerView=binding.recyclerView;
        return binding.getRoot();
    }
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    private GetSelectedMentorListener listener;
    public interface GetSelectedMentorListener{
        void onMentorSelected(MentorHelper helper);
    }
    public void onMentorSelected(GetSelectedMentorListener listener){
        this.listener=listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        MentorsAdapter adapter=new MentorsAdapter(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCommonData());
        adapter.getFilter().filter("");
        adapter.notifyDataSetChanged();

        adapter.onMentorClick(new MentorsAdapter.OnMentorClickListener() {
            @Override
            public void onMentorClick(MentorHelper helper) {
                listener.onMentorSelected(helper);
                dismiss();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit: "+query );
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


    @Override
    public void onDetach() {
        super.onDetach();
      listener=null;
    }

}
