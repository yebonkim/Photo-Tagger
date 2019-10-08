package com.example.phototagger.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.common.PermissionList;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;
import com.example.phototagger.slide.SlideActivity;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.galleryList)
    RecyclerView galleryList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.galleryChangeText)
    TextView galleryChange;

    ArrayList<Image> allImages;
    ArrayList<Gallery> galleries;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if(isPermitted()) {
            allImages = getAllImages();
            galleries = splitByAlbum(getAllImages());
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        adapter = new GalleryAdapter(this, galleries);
        galleryList.setLayoutManager(new LinearLayoutManager(this));
        galleryList.setAdapter(adapter);
    }

    private ArrayList<Gallery> splitByAlbum(@NonNull ArrayList<Image> allImages) {
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


    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkAndRequestPermission();
        }
        return true;
    }

    private boolean checkAndRequestPermission() {
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
        Image newImage;
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

    private void goToSlideActivity() {
        Intent i = new Intent(this, SlideActivity.class);
        i.putExtra(IntentConstant.GALLERY, galleries.get(0));
        startActivity(i);
        finish();
    }

    @OnClick(R.id.galleryChange)
    public void popUpGalleryChangeMenu() {
        PopupMenu menu = new PopupMenu(getApplicationContext(), galleryChange);
        getMenuInflater().inflate(R.menu.toolbar_title_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_all:
                        break;
                    case R.id.menu_photo_tagger:
                        break;
                }
                galleryChange.setText(item.getTitle());
                return false;
            }
        });
        menu.show();
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
                goToSlideActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
