package com.coremacasia.learnatadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentMentorBinding;
import com.coremacasia.learnatadmin.databinding.ListMentorSelectorBinding;
import com.coremacasia.learnatadmin.databinding.ListMentorsBinding;
import com.coremacasia.learnatadmin.databinding.ListUpcomingCoursesBinding;
import com.coremacasia.learnatadmin.dialogs.Dialog_Add_Mentor;
import com.coremacasia.learnatadmin.dialogs.Dialog_Mentor_Search_list;
import com.coremacasia.learnatadmin.dialogs.Dialog_Selector_Course;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class MentorSelectorAdapter extends RecyclerView.Adapter<MentorSelectorAdapter.ViewHolder> {
    private CommonDataModel commonDataModel;
    private String cat;
    private TextView tAdd;
    private CircularProgressButton tSave;
    private String from;
    private Context activity;
    private static final String TAG = "MentorSelectorAdapter";
    private ArrayList<String> list = new ArrayList<>();

    public MentorSelectorAdapter(Context activity) {
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataModel(CommonDataModel commonDataModel, String cat, TextView tAdd,
                             CircularProgressButton tSave, String from) {
        this.commonDataModel = commonDataModel;
        this.cat = cat;
        this.tAdd = tAdd;
        this.tSave = tSave;
        this.from = from;
        list = commonDataModel.getMentor_id();

        onClicks();
    }

    private void onClicks() {
        tAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) activity)
                        .getSupportFragmentManager();
                Dialog_Mentor_Search_list dialog = Dialog_Mentor_Search_list.newInstance();
                dialog.show(manager, dialog.TAG);
                dialog.onMentorSelected(new Dialog_Mentor_Search_list.GetSelectedMentorListener() {
                    @Override
                    public void onMentorSelected(MentorHelper helper) {
                        list.add(helper.getMentor_id());
                        notifyDataSetChanged();
                    }
                });

            }
        });
        tSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tSave.startAnimation();
                saveData();
            }
        });

    }

    private void saveData() {
        DocumentReference reference= Reference.superRef(cat);
        Map map=new HashMap();
        map.put(kMap.mentor_id,list);
        reference.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    tSave.revertAnimation();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListMentorSelectorBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String mentor_id = list.get(position);
        for (MentorHelper helper : MyStore.getCommonData().getMentors()) {
            if (helper.getMentor_id().equals(mentor_id)) {

                holder.tName.setText(helper.getName());
                new ImageSetterGlide().defaultImg(holder.itemView.getContext()
                        , helper.getImage(), holder.imageView);
            }
        }

        holder.bRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.bReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) activity)
                        .getSupportFragmentManager();
                Dialog_Mentor_Search_list dialog = Dialog_Mentor_Search_list.newInstance();
                dialog.show(manager, dialog.TAG);
                dialog.onMentorSelected(new Dialog_Mentor_Search_list.GetSelectedMentorListener() {
                    @Override
                    public void onMentorSelected(MentorHelper helper) {
                        list.set(position, helper.getMentor_id());
                        notifyDataSetChanged();
                    }
                });

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tName;
        private View mainView;
        private Button bReplace, bRemove;

        public ViewHolder(ListMentorSelectorBinding binding) {
            super(binding.getRoot());
            tName = binding.textView40;
            imageView = binding.imageView12;
            mainView = binding.mainView;
            bRemove = binding.button17;
            bReplace = binding.button16;
        }

    }
}
