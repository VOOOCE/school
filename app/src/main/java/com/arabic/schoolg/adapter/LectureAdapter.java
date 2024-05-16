package com.arabic.schoolg.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.arabic.schoolg.data.Lecture;
import com.arabic.schoolg.databinding.LectureBinding;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.MyViewHolder> {
    private final AsyncListDiffer<Lecture> differ;
    private static OnClickListener onClick;

    public LectureAdapter(AsyncListDiffer<Lecture> differ, OnClickListener onClick) {
        this.differ = differ;
        this.onClick = onClick;
    }

    public interface OnClickListener {
        void onClick(Lecture lecture);
    }

    public LectureAdapter() {
        DiffUtil.ItemCallback<Lecture> diffUtil = new DiffUtil.ItemCallback<Lecture>() {
            @Override
            public boolean areItemsTheSame(@NonNull Lecture oldItem, @NonNull Lecture newItem) {
                return oldItem == newItem;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Lecture oldItem, @NonNull Lecture newItem) {
                return oldItem.equals(newItem);
            }

        };
        differ = new AsyncListDiffer<>(this, diffUtil);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        onClick = onClickListener;
    }

    public void submitList(List<Lecture> newList) {
        differ.submitList(newList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(LectureBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Lecture currentDoctor = differ.getCurrentList().get(position);
        holder.bind(currentDoctor);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final LectureBinding binding;

        public MyViewHolder(@NonNull LectureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Lecture lecture) {
            binding.teacherName.setText(lecture.getName());
            binding.teacherName.setText(lecture.getTeacher());
            binding.card.setOnClickListener(v -> {
                if (onClick != null) {
                    onClick.onClick(lecture);
                }
            });
        }
    }
}
