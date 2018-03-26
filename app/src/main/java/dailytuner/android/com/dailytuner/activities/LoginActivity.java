package dailytuner.android.com.dailytuner.activities;

/*Created by akhil on 29/1/18.*/

/*
 * login activity of the user
 * respective layout:-sqlite_room_login_activity
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.utils.CommonUtils;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText  alrtEmail;
    ProgressDialog dialog;
    Button submit;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    String userid;
    private EditText etEmail,etPassword;
    private FirebaseAuth auth;
    public static Activity finishLoginActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        finishLoginActivity = this;

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        TextView txtNewUser = findViewById(R.id.txtNewUser);
        txtNewUser.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        TextView txtForgotPass = findViewById(R.id.txtForgotPass);
        txtForgotPass.setOnClickListener(this);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title_login);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.dialog_loading));
        dialog.setCancelable(false);
        alertDialogBuilder = new AlertDialog.Builder(this);

    }

    /**
     * on click of buttons:login
     * Textview:newuser,forgot password
     * button:submit for the dialog
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                loginWithDetails();
                break;

            case R.id.txtNewUser:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.txtForgotPass:
                loadDialog();
                break;

            case R.id.btnSubmit:
               validateEmail();
                break;
        }
    }

    /**
     * it validates email for the forgot password dialog
     */
    private void validateEmail() {

        if (TextUtils.isEmpty(alrtEmail.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.empty_email_register,this);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(alrtEmail.getText().toString().trim()).matches()) {
            CommonUtils.toastMessage(R.string.validation_email,this);
        } else {
               dialog.show();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(alrtEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.cancel();
                                alertDialog.dismiss();
                                Toasty.success(LoginActivity.this,getString(R.string.resetPassword)).show();
                            }
                            else {
                                Log.i("error",""+task.getException());
                                Toasty.error(LoginActivity.this,"Please enter Registered Email").show();
                                dialog.cancel();
                                alertDialog.dismiss();
                                alertDialog.dismiss();
                            }
                        }
                    });
        }
    }

   /*
   all the email clients are opened to send recovery email
    */
 /*   private void emailClient() {
        String body = getString(R.string.message_reset_password) + CommonUtils.randomString(8);
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{alrtEmail.getText().toString().trim()});
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        Email.putExtra(Intent.EXTRA_TEXT, body);
        Email.setType("message/rfc822");
        startActivity(Intent.createChooser(Email, getString(R.string.choose_intent_email)));
        alertDialog.cancel();
    }
*/

    /**
     * it logins user using async task which runs in background
     */

    private void loginWithDetails() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, R.string.empty_email, Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();

        } else  {
           // loginUser();
            firebaseLogin();
            //new AsynTask().execute();
          //loginwithFirebase();

        }
    }


    private void firebaseLogin(){
        dialog.show();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    dialog.cancel();
                    Toasty.success(LoginActivity.this,"Login Success").show();
                    loginSuccess(task.getResult().getUser().getUid());
                }
                else {
                    dialog.cancel();
                    Toasty.error(LoginActivity.this,"Login Failed").show();
                }
            }
        });

    }

    /**
     * it builds dialog forgot password.
      */
 private void loadDialog(){
    View alerdialogview = getLayoutInflater().inflate(R.layout.forgotpassword_dilaog,null);
    alrtEmail = alerdialogview.findViewById(R.id.alrtdialog_Email);
     submit = alerdialogview.findViewById(R.id.btnSubmit);
     submit.setOnClickListener(this);
    alertDialogBuilder.setView(alerdialogview);
    alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }


    /**
     * if the login is success then open samplenavigationdrawer class.
     * @param uid,idToken,refreshToken
     */
    private void loginSuccess(String uid){
        setPreferences(uid);

        Intent intent = new Intent(this,NavigationDrawerActivity.class);
        intent.putExtra("userid",userid);
        startActivity(intent);
        etEmail.setText("");
        etPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * it sets current user to check if the user is logged in throughout the application
     */
    private void setPreferences(String uid){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentuser",uid);
        editor.putString("currentuser",uid);
        editor.putBoolean("isLogin",true);
        editor.apply();
    }
}
