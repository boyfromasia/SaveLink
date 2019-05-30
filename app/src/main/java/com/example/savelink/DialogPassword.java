package com.example.savelink;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogPassword extends DialogFragment implements View.OnClickListener{
    MainActivity activity;
    TextView textview;
    TextInputEditText password;
    String dialog_password;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.fragment_dialog_password, null);

        textview = v.findViewById(R.id.item_name_password);
        textview.setText(activity.getName());

        password = v.findViewById(R.id.dialog_password);

        v.findViewById(R.id.cancel_password).setOnClickListener(this);
        v.findViewById(R.id.open_password).setOnClickListener(this);

        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        password.setText("");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        password.setText("");
    }

    @Override
    public void onClick(View v) {
        dialog_password = password.getText().toString();
        if(v == v.findViewById(R.id.cancel_password)){
            dismiss();
        }else if(v == v.findViewById(R.id.open_password)){
            if(dialog_password.equals(activity.password_now)) {
                startActivity(activity.item_intent);
                dismiss();
            }else{
                password.setText("");
                Toast.makeText(getActivity(), "Password is wrong", Toast.LENGTH_LONG).show();
            }

        }
    }
}
