package com.example.bluetoothlowenergytests.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bluetoothlowenergytests.Constants;
import com.example.bluetoothlowenergytests.R;
import com.example.bluetoothlowenergytests.Utils;
import com.example.bluetoothlowenergytests.models.ResRepository;

import java.util.ArrayList;
import java.util.List;

public class ContainerPicker extends DialogFragment {
    List<String> containers;
    ArrayAdapter adapter;
    ContainerPickerListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containers = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this.requireContext(),
                android.R.layout.simple_list_item_1, containers);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ContainerPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement ContainerPickerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ResRepository.Companion.getInstance().getList((isSuccess, response) -> {
            if (isSuccess) {
                for (String url: response) {
                    int size = url.split("/").length;
                    if(url.startsWith("/onem2m/"+Constants.APP_NAME) && size == 4)
                        containers.add(url);
                }
            } else {
                Utils.toast(getActivity(), "Error connecting with Mobius");
            }
            adapter.notifyDataSetChanged();
            return null;
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.container_picker)
            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onPick(containers.get(which));
                    dialog.dismiss();
                }
            });

        return builder.create();
    }

    public interface ContainerPickerListener {
        public void onPick(String path);
    }
}
