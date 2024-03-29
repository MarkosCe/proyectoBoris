package com.proyect.proyectoboris.providers;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proyect.proyectoboris.utils.CompressorBitmapImage;

import java.io.File;
import java.util.Date;

public class ImageProvider {

    StorageReference mStorage;

    public ImageProvider(){
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(), 500, 500);
        StorageReference storage = mStorage.child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    public Task<Uri> getDownloadUri(){
        return mStorage.getDownloadUrl();
    }
}
