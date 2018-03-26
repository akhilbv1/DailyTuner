package dailytuner.android.com.dailytuner.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import dailytuner.android.com.dailytuner.firebase.FirebasePhysicalActivitiesTable;
import dailytuner.android.com.dailytuner.R;


public class DashboardFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart mChart;

    private float physicalCount=0
                 ,collegeCount=0
                  ,houseCount=0
                 ,readingCount=0
                 ,familyCount=0
                  ,sleepCount=0
                  ,studyingCount=0,
                  entertainmentCount=0
                 ,otherCount=0;
    private String userid;

    private ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        setHasOptionsMenu(true);

        mChart = view.findViewById(R.id.piechart);
        mChart.setUsePercentValues(true);
       // mChart.setDescription();
        mChart.setExtraOffsets(5, 10, 5, 5);

        /*hide entry lable values*/
        mChart.setDrawEntryLabels(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterTextTypeface(mTfLight);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(40f);
        mChart.setTransparentCircleRadius(40f);

        mChart.setDrawCenterText(true);


        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        userid = getArguments().getString("userid");
        Log.i("userid",""+userid);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("loading");

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(14f);

        mChart.setCenterText("Hours Spent(%)");
      //  new Asyntask().execute();
    retrievePhysicalActivitiesFromFirebase(container.getContext());
        return view;
    }


    private void retrievePhysicalActivitiesFromFirebase(final Context context) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Activities");
       dialog.show();
        mDatabase.orderByChild("userid").equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("list", "" + dataSnapshot.getValue());
                Iterable<DataSnapshot> details = dataSnapshot.getChildren();
                for (DataSnapshot data : details) {
                    FirebasePhysicalActivitiesTable c = data.getValue(FirebasePhysicalActivitiesTable.class);
                    Log.i("type", "" + c.getActivityType());
                    if (c.getActivityType().equals("Physical")) {

                        Log.i("ActivityName", "PhysicalActivity");
                        Log.i("count",""+c.getHoursSpent());
                        physicalCount = physicalCount+(float)c.getHoursSpent();
                        Log.i("physicalCount",""+physicalCount);


                    } else if (c.getActivityType().equals("College")) {

                        Log.i("ActivityName", "College");

                        Log.i("count",""+c.getHoursSpent());
                        collegeCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("collegeCount",""+collegeCount);

                    } else if (c.getActivityType().equals("House")) {

                        Log.i("ActivityName", "House");
                        Log.i("count",""+c.getHoursSpent());
                        houseCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("houseCount",""+houseCount);

                    } else if (c.getActivityType().equals("Reading")) {

                        Log.i("ActivityName", "Reading");
                        Log.i("count",""+c.getHoursSpent());
                        readingCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("readingCount",""+readingCount);

                    } else if (c.getActivityType().equals("Family")) {

                        Log.i("ActivityName", "Family");
                        Log.i("count",""+c.getHoursSpent());
                        familyCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("familyCount",""+familyCount);

                    } else if (c.getActivityType().equals("Sleep")) {

                        Log.i("ActivityName", "Sleep");
                        Log.i("count",""+c.getHoursSpent());
                        sleepCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("sleepCount",""+sleepCount);

                    } else if (c.getActivityType().equals("Study")) {

                        Log.i("ActivityName", "Study");
                        Log.i("count",""+c.getHoursSpent());
                        studyingCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("studyingCount",""+studyingCount);
                    } else if (c.getActivityType().equals("Entertainment")) {

                        Log.i("ActivityName", "Entertainment");
                        Log.i("count",""+c.getHoursSpent());
                        entertainmentCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("entertainmentCount",""+entertainmentCount);
                    }
                    else if (c.getActivityType().equals("Other")) {

                        Log.i("ActivityName", "OtherActivity");
                        Log.i("count",""+c.getHoursSpent());
                        otherCount = collegeCount+(float)c.getHoursSpent();
                        Log.i("otherCount",""+otherCount);
                    }

                }
                dialog.cancel();
                setData();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("databaseErrorrr",""+databaseError.getMessage());
                //Toasty.error(getContext(),"data fetching failed").show();
            }
        });

    }


    private void setData() {


        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(physicalCount, "Physical Activities"));
        entries.add(new PieEntry(collegeCount, "College/Work"));
        entries.add(new PieEntry(houseCount, "House Work"));
        entries.add(new PieEntry(readingCount, "Reading"));
        entries.add(new PieEntry(familyCount, "Family & Friends"));
        entries.add(new PieEntry(sleepCount, "Sleep"));
        entries.add(new PieEntry(studyingCount, "Studying"));
        entries.add(new PieEntry(entertainmentCount, "Entertainment"));
        entries.add(new PieEntry(otherCount, "Other Activities"));


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ColorTemplate.rgb("#0080ff")); // blue
        colors.add(ColorTemplate.rgb("#faa61a")); // yellow
        colors.add(ColorTemplate.rgb("#76b043")); // green
        colors.add(ColorTemplate.rgb("#58595b")); // black
        colors.add(ColorTemplate.rgb("#FFFF4081")); // pink
        colors.add(ColorTemplate.rgb("#FFFF8340")); // orange
        colors.add(ColorTemplate.rgb("#FF21D9D3")); // light green
        colors.add(ColorTemplate.rgb("#FFA6A2A4")); // light gray
        colors.add(ColorTemplate.rgb("#FF127068")); // dark green

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();


    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            if(value > 0) {

                return mFormat.format(value) + " %";
            }
            else {
                return "";
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // appDatabase = AppDatabase.getAppDatabase(context);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
