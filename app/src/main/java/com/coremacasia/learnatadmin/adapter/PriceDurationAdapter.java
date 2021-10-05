package com.coremacasia.learnatadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.ListCoursePriceBinding;
import com.coremacasia.learnatadmin.helpers.PriceDurationHelper;

import java.util.ArrayList;

public class PriceDurationAdapter extends RecyclerView.Adapter<PriceDurationAdapter.Holder> {
    private ArrayList<PriceDurationHelper> list;
    private Button bAddPrice;
    private FragmentActivity activity;

    public PriceDurationAdapter(ArrayList<PriceDurationHelper> list, Button bAddPrice,
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
        PriceDurationHelper helper = list.get(holder.getAbsoluteAdapterPosition());

        holder.tPrice.setText("Rs."+helper.getPrice());
        holder.tDuration.setText(helper.getDuration()+" "+helper.getDuration_unit());
        holder.iCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tPrice, tDuration;
        private ImageView iCancel;

        public Holder(@NonNull ListCoursePriceBinding itemView) {
            super(itemView.getRoot());
            tPrice = itemView.textView62;
            tDuration = itemView.textView63;
            iCancel = itemView.imageView18;
        }
    }

    class CourseFillDialog extends AlertDialog {
        private static final String TAG = "CourseFillDialog";

        protected CourseFillDialog(Context context) {
            super(context);
        }

        private EditText ePrice, eDuration;
        private RadioGroup radioGroup;
        private Button bAdd;
        private String durationUnit;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().clearFlags(WindowManager
                    .LayoutParams.FLAG_NOT_FOCUSABLE
                    |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            setContentView(R.layout.dialog_course_price_entry);
            ePrice = findViewById(R.id.editTextTextPersonName9);
            eDuration = findViewById(R.id.editTextTextPersonName15);
            radioGroup = findViewById(R.id.radio);
            bAdd = findViewById(R.id.button20);
            RadioButton rb1 = findViewById(R.id.rbmoths);
            getRadioValue();
            rb1.setChecked(true);

            bAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getInputs();
                }
            });
        }

        private void getInputs() {
            String price = ePrice.getText().toString().trim();
            String duration = eDuration.getText().toString().trim();
            Log.e(TAG, "getInputs: " + durationUnit);
            if (!price.equals("") & !duration.equals("")) {
                PriceDurationHelper helper=new PriceDurationHelper(durationUnit,price,duration);
                list.add(helper);
                notifyDataSetChanged();
                dismiss();
                eDuration.setText("");
                ePrice.setText("");
            }
        }

        private void getRadioValue() {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    durationUnit = rb.getText().toString();
                }
            });
        }
    }
}
