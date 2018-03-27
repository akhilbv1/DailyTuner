package dailytuner.android.com.dailytuner.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.utils.CommonUtils;
import es.dmoral.toasty.Toasty;

/**Created by akhil on 2/2/18.
 */

public class ChangePasswordFragment extends Fragment {

    //private Toolbar toolbar;
    private TextInputEditText etOldPassword,etNewPassword,etConfNewPassword;
    private ProgressDialog dialog;
    private String userId,password;
    private FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changepassword,container,false);
       /* toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActio`Bar(toolbar);
        toolbar.setTitle(R.string.change_password);*/
        etOldPassword = view.findViewById(R.id.etOldPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfNewPassword = view.findViewById(R.id.etConfNewPassword);
        password = getArguments().getString("password");
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.dialog_loading));
        auth = FirebaseAuth.getInstance();
        userId = getArguments().getString("userid");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_savepassword,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menusave){
            validationPassword();
            return  true;
        }
        else
        return super.onOptionsItemSelected(item);
    }

    private void validationPassword(){
        if(TextUtils.isEmpty(etOldPassword.getText().toString().trim())){
            CommonUtils.toastMessage(getString(R.string.empty_password),getContext());
        }
        else if(etOldPassword.getText().toString().trim().length() < 6){
            CommonUtils.toastMessage(getString(R.string.validation_password),getContext());
        }
        else if(!etOldPassword.getText().toString().trim().equals(password)){

            Toasty.error(getContext(),"please enter valid current password").show();
        }
        else if(TextUtils.isEmpty(etNewPassword.getText().toString().trim())){
            CommonUtils.toastMessage(getString(R.string.empty_new_password),getContext());
        }
        else if(etNewPassword.getText().toString().trim().length() < 6){
            CommonUtils.toastMessage(getString(R.string.validation_password),getContext());
        }
        else if(etNewPassword.getText().toString().trim().equals(password)){
            Toasty.error(getContext(),"old and new password should not be same").show();
        }
        else if(TextUtils.isEmpty(etConfNewPassword.getText().toString().trim())){
            CommonUtils.toastMessage(getString(R.string.empty_password),getContext());
        }
        else if(etNewPassword.getText().toString().trim().equals(etConfNewPassword.getText().toString().trim())){
            //new ChangePasswordFragment.Asynctask().execute();
            changePasswordFirebaseAuth();
        }
        else {
            CommonUtils.toastMessage(getString(R.string.check_passwords),getContext());
        }
    }

    private void changePasswordFirebaseAuth(){
        final String password = etNewPassword.getText().toString().trim();
        FirebaseUser user = auth.getCurrentUser();

        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  changePasswordFirebaseDb(password);
              }
              else {
                  Log.i("failed",""+task.getException());
                  Toasty.success(getContext(),""+task.getException()).show();
              }

            }
        });

    }

    private void changePasswordFirebaseDb(String password){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");


        mDatabase.child(userId).child("password").setValue(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toasty.success(getContext(),"password changed successfully").show();

                }
                else {
                    Toasty.success(getContext(),"passsword change failed").show();

                }
            }
        });
    }




    @Override
    public void onStop() {
        super.onStop();
        dialog.dismiss();
    }
}
