package com.coremacasia.learnatadmin.menus.category.CatDetail;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.menus.subjects.SubjectHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CATSubjectAdapter extends RecyclerView.Adapter<CATSubjectAdapter.Holder> {
    private Activity activity;
    private static final String TAG = "SubjectAdapter";
    private CommonDataModel dataModel;
    private ArrayList<String> list = new ArrayList<>();
    private String CAT;

    public void setDataModel(CommonDataModel dataModel, String CAT) {
        this.dataModel = dataModel;
        list = dataModel.getSubject_id();
        this.CAT = CAT;
    }

    public CATSubjectAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_subjects, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        String subjectId = list.get(position);
        ArrayList<SubjectHelper> mainSubjectList = MyStore.getCommonData().getAll_subjects();
        for (SubjectHelper helper1 : mainSubjectList) {
            if (subjectId.equals(helper1.getSubject_id())) {
                new ImageSetterGlide().defaultImg(holder.itemView.getContext(),
                        helper1.getIcon(), holder.imageView);
                holder.tName.setText(helper1.getTitle());
                return;
            }
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;

    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Subject Holder";
        private TextView tName;
        private CircleImageView imageView;
        private View mainView;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.textView38);
            imageView = itemView.findViewById(R.id.imageView11);
            mainView = itemView.findViewById(R.id.mainView);
        }

    }
}
