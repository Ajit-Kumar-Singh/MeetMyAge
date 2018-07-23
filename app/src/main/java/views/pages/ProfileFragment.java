package views.pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import data.model.ProfilePhotoRequest;
import data.model.ProfilePhotoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

// Managing the Profile in this class
public class ProfileFragment extends Fragment {

    private static Bitmap mBitmap = null;
    private OnFragmentInteractionListener mListener;
    private String TAG = ProfileFragment.class.getSimpleName();
    private boolean isPreview = true;
    private String mImagePath;
    private Uri mCommonUri;

    @BindView(R.id.userName) TextView mProfileName;
    @BindView(R.id.profileWork) TextView mProfileWork;
    @BindView(R.id.profileStory) TextView mProfileStory;
    @BindView(R.id.city) TextView mProfileCity;
    @BindView(R.id.emailId) TextView mEmailId;

    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.layoutPreview) LinearLayout mLayoutPreview;
    @BindView(R.id.layoutEdit) LinearLayout mLayoutEdit;
    @BindView(R.id.saveProfile) Button saveProfile;

    @BindView(R.id.editprofileName) EditText mEditProfileName;
    @BindView(R.id.editprofileStory) EditText mEditProfileStrory;
    @BindView (R.id.editprofileWork) EditText mEditProfileWork;

    @BindView(R.id.profileImage) ImageView mImageView;
    @BindView(R.id.addPhoto) FloatingActionButton mAddPhoto;

    private ProgressDialog mProgressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.fragment_profile, container, false);
            ButterKnife.bind(this,view);
            return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        showPreviewPage();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (isPreview)
                    {
                        showPreviewPage();
                    }
                    return true;
                }
                return false;
            }
        });

        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
    }


    private void showPreviewPage()
    {
        if (isPreview)
        {
            mLayoutEdit.setVisibility(View.GONE);
            mLayoutPreview.setVisibility(View.VISIBLE);
            //Set Initial data
            Profile savedProfile = SessionManagementUtil.getUserData();
            mProfileName.setText(savedProfile.getProfileName());
            mProfileStory.setText(savedProfile.getProfileStory());
            mEmailId.setText(savedProfile.getProfileEmail());
            mProfileWork.setText(savedProfile.getProfileWork());
            mProfileCity.setText(savedProfile.getLocation().getCity());
            isPreview = false;
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditPage();
                }
            });

            if (mBitmap == null)
            {
                fetchProfilePicFromServerAndSaveToBitmap();
            }
            else
            {
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }

    private void showEditPage()
    {
        if (!isPreview)
        {
            //Set Initial data
            Profile savedProfile = SessionManagementUtil.getUserData();
            mEditProfileName.setText(savedProfile.getProfileName());
            mEditProfileStrory.setText(savedProfile.getProfileStory());
            mEditProfileWork.setText(savedProfile.getProfileWork());
            mLayoutEdit.setVisibility(View.VISIBLE);
            mLayoutPreview.setVisibility(View.GONE);
            isPreview =  true;
            saveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDataToServer();
                }
            });
        }
    }

    private void fetchProfilePicFromServerAndSaveToBitmap()
    {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Fetching Data");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        //Update Data to server
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ProfilePhotoResponse> call = null;
        call = apiService.fetchProfileData(SessionManagementUtil.getUserData().getProfileId());
        call.enqueue(new Callback<ProfilePhotoResponse>() {
            @Override
            public void onResponse(Call<ProfilePhotoResponse> call, Response<ProfilePhotoResponse> response) {
                ProfilePhotoResponse responseProfile = response.body();
                String data = responseProfile.getData();
                if (data.isEmpty())
                {
                    mImageView.setBackgroundResource(R.drawable.man);
                }
                else
                {
                    mBitmap = CommonUtil.convertStringToBitmap(responseProfile.getData());
                    mImageView.setImageBitmap(mBitmap);
                }
                mProgressDialog.hide();
            }

            @Override
            public void onFailure(Call<ProfilePhotoResponse> call, Throwable t) {
                // Log error here since request failed
                mProgressDialog.hide();
                mImageView.setBackgroundResource(R.drawable.man);
                Log.e(TAG, t.toString());
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
                new Profile(SessionManagementUtil.getUserData().getProfileId(),mEditProfileName.getText().toString(),mEditProfileStrory.getText().toString(),mEditProfileWork.getText().toString(),new Location()));

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile responseProfile = response.body();
                SessionManagementUtil.updateProfile(
                        responseProfile.getProfileName(),"ajit.sumitsingh@gmail.com",responseProfile.getProfileStory(),responseProfile.getProfileWork());
                showPreviewPage();
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
                            mCommonUri = uri;
                            mImagePath = profilepath.getAbsolutePath();
                        }
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonUri);
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
                mImagePath = c.getString(columnIndex);
                c.close();
            }
        }
        convertPathToBitmap();
        mImageView.setImageBitmap(mBitmap);
        saveImageProfilePicToServer();
    }

    private void convertPathToBitmap()
    {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        mBitmap = BitmapFactory.decodeFile(mImagePath, bmOptions);
    }

    private void saveImageProfilePicToServer()
    {
        if (mBitmap != null)
        {
            String base64String = CommonUtil.encodeImage(mBitmap);
            //Update Data to server
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Void> call = null;
            call = apiService.uploadProfilePhoto(SessionManagementUtil.getUserData().getProfileId(),
                    new ProfilePhotoRequest(SessionManagementUtil.getUserData().getProfileName(), base64String));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                    {
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                  
                }
            });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
