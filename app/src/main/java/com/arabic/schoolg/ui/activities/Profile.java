package com.arabic.schoolg.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arabic.schoolg.R;
import com.arabic.schoolg.databinding.ProfileBinding;
import com.arabic.schoolg.ui.Home;
import com.arabic.schoolg.ui.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
    ProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getUserDetails();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getBaseContext(), SignIn.class));
                finish();
            }
        });
    }

    private void getUserDetails() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String a = "null";
        db.collection("Students").document(String.valueOf(auth.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String name = task.getResult().get("name").toString();
                    String email = task.getResult().get("email").toString();
                    binding.studentName.setText(name);
                    binding.studentEmail.setText(email);
                } else {
                    Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), Home.class));
        finish();
    }
}