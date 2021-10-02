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

import com.coremacasia.learnatadmin.adapter.TrendingAdapter;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.comp_exam.CategoryViewModel;
import com.coremacasia.learnatadmin.databinding.FragmentTrendingBinding;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrendingSelectorFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendingSelectorFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Upcoming";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String CAT,FROM;
    public TrendingSelectorFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Upcoming.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendingSelectorFrag newInstance(String param1, String param2) {
        TrendingSelectorFrag fragment = new TrendingSelectorFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

    private FragmentTrendingBinding binding;
    private TextView tAdd;
    private CircularProgressButton tSave;
    private TextView tTrending;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentTrendingBinding.inflate(LayoutInflater.from(inflater.getContext()));
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
        if(FROM.equals("trending")){
            tTrending.setText("Trending Courses");
        }else if(FROM.equals("popular")){
            tTrending.setText("Popular Courses");

        }
    }

    private CategoryViewModel categoryViewModel;
    DocumentReference categoryRef;
    private void setRecyclerView() {
        categoryRef = Reference.superRef(CAT);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        TrendingAdapter adapter=new TrendingAdapter(getActivity());
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