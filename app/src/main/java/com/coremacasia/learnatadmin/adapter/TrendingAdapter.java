package com.coremacasia.learnatadmin.adapter;

import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.ListUpcomingCoursesBinding;
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

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.Holder> {
    private static final String TAG = "UpcomingAdapter";
    private CommonDataModel dataModel;
    private String CAT;
    private TextView tAdd;
    private CircularProgressButton tSave;
    private String FROM;
    private Context activity;
    private ArrayList<String> list = new ArrayList<>();
    private ViewGroup parent;
    private int viewType;

    public TrendingAdapter(Context activity) {
        this.activity = activity;
        Log.e(TAG, "TrendingAdapter: ");
    }

    public void setDataModel(CommonDataModel dataModel, String CAT,
                             TextView tAdd, CircularProgressButton tSave, String FROM) {
        this.dataModel = dataModel;
        if (FROM.equals("trending")) {
            list = dataModel.getTrending();
        } else if (FROM.equals("popular")) {
            list = dataModel.getPopular();
        }

        this.CAT = CAT;
        this.tAdd = tAdd;
        this.tSave = tSave;
        this.FROM = FROM;
        tAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) activity)
                        .getSupportFragmentManager();
                Dialog_Selector_Course dialog = Dialog_Selector_Course.newInstance();
                dialog.show(manager, dialog.TAG);
                dialog.onCourseSelected(new Dialog_Selector_Course.OnGetSelectedCourseListener() {
                    @Override
                    public void onCourseSelected(CourseHelper helper) {
                        list.add(helper.getCourse_id());
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

        DocumentReference reference = Reference.superRef(CAT);
        Map map = new HashMap();
        if (FROM.equals("trending")) {
            map.put(kMap.trending, list);
        } else if (FROM.equals("popular")) {
            map.put(kMap.popular, list);
        }

        reference.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    tSave.revertAnimation();
                }
            }
        });

    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new TrendingAdapter.Holder(ListUpcomingCoursesBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TrendingAdapter.Holder holder, int position) {
        String course_id = list.get(position);
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {

                holder.tTitle.setText(helper.getTitle());
                if (helper.isIs_live()) {
                    holder.tLive.setVisibility(View.VISIBLE);
                    if (helper.getStart_date() != null) {
                        holder.tStartDate.setVisibility(View.VISIBLE);
                        String myFormat = "dd-MMMM"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        holder.tStartDate.setText("Starting from: " + sdf.format(helper.getStart_date()));
                    } else holder.tStartDate.setVisibility(View.GONE);
                } else {
                    holder.tLive.setVisibility(View.GONE);
                    holder.tStartDate.setVisibility(View.GONE);
                }
                if (helper.getCourse_price() != null) {
                    holder.tPrice.setText("Rs." + helper.getCourse_price());
                }
                holder.tDescription.setText(helper.getDesc());
                new ImageSetterGlide().defaultImg(holder.context, helper.getThumbnail(),
                        holder.imageView);

                ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                        holder.tMentorName.setText(helper1.getName());
                    }
                }

                holder.mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                holder.bRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

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
                        Dialog_Selector_Course dialog = Dialog_Selector_Course.newInstance();
                        dialog.show(manager, dialog.TAG);
                        dialog.onCourseSelected(new Dialog_Selector_Course.OnGetSelectedCourseListener() {
                            @Override
                            public void onCourseSelected(CourseHelper helper) {
                                list.set(position, helper.getCourse_id());
                                notifyDataSetChanged();
                            }
                        });
                    }
                });

                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView tTitle, tMentorName, tLive;
        private ImageView imageView;
        private Context context;
        private View mainView;
        private Button bEdit, bReplace, bRemove;
        private TextView tStartDate, tDescription, tPrice;

        public Holder(ListUpcomingCoursesBinding binding) {
            super(binding.getRoot());
            context = binding.getRoot().getContext();
            tTitle = itemView.findViewById(R.id.textView43);
            tMentorName = itemView.findViewById(R.id.textView44);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
            bEdit = binding.button9;
            mainView = binding.mainView;
            tStartDate = binding.textView51;
            tDescription = binding.textView53;
            tPrice = binding.textView52;
            bReplace = binding.button9;
            bRemove = binding.button13;
        }

    }
}
