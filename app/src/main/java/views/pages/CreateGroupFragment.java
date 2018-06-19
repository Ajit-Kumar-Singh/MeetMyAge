package views.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import data.model.GroupRequest;
import data.model.GroupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateGroupFragment extends Fragment {

	@BindView(R.id.groupImage)
	ImageView mGrpImage;

	@BindView(R.id.groupName)
	EditText mGrpName;

	@BindView(R.id.groupStory)
	EditText mGrpStory;

	@BindView(R.id.saveGroup)
	Button mSaveGroup;

	private Uri mCommonUri;
	private OnFragmentInteractionListener mListener;

	private static final String TAG = CreateGroupFragment.class.getSimpleName();

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

		mGrpImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openImagePicker();
			}
		});

		mSaveGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saveGroupDataToServer();
			}
		});
		return view;
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
							File f = new File(Environment.getExternalStorageDirectory(), "GROUP"+System.currentTimeMillis()+".jpg");
							Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",f);
							mCommonUri = uri;
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
			if (requestCode == 1) {
				mGrpImage.setImageURI(mCommonUri);
			} else if (requestCode == 2) {
				Uri selectedImage = data.getData();
				String[] filePath = {MediaStore.Images.Media.DATA};
				Cursor c = getApplicationContext().getContentResolver().query(selectedImage, filePath, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
				drawable = new BitmapDrawable(thumbnail);
				mGrpImage.setImageDrawable(drawable);
			}
		}
	}

	void saveGroupDataToServer()
	{
		//Update Data to server
		ApiInterface apiService =
				ApiClient.getClient().create(ApiInterface.class);

		Call<GroupResponse> call = null;
		call = apiService.createGroup(SessionManagementUtil.getUserData().getProfileId(),
				new GroupRequest(mGrpName.getText().toString(),mGrpStory.getText().toString(),SessionManagementUtil.getLocation()));

		call.enqueue(new Callback<GroupResponse>() {
			@Override
			public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
				Toast.makeText(getActivity(), "This is my Toast message!",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Call<GroupResponse> call, Throwable t) {
				// Log error here since request failed
				Toast.makeText(getActivity(), "This is my failure message!",
						Toast.LENGTH_LONG).show();
				Log.e(TAG, t.toString());
			}
		});
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
