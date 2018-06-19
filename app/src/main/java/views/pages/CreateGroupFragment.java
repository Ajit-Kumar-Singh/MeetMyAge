package views.pages;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meetmyage.com.meetmyageapp.BuildConfig;
import com.meetmyage.com.meetmyageapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateGroupFragment extends Fragment {

    @BindView(R.id.groupImage1)
    ImageView mGrpImageFirst;

    @BindView(R.id.groupImage2)
    ImageView mGrpImageSecond;

    @BindView(R.id.groupImage3)
    ImageView mGrpImageThird;

    @BindView(R.id.groupImage4)
    ImageView mGrpImageFourth;

    private Uri mCommonUri;
    private int mSelectedIndex;
    private OnFragmentInteractionListener mListener;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;  // Declare this integer globally
    public CreateGroupFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_create_group, container, false);
        ButterKnife.bind(this,view);

        mGrpImageFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker(0);
            }
        });

        mGrpImageSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker(1);
            }
        });

        mGrpImageThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker(2);
            }
        });

        mGrpImageFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImagePicker(3);
            }
        });
        return view;
    }

    public static List<String> checkAndRequestPermissions(Context context) {

        int camera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        int readStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStorage = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int fineLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (fineLoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarseLoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        return listPermissionsNeeded;
    }

    private boolean permissions(List<String> listPermissionsNeeded) {

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void openImagePicker(final int index)
    {
        List<String> permissionList = checkAndRequestPermissions(getApplicationContext());
        final Fragment frag = this;
        if (permissions(permissionList)) {
            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            mSelectedIndex = index;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo"))
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            // intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri());
                        } else {
                            File f = new File(Environment.getExternalStorageDirectory(), "GROUP"+System.currentTimeMillis()+".jpg");
                            Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",f);
                            mCommonUri = uri;
                        }
                        startActivityForResult(intent, 1); // REQUEST_IMAGE_CAPTURE = 12345
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
        //super.onActivityResult(requestCode, resultCode, data);
        Drawable drawable = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null)
                {
                    try {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(mCommonUri);
                        drawable = Drawable.createFromStream(inputStream, mCommonUri.toString() );
                    } catch (FileNotFoundException e) {
                        drawable = getResources().getDrawable(R.drawable.man);
                    }
                }
            }
            else if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getApplicationContext().getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                drawable=new BitmapDrawable(thumbnail);
            }
            setPreviewToImageView(drawable);
        }
    }

    private void setPreviewToImageView(Drawable drawable)
    {
        switch (mSelectedIndex)
        {
            case 0:
                mGrpImageFirst.setImageDrawable(drawable);
                break;
            case 1:
                mGrpImageSecond.setImageDrawable(drawable);
                break;
            case 2:
                mGrpImageThird.setImageDrawable(drawable);
                break;
            case 3:
                mGrpImageFourth.setImageDrawable(drawable);
                break;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
