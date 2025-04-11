package android.example.bobo.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.MyAccountViewModel;
import android.example.bobo.ui.viewmodel.SettingViewModel;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class SettingActivity extends AppCompatActivity {
    private LinearLayout backContainer;
    private TextView errorMessageTV;
    private Button backButton, agreeButton, logoutButton;
    private SettingViewModel settingViewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backContainer = findViewById(R.id.back_container);
        logoutButton = findViewById(R.id.btn_log_out);

        settingViewModel= new ViewModelProvider(this).get(SettingViewModel.class);

        settingViewModel.getLogoutResult().observe(this, result -> {
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("access_token");
            editor.apply();

            Intent intent = new Intent(SettingActivity.this, Onboarding1.class);
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            backButton = dialogView.findViewById(R.id.btn_back_error);
            agreeButton = dialogView.findViewById(R.id.btn_ok);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            backButton.setOnClickListener( v1 -> {
                dialog.dismiss();
            });

            agreeButton.setOnClickListener( v2 -> {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = preferences.getString("access_token", "");
                settingViewModel.logout(token);
            });
            dialog.show();
        });
        backContainer.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, MenuSideDrawerActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
