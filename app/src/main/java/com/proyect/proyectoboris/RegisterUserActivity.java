package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proyect.proyectoboris.providers.ImageProvider;
import com.proyect.proyectoboris.utils.CompressorBitmapImage;
import com.proyect.proyectoboris.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterUserActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    ImageProvider mImageProvider;

    CircleImageView mCircleImagePhoto;
    //ImageView mImageViewProfile;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputEmail;
    Button mButtonCreateProfile;

    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();

    private File mImageFile;
    private String mImageUrl;
    private String mName;
    private String mEmail;

    private ProgressDialog mProgressDialog;

    private final int GALLERY_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mImageProvider = new ImageProvider();

        mProgressDialog = new ProgressDialog(this);

        mCircleImagePhoto = findViewById(R.id.imageViewProfile);
        //mImageViewProfile = findViewById(R.id.imageViewProfile);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mButtonCreateProfile = findViewById(R.id.btnCreateProfile);

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)
                .setExcludeVideos(false)//Pre selected Image Urls//Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage


        mCircleImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starPix();
            }
        });

        /*mImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });*/

        mButtonCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mTextInputName.getText().toString();
                mEmail = mTextInputEmail.getText().toString();
                if (!mName.equals("") && mImageFile != null){
                    saveImage();
                }else {
                    Toast.makeText(RegisterUserActivity.this, "Selecciona una imagen e ingresa un nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void starPix() {
        Pix.start(RegisterUserActivity.this, mOptions);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile = new File(mReturnValues.get(0));
            mCircleImagePhoto.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(RegisterUserActivity.this, mOptions);
            } else {
                Toast.makeText(RegisterUserActivity.this, "Otorga los permisos a la cámara", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR","Mensaje"+e.getMessage());
            }
        }
    }*/

    private void updateUserInfo(String url){
        //if (!mName.isEmpty() && !mEmail.isEmpty()){
            User user = new User();
            user.setId(mAuthProvider.getId());
            user.setName(mName);
            user.setEmail(mEmail);
            user.setImage(url);
            mUserProvider.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //mProgressDialog.dismiss();
                    Toast.makeText(RegisterUserActivity.this, "Listo!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterUserActivity.this, MapUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        //}
    }

    /*void clickRegister(){
        mName = mTextInputName.getText().toString();
        mEmail = mTextInputEmail.getText().toString();


        if(!mName.isEmpty() && !mEmail.isEmpty() && mImageFile != null){
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveImage();

            /*User user = new User();
            user.setId(mAuthProvider.getId());
            user.setName(name);
            user.setEmail(email);*/


            //update(user);
    /*
        }else{
            Toast.makeText(this, "Ingrese todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void saveImage() {
        mImageProvider.save(RegisterUserActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            updateUserInfo(image);
                        }
                    });
                }else {
                    Toast.makeText(RegisterUserActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*private void saveImage() {
        byte[] imageByte = CompressorBitmapImage.getImage(this,mImageFile.getPath(), 500, 500);
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("user_images").child(mAuthProvider.getId() + ".jpg");
        UploadTask uploadTask = storage.putBytes(imageByte);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            User user = new User();
                            user.setId(mAuthProvider.getId());
                            user.setName(mName);
                            user.setEmail(mEmail);
                            user.setImage(image);
                            mUserProvider.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(RegisterUserActivity.this, "Listo!!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterUserActivity.this, MapUserActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(RegisterUserActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    void update(User user){
        mUserProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(RegisterUserActivity.this, MapUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterUserActivity.this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}