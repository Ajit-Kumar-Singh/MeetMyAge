package views.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.meetmyage.com.meetmyageapp.BuildConfig;
import com.meetmyage.com.meetmyageapp.R;

import java.io.File;
import java.util.List;

import Util.CommonUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.Location;
import data.model.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class EditProfileFragment extends Fragment {

    private String TAG = EditProfileFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.saveProfile) Button saveProfile;
    @BindView(R.id.editprofileName) EditText profileName;
    @BindView(R.id.editprofileStory) EditText profileStory;
    @BindView (R.id.editprofileWork) EditText profileWork;

    @BindView (R.id.gallaryPicFirst) ImageView gallaryPicFirst;
    @BindView (R.id.gallaryPicSecond) ImageView gallaryPicSecond;
    @BindView (R.id.gallaryPicThird) ImageView gallaryPicThird;
    @BindView (R.id.gallaryPicFourth) ImageView gallaryPicFourth;
    @BindView (R.id.gallaryPicFifth) ImageView gallaryPicFifth;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this,view);

        //Set Initial data
        Profile savedProfile = SessionManagementUtil.getUserData();
        profileName.setText(savedProfile.getProfileName());
        profileStory.setText(savedProfile.getProfileStory());
        profileWork.setText(savedProfile.getProfileWork());
        createAndAddDrawable();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    private void createAndAddDrawable()
    {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(3, Color.parseColor("#8E24AA"));
        drawable.setCornerRadius(8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            saveProfile.setBackground(drawable);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataToServer();
            }
        });
    }

    private void updateDataToServer()
    {
        //Update Data to server
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Profile> call = null;
        call = apiService.updateProfile(SessionManagementUtil.getUserData().getProfileId(),
                    new Profile(SessionManagementUtil.getUserData().getProfileId(),
                            profileName.getText().toString(),
                            SessionManagementUtil.getUserData().getProfileEmail(),
                            profileStory.getText().toString(),
                            profileWork.getText().toString(),new Location()));

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile responseProfile = response.body();
                SessionManagementUtil.updateProfile(
                        responseProfile.getProfileName(),responseProfile.getProfileEmail(),responseProfile.getProfileStory(),responseProfile.getProfileWork());
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.fragment_conatiner, new ProfileFragment());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }


    private boolean permissions(List<String> listPermissionsNeeded) {

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), CommonUtil.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void openImagePicker()
    {
        List<String> permissionList = CommonUtil.checkAndRequestPermissions(getApplicationContext());
        final Fragment frag = this;
        if (permissions(permissionList)) {
            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo"))
                    {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            // intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri());
                        } else {
                            File profilepath = new File(Environment.getExternalStorageDirectory(), "Profile"+System.currentTimeMillis()+".jpg");
                            Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",profilepath);
//                            mCommonUri = uri;
//                            mImagePath = profilepath.getAbsolutePath();
                        }
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonUri);
                        startActivityForResult(cameraIntent, 1); // REQUEST_IMAGE_CAPTURE = 12345
                    }
                    else if (options[item].equals("Choose from Gallery"))
                    {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }

                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Drawable drawable = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getApplicationContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
//                mImagePath = c.getString(columnIndex);
                c.close();
            }
        }
        convertPathToBitmap();
//        mImageView.setImageBitmap(mBitmap);
//        saveImageProfilePicToServer();
    }

    private void convertPathToBitmap()
    {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        mBitmap = BitmapFactory.decodeFile(mImagePath, bmOptions);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
