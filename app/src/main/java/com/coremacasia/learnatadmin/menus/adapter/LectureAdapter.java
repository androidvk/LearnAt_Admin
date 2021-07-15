package com.coremacasia.learnatadmin.menus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.databinding.ListLectureBinding;
import com.coremacasia.learnatadmin.menus.helpers.LectureHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.Holder> {
    private List<LectureHelper> list;

    public LectureAdapter(Context context) {

    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Holder(ListLectureBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        LectureHelper helper=list.get(position);
        holder.tTitle.setText(helper.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLectureList(List<LectureHelper> list) {
        this.list = list;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Holder";
        private TextView tTitle;
        public Holder(@NotNull @NonNull ListLectureBinding itemView) {
            super(itemView.getRoot());
            tTitle=itemView.textView35;
        }
    }
}
