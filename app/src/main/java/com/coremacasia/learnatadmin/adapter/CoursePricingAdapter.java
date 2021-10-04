package com.coremacasia.learnatadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.ListCoursePriceBinding;
import com.coremacasia.learnatadmin.databinding.ListNamesBinding;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.helpers.CoursePriceHelper;

import java.util.ArrayList;

public class CoursePricingAdapter extends RecyclerView.Adapter<CoursePricingAdapter.Holder> {
    private ArrayList<CoursePriceHelper> list;
    private Button bAddPrice;
    private FragmentActivity activity;

    public CoursePricingAdapter(ArrayList<CoursePriceHelper> list, Button bAddPrice,
                                FragmentActivity activity) {
        this.list = list;
        this.bAddPrice = bAddPrice;
        this.activity = activity;

        bAddPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CourseFillDialog(activity).show();
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ListCoursePriceBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CoursePriceHelper helper=list.get(holder.getAbsoluteAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tPrice,tDuration;
        private ImageView iCancel;
        public Holder(@NonNull ListCoursePriceBinding itemView) {
            super(itemView.getRoot());
            tPrice=itemView.textView62;
            tDuration=itemView.textView63;
            iCancel=itemView.imageView18;
        }
    }
    class CourseFillDialog extends AlertDialog{
        private static final String TAG = "CourseFillDialog";
        protected CourseFillDialog(Context context) {
            super(context);
        }
        private EditText ePrice,eDuration;
        private RadioGroup radioGroup;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_course_price_entry);
        }
    }
}
