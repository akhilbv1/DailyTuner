package dailytuner.android.com.dailytuner.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import dailytuner.android.com.dailytuner.R;

/**
 Created by akhil on 26/3/18.
 */

public class DailyTunerScheduleFragment extends Fragment {

    private LinearLayout physicalLl,personalLl,studyingLl,sleepingLl,othersLl,collegeLl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_schedule,container,false);

        physicalLl = view.findViewById(R.id.physicalLl);
        personalLl = view.findViewById(R.id.personalLl);
        studyingLl = view.findViewById(R.id.studyingLl);
        sleepingLl = view.findViewById(R.id.sleepingLl);
        othersLl = view.findViewById(R.id.othersLl);
        collegeLl = view.findViewById(R.id.collegeLl);

        physicalLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        personalLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        sleepingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        collegeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        studyingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        othersLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        return view;
    }
}
