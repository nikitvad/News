package com.example.nikit.news.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.AppUser;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.firebase.FirebaseUserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btSubmit;
    private TextView tvAlreadyHaveAnAccount;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etFullName = (EditText) findViewById((R.id.et_registration_name));
        etEmail = (EditText) findViewById((R.id._et_registration_email));
        etPassword = (EditText) findViewById((R.id.et_registration_password));
        etRepeatPassword = (EditText) findViewById((R.id.et_registration_password_repeat));
        btSubmit = (Button) findViewById((R.id.bt_registration_submit));
        tvAlreadyHaveAnAccount = (TextView) findViewById(R.id.tv_already_have_an_account);

        tvAlreadyHaveAnAccount.setOnClickListener(this);
        btSubmit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            AppUser user = new AppUser();

                            user.setName(etFullName.getText().toString());
                            user.setEmail(firebaseUser.getEmail());
                            user.setId(firebaseUser.getUid());

                            //FirebaseUserManager.pushUserInfo();
                            FirebaseUserManager.pushUserInfo_v2(user);
                            FirebaseUserManager.synchronizeUserData(getApplicationContext());
                            Prefs.setLoggedType(Prefs.EMAIL_LOGIN);


                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        String passwordRepeat = etRepeatPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        if (!password.equals(passwordRepeat)) {
            etRepeatPassword.setError("passwords not matching");
            valid = false;
        }

        return valid;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("wait please");
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registration_submit:
                createAccount(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.tv_already_have_an_account:
                finish();
                break;
        }
    }
}
