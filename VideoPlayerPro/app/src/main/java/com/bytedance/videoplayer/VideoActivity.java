package com.bytedance.videoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    final private String Tag = "VideoActivityhj";
    int length = 0;
    int current = 0;
    final private int TAG = 1;
    private Handler handler;
    private int SEEKBAR = 1;
    private  int TEXT = 2;
    boolean flag = false;
    private Button open;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(Tag, "Oncreate");
        super.onCreate(savedInstanceState);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d(Tag, "现在为横屏播放");
            // 去掉标题栏
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            Log.d(Tag, "标题取消成功");
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置videoView全屏播放
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);//设置videoView横屏播放
        }

        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);
        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        open = findViewById(R.id.open);

        final SeekBar slip = findViewById(R.id.slip);
        final TextView currenttime = findViewById(R.id.currenttime);

        Uri uri = getIntent().getData();
        if (uri != null) {
            videoView.setVideoPath(uri.getPath());
        }
        Log.d(Tag, "设置路径成功"+videoView.getDuration());
        Log.d(Tag, length+" "+current);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            videoView.start();
        }

        slip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Log.d(Tag, i + " " + seekBar.getMax() + " " + i/seekBar.getMax());
                if (flag){
                    videoView.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                flag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                flag = false;
            }
        });
        Log.d(Tag, "当前手机状态为:"+String.valueOf(getResources().getConfiguration().orientation));


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag, "点击开始按钮");
                Log.d(Tag, String.valueOf(videoView.getCurrentPosition()) + " " + videoView.getDuration());
                videoView.start();
                length = videoView.getDuration();
                current = videoView.getCurrentPosition();
                slip.setMax(videoView.getDuration());
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag, "点击终止按钮");

                videoView.pause();
            }
        });




        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == SEEKBAR){
                    //Log.d(Tag, "接受到SEEKBAR信息"+videoView.getDuration()+" "+videoView.getCurrentPosition() + " "+slip.getProgress());
                    slip.setProgress(videoView.getCurrentPosition());
                    handler.sendEmptyMessageDelayed(SEEKBAR, 1000);
                }
                if(msg.what == TEXT){
                    //Log.d(Tag, "接受到TEXT消息:"+getTime(videoView.getCurrentPosition()));

                    currenttime.setText(getTime(videoView.getCurrentPosition()));
                    handler.sendEmptyMessageDelayed(TEXT, 1000);;
                }
                return true;

            }

        });

        handler.sendEmptyMessage(SEEKBAR);
        handler.sendEmptyMessage(TEXT);

    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    //构造当前视频时间
    private String getTime(int mesc){
        int second = (int) (mesc / 1000.0);
        int hour = second / 3600;
        second = second - hour * 3600;
        int minute = second /60;
        second = second - minute * 60;
        String h;
        String m;
        String s;
        if (hour < 10){h = "0" + hour;}else{h = ""+hour;}
        Log.d(Tag, h + " " + hour);
        if(minute<10){m = "0" + minute;}else{m = ""+minute;}
        Log.d(Tag, m + " " + minute);
        if(second < 10){s = "0" + second;}else{s = ""+second;}
        Log.d(Tag, s + " " + second);
        return h+":"+m+":"+s;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(Tag, "Result_ok");
            if (uri != null) {
                Log.d(Tag, uri.getPath());
                videoView.setVideoPath(uri.getPath());
            }
        }
    }

}
