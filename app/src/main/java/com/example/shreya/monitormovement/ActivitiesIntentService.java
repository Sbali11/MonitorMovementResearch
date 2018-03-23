package com.example.shreya.monitormovement;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ActivitiesIntentService
        extends IntentService {
    protected static final String TAG = "DetectedActivitiesIS";

    public ActivitiesIntentService() {
        super("DetectedActivitiesIS");
    }

    public void onCreate() {
        super.onCreate();
    }


    protected void onHandleIntent(Intent intent) {
        Log.i("DetectedActivitiesIS", "entered");
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString("com.google.android.gms.location.activityrecognition.DETECTED_ACTIVITIES",
                        Utils.detectedActivitiesToJson(detectedActivities))
                .apply();
        File file = new File(this.getFilesDir().getPath() + "/Code.txt");
        if (file.exists()) {
            try {
                FileInputStream fin = openFileInput("Code.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fin));
                String text;
                while ((text = br.readLine()) != null) {
                    String[] splitted = text.split(" ");
                    int i = 0;
                    for (String part : splitted) {
                        if (part.equals("Sitting:")) {
                            Utils.TIME[1] = Integer.parseInt(splitted[++i]);
                        } else if (part.equals("Vehicle:")) {
                            Utils.TIME[0] = Integer.parseInt(splitted[++i]);
                        } else if (part.equals("Running:")) {
                            Utils.TIME[2] = Integer.parseInt(splitted[++i]);
                        } else if (part.equals("Walking:")) {
                            Utils.TIME[3] = Integer.parseInt(splitted[++i]);
                        }
                    }
                }
                Toast.makeText(getBaseContext(), "file read", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
            Log.i("DetectedActivitiesIS", "activities detected");
            for (DetectedActivity da : detectedActivities) {
                if (da.getConfidence() > 40) {
                    switch (da.getType()) {
                        case 8:
                            Utils.TIME[2] += 1;
                            break;
                        case 7:
                            Utils.TIME[3] += 1;
                            break;
                        case 0:
                            Utils.TIME[0] += 1;
                            break;
                        case 3:
                            Utils.TIME[1] += 1;
                    }

                }
            }
            FileOutputStream fileOutputStream = null;
            try {
                file = getFilesDir();
                fileOutputStream = openFileOutput("Code.txt", Context.MODE_PRIVATE); //MODE PRIVATE
                fileOutputStream.write((("Sitting: " + Integer.toString(Utils.TIME[1])) + "\n").getBytes());
                fileOutputStream.write((("Walking: " + Integer.toString(Utils.TIME[3])) + "\n").getBytes());
                fileOutputStream.write((("Vehicle: " + Integer.toString(Utils.TIME[0])) + "\n").getBytes());
                fileOutputStream.write((("Running: " + Integer.toString(Utils.TIME[2])) + "\n").getBytes());
                //Toast.makeText(this, "Saved \n" + "Path --" + file + "\tCode.txt", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}