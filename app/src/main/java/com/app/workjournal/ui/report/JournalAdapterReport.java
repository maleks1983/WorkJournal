package com.app.workjournal.ui.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.dto.JournalWithOperation;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapterReport extends RecyclerView.Adapter<JournalAdapterReport.OperationViewHolder> {
    private final List<JournalWithOperation> journals;

    public JournalAdapterReport(List<JournalWithOperation> journals) {
        this.journals = journals != null ? journals : new ArrayList<>();

    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_report, parent, false);
        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        if (journals.isEmpty()) {
            return;
        }
        JournalWithOperation operation = journals.get(position);
        holder.operationName.setText(operation.operationName());
        holder.textViewQuantity.setText(String.valueOf(operation.quantity()));

    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public static class OperationViewHolder extends RecyclerView.ViewHolder {
        TextView operationName, textViewQuantity;

        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);
            operationName = itemView.findViewById(R.id.textViewReportName);
            textViewQuantity = itemView.findViewById(R.id.textViewReportQuantity);

        }
    }

}

