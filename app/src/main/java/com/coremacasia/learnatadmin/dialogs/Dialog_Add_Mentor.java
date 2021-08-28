package com.coremacasia.learnatadmin.dialogs;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.databinding.DialogAddMentorBinding;
import com.coremacasia.learnatadmin.helpers.CategoryHelper;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.utility.FileUtil;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.coremacasia.learnatadmin.utility.RMAP;
import com.coremacasia.learnatadmin.utility.Reference;
import com.coremacasia.learnatadmin.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION.SDK_INT;

import id.zelory.compressor.Compressor;


public class Dialog_Add_Mentor extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "DF_Add_Mentor";
    private static final int PICK_IMAGE = 101;
    private static int from;
    private static MentorHelper selectedMentorHelper;
    private EditText eName, eQualification, eImageLink;
    private DialogAddMentorBinding binding;
    private Button bSubmit, bUpdateImage;
    private ImageView iBack, iImage;
    private TextView tCategory;
    private DocumentReference commonListRef = Reference.superRef(RMAP.list);
    private List<CategoryHelper> categoryList = new ArrayList<>();
    private String sName, sQualification, sCAT, sImagelink;

    public static Dialog_Add_Mentor newInstance(int i, MentorHelper helper) {
        Dialog_Add_Mentor.from = i;
        selectedMentorHelper = helper;
        Bundle args = new Bundle();
        Dialog_Add_Mentor fragment = new Dialog_Add_Mentor();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogAddMentorBinding.inflate(LayoutInflater.from(inflater.getContext()));
        eName = binding.editTextTextPersonName4;
        eQualification = binding.editTextTextPersonName8;
        eImageLink = binding.editTextTextPersonName9;
        iImage = binding.imageView16;
        bSubmit = binding.button8;
        iBack = binding.imageView13;
        tCategory = binding.textView26;
        bUpdateImage = binding.button18;
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bUpdateImage.setVisibility(View.GONE);

        if (from == 2) {
            bSubmit.setText("Update");
            sCAT = selectedMentorHelper.getCategory();
            eImageLink.setText(selectedMentorHelper.getImage());
            //eQualification.setText(selectedMentorHelper.get);
            eName.setText(selectedMentorHelper.getName());
            tCategory.setText("Category: " + selectedMentorHelper.getCategory());
            new ImageSetterGlide().defaultImg(getContext(),
                    selectedMentorHelper.getImage(), iImage);
        }
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = eName.getText().toString().trim();
                sQualification = eQualification.getText().toString().trim();
                sImagelink = eImageLink.getText().toString().trim();

                if (!sName.equals("") /*&& !sQualification.equals("")*/ && !sImagelink.equals("")) {
                    if (from == 1) {
                        writeData();
                    } else {
                        updateData();
                    }

                } else Toast.makeText(getActivity(), "Input Fields", Toast.LENGTH_SHORT).show();
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Selector_CAT dialog = Dialog_Selector_CAT.newInstance();
                dialog.show(getActivity().getSupportFragmentManager(), Dialog_Selector_CAT.TAG);

                dialog.onCategoryClick(new Dialog_Selector_CAT.CategoryClickListener() {
                    @Override
                    public void OnCategoryClick(CategoryHelper helper) {
                        tCategory.setText("Category: " + helper.getName());
                        sCAT = helper.getId();
                    }

                    @Override
                    public void OnError() {

                    }
                });

            }
        });

        iImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkPermission()) {
                    showPermissionDialog();
                } else {
                    setFileManagerIntent();
                }

            }
        });


        bUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });

    }

    private void showPermissionDialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",
                        new Object[]{getActivity()
                                .getPackageName()})));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2000);

            }

        } else
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 333);
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(getActivity(),
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getActivity(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (read && write) {
                    setFileManagerIntent();
                } else {

                }
            }
        }
    }

    private void updateImage() {
        File path = null;

        try {
            path = FileUtil.from(getContext(), resultUri);
            File compressedImage = new Compressor(getActivity())
                    .setMaxWidth(600)
                    .setMaxHeight(600)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath
                            (Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(path);

            imageUploader(Uri.fromFile(compressedImage), compressedImage);
            //imageUploader(Uri.fromFile(path),path);

        } catch (IOException e) {
            Log.e(TAG, "processImage: ", e);

        }
    }

    private void imageUploader(Uri uri, File compressedImage) {
        Log.d(TAG, "imageUploader: ");
        //Get Image Size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        //Image Size End

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/webp")
                .setCustomMetadata(kMap.width, String.valueOf(imageWidth))
                .setCustomMetadata(kMap.height, String.valueOf(imageHeight)).build();

        StorageReference storageReference = Reference.getMentorImageRef().
                child(selectedMentorHelper.getMentor_id() + ".webp");
        bSubmit.setEnabled(false);
        storageReference.putFile(Uri.fromFile(compressedImage), metadata).addOnCompleteListener
                (new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: ");
                            task.getResult().getMetadata().getReference().getDownloadUrl().
                                    addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                sImagelink = task.getResult().toString();
                                                bSubmit.setEnabled(true);
                                            }
                                        }
                                    });


                        }
                    }
                });

    }

    private Uri resultUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ");

        if (requestCode == PICK_IMAGE) {
            if(data!=null){
                bUpdateImage.setVisibility(View.VISIBLE);
                bSubmit.setEnabled(false);
                resultUri = data.getData();
                iImage.setImageURI(resultUri);
            }


        }
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                setFileManagerIntent();
            }
        }
    }

    private void setFileManagerIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }


    private void updateData() {
        String mentor_id = selectedMentorHelper.getMentor_id();
        Map map = new HashMap();
        map.put(kMap.name, sName);
        map.put(kMap.mentor_id, mentor_id);
        map.put(kMap.image, sImagelink);
        map.put(kMap.category, sCAT);

        ArrayList<MentorHelper> list = MyStore.getCommonData().getMentors();
        int position = 0;
        for (MentorHelper mentorHelper : list) {
            if (mentorHelper.getMentor_id().equals(selectedMentorHelper.getMentor_id())) {
                break;
            }
            position++;
        }
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(map);
        MentorHelper helper = gson.fromJson(jsonElement, MentorHelper.class);
        list.set(position, helper);

        Map map1 = new HashMap();
        map1.put(kMap.mentors, list);

        Reference.superRef(kMap.list).set(map1, SetOptions.merge()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismiss();
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void writeData() {
        DocumentReference documentReference = FirebaseFirestore.
                getInstance().collection("df").document();
        String mentor_id = documentReference.getId();
        Map map = new HashMap();
        map.put(kMap.name, sName);
        map.put(kMap.mentor_id, mentor_id);
        map.put(kMap.image, sImagelink);
        map.put(kMap.category, sCAT);

        Map map1 = new HashMap();
        map1.put(kMap.mentors, FieldValue.arrayUnion(map));
        Reference.superRef(kMap.list).set(map1, SetOptions.merge()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismiss();
                        }
                    }
                }
        );
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        if (categoryList.size() != 0) {
            sCAT = categoryList.get(position).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
