package com.coremacasia.learnatadmin.commons.comp_exam;

import android.util.Log;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CompExamRepo {
    private static final String TAG = "CompExamRepo";
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    public CompExamRepo(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value!=null && value.exists()) {
                    onFirestoreTaskComplete.commonData(value
                            .toObject(CommonDataModel.class));
                    
                } else {
                    onFirestoreTaskComplete.error(error);
                }
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void commonData(CommonDataModel commonDataModel);
        void error(Exception e);
    }
}
