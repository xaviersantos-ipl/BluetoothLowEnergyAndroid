package com.example.bluetoothlowenergytests.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;

import com.example.bluetoothlowenergytests.Constants;
import com.example.bluetoothlowenergytests.R;
import com.example.bluetoothlowenergytests.Utils;
import com.example.bluetoothlowenergytests.models.ResRepository;

public class AddContainerDialog extends DialogFragment {
    AddContainerDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddContainerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement AddContainerDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_with_edittext, null);
        EditText nameText  = dialogLayout.findViewById(R.id.editText);

        builder.setView(dialogLayout)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = nameText.getText().toString().trim();
                        ResRepository.Companion.getInstance().addContainer(name, "onem2m/"+Constants.APP_NAME, (isSuccess) -> {
                            if(isSuccess){
                                listener.onAddContainer("onem2m/"+Constants.APP_NAME+"/"+name);
                            } else {
                                Utils.toast(getActivity(), "Error connecting with Mobius");
                                listener.onAddContainer(null);
                            }
                            dialog.dismiss();
                            return null;
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public interface AddContainerDialogListener {
        public void onAddContainer(String path);
    }
}