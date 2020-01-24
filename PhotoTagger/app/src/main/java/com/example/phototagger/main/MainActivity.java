package com.example.phototagger.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;
import com.example.phototagger.server.EsQueryResponse;
import com.example.phototagger.server.ServerQuery;
import com.example.phototagger.slide.SlideActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class MainActivity extends AppCompatActivity {
    private final static String INVALID_GALLERY_NAME = "INVALID_GALLERY_NAME";
    private final static int DOUBLE_BACK_KEY_INTERVAL_TIME = 2000;

    @BindView(R.id.list_gallery)
    RecyclerView mGalleryList;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    SearchView mSearchView;

    private final static String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ArrayList<Image> mAllImages;
    private ArrayList<Gallery> mAllGalleries;
    private ArrayList<Gallery> mQueriedGalleries;
    private GalleryAdapter mAdapter;

    private boolean mIsAllGallerySet;

    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.allGallery));

        mQueriedGalleries = new ArrayList<>();

        if (isPermitted()) {
            mAllImages = getAllImages();
            mAllGalleries = splitByGallery(mAllImages);
            setRecyclerView(mAllGalleries, true);
        }
    }

    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (query.isEmpty()) {
                setRecyclerViewForEmptyQuery();
            } else {
                setRecyclerViewForQuery(query);
            }
            return false;
        }
        @Override
        public boolean onQueryTextChange(String query) {
            if (query.isEmpty()) {
                setRecyclerViewForEmptyQuery();
            }
            return false;
        }

        private void setRecyclerViewForEmptyQuery() {
            if (!mIsAllGallerySet) {
                setRecyclerView(mAllGalleries, true);
            }
        }

        private void setRecyclerViewForQuery(String query) {
            ServerQuery.queryToES(query, new Callback<EsQueryResponse>() {
                @Override
                public void onResponse(Call<EsQueryResponse> call, Response<EsQueryResponse> response) {
                    if (response != null && response.body() != null) {
                        EsQueryResponse queryResponse = response.body();
                        Gallery gallery = innerHitsToGallery(query, queryResponse.getHits().getHits());
                        mQueriedGalleries.clear();
                        mQueriedGalleries.add(gallery);
                        setRecyclerView(mQueriedGalleries, false);
                    }
                }

                @Override
                public void onFailure(Call<EsQueryResponse> call, Throwable t) {

                }
            });
        }

        private Gallery innerHitsToGallery(String query, List<EsQueryResponse.InnerHits> responses) {
            ArrayList<Image> images = new ArrayList<>();
            Image image;
            Gallery gallery = new Gallery(query, true);

            for (EsQueryResponse.InnerHits res : responses) {
                image = pathToImage(res.getSource().getImageName());

                if (image != null) {
                    images.add(image);
                }
            }
            gallery.setImages(images);
            return gallery;
        }
    };

    @NonNull
    private Image pathToImage(@NonNull String imagePath) {
        File file = new File(imagePath);
        Image newImage;
        BitmapFactory.Options options = new BitmapFactory.Options();

        if (!file.exists()) {
            return null;
        }

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        newImage = new Image(imagePath, options.outWidth + "", options.outHeight + "");
        newImage.setTitle(getImageNameFromPath(imagePath));

        return newImage;
    }

    private void setRecyclerView(ArrayList<Gallery> galleries, boolean isAllGallerySet) {
        mAdapter = new GalleryAdapter(galleries);
        mGalleryList.setLayoutManager(new LinearLayoutManager(this));
        mGalleryList.setAdapter(mAdapter);
        mIsAllGallerySet = isAllGallerySet;
    }

    @NonNull
    private String getImageNameFromPath(@NonNull String path) {
        StringTokenizer stk = new StringTokenizer(path, "/");
        int galleryIdx = stk.countTokens() - 1;
        String result = "";

        for (int i = 0; i <= galleryIdx; i++) {
            result = stk.nextToken();
        }

        return result;
    }

    private ArrayList<Gallery> splitByGallery(@NonNull ArrayList<Image> allImages) {
        ArrayList<Gallery> galleries = new ArrayList<>();
        StringTokenizer stk;
        String preGalleryName = INVALID_GALLERY_NAME;
        String galleryName = INVALID_GALLERY_NAME;
        int galleryNameIdx;
        Gallery gallery = null;

        boolean isFirst = true;     // for opening only first gallery
        boolean isOtherGallery;

        for (Image image : allImages) {
            stk = new StringTokenizer(image.getLocation(), "/");
            galleryNameIdx = stk.countTokens() - 2;

            for (int i = 0; i <= galleryNameIdx; i++) {
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
        intent.putExtra(IntentConstant.GALLERY, mAllGalleries.get(0));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (!mIsAllGallerySet) {
            setRecyclerView(mAllGalleries, true);
        } else {
            if (System.currentTimeMillis() - mBackPressedTime  < DOUBLE_BACK_KEY_INTERVAL_TIME) {
                finish();
            } else {
                mBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.msg_back_again), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        mSearchView = (SearchView) mToolbar.getMenu().findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(mQueryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_slide:
                if (mAllGalleries.size() == 0) {
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
