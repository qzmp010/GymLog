package com.scheng.gymlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.scheng.gymlog.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.loginButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext(), 0);
        startActivity(intent);
      }
    });
  }

  static Intent loginIntentFactory(Context context) {
    return new Intent(context, LoginActivity.class);
  }
}