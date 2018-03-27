package dailytuner.android.com.dailytuner.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dailytuner.android.com.dailytuner.activities.ActivityDetailsListActivity;
import dailytuner.android.com.dailytuner.R;


/*Created by akhil on 15/2/18.
 */

public class
ActivitiesListFragment extends Fragment implements View.OnClickListener {
    /*    private List<EntityActivitiesListPojo> mList = new ArrayList<>();
        private RecyclerView recyclerView;
        private ActivitiesListAdapter activitiesListAdapter;*/
    String userid;
    Button btn_physicalActivities,
            btn_CollegeActivities,
            btn_houseworkActivities,
            btn_ReadingActivities,
            btn_FamActivities,
            btn_SleepActivities,
            btn_StudyActivities,
            btn_entertainmentActivities, btn_OtherActivities;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_activities, container,
                false);
        intializeUI(view);
        userid = getArguments().getString("userid");
        Log.i("list", "" + userid);
        setOnClickListeners();
        return view;
    }

    private void intializeUI(View view){
        btn_physicalActivities = view.findViewById(R.id.btn_physicalActivities);
        btn_CollegeActivities = view.findViewById(R.id.btn_CollegeActivities);
        btn_houseworkActivities = view.findViewById(R.id.btn_houseworkActivities);
        btn_ReadingActivities = view.findViewById(R.id.btn_ReadingActivities);
        btn_FamActivities = view.findViewById(R.id.btn_FamActivities);
        btn_SleepActivities = view.findViewById(R.id.btn_SleepActivities);
        btn_StudyActivities = view.findViewById(R.id.btn_StudyActivities);
        btn_entertainmentActivities = view.findViewById(R.id.btn_entertainmentActivities);
        btn_OtherActivities = view.findViewById(R.id.btn_OtherActivities);
    }

    private void setOnClickListeners() {
        btn_physicalActivities.setOnClickListener(this);
        btn_CollegeActivities.setOnClickListener(this);
        btn_houseworkActivities.setOnClickListener(this);
        btn_ReadingActivities.setOnClickListener(this);
        btn_FamActivities.setOnClickListener(this);
        btn_SleepActivities.setOnClickListener(this);
        btn_StudyActivities.setOnClickListener(this);
        btn_entertainmentActivities.setOnClickListener(this);
        btn_OtherActivities.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_physicalActivities:
                launchPhysicalActivity();
                break;

            case R.id.btn_CollegeActivities:
                launchCollegeActivity();
                break;

            case R.id.btn_houseworkActivities:
                launchHouseWorkActivity();
                break;

            case R.id.btn_ReadingActivities:
                launchReadingActivity();
                break;

            case R.id.btn_FamActivities:
                launchFamActivity();
                break;

            case R.id.btn_SleepActivities:
                launchSleepActivity();
                break;

            case R.id.btn_StudyActivities:
                launchStudyingActivity();
                break;

            case R.id.btn_entertainmentActivities:
                launchEntertainmentActivity();
                break;

            case R.id.btn_OtherActivities:
                launchOtherActivity();
                break;

        }

    }

    private void launchPhysicalActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("PhysicalActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }



    private void launchCollegeActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("CollegeActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchHouseWorkActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("HouseWorkActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchReadingActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("ReadingActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchFamActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("FamActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchSleepActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("SleepActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchStudyingActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("StudyingActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchEntertainmentActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("EntertainmentActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    private void launchOtherActivity() {
        Intent intent = new Intent(getContext(), ActivityDetailsListActivity.class);
        intent.putExtra("OtherActivity", true);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }


}
