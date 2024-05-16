package com.arabic.schoolg.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arabic.schoolg.databinding.SignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    SignUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAbsences();
                //signUpUser(String.valueOf(binding.email.getText()), String.valueOf(binding.password.getText()), String.valueOf(binding.name.getText()));
            }
        });
    }

    private void enterAbsences() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", "ammar@gmail.com");
        user.put("history", "22/03/2024");
        user.put("time", "14 - 16");
        user.put("topic", "math");
        user.put("hasReason", false);
        user.put("reasonLink","");
        db.collection("Absences").document().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("ADDED TO FIRE", String.valueOf(task.getResult()));
            }
        });
    }

    private void signUpUser(String email, String password, String name) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    binding.signUp.setText("جاري التسجيل");
                    Map<String, String> user = new HashMap<>();
                    user.put("email", email);
                    user.put("name", name);
                    db.collection("Students").document(String.valueOf(task.getResult().getUser().getUid())).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(binding.getRoot(), "Success", Snackbar.LENGTH_LONG).show();
                                binding.signUp.setText("تسجيل");
                            } else {
                                Snackbar.make(binding.getRoot(), "Faild", Snackbar.LENGTH_LONG).show();
                                binding.signUp.setText("تسجبل");
                            }
                        }
                    });
                } else {
                    Snackbar.make(binding.getRoot(), "Error" + task.getException().getMessage().toString(), Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }
}