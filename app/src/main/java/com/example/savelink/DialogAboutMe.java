package com.example.savelink;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class DialogAboutMe extends DialogFragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_contacts, null);

        v.findViewById(R.id.git_layout).setOnClickListener(this);
        v.findViewById(R.id.ok).setOnClickListener(this);
        v.findViewById(R.id.mail_layout).setOnClickListener(this);
        v.findViewById(R.id.vk_layout).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == v.findViewById(R.id.ok)){
           dismiss();
        }else if(v == v.findViewById(R.id.git_layout)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/boyfromasia"));
            startActivity(intent);
        }else if(v == v.findViewById(R.id.vk_layout)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/boyfromasia"));
            startActivity(intent);
        }else if(v == v.findViewById(R.id.mail_layout)){
            Toast.makeText(getContext(), "Use long press to copy text", Toast.LENGTH_LONG).show();
        }
    }
}
