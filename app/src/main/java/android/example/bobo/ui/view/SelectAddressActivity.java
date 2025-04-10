package android.example.bobo.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.example.bobo.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectAddressActivity extends AppCompatActivity {
    private String selectedAddress = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnSave = findViewById(R.id.btnSaveAddress);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            selectedAddress = radioButton.getText().toString();
        });
        TextView txtCancel = findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(v -> finish());


        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedAddress", selectedAddress);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}
