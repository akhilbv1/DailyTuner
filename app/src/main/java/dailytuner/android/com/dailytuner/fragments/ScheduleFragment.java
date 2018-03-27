package dailytuner.android.com.dailytuner.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dailytuner.android.com.dailytuner.R;
import dailytuner.android.com.dailytuner.activities.ActivityDetailsListActivity;
import dailytuner.android.com.dailytuner.activities.AlarmReceiver;
import es.dmoral.toasty.Toasty;

/**
  Created by akhil on 26/3/18.
 */

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    private LinearLayout physicalLl, personalLl, studyingLl, sleepingLl, othersLl, collegeLl,othersSecondLl;
    private TextView txtPersonal,txtOther,txtOthersSecond;
    private AlertDialog dialog;
    final static int RQS_1 = 1;
    private TimePickerDialog timePickerDialog;
    boolean isFromOtherFirst;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_schedule, container, false);

        physicalLl = view.findViewById(R.id.physicalLl);
        personalLl = view.findViewById(R.id.personalLl);
        studyingLl = view.findViewById(R.id.studyingLl);
        sleepingLl = view.findViewById(R.id.sleepingLl);
        othersSecondLl = view.findViewById(R.id.othersSecondLl);
        othersLl = view.findViewById(R.id.othersLl);
        collegeLl = view.findViewById(R.id.collegeLl);
        txtPersonal = view.findViewById(R.id.Txt_personal);
        txtOther = view.findViewById(R.id.Txt_other);
        txtOthersSecond = view.findViewById(R.id.othersSecondTv);

        physicalLl.setOnClickListener(this);

        personalLl.setOnClickListener(this);

        sleepingLl.setOnClickListener(this);

        collegeLl.setOnClickListener(this);

        studyingLl.setOnClickListener(this);

        othersLl.setOnClickListener(this);
        othersSecondLl.setOnClickListener(this);


        return view;
    }

    private void alertDialog(final int hour,final int min,final String activityStr) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Remainder!")
                .setMessage("Do you want to set Remainder for this Activity?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                       setCalenderObject(hour,min,activityStr);
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

    private void alertDialogForPesonalAndOthers(final int hour, final int min,final String activityStr) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Remainder!")
                .setMessage("Do you want to set Reminder for this Activity?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        setCalenderObject(hour,min,activityStr);
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

    private void setCalenderObject(int hour,int min,final String activityStr){
        if(hour==0 || min==0){
            Log.i("DailyTunerSchedule","hours and min are empty");
        }
        else{
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        Log.i("hour",""+hour);
        Log.i("hour",""+min);


        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, min);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);


        setAlarm(calSet,activityStr);}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.physicalLl:
                alertDialog(5,30,"Physical Activities");
                break;
            case R.id.personalLl:
                openTimePickerDialog(false,"Personal Work");
                break;
            case R.id.sleepingLl:
                alertDialog(22,30,"Sleeping");
                break;
            case R.id.collegeLl:
                alertDialog(9,30,"College");
                break;
            case R.id.studyingLl:
                alertDialog(18,30,"Studying");
                break;
            case R.id.othersLl:
                isFromOtherFirst =true;
                openTimePickerDialogOthers(false,"Other Activities");
                break;
            case R.id.othersSecondLl:
                isFromOtherFirst =false;
                openTimePickerDialogOthers(false,"Other Activities");
                break;
        }
    }
    private void openTimePickerDialog(boolean is24r,final String activityStr) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a");
        TimePickerDialog.OnTimeSetListener onTimeSetListener
                = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();
                Log.i("hour",""+hourOfDay);
                Log.i("hour",""+minute);


                calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

               /* if (calSet.compareTo(calNow) <= 0) {
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }*/

                setAlarm(calSet,activityStr);

                String time = localDateFormat.format(calSet.getTime());

                    txtPersonal.setText(time);
                    alertDialogForPesonalAndOthers(hourOfDay,minute,activityStr);


            }
        };

        timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();


    }

    private void openTimePickerDialogOthers(boolean is24r,final String activityStr) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a");
        TimePickerDialog.OnTimeSetListener onTimeSetListener
                = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();
                Log.i("hour",""+hourOfDay);
                Log.i("hour",""+minute);


                calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

               /* if (calSet.compareTo(calNow) <= 0) {
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }*/

                setAlarm(calSet,activityStr);

                String time = localDateFormat.format(calSet.getTime());

                if(isFromOtherFirst){
                    txtOther.setText(time);
                    alertDialogForPesonalAndOthers(hourOfDay,minute,activityStr);
                }
                else {
                    txtOthersSecond.setText(time);
                    alertDialogForPesonalAndOthers(hourOfDay,minute,activityStr);
                }
            }
        };

        timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();


    }
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
    private void setAlarm(Calendar targetCal,String activtyStr) {

       /* textAlarmPrompt.setText(
                "\n\n***\n"
                        + "Alarm is set@ " + targetCal.getTime() + "\n"
                        + "***\n");*/

        Intent intent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
        intent.putExtra("activityName", activtyStr);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), RQS_1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


    }



}
