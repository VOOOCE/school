package com.arabic.schoolg.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.arabic.schoolg.R;
import com.arabic.schoolg.adapter.CategoryAdapter;
import com.arabic.schoolg.data.Category;
import com.arabic.schoolg.databinding.HomeBinding;
import com.arabic.schoolg.ui.activities.Absences;
import com.arabic.schoolg.ui.activities.Notifications;
import com.arabic.schoolg.ui.activities.Profile;
import com.arabic.schoolg.ui.activities.SchoolTable;
import com.arabic.schoolg.ui.activities.Settings;
import com.arabic.schoolg.ui.activities.UploadedFiles;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class Home extends AppCompatActivity {
    private HomeBinding binding;
    private CategoryAdapter categoryAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getStudentName();
        if (!AppCompatDelegate.getApplicationLocales().toLanguageTags().equals("ar")) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"));
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setUpCategories();
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(0, R.drawable.table_icon, "الجدول"));
        categories.add(new Category(1, R.drawable.documents_icon, "الملفات"));
        categories.add(new Category(2, R.drawable.calendar_icon, "الحضور"));
        //categories.add(new Category(3, R.drawable.settings_icon, "الاعدادات"));
        categories.add(new Category(4, R.drawable.notification_icon, "الاشعارات"));
        categoryAdapter.submitList(categories);

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Profile.class));
                finish();
            }
        });
    }

    private void getStudentName() {
        db.collection("Students").document(String.valueOf(auth.getUid())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                binding.studentName.setText(String.valueOf(documentSnapshot.get("name")));
            }
        });
    }

    private void setUpCategories() {
        categoryAdapter = new CategoryAdapter();
        binding.categoryRecycler.setLayoutManager(new GridLayoutManager(getBaseContext(), 4));
        binding.categoryRecycler.setAdapter(categoryAdapter);
        categoryAdapter.setOnClickListener(new CategoryAdapter.OnClickListener() {
            @Override
            public void onClick(Category category) {
                switch (category.getId()) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), SchoolTable.class));
                        break;
                    case 1:
                        startActivity(new Intent(getBaseContext(), UploadedFiles.class));
                        break;
                    case 2:
                        startActivity(new Intent(getBaseContext(), Absences.class));
                        break;
                    case 3:
                        startActivity(new Intent(getBaseContext(), Settings.class));
                        break;
                    case 4:
                        startActivity(new Intent(getBaseContext(), Notifications.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}