package dailytuner.android.com.dailytuner.activities;

/*
 *this activity registers the user.
  * repective layout:-sqlite_room_registration_activity
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dailytuner.android.com.dailytuner.firebase.FirebaseUserTable;
import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.utils.CommonUtils;
import es.dmoral.toasty.Toasty;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etUsername, etEmail, etMobileNum, etPassword, etConfPassword;
    TextView txtExistingAccount;
    Toolbar toolbar;
    ProgressDialog dialog;
    Button btnSubmit;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUsername = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etMobileNum = findViewById(R.id.etMobileNum);
        etPassword = findViewById(R.id.etPassword);
        etConfPassword = findViewById(R.id.etConfPassword);

        txtExistingAccount = findViewById(R.id.txtExistingAccount);
        txtExistingAccount.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title_Registration);
        //toolbar.setNavigationIcon(R.drawable.a);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);

    }

    /**
     * onclick of
     * submit:inserts data into database
     * existing account:-open login screen
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:;
                registerByValidation();
                          break;

            case R.id.txtExistingAccount:

                LoginActivity.finishLoginActivity.finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * registerbyvalidation first validates the entire registration screen and the calls Asyntask
     * to perform database operations on the background.
     */
    private void registerByValidation() {

        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.empty_username, this);
        } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.empty_email, this);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            CommonUtils.toastMessage(R.string.validation_email, this);
        } else if (TextUtils.isEmpty(etMobileNum.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.empty_mobile_num, this);
        } else if (etMobileNum.getText().toString().trim().length() < 10) {
            CommonUtils.toastMessage(R.string.validation_mobilenum, this);
        } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.empty_password, this);
        } else if (etPassword.getText().toString().trim().length() < 6) {
            CommonUtils.toastMessage(R.string.validation_password, this);

        } else if (TextUtils.isEmpty(etConfPassword.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.check_passwords, this);

        } else if (!etPassword.getText().toString().trim().equals(etConfPassword.getText().toString().trim())) {
            CommonUtils.toastMessage(R.string.check_passwords, this);

        } else {
            registerUserInAuth();
          // new DatabaseSync().execute();
            //registerUser();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return  true;
        }
        else
        return super.onOptionsItemSelected(item);
    }


    private void registerUserInAuth(){
        dialog.show();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid = task.getResult().getUser().getUid();
                    registerUserInDb(uid);
                }
            }
        });
    }

    private void registerUserInDb(String uid){
       DatabaseReference mDatabaseReference;
       FirebaseUserTable user = new FirebaseUserTable();
       user.setEmailid(etEmail.getText().toString().trim());
       user.setPassword(etPassword.getText().toString().trim());
       user.setMobileNum(etMobileNum.getText().toString().trim());
       user.setUsername(etUsername.getText().toString().trim());
      mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

      mDatabaseReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              Log.i("success",""+task.isSuccessful());
              if(task.isSuccessful()){
                 dialog.cancel();
                 Toasty.success(RegistrationActivity.this,"user registered Successfully").show();
                 Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                 startActivity(intent);
             }
             else {
                 Toasty.error(RegistrationActivity.this,"user registered failed").show();
             }

          }
      });

    }

    /**
     * registers user using sqlite API not room
     */
 /*   private void registerUser(){
        SqliteUserPojo sqliteUserPojo = new SqliteUserPojo();

        sqliteUserPojo.setUsername(etUsername.getText().toString().trim());
        sqliteUserPojo.setEmailid(etEmail.getText().toString().trim());
        sqliteUserPojo.setMobileNum(etMobileNum.getText().toString().trim());
        sqliteUserPojo.setPassword(etPassword.getText().toString().trim());

        databaseHandler.adduser(sqliteUserPojo);

        clearForm();
    }*/




    /*
     *it clears all the edit texts
      */
    private void clearForm(){
        etUsername.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfPassword.setText("");
        etMobileNum.setText("");
    }

}
