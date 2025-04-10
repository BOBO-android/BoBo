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

public class SelectPaymentActivity extends AppCompatActivity {
    private String selectedPayment = "Cash on delivery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnSave = findViewById(R.id.btnSaveMethod);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            selectedPayment = radioButton.getText().toString();
        });
        TextView txtCancel = findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(v -> finish());


        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedPaymentMethod", selectedPayment);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}
