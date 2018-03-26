package dailytuner.android.com.dailytuner.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import dailytuner.android.com.dailytuner.firebase.FirebasePhysicalActivitiesTable;
import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.utils.CommonUtils;
import es.dmoral.toasty.Toasty;


/**
  Created by venkareddy on 20/3/18.
 */

public class ActivityDetailsListActivity extends AppCompatActivity {

    private RecyclerView activityRv;

    private TextView noActivitiesTv;

    private List<FirebasePhysicalActivitiesTable> activityList = new ArrayList<>();

    private FloatingActionButton addActivityFab;

    private AlertDialog addActivityDialog;

    private DatePickerDialog datePickerDialog;

    private int newYear, newMonth, newDay;

    private int currentHour, currentMinute;

    private String timeStr, userId;

    private EditText activityNmEt, activityDateEt, hrsSpentEt;

    private int shouldRemind;

    private TextView timeTv;

    private boolean isFromAdapter = false;

    private String activityID;

    private boolean PhysicalActivity, CollegeActivity, HouseWorkActivity, ReadingActivity, FamActivity,
            SleepActivity, StudyingActivity, EntertainmentActivity, OtherActivity;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    TimePicker myTimePicker;
    Button buttonstartSetDialog;
    Button buttonCancelAlarm;
    TextView textAlarmPrompt;
    TimePickerDialog timePickerDialog;

