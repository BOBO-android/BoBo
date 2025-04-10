package android.example.bobo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.example.bobo.R;
import android.example.bobo.data.model.OnboardingData;
import android.example.bobo.ui.viewmodel.OnboardingViewModel;

public class Onboarding1 extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTV;
    private TextView descriptionTV;
    private Button skipBTN;
    private Button nextBTN;
    private View dot1, dot2, dot3;
    private OnboardingViewModel onboardingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_boarding1);

        // anh xa tp giao dien
        imageView = findViewById(R.id.obImage);
        titleTV = findViewById(R.id.obTitle);
        descriptionTV = findViewById(R.id.obDescription);
        nextBTN = findViewById(R.id.btnNext);
        skipBTN = findViewById(R.id.btnSkip);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        // Khởi tạo ViewModel
        onboardingViewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        // Quan sát dữ liệu từ ViewModel
        onboardingViewModel.getOnboardingDataLiveData().observe(this, onboardingDataList -> {
            // Quan sát trang hiện tại để cập nhật nội dung
            onboardingViewModel.getCurrentPageLiveData().observe(this, currentPage -> {
                updateContent(onboardingDataList.get(currentPage));
                updateDots(currentPage);
                // Kiểm tra nếu là trang cuối, chuyển hướng khi nhấn "Next"
                if (onboardingViewModel.isLastPage()) {
                    nextBTN.setOnClickListener(v -> {
                        startActivity(new Intent(Onboarding1.this, Onboarding2.class));
                        finish();
                    });
                } else {
                    nextBTN.setOnClickListener(v -> onboardingViewModel.nextPage());
                }
            });
        });

        // Xử lý sự kiện nhấn nút "Skip"
        skipBTN.setOnClickListener(v -> {
            onboardingViewModel.skip();
            startActivity(new Intent(Onboarding1.this, Onboarding2.class));
            finish();
        });
    }

    private void updateContent(OnboardingData data) {
        imageView.setImageResource(data.getImageResId());
        titleTV.setText(data.getTitle());
        descriptionTV.setText(data.getDescription());
    }

    private void updateDots(int currentPage) {
        // Reset trạng thái dot
        dot1.setBackgroundResource(R.drawable.dot_unselected_background);
        dot2.setBackgroundResource(R.drawable.dot_unselected_background);
        dot3.setBackgroundResource(R.drawable.dot_unselected_background);

        // Cập nhật dot được chọn
        switch (currentPage) {
            case 0:
                dot1.setBackgroundResource(R.drawable.dot_selected_background);
                break;
            case 1:
                dot2.setBackgroundResource(R.drawable.dot_selected_background);
                break;
            case 2:
                dot3.setBackgroundResource(R.drawable.dot_selected_background);
                break;
        }
    }
}