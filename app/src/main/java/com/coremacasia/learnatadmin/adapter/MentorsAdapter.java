package com.coremacasia.learnatadmin.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.ListMentorsBinding;
import com.coremacasia.learnatadmin.dialogs.Fragment_Add_Mentor;
import com.coremacasia.learnatadmin.helpers.MentorHelper;import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;


public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.ViewHolder>
        implements Filterable {
    private ArrayList<MentorHelper> list = new ArrayList<>();
    private CommonDataModel dataModel;

    private Context activity;
    private int from;
    private CommonDataModel commonDataModel;
    private static final String TAG = "MentorsAdapter";

    public MentorsAdapter(Context activity, int from) {

        this.activity = activity;

        this.from = from;
    }

    private OnMentorClickListener onMentorClickListener;
    public interface OnMentorClickListener{
        void onMentorClick(MentorHelper helper);
    }

    public void onMentorClick(OnMentorClickListener onMentorClickListener){
        this.onMentorClickListener=onMentorClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ListMentorsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MentorHelper helper = filteredMentorList.get(position);

        holder.tName.setText(helper.getName());

        new ImageSetterGlide().defaultImg(holder.itemView.getContext()
                , helper.getImage(), holder.imageView);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from==1){
                    onMentorClickListener.onMentorClick(helper);
                }else {
                    Gson gson = new Gson();
                    String myJson = gson.toJson(helper);
                    Bundle bundle = new Bundle();
                    bundle.putInt("from",2);
                    bundle.putString("helper",myJson);
                    FragmentManager manager = ((AppCompatActivity) activity)
                            .getSupportFragmentManager();

                    FragmentTransaction fragmenttransaction =
                            manager.beginTransaction();


                    Fragment_Add_Mentor frag = new Fragment_Add_Mentor();
                    frag.setArguments(bundle);
                    fragmenttransaction.replace(android.R.id.content, frag)
                            .addToBackStack(frag.TAG);
                    fragmenttransaction.commit();
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return filteredMentorList.size();
    }

    public void setDataModel(CommonDataModel commonDataModel) {
        this.commonDataModel = commonDataModel;
        list = commonDataModel.getMentors();


    }

    private List<MentorHelper> filteredMentorList = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredMentorList = list;
                } else {
                    List<MentorHelper> filteredList = new ArrayList<>();
                    for (MentorHelper row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                            /*|| row.getMentor_id().contains(charSequence)*/) {
                            filteredList.add(row);
                        }
                    }

                    filteredMentorList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMentorList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredMentorList = (ArrayList<MentorHelper>) filterResults.values;
                if (filteredMentorList.size() == 0) {
                    filteredMentorList = list;
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tName;
        private View mainView;
        public ViewHolder(ListMentorsBinding binding) {
            super(binding.getRoot());
            tName = binding.textView40;
            imageView = binding.imageView12;
            mainView=binding.mainView;
        }

    }

}