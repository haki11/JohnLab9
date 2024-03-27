package john.smith.navigationdrawer3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class ShareFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button selectImageButton;
    private ImageView imageView;

    private ActivityResultLauncher<Intent> pickImageLauncher;


    // ActivityResultLauncher for handling permission request
    private ActivityResultLauncher<String> requestPermissionLauncher;


    public ShareFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_share, container, false);
        imageView = view.findViewById(R.id.imageView);
        selectImageButton = view.findViewById(R.id.button);

        // Initialize the ActivityResultLauncher for picking an image
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                            handleSelectedImage(result.getData());
                        }
                    }
                });

        // Initialize the ActivityResultLauncher for permission request
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {
                            showToast("Permission allowed. Can access photos.");
                            openGallery();
                        } else {
                            showToast("Permission denied. Cannot access photos.");
                            showAppSettings();
                        }
                    }
                });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenGallery();
            }
        });

        return view;
    }

    private void checkPermissionAndOpenGallery() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, open gallery
            openGallery();
        } else {
            // Request permission
            requestPermissionLauncher.launch(permission);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void handleSelectedImage(Intent data) {
        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            try {
                // Use ImageDecoder to load the image from URI
                ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(ImageDecoder.decodeBitmap(source));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }


}