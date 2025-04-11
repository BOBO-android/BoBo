package android.example.bobo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.example.bobo.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.example.bobo.databinding.ActivityRegisterStoreBinding;
import android.example.bobo.ui.viewmodel.RegisterStoreViewModel;
import android.view.View;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterStoreBinding binding;
    private RegisterStoreViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_store);
        viewModel = new ViewModelProvider(this).get(RegisterStoreViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        observeViewModel();
    }



    private void observeViewModel() {
        viewModel.getRegisterButtonClicked().observe(this, clicked -> {
            if (clicked != null && clicked) {
                // Chuyển sang màn RegisterCheckActivity
                Intent intent = new Intent(this, RegisterCheckActivity.class);
                startActivity(intent);

                // Reset trạng thái click để không bị lặp
                viewModel.getRegisterButtonClicked();
            }
        });
    }
}
