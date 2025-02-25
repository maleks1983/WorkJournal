package com.app.workjournal.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.entity.Journal;
import com.app.workjournal.ui.settings.OperationSelectedAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.OperationViewHolder> {
    private List<JournalWithOperation> journals;

    private final Calendar calendar;
    private final HomeViewModel viewModel;
    private View view;
    private final Context context;

    public JournalAdapter(List<JournalWithOperation> journals, @NonNull HomeViewModel viewModel, Context context) {
        this.journals = journals != null ? journals : new ArrayList<>(); // Prevent null pointer
        this.calendar = viewModel.getCurrentCalendar();
        this.viewModel = viewModel;
        this.context = context;
    }

    // Sets the new data for the list and notifies the adapter.
    @SuppressLint("NotifyDataSetChanged")
    public void setJournals(List<JournalWithOperation> newJournals) {
        this.journals = newJournals != null ? newJournals : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_month, parent, false);

        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        if (journals == null || journals.isEmpty()) {
            return;
        }

        JournalWithOperation operation = journals.get(position);

        holder.operationName.setText(operation.operationName());

        holder.textViewQuantity.setText(String.valueOf(operation.quantity()));

        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(v.getContext(), holder.operationName.getText().toString(), position);
        });
        holder.editTextQuantity.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                updateQuantity(holder);
                return true;
            }
            return false;
        });

        holder.updateButton.setOnClickListener(v -> {
            String quantity = holder.textViewQuantity.getText().toString();
            holder.editTextQuantity.setText(quantity);
            holder.textViewQuantity.setVisibility(View.GONE);
            holder.editTextQuantity.setVisibility(View.VISIBLE);
            holder.editTextQuantity.setSelection(0, quantity.length());
            holder.editTextQuantity.requestFocus();
            holder.updateButton.setVisibility(View.GONE);
            holder.saveButton.setVisibility(View.VISIBLE);

        });

        holder.editTextQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showKeyboard(holder.editTextQuantity);
            }
            if (!hasFocus) {
                hideKeyboard(context, v);
                closeAddButton(holder);
            }
        });

        holder.saveButton.setOnClickListener(v -> updateQuantity(holder));

        view.setOnTouchListener(new View.OnTouchListener() {
            private float startX;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        hideAllButtons((RecyclerView) v.getParent());
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float moveX = event.getX();
                        return Math.abs(moveX - startX) > 20;

                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        if (endX - startX > 150) { // Свайп зліва направо
                            holder.deleteButton.setVisibility(View.VISIBLE);
                            holder.updateButton.setVisibility(View.GONE);
                        } else if (startX - endX > 150) { // Свайп справа наліво
                            holder.updateButton.setVisibility(View.VISIBLE);
                            holder.deleteButton.setVisibility(View.GONE);
                        } else {
                            // Натискання поза кнопками -> ховаємо всі кнопки
                            holder.updateButton.setVisibility(View.GONE);
                            holder.deleteButton.setVisibility(View.GONE);
                            holder.saveButton.setVisibility(View.GONE);
                            holder.textViewQuantity.setVisibility(View.VISIBLE);
                            holder.editTextQuantity.setVisibility(View.GONE);
                        }
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    private void updateQuantity(@NonNull OperationViewHolder holder) {
        String quantity = holder.editTextQuantity.getText().toString();
        if (!quantity.isEmpty() && !quantity.equals("0")) {
            Journal j = new Journal(journals.get(holder.getAdapterPosition()));
            j.setQuantity(Integer.parseInt(quantity));
            viewModel.updateJournal(j);
            holder.textViewQuantity.setText(quantity);
            closeAddButton(holder);
        }
    }


    public static class OperationViewHolder extends RecyclerView.ViewHolder {
        TextView operationName, textViewQuantity;
        ImageButton deleteButton, updateButton, saveButton;
        EditText editTextQuantity;

        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);
            operationName = itemView.findViewById(R.id.textViewMonthName);
            textViewQuantity = itemView.findViewById(R.id.textViewMonthQuantity);
            updateButton = itemView.findViewById(R.id.buttonJournalUpdate);
            updateButton.setVisibility(View.GONE);
            deleteButton = itemView.findViewById(R.id.buttonJournalDel);
            deleteButton.setVisibility(View.GONE);
            editTextQuantity = itemView.findViewById(R.id.textJournalQuantityEdit);
            saveButton = itemView.findViewById(R.id.buttonJournalUpdateSave);
        }
    }

    private void showDeleteConfirmationDialog(Context context, @NonNull String journalName, int position) {
        Journal journal = new Journal(journals.get(position));
        new AlertDialog.Builder(context)
                .setTitle("Підтвердження видалення")
                .setMessage(String.format("Ви впевнені, що хочете видалити %s?", journalName))
                .setPositiveButton("Видалити", (dialog, which) -> {
                    viewModel.deleteJournal(journal);
                    journals.remove(position); // Видаляємо з адаптера
                    notifyItemRemoved(position); // Оновлюємо RecyclerView
                })
                .setNegativeButton("Скасувати", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void closeAddButton(@NonNull OperationViewHolder holder) {
        holder.textViewQuantity.setVisibility(View.VISIBLE);
        holder.editTextQuantity.setVisibility(View.GONE);
        holder.updateButton.setVisibility(View.GONE);
        holder.saveButton.setVisibility(View.GONE);
    }

    private void hideKeyboard(@NonNull Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideAllButtons(@NonNull RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);

            if (viewHolder instanceof OperationViewHolder holder) {
                if (holder.deleteButton.getVisibility() == View.VISIBLE || holder.updateButton.getVisibility() == View.VISIBLE) {
                    holder.deleteButton.setVisibility(View.GONE);
                    holder.updateButton.setVisibility(View.GONE);
                }
            }
        }
    }

}

