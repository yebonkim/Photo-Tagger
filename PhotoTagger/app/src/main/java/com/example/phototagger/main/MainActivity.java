package com.example.phototagger.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.common.PermissionList;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;
import com.example.phototagger.slide.SlideActivity;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<Image> allImages;
    ArrayList<Gallery> galleries;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        if(getPermission()) {
            allImages = getAllImages();
            galleries = splitByAlbum(getAllImages());
            setRecyclerView();
        }
    }

    protected void setRecyclerView() {
        adapter = new GalleryAdapter(this, galleries);
        galleryRV.setLayoutManager(new LinearLayoutManager(this));
        galleryRV.setAdapter(adapter);
    }

    protected ArrayList<Gallery> splitByAlbum(ArrayList<Image> allImages) {
        ArrayList<Gallery> results = new ArrayList<>();
        StringTokenizer stk;
        String preToken = "";
        Object token;
        Gallery newGallery;
        boolean isAlreadyContained;

        for(Image image : allImages) {
            stk = new StringTokenizer(image.getLocation(), "/");
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
                    image.setTitle(token.toString());
                    gallery.addImage(image);
                    break;
                }
            }
            if(isAlreadyContained == false) {
                newGallery = new Gallery(preToken);
                image.setTitle(token.toString());
                newGallery.addImage(image);
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

    private ArrayList<Image> getAllImages() {
        Uri uri;
        Cursor cursor;
        int pathIdx, widthIdx, heightIdx;
        ArrayList<Image> listOfAllImages = new ArrayList<Image>();
        Image newImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH};

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        pathIdx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        widthIdx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);
        heightIdx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);
        while (cursor.moveToNext()) {
            newImage = new Image(cursor.getString(pathIdx), cursor.getString(widthIdx), cursor.getString(heightIdx));
            listOfAllImages.add(newImage);
        }
        return listOfAllImages;
    }

    protected void goToSlideActivity() {
        Intent i = new Intent(this, SlideActivity.class);
        i.putExtra(IntentConstant.GALLERY, galleries.get(0));
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_slide:
                // User chose the "Settings" item, show the app settings UI...
                goToSlideActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
