package com.example.shreya.monitormovement;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Adapter that is backed by an array of {@code DetectedActivity} objects. Finds UI elements in the
 * detected_activity layout and populates each element with data from a DetectedActivity
 * object.
 */
class DetectedActivitiesAdapter extends ArrayAdapter<DetectedActivity>
{

    DetectedActivitiesAdapter(Context context,
                              ArrayList<DetectedActivity> detectedActivities)
    {
        super(context, 0, detectedActivities);
    }
    TextView vehicle, sitting, running, walking;
        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent) {
            DetectedActivity detectedActivity = getItem(position);
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(
                        R.layout.activity_detected_activities_adapter, parent, false);
            }

            // Find the UI widgets.
           // TextView activityName = (TextView) view.findViewById(R.id.detected_activity_name);
            TextView vehicle = (TextView) view.findViewById(R.id.vehicle);
            vehicle.setText(Integer.toString(Utils.TIME[0]));

            TextView sitting = (TextView) view.findViewById(R.id.Sitting);
            sitting.setText(Integer.toString(Utils.TIME[1]));

            TextView running = (TextView) view.findViewById(R.id.Running);
            running.setText(Integer.toString(Utils.TIME[2]));

            TextView walking = (TextView) view.findViewById(R.id.Walking);
            walking.setText(Integer.toString(Utils.TIME[3]));
            // Populate widgets with values.

            HashMap<Integer, Integer> detectedActivitiesMap = new HashMap<>();
            if (detectedActivity.getConfidence() > 30)
                    switch (detectedActivity.getType()) {
                        case 8://running;
                            detectedActivitiesMap.put(detectedActivity.getType(), ++Utils.TIME[2]);
                            break;
                        case 7://walking;
                            detectedActivitiesMap.put(detectedActivity.getType(), ++Utils.TIME[3]);
                            break;
                        case 0:// vehicle
                            detectedActivitiesMap.put(detectedActivity.getType(), ++Utils.TIME[0]);
                            break;
                        default:
                            detectedActivitiesMap.put(detectedActivity.getType(), ++Utils.TIME[1]);
                            break;
                    }
            ArrayList<DetectedActivity> tempList = new ArrayList<>();
            for (int i = 0; i < Constants.MONITORED_ACTIVITIES.length; i++) {
                if (detectedActivitiesMap.containsKey(8)) {
                    tempList.add(new DetectedActivity(8, Utils.TIME[2]));
                } else if (detectedActivitiesMap.containsKey(7)) {
                    tempList.add(new DetectedActivity(7, Utils.TIME[3]));
                } else if (detectedActivitiesMap.containsKey(0)) {
                    tempList.add(new DetectedActivity(0, Utils.TIME[0]));
                } else if (detectedActivitiesMap.containsKey(3)) {
                    tempList.add(new DetectedActivity(3, Utils.TIME[1]));
                }
            }

            for (DetectedActivity detectedActivity2 : tempList) {
                this.add(detectedActivity2);
            }

            return view;
        }

        /**
         * Process list of recently detected activities and updates the list of {@code DetectedActivity}
         * objects backing this adapter.
         *
         * @param detectedActivities the freshly detected activities
*/
        void updateActivities(ArrayList<DetectedActivity> detectedActivities)
        {
            HashMap<Integer, Integer> detectedActivitiesMap = new HashMap<>();
            for(DetectedActivity detectedActivity: detectedActivities)
            {
                if (detectedActivity.getConfidence() > 30)
                    switch (detectedActivity.getType()) {
                        case 8://running;
                            ++Utils.TIME[2];
                            running.setText(Integer.toString(Utils.TIME[2]));
                            break;
                        case 7://walking;
                            ++Utils.TIME[3];
                            walking.setText(Integer.toString(Utils.TIME[3]));
                            break;

                        case 0:// vehicle
                            ++Utils.TIME[0];
                            vehicle.setText(Integer.toString(Utils.TIME[0]));
                            break;
                        default:
                            ++Utils.TIME[1];
                            sitting.setText(Integer.toString(Utils.TIME[1]));
                            break;
                    }
            }




            ArrayList<DetectedActivity> tempList = new ArrayList<>();
            for (int i = 0; i < Constants.MONITORED_ACTIVITIES.length; i++) {
                if (detectedActivitiesMap.containsKey(8)) {
                    tempList.add(new DetectedActivity(8, Utils.TIME[1]));
                } else if (detectedActivitiesMap.containsKey(7)) {
                    tempList.add(new DetectedActivity(7, Utils.TIME[3]));
                } else if (detectedActivitiesMap.containsKey(0)) {
                    tempList.add(new DetectedActivity(0, Utils.TIME[0]));
                } else if (detectedActivitiesMap.containsKey(3)) {
                    tempList.add(new DetectedActivity(3, Utils.TIME[2]));
                }
            }

            for (DetectedActivity detectedActivity : tempList) {
                this.add(detectedActivity);
            }

        }


}