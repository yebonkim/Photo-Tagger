package com.example.phototagger.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class DetailEditActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.edit_name)
    EditText mNameEditText;
    @BindView(R.id.text_size)
    TextView mSizeText;
    @BindView(R.id.text_location)
    TextView mLocationTV;
    @BindView(R.id.text_tag_auto_complete)
    AutoCompleteTextView mTagText;

    Gallery mGallery;
    int mImageIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getIntentData();
        getSupportActionBar().setTitle(mGallery.getImages().get(mImageIdx).getTitle());
        setView();
    }

    protected void getIntentData() {
        mGallery = getIntent().getParcelableExtra(IntentConstant.GALLERY);
        mImageIdx = getIntent().getIntExtra(IntentConstant.IMAGE_POSITION, 0);
    }

    protected void setView() {
        Glide.with(this).load(mGallery.getImages().get(mImageIdx).getLocation()).into(mImage);
        mNameEditText.setText(mGallery.getImages().get(mImageIdx).getTitle());
        mLocationTV.setText(mGallery.getImages().get(mImageIdx).getLocation());
        mSizeText.setText(mGallery.getImages().get(mImageIdx).getWidth() + " * " + mGallery.getImages().get(mImageIdx).getHeight());
        mTagText.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                toArr(mGallery.getImages().get(mImageIdx).getTag())));
    }

    protected String[] toArr(ArrayList<String> tag) {
        String[] strArr = new String[tag.size()];
        int idx=0;

        for(String tagStr : tag) {
            strArr[idx++] = tagStr;
        }

        return strArr;
    }

    protected void goToDetailActivity() {
        Intent i = new Intent(DetailEditActivity.this, DetailActivity.class);
        i.putExtra(IntentConstant.GALLERY, mGallery);
        i.putExtra(IntentConstant.IMAGE_POSITION, mImageIdx);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                mGallery.getImages().get(mImageIdx).setTitle(mNameEditText.getText().toString());
                goToDetailActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
