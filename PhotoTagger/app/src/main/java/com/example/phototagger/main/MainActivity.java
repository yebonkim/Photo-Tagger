package com.example.phototagger.main;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.phototagger.R;
import com.example.phototagger.common.PermissionList;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.galleryRV)
    RecyclerView galleryRV;

    ArrayList<String> allImagePaths;
    ArrayList<Gallery> galleries;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(getPermission()) {
            allImagePaths = getAllShownImagesPath();
            galleries = splitByAlbum(allImagePaths);
            setRecyclerView();
        }
    }

    protected void setRecyclerView() {
        adapter = new GalleryAdapter(this, galleries);
        galleryRV.setLayoutManager(new LinearLayoutManager(this));
        galleryRV.setAdapter(adapter);
    }

    protected ArrayList<Gallery> splitByAlbum(ArrayList<String> allImagePaths) {
        ArrayList<Gallery> results = new ArrayList<>();
        StringTokenizer stk;
        String preToken = "";
        Object token;
        Gallery newGallery;
        boolean isAlreadyContained;

        for(String path : allImagePaths) {
            stk = new StringTokenizer(path, "/");
            token = stk.nextElement();
            isAlreadyContained = false;
            while(true) {
                preToken = token.toString();
                token = stk.nextElement();
                if(stk.hasMoreElements() == false)
                    break;
            }
            for(Gallery gallery : results) {
                if (gallery.getTitle().equals(preToken)) {
                    isAlreadyContained = true;
                    gallery.addImage(new Image(token.toString(), path));
                    break;
                }
            }
            if(isAlreadyContained == false) {
                newGallery = new Gallery(preToken);
                newGallery.addImage(new Image(token.toString(), path));
                results.add(newGallery);
            }
        }

        return results;
    }


    public boolean getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //마시멜로 이상버전에서만
            return checkAndRequestPermission();
        }
        return true;
    }

    public boolean checkAndRequestPermission() {
        int result;

        String[] permissionList = PermissionList.PERMISSION_LIST;

        for (int i = 0; i < permissionList.length; i++) {
            result = ContextCompat.checkSelfPermission(this, permissionList[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionList, 1);
                return false;
            }
        }

        return true;
    }

    private ArrayList<String> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
