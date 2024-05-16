package com.arabic.schoolg.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arabic.schoolg.databinding.SignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignIn extends AppCompatActivity {
    //FirebaseAuth auth = FirebaseAuth.getInstance();

    SignInBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Auth aaa", binding.email.getText().toString() + binding.password.getText().toString());
                if (!"".equals((String) (Objects.requireNonNull(binding.email.getText())).toString()) && !"".equals((String) (Objects.requireNonNull(binding.password.getText())).toString())) {
                    signIn(binding.email.getText().toString(), binding.password.getText().toString());
                }else {
                    Snackbar.make(binding.getRoot(), "Fill User Name & Email", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void signIn(String email, String password) {
        binding.signInBtn.setText("Signing ..");
        binding.signInBtn.setClickable(false);
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getBaseContext(), Home.class));
                            binding.signInBtn.setText("Sign in");
                            finish();
                        } else {
                            binding.signInBtn.setText("Sign in");
                            binding.signInBtn.setClickable(true);
                            Snackbar.make(binding.getRoot(), "Something went Error :(", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

        );
    }
}