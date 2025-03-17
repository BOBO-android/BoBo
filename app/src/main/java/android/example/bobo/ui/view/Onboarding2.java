package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Onboarding2  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.on_boarding2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onborading2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the Login Button (Make sure the ID matches your XML)
        Button loginButton = findViewById(R.id.btnLogin);

        // Set Click Listener for the Login Button
        loginButton.setOnClickListener(v -> {
            // Redirect to HomeActivity
            Intent intent = new Intent(Onboarding2.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Close Onboarding2 so the user can't go back
        });
    }
}
