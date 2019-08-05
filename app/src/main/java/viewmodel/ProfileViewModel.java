package viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import repository.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<String> mProfileImagePath;
    private ProfileRepository mInstance;

    public ProfileViewModel(Application application) {
        super(application);
        mInstance = ProfileRepository.getInstance();
        mProfileImagePath = mInstance.getProfileImagePath();
    }

    public LiveData<String> getProfileImagePath()
    {
        return mProfileImagePath;
    }

    public void saveImageProfilePicToServer(String imagePath)
    {
        mInstance.saveImagePathToServer(imagePath);
    }
}
