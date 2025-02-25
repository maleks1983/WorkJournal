package com.app.workjournal.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.workjournal.R;

public class SettingsDialogFragment extends DialogFragment {
    private EditText editText;
    private final SettingsViewModel viewModel;

    public SettingsDialogFragment(SettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container, false);
        editText = view.findViewById(R.id.add_operations_edit);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                viewModel.addNewOperations(editText.getText().toString());
                viewModel.updateOperationsList();
                dismiss();
                return true;
            }
            return false;
        });
        return view;
    }


}
