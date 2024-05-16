package com.arabic.schoolg.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arabic.schoolg.data.Category;
import com.arabic.schoolg.data.Lecture;
import com.arabic.schoolg.databinding.CategoryItemBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private final AsyncListDiffer<Category> differ;
    private static OnClickListener onClick;

    public CategoryAdapter(AsyncListDiffer<Category> differ, CategoryAdapter.OnClickListener onClick) {
        this.differ = differ;
        this.onClick = onClick;
    }
    public interface OnClickListener {
        void onClick(Category category);
    }
    public CategoryAdapter() {
        DiffUtil.ItemCallback<Category> diffUtil = new DiffUtil.ItemCallback<Category>() {
            @Override
            public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                return oldItem == newItem;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                return oldItem.equals(newItem);
            }

        };
        differ = new AsyncListDiffer<>(this, diffUtil);
    }
    public void setOnClickListener(CategoryAdapter.OnClickListener onClickListener) {
        onClick = onClickListener;
    }

    public void submitList(List<Category> newList) {
        differ.submitList(newList);
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CategoryAdapter.MyViewHolder(CategoryItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Category currentDoctor = differ.getCurrentList().get(position);
        holder.bind(currentDoctor);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemBinding binding;

        public MyViewHolder(@NonNull CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category) {
            binding.categoryImage.setImageResource(category.getImage());
            binding.categoryName.setText(category.getName());
            binding.card.setOnClickListener(v -> {
                if (onClick != null) {
                    onClick.onClick(category);
                }
            });
        }
    }
}
