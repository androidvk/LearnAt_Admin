package com.coremacasia.learnatadmin.commons.comp_exam;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.google.firebase.firestore.DocumentReference;

public class CategoryViewModel extends ViewModel implements CompExamRepo.OnFirestoreTaskComplete {
    private static final String TAG = "CommonListViewModel";
    private CompExamRepo CompExamRepo = new CompExamRepo(this);

    public LiveData<CommonDataModel> getCommonMutableLiveData(DocumentReference documentReference) {
        CompExamRepo.getCommonData(documentReference);
        return commonMutableLiveData;
    }

    private MutableLiveData<CommonDataModel> commonMutableLiveData = new MutableLiveData<>();

    public CategoryViewModel() {
        //firebaseRepo.getCommonData(documentReference);
    }

    @Override
    public void commonData(CommonDataModel commonDataModel) {
        commonMutableLiveData.setValue(commonDataModel);
    }

    @Override
    public void error(Exception e) {
        Log.e(TAG, "error: ", e);
    }
}
