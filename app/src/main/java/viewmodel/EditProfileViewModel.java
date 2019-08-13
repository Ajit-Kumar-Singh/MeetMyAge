package viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import repository.EditProfileRepository;
import repository.ProfileRepository;

public class EditProfileViewModel extends AndroidViewModel{

    private EditProfileRepository mInstance;

    public EditProfileViewModel(Application application) {
        super(application);
        mInstance = EditProfileRepository.getInstance();
    }
}
