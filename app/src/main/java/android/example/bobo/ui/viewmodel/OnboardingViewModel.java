package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.OnboardingData;
import android.example.bobo.data.model.OnboardingDataProvider;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class OnboardingViewModel extends ViewModel {
    private final MutableLiveData<List<OnboardingData>> onboardingDataLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPageLiveData = new MutableLiveData<>();
    private final List<OnboardingData> onboardingDataList;

    public OnboardingViewModel() {
        onboardingDataList = OnboardingDataProvider.getOnboardingData();
        onboardingDataLiveData.setValue(onboardingDataList);
        currentPageLiveData.setValue(0);
    }

    public LiveData<List<OnboardingData>> getOnboardingDataLiveData() {
        return onboardingDataLiveData;
    }

    public LiveData<Integer> getCurrentPageLiveData() {
        return currentPageLiveData;
    }

    public void nextPage() {
        int currentPage = currentPageLiveData.getValue() != null ? currentPageLiveData.getValue() : 0;
        if (currentPage < onboardingDataList.size() - 1) {
            currentPageLiveData.setValue(currentPage + 1);
        }
    }

    public void skip() {

    }

    public boolean isLastPage() {
        int currentPage = currentPageLiveData.getValue() != null ? currentPageLiveData.getValue() : 0;
        return currentPage == onboardingDataList.size() - 1;
    }
}