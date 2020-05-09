package com.bytedance.videoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    String Tag = "MainActivityhj";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        String url = "https://s3.pstatp.com/toutiao/static/img/logo.271e845.png";
        Glide.with(this).load(url).into(imageView);
        Button button = findViewById(R.id.openvideo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Tag, "点击");
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
    }
}
