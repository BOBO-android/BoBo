package android.example.bobo.ui.view;

import android.example.bobo.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CancelOrderActivity extends AppCompatActivity {

    private EditText etCancelReason;
    private TextView tvCharacterCount;
    private Button btnSubmit;
    private ImageButton btnBack;
    private static final int MAX_CHARACTERS = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        // Initialize views
        etCancelReason = findViewById(R.id.etCancelReason);
        tvCharacterCount = findViewById(R.id.tvCharacterCount);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Set up character counter
        etCancelReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String countText = length + " / " + MAX_CHARACTERS;
                tvCharacterCount.setText(countText);

                // Optional: Limit text length
                if (length > MAX_CHARACTERS) {
                    s.delete(MAX_CHARACTERS, length);
                }
            }
        });

        // Set up submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = etCancelReason.getText().toString().trim();
                if (reason.isEmpty()) {
                    Toast.makeText(CancelOrderActivity.this, "Please enter your reason", Toast.LENGTH_SHORT).show();
                } else {
                    // Here you would send the reason to your backend or process it
                    submitCancellationReason(reason);
                }
            }
        });

        // Set up back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and go back
            }
        });
    }

    private void submitCancellationReason(String reason) {
        // TODO: Implement the API call or processing for the cancellation reason

        // For now, just show a success message and close the activity
        Toast.makeText(this, "Order cancellation submitted", Toast.LENGTH_SHORT).show();
        finish();
    }
}