package com.app.workjournal.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.entity.Journal;
import com.app.workjournal.data.entity.Operation;

import java.util.Calendar;
import java.util.List;

public class OperationsUserAdapter extends RecyclerView.Adapter<OperationsUserAdapter.OperationViewHolder> {
    private final List<Operation> operationListDay;
    private final Calendar calendar;
    private final HomeViewModel viewModel;
    private final Context context;


    public OperationsUserAdapter(List<Operation> operationListDay, @NonNull HomeViewModel viewModel, Context context) {
        this.calendar = viewModel.getCurrentCalendar();
        this.operationListDay = operationListDay;
        this.viewModel = viewModel;
        this.context = context;
    }


    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_day, parent, false);
        return new OperationViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        Operation operation = operationListDay.get(position);
        holder.operationName.setText(operation.getNameOperation());
        holder.operationName.setOnClickListener(v -> {
            closeAddButton(holder);
        });


        holder.journalQuantity.setOnClickListener(v -> openAddButton(holder));

        holder.journalQuantityEdit.setOnEditorActionListener((textView, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                updateQuantity(holder);
                hideKeyboard(textView.getContext(), textView);
                return true;
            }
            return false;
        });

        holder.buttonAdd.setOnClickListener(v -> updateQuantity(holder));

        holder.journalQuantityEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showKeyboard(holder.journalQuantityEdit);
            }
            if (!hasFocus) {
                hideKeyboard(v.getContext(), v);
                closeAddButton(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return operationListDay.size();
    }

    private void updateQuantity(@NonNull OperationViewHolder holder) {
        if (!holder.journalQuantityEdit.getText().toString().isEmpty()) {
            int quantity = Integer.parseInt(holder.journalQuantityEdit.getText().toString());
            if (quantity > 0) {
                Operation o = operationListDay.get(holder.getAdapterPosition());
                Journal journal = new Journal(calendar.getTimeInMillis(), o.getId(), quantity);
                viewModel.updateJournal(journal);
            }
        }
        closeAddButton(holder);
    }


    public static class OperationViewHolder extends RecyclerView.ViewHolder {
        TextView operationName, journalQuantity;
        EditText journalQuantityEdit;
        ImageButton buttonAdd;

        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);
            operationName = itemView.findViewById(R.id.textJournalOperationName);
            journalQuantity = itemView.findViewById(R.id.textJournalQuantity);
            journalQuantityEdit = itemView.findViewById(R.id.textJournalQuantityEdit);
            journalQuantityEdit.setVisibility(View.GONE);
            buttonAdd = itemView.findViewById(R.id.buttonJournalAdd);
        }
    }


    private void openAddButton(@NonNull OperationViewHolder holder) {
        holder.journalQuantityEdit.setFocusable(true);
        holder.journalQuantityEdit.setCursorVisible(true);
        holder.journalQuantity.setVisibility(View.GONE);
        holder.journalQuantityEdit.setVisibility(View.VISIBLE);
        holder.journalQuantityEdit.requestFocus();
        holder.buttonAdd.setVisibility(View.VISIBLE);
        holder.journalQuantityEdit.setSelection(0, 1);
        holder.journalQuantityEdit.requestFocus();
    }

    private void closeAddButton(@NonNull OperationViewHolder holder) {
        holder.journalQuantityEdit.setText("1");
        holder.journalQuantityEdit.setVisibility(View.GONE);
        holder.journalQuantity.setVisibility(View.VISIBLE);
        holder.buttonAdd.setVisibility(View.GONE);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard(@NonNull Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Operation> newOperations) {
        this.operationListDay.clear();
        this.operationListDay.addAll(newOperations);
        notifyDataSetChanged();
    }

}