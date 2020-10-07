package com.example.files.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.files.R;
import com.example.files.model.FileModel;

import java.util.Collections;
import java.util.List;

public class FileModelAdapter extends RecyclerView.Adapter<FileModelAdapter.ViewHolder> {

    private List<FileModel> items = Collections.emptyList();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView filenameTextView;
        private TextView pathTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            filenameTextView = itemView.findViewById(R.id.filenameTextView);
            pathTextView = itemView.findViewById(R.id.pathTextView);
        }

        void bind(FileModel fileModel) {
            filenameTextView.setText(fileModel.getFilename());
            pathTextView.setText(fileModel.getPath());
        }
    }
}