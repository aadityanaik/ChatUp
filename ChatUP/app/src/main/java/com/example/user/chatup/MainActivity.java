package com.example.user.chatup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView iv_call, iv_chat;
    ViewFlipper flipper;
    int eid, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_call = findViewById(R.id.iv_call);
        iv_chat = findViewById(R.id.iv_chat);
        flipper = findViewById(R.id.flipper);

        final SharedPreferences editor = getSharedPreferences("settings", MODE_PRIVATE);
        uid = editor.getInt("uid", 0);

        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {

                    class Idgetter {
                        int exp_id;
                        String fname, lname, category;
                        float ratings, consistency;
                    }

                    @Override
                    protected String doInBackground(Void... voids) {
                        try {
                            return new NetworkManager().getDataFromURL(new URL("http://192.168.43.36/helpline/get_exp.php?uid="+uid));
                        } catch (IOException e) {
                            return "NAN";
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (!s.equals("NAN")) {
                            Gson parser = new Gson();
                            Idgetter idgetter = parser.fromJson(s, Idgetter.class);
                            eid = idgetter.exp_id;
                            String f = idgetter.fname;
                            String l = idgetter.lname;
                            String cat = idgetter.category;
                            Log.i("Hmmm", "" + eid);
                            Intent i = new Intent(MainActivity.this, ChatActivity.class);
                            i.putExtra("eid", eid);
                            i.putExtra("fname", f);
                            i.putExtra("lname", l);
                            i.putExtra("cat", cat);
                            startActivity(i);
                        }
                    }
                }.execute();
            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "91-22-27546669"));
                startActivity(i);
            }
        });

        int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};

        for(int image : images) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(image);
            flipper.addView(imageView);
        }

        flipper.setAutoStart(true);
        flipper.setFlipInterval(6500);
        flipper.startFlipping();
    }
}
