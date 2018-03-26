package dailytuner.android.com.dailytuner.firebase;

/**
  Created by akhil on 23/3/18.
 */

public class FirebasePhysicalActivitiesTable {

    private String ActviityId;

    private String userid;

    private String ActivityName;

    private String ActivityType;

    private String ActivityDate;

    private double hoursSpent;

    private int shouldRemind;

    private String alarm;

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getActviityId() {
        return ActviityId;
    }

    public void setActviityId(String actviityId) {
        ActviityId = actviityId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getActivityDate() {
        return ActivityDate;
    }

    public void setActivityDate(String activityDate) {
        ActivityDate = activityDate;
    }

    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public int getShouldRemind() {
        return shouldRemind;
    }

    public void setShouldRemind(int shouldRemind) {
        this.shouldRemind = shouldRemind;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
