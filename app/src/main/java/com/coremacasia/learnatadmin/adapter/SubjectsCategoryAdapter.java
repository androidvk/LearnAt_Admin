package com.coremacasia.learnatadmin.adapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentSubjectsBinding;
import com.coremacasia.learnatadmin.dialogs.Dialog_Add_Subject;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.RMAP;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SubjectsCategoryAdapter extends RecyclerView.Adapter<SubjectsCategoryAdapter.ViewHolder>
        implements Filterable {
    private static final String TAG = "SubjectsAdapter";
    private ArrayList<SubjectHelper> list = new ArrayList<>();

    private CommonDataModel commonDataModel;
    private Context activity;
    private int from;
    private ArrayList<String> selectedSubjectsList;
    private String CAT;

    public SubjectsCategoryAdapter(Context activity, int from,
                                   ArrayList<String> selectedSubjectsList) {

        this.activity = activity;
        this.from = from;
        this.selectedSubjectsList = selectedSubjectsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentSubjectsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    public OnSubjectClickListener listener;

    public void setCategory(String CAT) {
        this.CAT = CAT;
    }

    public interface OnSubjectClickListener {
        void onSubjectClick(SubjectHelper helper);
    }

    public void onSubjectClick(OnSubjectClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SubjectHelper helper = filteredSubjectList.get(holder.getAbsoluteAdapterPosition());
        holder.tCode.setText(helper.getCategory() + " | " + helper.getSubject_code());
        holder.tDesc.setText(helper.getDesc());
        holder.tTitle.setText(helper.getTitle());
        new ImageSetterGlide().defaultImg(holder.context, helper.getIcon(), holder.imageView);

        if(helper.getCategory().equals(RMAP.comp_exam)){
            holder.mainView.setCardBackgroundColor(holder.context.getResources()
                    .getColor(R.color.comp_color));

        }else if(helper.getCategory().equals(RMAP.ent_exam)){
            holder.mainView.setCardBackgroundColor(holder.context.getResources()
                    .getColor(R.color.ent_color));
        }else if(helper.getCategory().equals(RMAP.itcoding)){
            holder.mainView.setCardBackgroundColor(holder.context.getResources()
                    .getColor(R.color.itcoding_color));
        }else if(helper.getCategory().equals(RMAP.comm)){
            holder.mainView.setCardBackgroundColor(holder.context.getResources()
                    .getColor(R.color.comm_color));
        }else if (helper.getCategory().equals(RMAP.sc_school)){
            holder.mainView.setCardBackgroundColor(holder.context.getResources()
                    .getColor(R.color.sc_school_color));
        }

        if(selectedSubjectsList!=null){
            for(String s:selectedSubjectsList){
                if(s.equals(helper.getSubject_id())){
                    holder.tTitle.setText("✅ "+helper.getTitle());
                    holder.selected=true;
                }
            }
        }
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: "+holder.selected );
                if (from == 1) {
                    if(!holder.selected){
                        listener.onSubjectClick(helper);
                    }

                } else {
                    FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
                    Dialog_Add_Subject dialog_add_subject = new Dialog_Add_Subject(helper.getCategory(), 10, helper, position, list);
                    dialog_add_subject.show(manager, Dialog_Add_Subject.TAG);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredSubjectList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDataModel(CommonDataModel commonDataModel) {
        this.commonDataModel = commonDataModel;
        if (CAT != null) {
            for (SubjectHelper sh : commonDataModel.getAll_subjects()) {
                if (sh.getCategory().equals(CAT)) {
                    list.add(sh);
                }
            }
        } else{
            list = commonDataModel.getAll_subjects();
            list.sort((Comparator.comparing(SubjectHelper::getCategory)));
        }

    }

    private List<SubjectHelper> filteredSubjectList = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    filteredSubjectList = list;
                } else {
                    List<SubjectHelper> filteredList = new ArrayList<>();
                    for (SubjectHelper row : list) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredSubjectList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSubjectList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredSubjectList = (ArrayList<SubjectHelper>) filterResults.values;
                if (filteredSubjectList.size() == 0) {
                    filteredSubjectList = list;
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private TextView tDesc, tCode, tAllDetails, tTitle;
        private ImageView imageView;
        private Context context;
        private CardView mainView;
        private boolean selected=false;
        public ViewHolder(FragmentSubjectsBinding binding) {
            super(binding.getRoot());
            tCode = binding.textView30;
            tDesc = binding.textView31;
            tAllDetails = binding.textView32;
            imageView = binding.imageView12;
            tTitle = binding.textView40;
            context = binding.getRoot().getContext();
            mainView = binding.mainView;

        }
    }
}