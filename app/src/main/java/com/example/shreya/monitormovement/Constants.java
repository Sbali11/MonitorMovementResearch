package com.example.shreya.monitormovement;
import com.google.android.gms.location.DetectedActivity;


/**
 * Created by shreya on 2/8/18.
 */

final class Constants {

    private Constants() {}

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.activityrecognition";

    static final String KEY_ACTIVITY_UPDATES_REQUESTED = PACKAGE_NAME +
            ".ACTIVITY_UPDATES_REQUESTED";

    static final String KEY_DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";

    /**
     * The desired time between activity detections. Larger values result in fewer activity
     * detections while improving battery life. A value of 0 results in activity detections at the
     * fastest possible rate.
     */
    static final long DETECTION_INTERVAL_IN_MILLISECONDS = 60 * 1000; // 60 seconds
    /**
     * List of DetectedActivity types that we monitor in this sample.
     */

    static final int[] MONITORED_ACTIVITIES = {

            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING,
            DetectedActivity.STILL,
            DetectedActivity.TILTING,
            DetectedActivity.WALKING

    };
}