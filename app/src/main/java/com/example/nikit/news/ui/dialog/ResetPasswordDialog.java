package com.example.nikit.news.ui.dialog;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.ui.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by nikit on 12.05.2017.
 */

public class ResetPasswordDialog extends DialogFragment {
    private EditText etEmail;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View baseView = inflater.inflate(R.layout.dialog_reset_password, null);
        etEmail = (EditText) baseView.findViewById(R.id.et_reset_pass_email);

        builder.setView(baseView)
                .setTitle(R.string.dialog_reset_pass_title)
                .setPositiveButton(R.string.dialog_reset_pass_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if (etEmail.getText().length() > 0) {
                            auth.sendPasswordResetEmail(etEmail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(baseView.getContext(), R.string.toast_msg_sent_password_reset_instructions,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            etEmail.setError("Required.");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ResetPasswordDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
