package com.coremacasia.learnatadmin.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.adapter.MentorSelectorAdapter;
import com.coremacasia.learnatadmin.adapter.TrendingAdapter;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.comp_exam.CategoryViewModel;
import com.coremacasia.learnatadmin.databinding.FragmentMentorSelectorBinding;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MentorSelectorFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentorSelectorFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG = "MentorSelectorFrag";
    public MentorSelectorFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MentorSelectorFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MentorSelectorFrag newInstance(String param1, String param2) {
        MentorSelectorFrag fragment = new MentorSelectorFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private String CAT,FROM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            CAT=getArguments().getString("cat");
            FROM=getArguments().getString("from");
        }
    }

    private FragmentMentorSelectorBinding binding;
    private TextView tAdd;
    private CircularProgressButton tSave;
    private TextView tTrending;
    private RecyclerView recyclerView;
    private CategoryViewModel categoryViewModel;
    DocumentReference categoryRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMentorSelectorBinding.inflate(LayoutInflater.from(inflater.getContext()));
        tAdd=binding.textView55;
        tTrending =binding.textView54;
        recyclerView=binding.recyclerView2;
        tSave=binding.textView56;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }

    private void setRecyclerView() {
        categoryRef = Reference.superRef(CAT);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MentorSelectorAdapter adapter=new MentorSelectorAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCommonMutableLiveData(categoryRef).observe(getActivity(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel, CAT,tAdd,tSave,FROM);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}