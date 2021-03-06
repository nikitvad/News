package com.example.nikit.news.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.firebase.AppUser;
import com.example.nikit.news.ui.dialog.ResetPasswordDialog;
import com.example.nikit.news.util.NetworkUtil;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.firebase.FirebaseUserManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private Button btLoginByEmail;
    private Button btLoginByFacebook;
    private Button btLoginByGoogle;
    private EditText etAuthEmail;
    private EditText etAuthPassword;
    private TextView tvCreateAccount;
    private ProgressDialog mProgressDialog;
    private TextView tvForgotPassword;

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAuthEmail = (EditText) findViewById(R.id.et_auth_email);
        etAuthPassword = (EditText) findViewById(R.id.et_auth_pass);
        tvCreateAccount = (TextView) findViewById(R.id.tv_create_account);
        tvForgotPassword = (TextView) findViewById(R.id.tv_login_forgot_password);

        tvCreateAccount.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        //Firebase initializing
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    FirebaseUserManager.synchronizeUserData(getApplicationContext());
                } else {
                    removeUserData();
                }
            }
        };

        //facebook login initializing
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        //google sign initializing
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //login buttons
        btLoginByFacebook = (Button) findViewById(R.id.login_by_facebook);
        btLoginByEmail = (Button) findViewById(R.id.bt_login_by_email);
        btLoginByGoogle = (Button) findViewById(R.id.bt_login_by_google);

        btLoginByGoogle.setOnClickListener(this);
        btLoginByFacebook.setOnClickListener(this);
        btLoginByEmail.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthStateListener != null) {
            mAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void signInByGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Prefs.setLoggedType(Prefs.GOOGLE_LOGIN);
                            AppUser user = new AppUser(mAuth.getCurrentUser());
                            FirebaseUserManager.pushUserInfo(user);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });

    }

    private void signInByEmail(String email, String password) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Prefs.setLoggedType(Prefs.EMAIL_LOGIN);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void handleFacebookAccessToken(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    AppUser user = new AppUser(mAuth.getCurrentUser());
                    FirebaseUserManager.pushUserInfo(user);
                    Prefs.setLoggedType(Prefs.FACEBOOK_LOGIN);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etAuthEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etAuthEmail.setError("Required.");
            valid = false;
        } else {
            etAuthEmail.setError(null);
        }

        String password = etAuthPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etAuthPassword.setError("Required.");
            valid = false;
        } else {
            etAuthPassword.setError(null);
        }

        return valid;
    }

    private void removeUserData() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        SqLiteDbHelper dbHelper = new SqLiteDbHelper(getApplicationContext());
        dbHelper.clearLikedNewsTable(db);
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login_by_email:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    signInByEmail(etAuthEmail.getText().toString(), etAuthPassword.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_create_account:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.login_by_facebook:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                            Arrays.asList("public_profile", "user_friends", "read_custom_friendlists", "email"));
                } else {
                    Toast.makeText(LoginActivity.this, R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_login_by_google:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    signInByGoogle();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_login_forgot_password:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog();
                    resetPasswordDialog.show(getSupportFragmentManager(), TAG);
                }
                break;
        }
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
}
