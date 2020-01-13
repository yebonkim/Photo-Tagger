package com.example.phototagger.main;

import android.Manifest;
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
import android.widget.Toast;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
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
    private final static String INVALID_GALLERY_NAME = "INVALID_GALLERY_NAME";

    @BindView(R.id.list_gallery)
    RecyclerView mGalleryList;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_gallery_name)
    TextView mGalleryName;

    private final static String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ArrayList<Image> mAllImages;
    private ArrayList<Gallery> mGalleries;
    private GalleryAdapter mAdapter;

    private PopupMenu mPopupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mPopupMenu = new PopupMenu(getApplicationContext(), mGalleryName);

        if (isPermitted()) {
            mAllImages = getAllImages();
            mGalleries = splitByGallery(mAllImages);
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        mAdapter = new GalleryAdapter(mGalleries);
        mGalleryList.setLayoutManager(new LinearLayoutManager(this));
        mGalleryList.setAdapter(mAdapter);
    }

    private ArrayList<Gallery> splitByGallery(@NonNull ArrayList<Image> allImages) {
        ArrayList<Gallery> galleries = new ArrayList<>();
        StringTokenizer stk;
        String preGalleryName = INVALID_GALLERY_NAME;
        String galleryName = INVALID_GALLERY_NAME;
        int galleryIdx;
        Gallery gallery = null;

        boolean isFirst = true;     // for opening only first gallery
        boolean isOtherGallery;

        for (Image image : allImages) {
            stk = new StringTokenizer(image.getLocation(), "/");
            galleryIdx = stk.countTokens() - 2;

            for (int i = 0; i <= galleryIdx; i++) {
                galleryName = stk.nextToken();
            }

            isOtherGallery = !preGalleryName.equals(galleryName);
            preGalleryName = galleryName;

            if (isOtherGallery) {
                gallery = new Gallery(preGalleryName, isFirst);
                isFirst = false;
                galleries.add(gallery);
            }

            image.setTitle(stk.nextToken());
            gallery.addImage(image);
        }

        return galleries;
    }

    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkAndRequestPermission();
        }
        return true;
    }

    private boolean checkAndRequestPermission() {
        int result;

        for (int i = 0; i < PERMISSION_LIST.length; i++) {
            result = ContextCompat.checkSelfPermission(this, PERMISSION_LIST[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION_LIST, 1);
                return false;
            }
        }

        return true;
    }

    private ArrayList<Image> getAllImages() {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        ArrayList<Image> listOfAllImages = new ArrayList<Image>();

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH};

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        int pathIdx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int widthIdx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);
        int heightIdx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);

        if (!cursor.moveToFirst()) {
            return listOfAllImages;
        }

        while (cursor.moveToNext()) {
            listOfAllImages.add(new Image(cursor.getString(pathIdx), cursor.getString(widthIdx),
                    cursor.getString(heightIdx)));
        }

        return listOfAllImages;
    }

    private void goToSlideActivity() {
        Intent intent = new Intent(this, SlideActivity.class);
        intent.putExtra(IntentConstant.GALLERY, mGalleries.get(0));
        startActivity(intent);
    }

    @OnClick(R.id.layout_gallery_name)
    public void popUpGalleryChangeMenu() {
        getMenuInflater().inflate(R.menu.toolbar_title_menu, mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_all:
                        break;
                    case R.id.menu_photo_tagger:
                        break;
                }
                mGalleryName.setText(item.getTitle());
                return false;
            }
        });
        mPopupMenu.show();
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
                if (mGalleries.size() == 0) {
                    Toast.makeText(this, R.string.noGallery, Toast.LENGTH_SHORT).show();
                } else {
                    goToSlideActivity();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
