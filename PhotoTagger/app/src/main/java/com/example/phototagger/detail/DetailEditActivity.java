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
    Toolbar toolbar;
    @BindView(R.id.imageV)
    ImageView imageV;
    @BindView(R.id.nameETV)
    EditText nameETV;
    @BindView(R.id.sizeTV)
    TextView sizeTV;
    @BindView(R.id.locationTV)
    TextView locationTV;
    @BindView(R.id.tagAutoCompleteTV)
    AutoCompleteTextView tagTV;

    Gallery gallery;
    int imageIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getIntentConstant();
        getSupportActionBar().setTitle(gallery.getImages().get(imageIdx).getTitle());
        setView();
    }

    protected void getIntentConstant() {
        gallery = getIntent().getParcelableExtra(IntentConstant.GALLERY);
        imageIdx = getIntent().getIntExtra(IntentConstant.IMAGE_POSITION, 0);
    }

    protected void setView() {
        Glide.with(this).load(gallery.getImages().get(imageIdx).getLocation()).into(imageV);
        nameETV.setText(gallery.getImages().get(imageIdx).getTitle());
        locationTV.setText(gallery.getImages().get(imageIdx).getLocation());
        sizeTV.setText(gallery.getImages().get(imageIdx).getWidth()+" * "+gallery.getImages().get(imageIdx).getHeight());
        tagTV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, toArr(gallery.getImages().get(imageIdx).getTag())));
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
        i.putExtra(IntentConstant.GALLERY, gallery);
        i.putExtra(IntentConstant.IMAGE_POSITION, imageIdx);
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
                // User chose the "Settings" item, show the app settings UI...
                gallery.getImages().get(imageIdx).setTitle(nameETV.getText().toString());
                goToDetailActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
