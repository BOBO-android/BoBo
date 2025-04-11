package android.example.bobo.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.MenuSideDrawerViewModel;
import android.example.bobo.ui.viewmodel.MyAccountViewModel;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuSideDrawerActivity extends AppCompatActivity {
    private Button myAccountButton,settingButton;
    private MenuSideDrawerViewModel menuSideDrawerViewModel;
    private CircleImageView userAvatar;
    private TextView userName, userEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_side_drawer);

        myAccountButton = findViewById(R.id.btn_my_account);
        settingButton = findViewById(R.id.btn_setting);
        userAvatar = findViewById(R.id.user_avatar);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);

        //khoi tao
        menuSideDrawerViewModel = new ViewModelProvider(this).get(MenuSideDrawerViewModel.class);

        menuSideDrawerViewModel.getUserInfo().observe(this, userInfo -> {
            if (userInfo != null) {
                userName.setText(userInfo.getFullName());
                userEmail.setText(userInfo.getEmail());
                if (userInfo.getImage() != null && !userInfo.getImage().isEmpty()) {
                    Glide.with(this)
                            .load(userInfo.getImage())
                            .placeholder(R.drawable.test_image)
                            .error(R.drawable.test_image)
                            .into(userAvatar);
                } else {
                    userAvatar.setImageResource(R.drawable.avatars);
                }

            } else {
                Toast.makeText(this, "User info is null", Toast.LENGTH_SHORT).show();
            }
        });

        menuSideDrawerViewModel.getUserInfoErrorMessage().observe(this, error -> {
                    Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        });


        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = preferences.getString("access_token", "");
        menuSideDrawerViewModel.getUserInformation(token);

        if (!token.isEmpty()) {
            menuSideDrawerViewModel.getUserInformation(token);
        } else {
            Toast.makeText(this, "Token is empty. Please log in again.", Toast.LENGTH_LONG).show();
        }
        // chuyen trang
        myAccountButton.setOnClickListener( v -> {
            Intent intent = new Intent(MenuSideDrawerActivity.this, MyAccountActivity.class);
            startActivity(intent);
            finish();

        });
        // chuyen trang
        settingButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuSideDrawerActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
