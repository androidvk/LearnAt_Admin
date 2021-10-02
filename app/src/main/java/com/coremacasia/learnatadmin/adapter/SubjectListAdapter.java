package com.coremacasia.learnatadmin.adapter;

import static com.coremacasia.learnatadmin.activities.SubjectsList.TAG;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.activities.SubjectsList;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.ListNamesBinding;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.Holder> {
    private final Activity activity;
    private TextView tAddMentor;

    public SubjectListAdapter(Activity activity, Button tAddMentor,
                              ArrayList<String> mentorSubjectList) {
        this.activity = activity;

        this.tAddMentor = tAddMentor;
        this.mentorSubjectList = mentorSubjectList;


        tAddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String myJson = gson.toJson(mentorSubjectList);
                Bundle bundle = new Bundle();
                bundle.putString("selectedMentorList", myJson);
                bundle.putInt("from", 1);
                FragmentManager manager = ((AppCompatActivity) activity)
                        .getSupportFragmentManager();
                FragmentTransaction fragmenttransaction =
                        manager.beginTransaction();

                SubjectsList frag = new SubjectsList();
                frag.setArguments(bundle);
                fragmenttransaction.replace(android.R.id.content, frag)
                        .addToBackStack(TAG);
                fragmenttransaction.commit();
                frag.onSubjectClick(new SubjectsList.OnSubjectClickListener() {
                    @Override
                    public void onSubjectClick(SubjectHelper helper) {
                        Log.e(TAG, helper.getSubject_code());
                        mentorSubjectList.add(helper.getSubject_id());
                        Log.e(TAG, "onSubjectClick: " + helper.getSubject_id());
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ListNamesBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        String subjectId = mentorSubjectList.get(holder.getAbsoluteAdapterPosition());
        for (SubjectHelper helper : allSubjectList) {
            if (helper.getSubject_id().equals(subjectId)) {
                holder.tName.setText(helper.getTitle() + "  |  " + helper.getCategory()
                        + "  |  " + helper.getSubject_code());
            }
        }

        holder.i_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mentorSubjectList.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mentorSubjectList.size();
    }

    private ArrayList<SubjectHelper> allSubjectList = new ArrayList<>();
    private ArrayList<String> mentorSubjectList = new ArrayList<>();

    public void setDataModel(CommonDataModel commonDataModel) {
        allSubjectList.addAll(commonDataModel.getAll_subjects());

    }

    public class Holder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tName;
        private ImageView i_cancel;
        public Holder(@NonNull ListNamesBinding itemView) {
            super(itemView.getRoot());
            tName = itemView.textView60;
            i_cancel=itemView.imageView17;
        }
    }
}
