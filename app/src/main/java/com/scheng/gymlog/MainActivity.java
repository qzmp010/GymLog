package com.scheng.gymlog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import com.scheng.gymlog.database.GymLogRepository;
import com.scheng.gymlog.database.entities.GymLog;
import com.scheng.gymlog.database.entities.User;
import com.scheng.gymlog.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String MAIN_ACTIVITY_USER_ID = "com.scheng.gymlog.MAIN_ACTIVITY_USER_ID";
  private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.scheng.gymlog.SAVED_INSTANCE_STATE_USERID_KEY";
  private static final int LOGGED_OUT = -1;

  private ActivityMainBinding binding;
  private GymLogRepository repository;

  public static final String TAG = "SC_GYMLOG";

  String exercise = "";
  double weight = 0.0;
  int reps = 0;

  private int loggedInUserId = LOGGED_OUT;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    repository = GymLogRepository.getRepository(getApplication());
    loginUser(savedInstanceState);

    //user not logged in at this point; go to login screen
    if (loggedInUserId == LOGGED_OUT) {
      Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
      startActivity(intent);
    }

    updateSharedPreference();

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

  private void loginUser(Bundle savedInstanceState) {
    //check shared preference for logged in user read from the file
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

    if (loggedInUserId == LOGGED_OUT && savedInstanceState != null
        && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
      loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
    }
    if (loggedInUserId == LOGGED_OUT) {
      loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
    }
    if (loggedInUserId == LOGGED_OUT) {
      return;
    }

    LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
    userObserver.observe(this, user -> {
      this.user = user;
      if (this.user != null) {
        invalidateOptionsMenu();
      }
    });
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
    updateSharedPreference();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.logout_menu, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuItem item =menu.findItem(R.id.logoutMenuItem);
    item.setVisible(true);
    if (user == null) {
      return false;
    }
    item.setTitle(user.getUsername());
    item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(@NonNull MenuItem item) {
        showLogoutDialog();
        return false;
      }
    });
    return true;
  }

  private void showLogoutDialog() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
    final AlertDialog alertDialog = alertBuilder.create();

    alertBuilder.setMessage("Logout?");

    alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        logout();
      }
    });

    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        alertDialog.dismiss();
      }
    });

    alertBuilder.create().show();
  }

  private void logout() {
    loggedInUserId = LOGGED_OUT;
    updateSharedPreference();
    getIntent().putExtra(MAIN_ACTIVITY_USER_ID, loggedInUserId);

    startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
  }

  private void updateSharedPreference() {
    SharedPreferences sharedPreferences = getApplicationContext()
        .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
    sharedPrefEditor.putInt(getString(R.string.preference_userId_key), LOGGED_OUT);
    sharedPrefEditor.apply();
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
    ArrayList<GymLog> allLogs = repository.getAllLogsByUserId(loggedInUserId);
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