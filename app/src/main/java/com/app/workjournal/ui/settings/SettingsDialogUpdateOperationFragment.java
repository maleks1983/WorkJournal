package com.app.workjournal.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.workjournal.R;
import com.app.workjournal.data.entity.Operation;

public class SettingsDialogUpdateOperationFragment extends DialogFragment {
    private EditText editText;
    private final SettingsViewModel viewModel;
    private final Operation operation;

    public SettingsDialogUpdateOperationFragment(SettingsViewModel viewModel, Operation operation) {
        this.viewModel = viewModel;
        this.operation = operation;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_operation_layout, container, false);
        editText = view.findViewById(R.id.add_operations_edit);
        editText.setText(operation.getNameOperation());
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                operation.setNameOperation(editText.getText().toString());
                viewModel.updateOperation(operation);
                viewModel.updateOperationsList();
                dismiss();
                return true;
            }
            return false;
        });
        return view;
    }


}
