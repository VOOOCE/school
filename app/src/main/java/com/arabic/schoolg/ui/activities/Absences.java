package com.arabic.schoolg.ui.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arabic.schoolg.adapter.AbsencseAdapter;
import com.arabic.schoolg.data.Absence;
import com.arabic.schoolg.databinding.AbsencesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Absences extends AppCompatActivity {
    AbsencesBinding binding;
    AbsencseAdapter absencseAdapter;
    //    ArrayList<Absence> absences;
    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference storageRef;
    static final int PICK_IMAGE_REQUEST = 1;
    static Absence ABSENCE = null;
    Uri imageUri;

    Snackbar snackbar;
    TextView cardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AbsencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("absences_files");

        getAbsences();
        setUpAbsences();


        snackbar = Snackbar.make(binding.getRoot(), "Uploading..", Snackbar.LENGTH_INDEFINITE);
//
//        absences = new ArrayList<Absence>();
//        absences.add(new Absence("0", "الرياضيات", "يوم 10/12/2023", false));
//        absences.add(new Absence("1", "اللغة العربية", "يوم 7/9/2023", true));
//        absences.add(new Absence("2", "اللغة الانجليزية", "يوم 11/6/2023", false));
//        absences.add(new Absence("3", "التاريخ", "يوم 5/3/2023", true));

        absencseAdapter.setOnClickListener(new AbsencseAdapter.OnClickListener() {
            @Override
            public void onClick(Absence absence, TextView textView) {
                if (!absence.getHasReason()) {
                    // Put Absence Reason -->
                    //Snackbar.make(binding.getRoot(), absence.getTitle(), Snackbar.LENGTH_LONG).show();
                    openFileChooser();
                    cardTextView = textView;
                    ABSENCE = absence;
                }
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.loading.setVisibility(View.INVISIBLE);
//                binding.absences.setVisibility(View.VISIBLE);
//                absencseAdapter.submitList(absences);
//            }
//        }, 1000);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getAbsences() {
        db.collection("Absences").whereEqualTo("email", String.valueOf(auth.getCurrentUser().getEmail())).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    binding.progress.setVisibility(View.INVISIBLE);
                    binding.loadingText.setText("No Absences :)");
                } else {
                    List<Absence> absences = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                        // Access each document
                        Map<String, Object> data = documentSnapshot.getData();
                        // Handle the document data
                        absences.add(
                                new Absence(
                                        documentSnapshot.getId(),
                                        String.valueOf(data.get("topic")),
                                        String.valueOf(data.get("time")),
                                        (Boolean) data.get("hasReason"),
                                        String.valueOf(data.get("history")),
                                        String.valueOf(data.get("email")),
                                        String.valueOf(data.get("linkReason"))));
                    }
                    binding.loading.setVisibility(View.INVISIBLE);
                    binding.absences.setVisibility(View.VISIBLE);
                    absencseAdapter.submitList(absences);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(binding.getRoot(), "Error : " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setUpAbsences() {
        absencseAdapter = new AbsencseAdapter();
        binding.absences.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        binding.absences.setAdapter(absencseAdapter);
    }

    private void uploadFile(Uri imageUri) {
        if (imageUri != null) {
            snackbar.show();
            StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + String.valueOf(Math.random() * Math.random()));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Map<String, Object> absence = new HashMap<>();
                                    absence.put("email", ABSENCE.getEmail());
                                    absence.put("history", ABSENCE.getHistory());
                                    absence.put("hasReason", true);
                                    absence.put("reasonLink", downloadUrl);
                                    absence.put("time", ABSENCE.getTime());
                                    absence.put("topic", ABSENCE.getTopic());
                                    db.collection("Absences").document(ABSENCE.getId()).set(absence).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            snackbar.setText("Success");
                                            getAbsences();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(MainActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            //Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadFile(imageUri);
        }
    }
}