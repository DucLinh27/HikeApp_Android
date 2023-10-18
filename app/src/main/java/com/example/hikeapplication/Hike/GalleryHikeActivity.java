package com.example.hikeapplication.Hike;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hikeapplication.ConnectDb;
import com.example.hikeapplication.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

public class GalleryHikeActivity extends AppCompatActivity {
ImageView preview_img;
ImageButton photo_btn, prev_btn, next_btn, delete_btn;
    int index;
    ArrayList<String> listImage;
    Bitmap bitmapImage;
    boolean checkFileBlob = false;
    ConnectDb db;
    final int CAMERA_INTENT = 51;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        assert ab != null;
        ab.setTitle("Gallery of hike");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_hike);

        preview_img = findViewById(R.id.preview_img);
        listImage = new ArrayList<>();
        index = 0;
        getAllImage();
        if (listImage.isEmpty()) {
            Toast.makeText(this, "No Image in database!", Toast.LENGTH_LONG).show();
        } else {
            display();
        }

        prev_btn = findViewById(R.id.prev_btn);
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listImage.isEmpty()) {
                    Toast.makeText(GalleryHikeActivity.this, "No Image!", Toast.LENGTH_LONG).show();
                } else {
                    if (index == 0) {
                        index = listImage.size() - 1;
                    } else {
                        index -= 1;
                    }
                    display();
                }
            }
        });
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listImage.isEmpty()) {
                    Toast.makeText(GalleryHikeActivity.this, "No Image!", Toast.LENGTH_LONG).show();

                } else {
                    int indexOld = listImage.size() - 1;
                    if (index == indexOld) {
                        index = 0;
                    } else {
                        index += 1;
                    }
                    display();
                }
            }
        });
        photo_btn = findViewById(R.id.photo_btn);
        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                someActivityResultLauncher.launch(openCamera);
            }
        });

        delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new ConnectDb(getApplicationContext());
                db.deleteImage(listImage.get(index));
                getAllImage();
                index = listImage.size() - 1;
                display();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        preview_img.setImageBitmap(photo);
                        if (photo != null) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] img = bos.toByteArray();
                            String image = Base64.getEncoder().encodeToString(img);
                            int hike_id = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("hike_id")));
                            db = new ConnectDb(getApplicationContext());
                            db.addImage(image, hike_id);
                            getAllImage();
                            index = listImage.size() - 1;
                            display();
                        }
                    }
                }
            });

    void getAllImage() {
        listImage.clear();
        int hike_id = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("hike_id")));
        db = new ConnectDb(getApplicationContext());
        Cursor cursor = db.getAllImage(hike_id);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data!", Toast.LENGTH_LONG).show();
        }
        while (cursor.moveToNext()) {
            listImage.add(cursor.getString(1));
        }
    }
    private void display() {
        Picasso.get().load(listImage.get(index)).into(preview_img, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                String img = listImage.get(index);
                byte[] theByteArray = Base64.getDecoder().decode(img);
                Bitmap image = BitmapFactory.decodeByteArray(theByteArray, 0, theByteArray.length);
                if (image != null) {
                    preview_img.setImageBitmap(Bitmap.createBitmap(image));
                } else {
                    Toast.makeText(GalleryHikeActivity.this, "URL Image is incorrect!", Toast.LENGTH_LONG).show();
                    db = new ConnectDb(getApplicationContext());
                    db.deleteImage(listImage.get(index));
                    index = listImage.size() - 1;
                    display();
                }

            }
        });
    }
}