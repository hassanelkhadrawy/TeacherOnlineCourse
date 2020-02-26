package com.example.teacheronlinecourse.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.AdminModel;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.Models.SearchModel;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {


    private TextView profName;
    private TextView profImagetxt;
    private TextView profEmail;
    private TextView userCourses;
    private TextView SinOut;
    private TextView ExamScor;
    private TextView addCourse;
    private TextView addAdmin;
    private TextView Edit;
    private TextView Users;
    private ImageView proImage;
    private ImageView editProfImage;
    private EditText editTextName;
    private EditText editTextPassword;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ImageButton addCourseImage;
    private Comfortaa_Regular addCourseName;
    private Comfortaa_Regular addCourseDes;
    private Comfortaa_Bold cancleAddCourse;
    private Comfortaa_Bold uploadCourse;
    private Uri ImageUri=null;
    private LinearLayout profCountainer;
    private TextView addCategoryName;
    private Comfortaa_Bold cancleAddCategory;
    private Comfortaa_Bold uploadategory;
    private TextView addcategory;
    private Spinner categorySpinner;
    private ArrayAdapter<String> adapterCategory;
    private ArrayList<String> categoryList = new ArrayList<>();



    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        hideKeyboard(getActivity());
        initView(view);
        HideAdminItems();
        Action();
        return view;
    }


    private void initView(View view) {
        profName = view.findViewById(R.id.prof_Name);
        profImagetxt = view.findViewById(R.id.prof_img);
        profEmail = view.findViewById(R.id.prof_Email);
        userCourses = view.findViewById(R.id.usercourses);
        SinOut = view.findViewById(R.id.sinout);
        ExamScor = view.findViewById(R.id.examscors);
        addCourse = view.findViewById(R.id.addCourse);
        addAdmin = view.findViewById(R.id.add_admin);
        Edit = view.findViewById(R.id.edit);
        Users=view.findViewById(R.id.users);
        proImage = view.findViewById(R.id.proImage);
        profCountainer = (LinearLayout) view.findViewById(R.id.prof_countainer);
        addcategory = view.findViewById(R.id.addcategory);


        AddCAtegoryToList();
    }


    private void Action() {

        profImagetxt.setText("");
        String x = Commans.registerModel.getName();
        String[] myName = x.split(" ");
        for (int i = 0; i < myName.length; i++) {
            String s = myName[i];
            profImagetxt.append("" + s.charAt(0));
        }

        profName.setText(Commans.registerModel.getName());
        profEmail.setText(Commans.registerModel.getEmail());

        if (Commans.registerModel.getImage().equals("null")) {
            proImage.setVisibility(View.GONE);
        } else {
            profImagetxt.setVisibility(View.GONE);
            proImage.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(Commans.registerModel.getImage()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(proImage);

        }

        SinOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveState("null","null");
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });

        ExamScor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ExamScors.class));
            }
        });
        userCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserCourses.class));
            }
        });
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
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OldPassword();
            }
        });
        Users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),UsersAvtivity.class));
            }
        });
        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAdmin();

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
               SelectImage(1);
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

    private void SelectImage(int REQUEST_CODE ) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), REQUEST_CODE);

    }


    private void AddCAtegoryToList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoryList.clear();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ImageUri = data.getData();
                addCourseImage.setImageURI(ImageUri);
            } else if (requestCode == 2) {
                ImageUri = data.getData();
                editProfImage.setImageURI(ImageUri);
            }

        } else {
            Toast.makeText(getActivity(), "faild", Toast.LENGTH_SHORT).show();


        }
    }

    private void UploadCourseData(final AlertDialog dialog) {
        String imgName = UUID.randomUUID().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(categorySpinner.getSelectedItem().toString());
        storageReference = FirebaseStorage.getInstance().getReference("images/" + imgName);
        final String courseID = String.valueOf(System.currentTimeMillis());

        if (ImageUri != null) {
            storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            CourseModel courseModel = new CourseModel(uri.toString(), addCourseName.getText().toString(), addCourseDes.getText().toString(), courseID);
                            databaseReference.child(courseID).setValue(courseModel);

                            // add to search

                            databaseReference = FirebaseDatabase.getInstance().getReference("Search");
                            SearchModel searchModel = new SearchModel(categorySpinner.getSelectedItem().toString(), courseID, addCourseName.getText().toString());
                            databaseReference.child(courseID).setValue(searchModel);

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
            CourseModel courseModel = new CourseModel("null", addCourseName.getText().toString(), addCourseDes.getText().toString(), courseID);
            databaseReference.child(courseID).setValue(courseModel);
            //  add to search

            databaseReference = FirebaseDatabase.getInstance().getReference("Search");
            SearchModel searchModel = new SearchModel(categorySpinner.getSelectedItem().toString(), courseID, addCourseName.getText().toString());
            databaseReference.child(courseID).setValue(searchModel);

            dialog.dismiss();
            Commans.progressDialog.dismiss();
            Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

        }

    }

    private void saveState(String Email,String Password) {
        SharedPreferences aSharedPreferences = getActivity().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putString("Email", Email);
        aSharedPreferencesEdit.putString("Password", Password);
        aSharedPreferencesEdit.commit();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void AddAdmin() {


        final boolean[] Flag = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText editText = new EditText(getActivity());

        editText.setHint(getString(R.string.enter_password));
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(editText);


        builder.setCancelable(false);
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button Positive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button Cancel = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Flag[0]) {

                            if (!TextUtils.isEmpty(editText.getText().toString())) {

                                if (Commans.registerModel.getPassword().equals(editText.getText().toString())) {
                                    editText.setText("");
                                    editText.setHint(R.string.okaddadminemail);
                                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                                    Flag[0] = false;

                                } else {
                                    Toast.makeText(getActivity(), R.string.password_wrong, Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            if (!TextUtils.isEmpty(editText.getText().toString())) {

                                databaseReference = FirebaseDatabase.getInstance().getReference("Admins");
                                AdminModel adminModel = new AdminModel(editText.getText().toString());
                                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(adminModel);
                                Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                                mAlertDialog.dismiss();

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();

                            }
                        }


                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);


    }

    private void HideAdminItems() {
        for (int i = 0; i < Commans.adminList.size(); i++) {
            if (!Commans.registerModel.getEmail().equals(Commans.adminList.get(i))) {
                addAdmin.setVisibility(View.GONE);
                addcategory.setVisibility(View.GONE);
                addCourse.setVisibility(View.GONE);
                Users.setVisibility(View.GONE);
            }else {
                addAdmin.setVisibility(View.VISIBLE);
                addcategory.setVisibility(View.VISIBLE);
                addCourse.setVisibility(View.VISIBLE);
                Users.setVisibility(View.VISIBLE);
                break;
            }
        }

    }

    private void EditALert() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View viewcountainer = LayoutInflater.from(getActivity()).inflate(R.layout.edit_item, null);
        editProfImage = viewcountainer.findViewById(R.id.editprofImage);
        ImageView SelectImage = viewcountainer.findViewById(R.id.selecteditprofImage);
        editTextName = viewcountainer.findViewById(R.id.editName);
        editTextPassword = viewcountainer.findViewById(R.id.editPassword);

        if (!Commans.registerModel.getImage().equals("null")) {
            Picasso.with(getActivity()).load(Commans.registerModel.getImage()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(editProfImage);

        }

        editTextName.setText(Commans.registerModel.getName());
        editTextPassword.setText(Commans.registerModel.getPassword());

        builder.setView(viewcountainer);

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SelectImage(2);
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button Positive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button Cancel = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                            if (editTextName.getText().toString().isEmpty()) {

                                Toast.makeText(getActivity(), getString(R.string.enter_name), Toast.LENGTH_SHORT).show();


                            }else if (editTextPassword.getText().toString().isEmpty()){

                                Toast.makeText(getActivity(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();

                            } else if (editTextPassword.getText().toString().length() <6){
                                Toast.makeText(getActivity(), "Password should more than 6 charcters", Toast.LENGTH_SHORT).show();

                            }else {
                                Commans.Prograss(getActivity(),getString(R.string.waiting));
                                Commans.progressDialog.show();

                                String imgName = UUID.randomUUID().toString();
                                storageReference = FirebaseStorage.getInstance().getReference("images/" + imgName);

                                if (ImageUri != null) {
                                    storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Commans.registerModel.getEmail().replace(".", "Dot"));
                                                    RegisterModel registerModel = new RegisterModel(editTextName.getText().toString(), Commans.registerModel.getEmail(),editTextPassword.getText().toString(), uri.toString());
                                                    databaseReference.setValue(registerModel);
                                                    Commans.registerModel = registerModel;
                                                    saveState(Commans.registerModel.getEmail(),editTextPassword.getText().toString());
                                                    ImageUri = null;
                                                    mAlertDialog.dismiss();
                                                    Commans.progressDialog.dismiss();
                                                    Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Commans.registerModel.getEmail().replace(".", "Dot"));
                                            RegisterModel registerModel = new RegisterModel(editTextName.getText().toString(), Commans.registerModel.getEmail(), editTextPassword.getText().toString(), Commans.registerModel.getImage());
                                            databaseReference.setValue(registerModel);
                                            Commans.registerModel = registerModel;
                                            saveState(Commans.registerModel.getEmail(),editTextPassword.getText().toString());
                                            mAlertDialog.dismiss();
                                            Commans.progressDialog.dismiss();
                                            Snackbar.make(profCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                                        }
                                    });

                                } else {

                                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Commans.registerModel.getEmail().replace(".", "Dot"));
                                    RegisterModel registerModel = new RegisterModel(editTextName.getText().toString(), Commans.registerModel.getEmail(), editTextPassword.getText().toString(), Commans.registerModel.getImage());
                                    databaseReference.setValue(registerModel);
                                    Commans.registerModel = registerModel;
                                    saveState(Commans.registerModel.getEmail(),editTextPassword.getText().toString());
                                    mAlertDialog.dismiss();
                                    Commans.progressDialog.dismiss();
                                    Snackbar.make(profCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                                }
                            }



                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background_withborder);


    }

    private void OldPassword() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText editText = new EditText(getActivity());

        editText.setHint(getString(R.string.enter_password));
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(editText);


        builder.setCancelable(false);
        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button Positive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button Cancel = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!TextUtils.isEmpty(editText.getText().toString())) {

                            if (Commans.registerModel.getPassword().equals(editText.getText().toString())) {

                                EditALert();
                                mAlertDialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), R.string.password_wrong, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();

                        }
                    }



                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);


    }



}
