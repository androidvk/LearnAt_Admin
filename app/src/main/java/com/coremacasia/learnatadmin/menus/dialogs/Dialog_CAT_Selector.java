package com.coremacasia.learnatadmin.menus.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.databinding.DialogCatSelectorBinding;
import com.coremacasia.learnatadmin.databinding.ListCatBinding;
import com.coremacasia.learnatadmin.menus.helpers.CategoryHelper;
import com.coremacasia.learnatadmin.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Dialog_CAT_Selector extends DialogFragment {
    public static final String TAG = "Dialog_CAT_Selector";

    private DialogCatSelectorBinding binding;

    public static Dialog_CAT_Selector newInstance() {

        Bundle args = new Bundle();
        Dialog_CAT_Selector fragment = new Dialog_CAT_Selector();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTheme() {
        return DialogFragment.STYLE_NO_FRAME;
    }

    private RecyclerView recyclerView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DialogCatSelectorBinding.inflate(LayoutInflater.from(inflater.getContext()));
        recyclerView = binding.recyclerView;
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        Adapter adapter = new Adapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setCATList(MyStore.getCommonData().getCategory());
        adapter.notifyDataSetChanged();

    }

    class Adapter extends RecyclerView.Adapter<Dialog_CAT_Selector.Adapter.Holder> {

        private ArrayList<CategoryHelper> list;

        @NonNull
        @NotNull
        @Override
        public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new Holder(ListCatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
            CategoryHelper helper = list.get(position);
            holder.tTitle.setText(helper.getName());
            holder.tTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnCategoryClick(helper);
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setCATList(ArrayList<CategoryHelper> category) {
            this.list = category;
        }

        public class Holder extends RecyclerView.ViewHolder {
            private static final String TAG = "Holder";
            private TextView tTitle;

            public Holder(ListCatBinding binding) {
                super(binding.getRoot());
                tTitle = binding.textView34;
            }
        }

    }
    public CategoryClickListener listener;
    public interface CategoryClickListener{
       void OnCategoryClick(CategoryHelper helper);
        void OnError();
    }
    public void onCategoryClick(CategoryClickListener listener){
        this.listener=listener;
    }

}
