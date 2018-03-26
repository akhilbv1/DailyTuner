package dailytuner.android.com.dailytuner.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.utils.CommonUtils;
import es.dmoral.toasty.Toasty;

/**
 Created by akhil on 19/3/18.
 */

public class EditProfileDetailsFragment extends Fragment {

    private TextInputEditText etUsername, etEmail, etMobileNum;
    private ProgressDialog dialog;
    private String userId,email,mobileNum,userName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editinfo,container,false);
       /* toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActio`Bar(toolbar);
        toolbar.setTitle(R.string.change_password);*/
        etUsername = view.findViewById(R.id.etUserName);
        etEmail = view.findViewById(R.id.etEmail);
        etMobileNum = view.findViewById(R.id.etMobileNum);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.dialog_loading));
        //new getdataAsyntask().execute();
        userId = getArguments().getString("userid");
        email = getArguments().getString("email");
        mobileNum = getArguments().getString("mobile");
        userName = getArguments().getString("username");
        setUserDetails();

        return view;
    }

    private void setUserDetails(){
        etEmail.setText(email);
        etMobileNum.setText(mobileNum);
        etUsername.setText(userName);
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
            validateFields();
            return  true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    private void validateFields(){
        if(TextUtils.isEmpty(etUsername.getText().toString().trim())){
            CommonUtils.toastMessage(R.string.empty_username,getContext());
        }
        else if(TextUtils.isEmpty(etMobileNum.getText().toString().trim())){
            CommonUtils.toastMessage(R.string.empty_mobile_num,getContext());
        }
        else if(etMobileNum.getText().toString().trim().length() < 10){
            CommonUtils.toastMessage(R.string.validation_mobilenum,getContext());
        }
        else {
            editProfileFirebase();
         //  new Asynctask().execute();

        }
    }



    private void editProfileFirebase()
    {
        String username = etUsername.getText().toString().trim();
        String mobilenumber = etMobileNum.getText().toString().trim();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
         mDatabase.child(userId).child("username").setValue(username);
        mDatabase.child(userId).child("mobileNum").setValue(mobilenumber);
        Toasty.success(getContext(),"user details updated sucessfully").show();

    }



    @Override
    public void onStop() {
        super.onStop();
        dialog.dismiss();
    }
}
