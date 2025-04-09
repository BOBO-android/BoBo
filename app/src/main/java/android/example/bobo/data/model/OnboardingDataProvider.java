package android.example.bobo.data.model;

import android.example.bobo.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardingDataProvider {
    public static List<OnboardingData> getOnboardingData() {
        List<OnboardingData> onboardingDataList = new ArrayList<>();
        onboardingDataList.add(new OnboardingData(
                R.drawable.image_onboarding1,
                "Welcome to the most \n tastiest app",
                "You know, this app is edible meaning you \n can eat it!"
        ));
        onboardingDataList.add(new OnboardingData(
                R.drawable.image_onboarding2,
                "We use nitro on \n bicycles for delivery!",
                "For very fast delivery we use nitro on \n bicycles, kidding, but we’re very fast."
        ));
        onboardingDataList.add(new OnboardingData(
                R.drawable.image_onboarding3,
                "We’re the besties \n of birthday peoples",
                "We send cakes to our plus members, (only \n one cake per person)"
        ));
        return onboardingDataList;
    }
}