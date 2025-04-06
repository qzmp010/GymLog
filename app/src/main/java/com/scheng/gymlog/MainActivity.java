package com.scheng.gymlog;

import android.content.Context;
import android.content.Intent;
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

  private static final String MAIN_ACTIVITY_USER_ID = "com.scheng.gymlog.MAIN_ACTIVITY_USER_ID";
  private ActivityMainBinding binding;
  private GymLogRepository repository;

  public static final String TAG = "SC_GYMLOG";

  String exercise = "";
  double weight = 0.0;
  int reps = 0;

  //TODO: add login info
  int loggedInUserId = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loginUser();

    if (loggedInUserId == -1) {
      Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
      startActivity(intent);
    }

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

  private void loginUser() {
    loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, -1);
  }

  static Intent mainActivityIntentFactory(Context context, int userId) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
    return intent;
  }

  private void insertGymLogRecord() {
    if (exercise.isEmpty()) {
      return;
    }
    GymLog log = new GymLog(exercise, weight, reps, loggedInUserId);
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