package dailytuner.android.com.dailytuner.firebase;

/**
 Created by akhil on 23/3/18.
 */

public class FirebaseUserTable {

    private String Username;

    private String Emailid;

    private String MobileNum;

    private String Password;

    public String getEmailid() {
        return Emailid;
    }

    public void setEmailid(String emailid) {
        Emailid = emailid;
    }

    public String getMobileNum() {
        return MobileNum;
    }

    public void setMobileNum(String mobileNum) {
        MobileNum = mobileNum;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
