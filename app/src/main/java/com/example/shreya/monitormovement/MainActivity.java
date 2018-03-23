package com.example.shreya.monitormovement;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;




public class MainActivity
        extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    protected static final String TAG = "MainActivity";
    private Context mContext;
    private ActivityRecognitionClient mActivityRecognitionClient;
    TextView vehicle;
    TextView sitting;
    TextView running;
    TextView walking;

    public MainActivity() {}

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_activities_adapter);
        mContext = this;
        setUpdatesRequestedState(true);
        mActivityRecognitionClient = new ActivityRecognitionClient(this);
        vehicle = ((TextView)findViewById(R.id.vehicle));
        vehicle.setText(Integer.toString(Utils.TIME[0]));

        sitting = ((TextView)findViewById(R.id.Sitting));
        sitting.setText(Integer.toString(Utils.TIME[1]));

        running = ((TextView)findViewById(R.id.Running));
        running.setText(Integer.toString(Utils.TIME[2]));

        walking = ((TextView)findViewById(R.id.Walking));
        walking.setText(Integer.toString(Utils.TIME[3]));
        File file=new File(this.getFilesDir().getPath()+"/Code.txt");
        if(file.exists()) {
            try {
                FileInputStream fin = openFileInput("Code.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fin));
                String text;
                while ((text = br.readLine()) != null) {
                    String[] splitted = text.split(" ");
                    int i = 0;
                    for (String part : splitted) {
                        if (part.equals("Sitting:")) {
                            sitting.setText(splitted[++i]);
                            Utils.TIME[1] = Integer.parseInt(sitting.getText().toString());
                        } else if (part.equals("Vehicle:")) {

                            vehicle.setText(splitted[++i]);
                            Utils.TIME[0] = Integer.parseInt(vehicle.getText().toString());
                        } else if (part.equals("Running:")) {
                            running.setText(splitted[++i]);
                            Utils.TIME[2] = Integer.parseInt(running.getText().toString());
                        } else if (part.equals("Walking:")) {
                            walking.setText(splitted[++i]);
                            Utils.TIME[3] = Integer.parseInt(walking.getText().toString());
                        }
                    }
                }
                Toast.makeText(getBaseContext(), "file read", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
        requestActivityUpdatesHandler();
    }




    protected void onPause()
    {
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        super.onPause();
        setUpdatesRequestedState(true);
        updateDetectedActivitiesList();
    }




    public void requestActivityUpdatesHandler()
    {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent());

        //task.addOnSuccessListener(new MainActivity(this));
        //task.addOnFailureListener(new MainActivity.2(this));
    }












    protected void onResume()
    {
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        super.onResume();
        File file = new File(getFilesDir().getPath() + "/Code.txt");
        if (file.exists()) {
            try
            {
                FileInputStream fin = openFileInput("Code.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fin));
                String text;
                if ((text = br.readLine()) != null) {}

                sitting.setText(text);

                //Toast.makeText(getBaseContext(), "file read", 0).show();
            }
            catch (Exception localException) {}
        }


        updateDetectedActivitiesList();
    }





    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        if (s.equals("com.google.android.gms.location.activityrecognition.DETECTED_ACTIVITIES"))
        {
            updateDetectedActivitiesList();
        }
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }









    private void setUpdatesRequestedState(boolean requesting)
    {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("com.google.android.gms.location.activityrecognition.ACTIVITY_UPDATES_REQUESTED", requesting).apply();
    }





    protected void updateDetectedActivitiesList()
    {
        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(mContext)
                        .getString("com.google.android.gms.location.activityrecognition.DETECTED_ACTIVITIES", ""));

        vehicle.setText(Integer.toString(Utils.TIME[0]));


        sitting.setText(Integer.toString(Utils.TIME[1]));


        running.setText(Integer.toString(Utils.TIME[2]));


        walking.setText(Integer.toString(Utils.TIME[3]));

        File file=null;
        FileOutputStream fileOutputStream = null;
        try {
            file = getFilesDir();
            fileOutputStream = openFileOutput("Code.txt", Context.MODE_PRIVATE); //MODE PRIVATE
            fileOutputStream.write((("Sitting: "+sitting.getText().toString())+"\n").getBytes());
            fileOutputStream.write((("Vehicle: "+vehicle.getText().toString())+"\n").getBytes());
            fileOutputStream.write((("Running: "+sitting.getText().toString())+"\n").getBytes());
            fileOutputStream.write((("Walking: "+sitting.getText().toString())+"\n").getBytes());
            Toast.makeText(this, "Saved \n" + "Path --" + file + "\tCode.txt", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex) {
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
