package com.example.teacheronlinecourse.Activities;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {


    private TextView profName;
    private TextView profEmail;
    private TextView student;
    private TextView setting;
    private TextView addCourse;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private ImageButton addCourseImage;
    private Comfortaa_Regular addCourseName;
    private Comfortaa_Regular addCourseDes;
    private Comfortaa_Bold cancleAddCourse;
    private Comfortaa_Bold uploadCourse;
    private Uri ImageUri;
    private LinearLayout profCountainer;
    private TextView addCategoryName;
    private Comfortaa_Bold cancleAddCategory;
    private Comfortaa_Bold uploadategory;
    private TextView addcategory;
    private Spinner categorySpinner;
    private ArrayAdapter<String> adapterCategory;
    ArrayList<String>categoryList=new ArrayList<>();

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        initView(view);
        Action();
        return view;
    }

    private void initView(View view) {
        profName = view.findViewById(R.id.prof_Name);
        profEmail =  view.findViewById(R.id.prof_Email);
        student =  view.findViewById(R.id.student);
        setting =  view.findViewById(R.id.setting);
        addCourse =  view.findViewById(R.id.addCourse);
        profCountainer = (LinearLayout) view.findViewById(R.id.prof_countainer);
        addcategory =  view.findViewById(R.id.addcategory);
        AddCAtegoryToList();
    }

    private void Action() {
        profName.setText(Commans.registerModel.getName());
        profEmail.setText(Commans.registerModel.getEmail());


        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCourseDialog();
            }
        });

        addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryDialog();
            }
        });

    }

    private void AddCourseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_course_item, null);
        addCourseImage = (ImageButton) view.findViewById(R.id.add_course_image);
        addCourseName = (Comfortaa_Regular) view.findViewById(R.id.addCourseName);
        addCourseDes = (Comfortaa_Regular) view.findViewById(R.id.addCoursDescribtion);
        cancleAddCourse = (Comfortaa_Bold) view.findViewById(R.id.cancleAddCourse);
        uploadCourse = (Comfortaa_Bold) view.findViewById(R.id.uploadCourse);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);

        Commans.Prograss(getActivity(), getString(R.string.waiting));


        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();



        adapterCategory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoryList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#C3464444"));
                return view;
            }
        };
        categorySpinner.setAdapter(adapterCategory);



        addCourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        uploadCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(addCourseName.getText().toString())) {
                    Commans.progressDialog.show();
                    UploadCourseData(dialog);
                } else {
                    Snackbar.make(profCountainer, getString(R.string.enter_name), Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        cancleAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);
    }

    private void AddCAtegoryToList() {
        categoryList.clear();
        databaseReference=FirebaseDatabase.getInstance().getReference("Category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        CategoryModel categoryModel = categorySnapshot.getValue(CategoryModel.class);
                        categoryList.add(categoryModel.getName());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void AddCategoryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_category_item, null);

        addCategoryName = (Comfortaa_Regular) view.findViewById(R.id.addCategoryName);
        cancleAddCategory = (Comfortaa_Bold) view.findViewById(R.id.cancleAddCategory);
        uploadategory = (Comfortaa_Bold) view.findViewById(R.id.uploadategory);
        Commans.Prograss(getActivity(), getString(R.string.waiting));


        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();


        uploadategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(addCategoryName.getText().toString())) {
                    Commans.progressDialog.show();
                    UploadCategoryData(dialog);
                } else {
                    Snackbar.make(profCountainer, getString(R.string.enter_name), Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        cancleAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);
    }

    private void UploadCategoryData(AlertDialog dialog) {
        final String categoryID = UUID.randomUUID().toString();

        CategoryModel categoryModel = new CategoryModel(addCategoryName.getText().toString(), categoryID);
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");
        databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(categoryModel);
        dialog.dismiss();
        Commans.progressDialog.dismiss();
        Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            ImageUri = data.getData();
            addCourseImage.setImageURI(ImageUri);

        } else {
            Toast.makeText(getActivity(), "faild", Toast.LENGTH_SHORT).show();


        }
    }

    private void UploadCourseData(final AlertDialog dialog) {
        String imgName = UUID.randomUUID().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(categorySpinner.getSelectedItem().toString());
        storageReference = FirebaseStorage.getInstance().getReference("images/"+imgName);
        final String courseID = UUID.randomUUID().toString();

        if (ImageUri != null) {
            storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            CourseModel courseModel = new CourseModel(uri.toString(), addCourseName.getText().toString(),addCourseDes.getText().toString(), courseID);
                            databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(courseModel);
                            ImageUri = null;
                            dialog.dismiss();
                            Commans.progressDialog.dismiss();
                            Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Commans.progressDialog.dismiss();
                    Snackbar.make(profCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                }
            });
        } else {
            CourseModel courseModel = new CourseModel("null", addCourseName.getText().toString(),addCourseDes.getText().toString(), courseID);
            databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(courseModel);
            dialog.dismiss();
            Commans.progressDialog.dismiss();
            Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

        }

    }
}
