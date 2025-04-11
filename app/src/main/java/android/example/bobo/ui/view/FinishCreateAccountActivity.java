package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

public class FinishCreateAccountActivity extends AppCompatActivity {
    private ImageButton close;
    private Button getStartedButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_create_account);

        close = findViewById(R.id.close_button);
        getStartedButton = findViewById(R.id.get_started_now_button);

        close.setOnClickListener( v -> {
            Intent intent = new Intent(FinishCreateAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(FinishCreateAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
