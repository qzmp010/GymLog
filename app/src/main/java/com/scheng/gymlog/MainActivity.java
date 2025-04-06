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
import java.util.ArrayList;

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

    updateDisplay();
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
    if (exercise.isEmpty()) {
      return;
    }
    GymLog log = new GymLog(exercise, weight, reps);
    repository.insertGymLog(log);
  }

  private void updateDisplay() {
    ArrayList<GymLog> allLogs = repository.getAllLogs();
    if (allLogs.isEmpty()) {
      binding.logDisplayTextValue.setText(R.string.nothing_to_show_time_to_hit_the_gym);
    }
    StringBuilder sb = new StringBuilder();
    for (GymLog log : allLogs) {
      sb.append(log);
    }
    binding.logDisplayTextValue.setText(sb.toString());
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