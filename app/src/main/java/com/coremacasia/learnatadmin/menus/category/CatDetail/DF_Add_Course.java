package com.coremacasia.learnatadmin.menus.category.CatDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogAddCourseBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;

public class DF_Add_Course extends DialogFragment {
    public static final String TAG = "DF_Add_Course";
    private DialogAddCourseBinding binding;
    private EditText eTitle, eDescription;
    private Spinner spSubject;
    private SwitchMaterial sIndividual, sLive, sVisible;
    private ImageView imageView;
    private Button bSubmit;

    public static DF_Add_Course newInstance(String CAT) {

        Bundle args = new Bundle();

        DF_Add_Course fragment = new DF_Add_Course();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogAddCourseBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eTitle = binding.editTextTextPersonName2;
        eDescription = binding.editTextTextPersonName3;
        spSubject = binding.spinner2;
        sLive = binding.switch1;
        sVisible = binding.switch2;
        sIndividual = binding.switch3;
        imageView = binding.imageView7;
        bSubmit = binding.button4;
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
