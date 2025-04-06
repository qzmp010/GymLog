package com.scheng.gymlog;

import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import com.scheng.gymlog.database.GymLogRepository;
import com.scheng.gymlog.database.entities.GymLog;
import com.scheng.gymlog.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private GymLogRepository repository;

  public static final String TAG = "SC_GYMLOG";

  String exercise = "";
  double weight = 0.0;
  int reps = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    repository = GymLogRepository.getRepository(getApplication());

    binding.logDisplayTextValue.setMovementMethod(new ScrollingMovementMethod());

    binding.logButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getInformationFromDisplay();
        insertGymLogRecord();
        updateDisplay();
      }
    });
  }

  private void insertGymLogRecord() {
    GymLog log = new GymLog(exercise, weight, reps);
    repository.insertGymLog(log);
  }

  private void updateDisplay() {
    String currentInfo = binding.logDisplayTextValue.getText().toString();
    Log.d(TAG, "Current Info: " + currentInfo);
    String newDisplay = String.format("Exercise: %s%nWeight: %.2f%nReps: %d%n=-=-=%n%s",
        exercise, weight, reps, currentInfo);
    binding.logDisplayTextValue.setText(newDisplay);
    Log.i(TAG, repository.getAllLogs().toString());
  }

  private void getInformationFromDisplay() {
    exercise = binding.exerciseInputEditText.getText().toString();

    try {
      weight = Double.parseDouble(binding.weightInputEditText.getText().toString());
    } catch (NumberFormatException e) {
      Log.d(TAG, "Error reading value from Weight edit text");
    }

    try {
      reps = Integer.parseInt(binding.repInputEditText.getText().toString());
    } catch (NumberFormatException e) {
      Log.d(TAG, "Error reading value from Reps edit text");
    }
  }
}