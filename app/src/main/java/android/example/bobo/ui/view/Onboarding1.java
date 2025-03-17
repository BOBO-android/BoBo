package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Onboarding1 extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTV;
    private TextView descriptionTV;
    private Button skipBTN;
    private Button nextBTN;

    private OnboardingData[]  onboardingData = new OnboardingData []{
        new OnboardingData(
                R.drawable.image_onboarding1,
                "Welcome to the most \n tastiest app",
                "You know, this app is edible meaning you \n can eat it!"
        ),
        new OnboardingData(
                R.drawable.image_onboarding2,
                "We use nitro on \n bicycles for delivery!",
                "For very fast delivery we use nitro on \n bicycles, kidding, but we’re very fast."
        ),
        new OnboardingData(
                R.drawable.image_onboarding3,
                "We’re the besties \n of birthday peoples",
                "We send cakes to our plus members, (only \n one cake per person)"
        )


    };

    private int currentPage = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_boarding1);

        // anh xa tp giao dien
        imageView = findViewById(R.id.obImage);
        titleTV = findViewById(R.id.obTitle);
        descriptionTV = findViewById(R.id.obDescription);
        nextBTN = findViewById(R.id.btnNext);
        skipBTN = findViewById(R.id.btnSkip);

        updateContent();
        updateDots();
        nextBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentPage++;
                if( currentPage <onboardingData.length){
                    updateContent();
                    updateDots();
                } else {
                    startActivity(new Intent(Onboarding1.this, Onboarding2.class));
                    finish();
                }

            }
        });

        skipBTN.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Onboarding1.this, Onboarding2.class));
            }
        });

    }
    private void updateContent() {
        if (currentPage < onboardingData.length) {
            OnboardingData data = onboardingData[currentPage];
            imageView.setImageResource(data.getImageResId());
            titleTV.setText(data.getTitle());
            descriptionTV.setText(data.getDescription());

        }
    }

    private void updateDots(){

        View dot1 = findViewById(R.id.dot1);
        View dot2 = findViewById(R.id.dot2);
        View dot3 = findViewById(R.id.dot3);

        // rest trang thai dot
        dot1.setBackgroundResource(R.drawable.dot_unselected_background);
        dot2.setBackgroundResource(R.drawable.dot_unselected_background);
        dot3.setBackgroundResource(R.drawable.dot_unselected_background);

        switch (currentPage){
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
        System.out.println(currentPage);
    }

}
