package com.scheng.gymlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.scheng.gymlog.database.GymLogRepository;
import com.scheng.gymlog.database.entities.User;
import com.scheng.gymlog.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding binding;

  private GymLogRepository repository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    repository = GymLogRepository.getRepository(getApplication());

    binding.loginButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!verifyUser()) {
          toastMaker("Invalid username or password");
        } else {
          Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext(), 0);
          startActivity(intent);
        }
      }
    });
  }

  private boolean verifyUser() {
    String username = binding.userNameLoginEditText.getText().toString();
    if (username.isEmpty()) {
      toastMaker("Username may not be blank");
      return false;
    }
    User user = repository.getUserByUserName(username);
    if (user != null) {
      String password = binding.passwordLoginEditText.getText().toString();
      if (password.equals(user.getPassword())) {
        return true;
      } else {
        toastMaker("Invalid password");
      }
    }
    toastMaker(String.format("No %s found", username));
    return false;
  }

  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  static Intent loginIntentFactory(Context context) {
    return new Intent(context, LoginActivity.class);
  }
}