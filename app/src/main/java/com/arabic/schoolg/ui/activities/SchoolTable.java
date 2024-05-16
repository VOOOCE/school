package com.arabic.schoolg.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.arabic.schoolg.adapter.ScheduleAdapter;
import com.arabic.schoolg.data.Lecture;
import com.arabic.schoolg.databinding.SchoolTableBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolTable extends AppCompatActivity {
    private SchoolTableBinding binding;
    private ScheduleAdapter scheduleAdapter;
    private FirebaseFirestore db;
    private List<Map<String, Object>> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SchoolTableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        int numberOfColumns = 6; // 5 أيام + 1 لعرض الساعات
        binding.lecturesTable.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        String[][] scheduleData = {
                {"", "8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "2:00 - 3:00", "4:00 - 5:00"},
                {"sunday", "رياضيات - د. أحمد", "فيزياء - د. خالد", "", "كيمياء - د. علي", ""},
                {"monday", "رياضيات - د. أحمد", "فيزياء - د. خالد", "", "كيمياء - د. علي", ""},
                {"Tuesday", "", "رياضيات - د. أحمد", "", "", "فيزياء - د. خالد"},
                {"Wednesday", "كيمياء - د. علي", "", "رياضيات - د. أحمد", "", ""},
                {"Thursday", "", "", "", "", "كيمياء - د. علي"},
        };


        Map<String, Object> data1 = new HashMap<>();
        data1.put("day", "Thursday");
        data1.put("start", 8);
        data1.put("end", 9);
        data1.put("teacherName", "omar");
        data1.put("topic", "math");


        Map<String, Object> data2 = new HashMap<>();
        data2.put("day", "Thursday");
        data2.put("start", 9);
        data2.put("end", 10);
        data2.put("teacherName", "omar");
        data2.put("topic", "math");

        Map<String, Object> data3 = new HashMap<>();
        data3.put("day", "Thursday");
        data3.put("start", 10);
        data3.put("end", 11);
        data3.put("teacherName", "ahmad");
        data3.put("topic", "math");

        Map<String, Object> data4 = new HashMap<>();
        data4.put("day", "Thursday");
        data4.put("start", 11);
        data4.put("end", 12);
        data4.put("teacherName", "ahmad");
        data4.put("topic", "math");

        Map<String, Object> data5 = new HashMap<>();
        data5.put("day", "Thursday");
        data5.put("start", 2);
        data5.put("end", 3);
        data5.put("teacherName", "ahmad");
        data5.put("topic", "math");

        Map<String, Object> data6 = new HashMap<>();
        data6.put("day", "Thursday");
        data6.put("start", 3);
        data6.put("end", 4);
        data6.put("teacherName", "ahmad");
        data6.put("topic", "math");

        Map<String, Object> data7 = new HashMap<>();
        data7.put("day", "Thursday");
        data7.put("start", 4);
        data7.put("end", 5);
        data7.put("teacherName", "ahmad");
        data7.put("topic", "math");

        List<Map<String, Object>> lectures = Arrays.asList(data1, data2, data3, data4, data5, data6, data7);

        for (Map<String, Object> data : lectures) {
            db.collection("Lectures").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    userList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        userList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userList.add(document.getData());
                            Log.e("All Data",userList.toString());
                        }
                    } else {
                        Snackbar.make(binding.getRoot(), "Error, ", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }

        scheduleAdapter = new ScheduleAdapter(scheduleData);
        binding.lecturesTable.setAdapter(scheduleAdapter);

    }
}