    final static int RQS_1 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activity);

        activityRv = findViewById(R.id.activitiesRv);
        noActivitiesTv = findViewById(R.id.noActivitiesTv);
        addActivityFab = findViewById(R.id.addActivityFab);
        checkActivities();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading");
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (PhysicalActivity) {
            toolbar.setTitle("Physical  Activities");

        } else if (CollegeActivity) {
            toolbar.setTitle("College Activities");

        } else if (HouseWorkActivity) {
            toolbar.setTitle("HouseWork Activities");

        } else if (ReadingActivity) {
            toolbar.setTitle("Reading Activities");
        } else if (FamActivity) {
            toolbar.setTitle("Family & Friends Activities");
        } else if (SleepActivity) {
            toolbar.setTitle("Sleep Activities");
        } else if (StudyingActivity) {
            toolbar.setTitle("Studying Activities");
        } else if (EntertainmentActivity) {
            toolbar.setTitle("Entertainment Activities");

        } else if (OtherActivity) {
            toolbar.setTitle("Other Activities");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityRv.setLayoutManager(mLayoutManager);
        activityRv.setHasFixedSize(true);
        //new Asyntask().execute();
        //showListEmpty();

        addActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addActivityDetailsDialog(false, "");
            }
        });

        //new Asyntask().execute();
        retrievePhysicalActivitiesFromFirebase();
      //  showEmpty();
    }

 /*   private void showEmpty() {
        if (physicalActivitiesList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (collegeList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (houseWorkList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (readingList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (famAndFrndsList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (sleepList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (studyList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (entertainmentList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        } else if (othersList.size() < 0) {
            noActivitiesTv.setVisibility(View.VISIBLE);
        }
    }*/


    private String generateActivityId() {

        if (PhysicalActivity) {

            return CommonUtils.generatePhysicalActivityID();

        } else if (CollegeActivity) {

            return CommonUtils.generateCollegeActivityID();

        } else if (HouseWorkActivity) {

            return CommonUtils.generateHouseworkActivityID();

        } else if (ReadingActivity) {

            return CommonUtils.generateReadingActivityID();

        } else if (FamActivity) {

            return CommonUtils.generateFamActivityID();

        } else if (SleepActivity) {

            return CommonUtils.generateSleepActivityID();

        } else if (StudyingActivity) {

            return CommonUtils.generateStudyActivityID();

        } else if (EntertainmentActivity) {

            return CommonUtils.generateEntertainmentActivityID();

        } else if (OtherActivity) {

            return CommonUtils.generateOtherActivityID();

        }
        return "";
    }

    private String getActivityType() {
        if (PhysicalActivity) {

            return "Physical";

        } else if (CollegeActivity) {

            return "College";

        } else if (HouseWorkActivity) {

            return "House";

        } else if (ReadingActivity) {

            return "Reading";

        } else if (FamActivity) {

            return "Family";

        } else if (SleepActivity) {

            return "Sleep";

        } else if (StudyingActivity) {

            return "Study";

        } else if (EntertainmentActivity) {

            return "Entertainment";

        } else if (OtherActivity) {

            return "Other";

        }
        return "";
    }

    private void checkActivities() {
        PhysicalActivity = getIntent().getBooleanExtra("PhysicalActivity", false);
        CollegeActivity = getIntent().getBooleanExtra("CollegeActivity", false);
        HouseWorkActivity = getIntent().getBooleanExtra("HouseWorkActivity", false);
        ReadingActivity = getIntent().getBooleanExtra("ReadingActivity", false);
        FamActivity = getIntent().getBooleanExtra("FamActivity", false);
        SleepActivity = getIntent().getBooleanExtra("SleepActivity", false);
        StudyingActivity = getIntent().getBooleanExtra("StudyingActivity", false);
        EntertainmentActivity = getIntent().getBooleanExtra("EntertainmentActivity", false);
        OtherActivity = getIntent().getBooleanExtra("OtherActivity", false);
        userId = getIntent().getStringExtra("userid");
    }


    /*forgot password dialaog*/
    public void addActivityDetailsDialog(final boolean isFromAdapter, final String activityid) {

        Log.i("boolean", "" + isFromAdapter);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_activity_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(false);
        addActivityDialog = builder.create();
        addActivityDialog.setView(dialogView, 0, 0, 0, 0);

        activityNmEt = dialogView.findViewById(R.id.activityNmEt);
        activityDateEt = dialogView.findViewById(R.id.activityDateEt);
        hrsSpentEt = dialogView.findViewById(R.id.hrsSpentEt);
        timeTv = dialogView.findViewById(R.id.timeTv);

        final Switch remainderSwitch = dialogView.findViewById(R.id.remainderSwitch);

        remainderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    openTimePickerDialog(false);
                    shouldRemind = 1;
                    //startTimePicker(timeTv,remainderSwitch);


                } else {

                    timeTv.setText("OFF");
                    shouldRemind = 0;

                }
            }
        });


        activityDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar newCalendar = Calendar.getInstance();

                newYear = newCalendar.get(Calendar.YEAR);
                newMonth = newCalendar.get(Calendar.MONTH);
                newDay = newCalendar.get(Calendar.DAY_OF_MONTH);

                final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                datePickerDialog = new DatePickerDialog(ActivityDetailsListActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);


                        activityDateEt.setText(dateFormatter.format(newDate.getTime()));

                        //String startDateStr = dateFormatter.format(newDate.getTime());

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();

            }
        });


        final Button addBtn = dialogView.findViewById(R.id.addBtn);

        final Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);

        //final boolean bool = Utils.isValidEmail(registeredEmailEt.getText().toString());


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(activityNmEt.getText().toString().trim())) {

                    Toast.makeText(ActivityDetailsListActivity.this, "Enter Activity Name", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(activityDateEt.getText().toString().trim())) {

                    Toast.makeText(ActivityDetailsListActivity.this, "Choose Activity Date", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(hrsSpentEt.getText().toString().trim())) {


                    Toast.makeText(ActivityDetailsListActivity.this, "Enter Hrs Spent in Activity", Toast.LENGTH_SHORT).show();

                } else {
                    if (isFromAdapter) {
                        Log.i("from_adapter", "adpter:" + isFromAdapter);
                        updateActivitiesFirebase(activityid);
                        //new UpdateAsyntask(activityid).execute();
                        finish();
                        startActivity(getIntent());
                    } else {
                        insertIntoFirebasePhysicalActivities();
                        //new InsertAsynTask().execute();
                        finish();
                        startActivity(getIntent());
                    }


                }

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addActivityDialog.dismiss();
            }
        });

        addActivityDialog.show();

    }

    private void setAlarms(String time) {
        Calendar c = Calendar.getInstance();
        Log.i("date",""+time);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String currrentTime = df.format(c.getTime());
        // formattedDate have current date/time
        Date currentTimeDate = null;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            currentTimeDate = format.parse(currrentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliSeconds = 0;

        Log.e("milliSeconds", "" + date1);

        if (milliSeconds > 0) {

            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            intent.putExtra("time", Long.parseLong(time));
            intent.putExtra("userid", userId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(time), intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(time), pendingIntent);
        }


    }

    private void startTimePicker(final TextView timeTv, final Switch reminderSwitch) {

        TimePickerDialog startTimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String am_pm = "";
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        if (c.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (c.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";
                        currentHour = hourOfDay;
                        currentMinute = minute;
                        String min;
                        //String hour = "";
                        int hours = set12hoursFormat(currentHour);
                        if (currentMinute < 10)
                            min = "0" + currentMinute;
                        else
                            min = String.valueOf(currentMinute);

                        String timeStr = String.valueOf(currentHour) + ':' +
                                min;

                        timeTv.setText(timeStr);
                        if (timeTv.getText().toString().equalsIgnoreCase("OFF")) {

                            reminderSwitch.setChecked(false);
                        } else {

                            reminderSwitch.setChecked(true);
                        }

                    }
                }, currentHour, currentMinute, false);

        startTimePickerDialog.show();
    }

    // 12 hour format
    public int set12hoursFormat(int hour) {
        if (hour > 12) {
            hour = hour - 12;
            return hour;
        } else if (hour == 0) {
            hour = 12;
            return hour;
        }
        return hour;
    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        TimePickerDialog.OnTimeSetListener onTimeSetListener
                = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                if (calSet.compareTo(calNow) <= 0) {
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }

                setAlarm(calSet);

                String time = localDateFormat.format(calSet.getTime());

                timeTv.setText(time);
            }
        };

        timePickerDialog = new TimePickerDialog(
                ActivityDetailsListActivity.this, onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();


    }


    private void setAlarm(Calendar targetCal) {

       /* textAlarmPrompt.setText(
                "\n\n***\n"
                        + "Alarm is set@ " + targetCal.getTime() + "\n"
                        + "***\n");*/

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("activityName", activityNmEt.getText().toString().trim());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void insertIntoFirebasePhysicalActivities() {
        String ActivityID = generateActivityId();
        if (ActivityID.equals("")) {
            Log.i("acvtivityId", "" + ActivityID);
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Activities");

            FirebasePhysicalActivitiesTable physicalActivities = new FirebasePhysicalActivitiesTable();
            physicalActivities.setActivityName(activityNmEt.getText().toString().trim());
            physicalActivities.setActivityDate(activityDateEt.getText().toString().trim());
            physicalActivities.setActviityId(ActivityID);
            physicalActivities.setUserid(userId);
            physicalActivities.setAlarm(timeTv.getText().toString().trim());
            physicalActivities.setActivityType(getActivityType());
            physicalActivities.setHoursSpent(Double.parseDouble(hrsSpentEt.getText().toString().trim()));
            physicalActivities.setShouldRemind(shouldRemind);

            mDatabase.child(ActivityID).setValue(physicalActivities).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toasty.success(ActivityDetailsListActivity.this, "activity added success").show();
                    } else {
                        Toasty.error(ActivityDetailsListActivity.this, "activity added failed").show();
                    }
                }
            });

            setAlarms(timeTv.getText().toString().trim());

        }
    }

    private void retrievePhysicalActivitiesFromFirebase() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Activities");
        progressDialog.show();

        mDatabase.orderByChild("userid").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.cancel();
                Log.i("list", "" + dataSnapshot.getValue());
                Iterable<DataSnapshot> details = dataSnapshot.getChildren();
                for (DataSnapshot data : details) {
                    FirebasePhysicalActivitiesTable c = data.getValue(FirebasePhysicalActivitiesTable.class);
                    Log.i("type", "" + c.getActivityType());
                    Log.i("type_name", "" + getActivityType());
                    if (c.getActivityType() != null) {
                        if ((c.getActivityType().equals(getActivityType()))) {
                            activityList.add(c);
                            Log.i("list-size", "" + activityList.size());

                        }

                    }
                }

                if(activityList.size() > 0) {

                    activityRv.setVisibility(View.VISIBLE);
                    noActivitiesTv.setVisibility(View.GONE);

                    ActivityListAdapter adapter = new ActivityListAdapter(ActivityDetailsListActivity.this, activityList);
                    activityRv.setAdapter(adapter);

                }else {

                    activityRv.setVisibility(View.GONE);
                    noActivitiesTv.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("detailsActivity",""+databaseError.getMessage());
                progressDialog.cancel();
                Toasty.error(ActivityDetailsListActivity.this, "data fetching failed").show();
            }
        });

    }

    private void updateActivitiesFirebase(String activityID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Activities");

        FirebasePhysicalActivitiesTable activities = new FirebasePhysicalActivitiesTable();
        activities.setUserid(userId);
        activities.setActivityName(activityNmEt.getText().toString().trim());
        activities.setActivityDate(activityDateEt.getText().toString().trim());
        activities.setHoursSpent(Double.parseDouble(hrsSpentEt.getText().toString().trim()));
        activities.setShouldRemind(shouldRemind);
        activities.setActivityType(getActivityType());
        activities.setAlarm(timeTv.getText().toString().trim());
        activities.setActviityId(activityID);

        mDatabase.child(activityID).setValue(activities).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toasty.success(ActivityDetailsListActivity.this, "user details updated sucessfully").show();

                } else {
                    Toasty.error(ActivityDetailsListActivity.this, "failed").show();
                }
            }
        });
        setAlarms(timeTv.getText().toString().trim());
    }

    private void deleteActivityFromFirebase(String activityID) {
        progressDialog.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Activities");


        mDatabase.child(activityID).child("activityDate").removeValue();
        mDatabase.child(activityID).child("activityName").removeValue();
        mDatabase.child(activityID).child("activityType").removeValue();
        mDatabase.child(activityID).child("actviityId").removeValue();
        mDatabase.child(activityID).child("alarm").removeValue();
        mDatabase.child(activityID).child("hoursSpent").removeValue();
        mDatabase.child(activityID).child("shouldRemind").removeValue();
        mDatabase.child(activityID).child("userid").removeValue();
        Toasty.success(this, "deleted sucess").show();
        progressDialog.cancel();
        finish();
        startActivity(getIntent());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {

        private final Context context;

        private List<FirebasePhysicalActivitiesTable> physicalActivitiesList = new ArrayList<>();
        //EntitityPhysicalActivities entitityPhysicalActivities;


        public ActivityListAdapter(Context context, List<FirebasePhysicalActivitiesTable> stringList) {
            super();
            this.context = context;
            this.physicalActivitiesList = stringList;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_details_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            //entitityPhysicalActivities = physicalActivitiesList.get(position);
            final int positions = position;
            holder.activityNmTv.setText(physicalActivitiesList.get(position).getActivityName());
            holder.dtTv.setText("Date: " + physicalActivitiesList.get(position).getActivityDate());
            holder.hrsSpentTv.setText(String.valueOf(physicalActivitiesList.get(position).getHoursSpent() + " hrs spent"));
            holder.remainderTmTv.setText(physicalActivitiesList.get(position).getAlarm());
            Log.i("activityid", "" + physicalActivitiesList.get(position).getActviityId());
            //activityID = physicalActivitiesList.get(position).getActviityId();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("position", "" + positions);

                    Log.i("activityname", "" + physicalActivitiesList.get(position).getActivityName());
                    addActivityDetailsDialog(true, physicalActivitiesList.get(position).getActviityId());
                    activityNmEt.setText(physicalActivitiesList.get(position).getActivityName());
                    activityDateEt.setText(physicalActivitiesList.get(position).getActivityDate());
                    hrsSpentEt.setText(String.valueOf(physicalActivitiesList.get(position).getHoursSpent()));
                    timeTv.setText(physicalActivitiesList.get(position).getAlarm());
                    //  clearfields();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    alertDialog(physicalActivitiesList.get(position).getActviityId());
                    return true;
                }
            });

        }


        private void alertDialog(final String activityid) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Delete!")
                    .setMessage("Do you want to Delete this Activity?")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            deleteActivityFromFirebase(activityid);
                            //new DelAsyntask(activityid).execute();
                            //new DeletAsyntask(position).execute();
                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });

            dialog = builder.create();
            dialog.show();
        }

        private void clearfields() {
            activityDateEt.setText("");
            activityNmEt.setText("");
            hrsSpentEt.setText("");
            timeTv.setText("");
        }





        @Override
        public int getItemCount() {

            return physicalActivitiesList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView activityNmTv;
            TextView dtTv;
            TextView hrsSpentTv;
            TextView remainderTmTv;

            public ViewHolder(View itemView) {
                super(itemView);
                activityNmTv = (TextView) itemView.findViewById(R.id.activityNmTv);
                dtTv = (TextView) itemView.findViewById(R.id.dateTv);
                hrsSpentTv = (TextView) itemView.findViewById(R.id.hrsSpentTv);
                remainderTmTv = (TextView) itemView.findViewById(R.id.timeTv);

            }


        }
    }

}
