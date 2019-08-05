package views.pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import data.model.Profile;
import data.model.ProfilePhotoRequest;
import data.model.ProfilePhotoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viewmodel.ProfileViewModel;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

// Managing the Profile in this class
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String TAG = ProfileFragment.class.getSimpleName();
    private String mImagePath;
    private Uri mCommonUri;

    @BindView(R.id.userName) TextView mProfileName;
    @BindView(R.id.profileWork) TextView mProfileWork;
    @BindView(R.id.profileStory) TextView mProfileStory;
    @BindView(R.id.city) TextView mProfileCity;
    @BindView(R.id.emailId) TextView mEmailId;

    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.setting) FloatingActionButton mSetting;

    @BindView(R.id.layoutPreview) LinearLayout mLayoutPreview;
    @BindView(R.id.profileImage) ImageView mImageView;
    @BindView(R.id.addPhoto) FloatingActionButton mAddPhoto;

    private ProgressDialog mProgressDialog;
    private ProfileViewModel mViewModel;

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
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Fetching Data");
            mProgressDialog.setCancelable(false);

            mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
            return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeProfilePage();
        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
    }


    private void initializeProfilePage()
    {
        Profile savedProfile = SessionManagementUtil.getUserData();
        mProfileName.setText(savedProfile.getProfileName());
        mProfileStory.setText(savedProfile.getProfileStory());
        mEmailId.setText(savedProfile.getProfileEmail());
        mProfileWork.setText(savedProfile.getProfileWork());
        mProfileCity.setText(savedProfile.getLocation().getCity());
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment editProfile = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, editProfile).commit();
            }
        });

        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Bitmap profileBitmap = ((BottomNavigation)getActivity()).getBitmap();
        if (profileBitmap == null)
        {
            observeProfilePic();
        }
        else
        {
            mImageView.setImageBitmap(profileBitmap);
        }
    }

    private void observeProfilePic()
    {
        mProgressDialog.show();
        mViewModel.getProfileImagePath().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s!=null)
                {
                    if (s.isEmpty())
                    {
                        mImageView.setBackgroundResource(R.drawable.man);
                        mProgressDialog.hide();
                    }
                    else
                    {
                        Bitmap profileBitmap = null;
                        profileBitmap = CommonUtil.convertStringToBitmap(s);
                        mImageView.setImageBitmap(profileBitmap);
                        ((BottomNavigation)getActivity()).setBitmap(profileBitmap);
                        mProgressDialog.hide();
                    }
                }
                else
                {
                    mProgressDialog.show();
                }
            }
        });
    }

    public void openImagePicker()
    {
        List<String> permissionList = CommonUtil.checkAndRequestPermissions(getApplicationContext());
        final Fragment frag = this;
        if (CommonUtil.permissions(permissionList, getActivity())) {
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

        // For gallery we get the data
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

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap profileBitmap = BitmapFactory.decodeFile(mImagePath, bmOptions);
        mImageView.setImageBitmap(profileBitmap);
        mViewModel.saveImageProfilePicToServer(mImagePath);
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
