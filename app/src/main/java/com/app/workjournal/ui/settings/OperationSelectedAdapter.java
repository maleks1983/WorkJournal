package com.app.workjournal.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.dto.SelectedOperation;
import com.app.workjournal.data.entity.Operation;

import java.util.List;

public class OperationSelectedAdapter extends RecyclerView.Adapter<OperationSelectedAdapter.OperationViewHolder> {
    private final SettingsViewModel viewModel;
    private final List<SelectedOperation> operationList;
    private final FragmentManager fragmentManager;

    public OperationSelectedAdapter(SettingsViewModel viewModel, List<SelectedOperation> operationList, FragmentManager fragmentManager) {
        this.viewModel = viewModel;
        this.operationList = operationList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operation, parent, false);
        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        SelectedOperation operation = operationList.get(position);
        System.out.println(operation);

        holder.operationName.setText(operation.getOperation().getNameOperation());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(operation.getSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            operation.setSelected(isChecked);
            notifyItemChanged(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(v.getContext(), operation.getOperation(), holder.getAdapterPosition());
        });

        holder.updateButton.setOnClickListener(v -> {
            updateOperation(operation.getOperation());
        });

        // Обробка свайпів
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private boolean isSwiping = false;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        isSwiping = false;
                        hideAllButtons((RecyclerView) v.getParent());
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float endX = event.getX();
                        float deltaX = endX - startX;

                        if (Math.abs(deltaX) > 100) {
                            isSwiping = true;
                            if (deltaX > 0) { // Свайп зліва направо
                                holder.deleteButton.setVisibility(View.VISIBLE);
                                holder.updateButton.setVisibility(View.GONE);
                            } else { // Свайп справа наліво
                                holder.updateButton.setVisibility(View.VISIBLE);
                                holder.deleteButton.setVisibility(View.GONE);
                            }
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isSwiping) {
                            holder.deleteButton.setVisibility(View.GONE);
                            holder.updateButton.setVisibility(View.GONE);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return operationList.size();
    }

    public static class OperationViewHolder extends RecyclerView.ViewHolder {
        TextView operationName;
        CheckBox checkBox;
        ImageButton deleteButton, updateButton;

        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);
            operationName = itemView.findViewById(R.id.textOperationName);
            checkBox = itemView.findViewById(R.id.checkboxOperation);
            deleteButton = itemView.findViewById(R.id.buttonSettingsDel);
            updateButton = itemView.findViewById(R.id.buttonSettingsUpdate);
        }
    }

    private void showDeleteConfirmationDialog(Context context, @NonNull Operation operation, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Підтвердження видалення")
                .setMessage(String.format("Ви впевнені, що хочете видалити %s?", operation.getNameOperation()))
                .setPositiveButton("Видалити", (dialog, which) -> {
                    viewModel.deleteOperation(operation);
                    operationList.remove(position); // Видаляємо з адаптера
                    notifyItemRemoved(position); // Оновлюємо RecyclerView
                })
                .setNegativeButton("Скасувати", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateOperation(Operation operation) {
        new SettingsDialogUpdateOperationFragment(viewModel, operation).show(fragmentManager, "Dialog");
    }
    private void hideAllButtons(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);

            if (viewHolder instanceof OperationSelectedAdapter.OperationViewHolder) {
                OperationSelectedAdapter.OperationViewHolder holder = (OperationSelectedAdapter.OperationViewHolder) viewHolder;
                if (holder.deleteButton.getVisibility() == View.VISIBLE || holder.updateButton.getVisibility() == View.VISIBLE) {
                    holder.deleteButton.setVisibility(View.GONE);
                    holder.updateButton.setVisibility(View.GONE);
                }
            }
        }
    }
}

