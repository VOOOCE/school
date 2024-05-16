package com.arabic.schoolg.adapter;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arabic.schoolg.R;
import com.arabic.schoolg.data.Absence;
import com.arabic.schoolg.databinding.AbsenceItemBinding;

import java.util.List;

public class AbsencseAdapter extends RecyclerView.Adapter<AbsencseAdapter.MyViewHolder> {
    private final AsyncListDiffer<Absence> differ;
    private static OnClickListener onClick;

    public AbsencseAdapter(AsyncListDiffer<Absence> differ, OnClickListener onClick) {
        this.differ = differ;
        this.onClick = onClick;
    }

    public interface OnClickListener {
        void onClick(Absence absence, TextView textView);
    }

    public AbsencseAdapter() {
        DiffUtil.ItemCallback<Absence> diffUtil = new DiffUtil.ItemCallback<Absence>() {
            @Override
            public boolean areItemsTheSame(@NonNull Absence oldItem, @NonNull Absence newItem) {
                return oldItem == newItem;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Absence oldItem, @NonNull Absence newItem) {
                return oldItem.equals(newItem);
            }

        };
        differ = new AsyncListDiffer<>(this, diffUtil);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        onClick = onClickListener;
    }

    public void submitList(List<Absence> newList) {
        differ.submitList(newList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(AbsenceItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Absence currentAbsences = differ.getCurrentList().get(position);
        holder.bind(currentAbsences);
        holder.setIsRecyclable(true);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final AbsenceItemBinding binding;

        public MyViewHolder(@NonNull AbsenceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"ResourceAsColor", "ResourceType"})
        public void bind(Absence absence) {
            binding.title.setText(absence.getTopic());
            binding.desc.setText(absence.getTime());
            if (absence.getHasReason()) {
                binding.hasReason.setText("معذور");
                binding.hasReason.setTextColor(Color.DKGRAY);
                binding.hasReasonCard.setBackgroundResource(R.drawable.btn_absence_shape_2);
                binding.hasReasonCard.setCardElevation(0);
                binding.hasReasonCard.setClickable(false);
            } else {
                binding.hasReason.setText("تقديم عذر !");
                binding.hasReason.setTextColor(Color.RED);
                binding.hasReasonCard.setBackgroundResource(R.drawable.btn_absence_shape);
                binding.hasReasonCard.setCardElevation(4);
                binding.hasReasonCard.setClickable(true);
            }
            binding.hasReasonCard.setOnClickListener(v -> {
                if (onClick != null) {
                    onClick.onClick(absence,binding.hasReason);
                }
            });
        }
    }
}
