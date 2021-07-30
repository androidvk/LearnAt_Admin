package com.coremacasia.learnatadmin.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentSubjectsBinding;
import com.coremacasia.learnatadmin.dialogs.Dialog_Add_Subject;
import com.coremacasia.learnatadmin.helpers.SubjectHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;


import java.util.ArrayList;
import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder>
        implements Filterable {
    private static final String TAG = "SubjectsAdapter";
    private ArrayList<SubjectHelper> list = new ArrayList<>();

    private CommonDataModel commonDataModel;
    private Context activity;
    private int from;
    private String CAT;

    public SubjectsAdapter(Context activity, int from) {

        this.activity = activity;
        this.from = from;
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
        SubjectHelper helper = filteredSubjectList.get(position);
        holder.tCode.setText(helper.getCategory() + " | " + helper.getSubject_code());
        holder.tDesc.setText(helper.getDesc());
        holder.tTitle.setText(helper.getTitle());
        new ImageSetterGlide().defaultImg(holder.context, helper.getIcon(), holder.imageView);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from == 1) {
                    listener.onSubjectClick(helper);
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

    public void setDataModel(CommonDataModel commonDataModel) {
        this.commonDataModel = commonDataModel;
        if (CAT != null) {
            for (SubjectHelper sh : commonDataModel.getAll_subjects()) {
                if (sh.getCategory().equals(CAT)) {
                    list.add(sh);
                }
            }
        } else list = commonDataModel.getAll_subjects();


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
        private View mainView;

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