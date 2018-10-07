package com.example.user.chatup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatview;
    EditText messageText;
    ImageButton imageButton;
    List<ChatMessage> data;
    String message;
    int eid, uid;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageText = findViewById(R.id.messageText);
        imageButton = findViewById(R.id.imageButton);
        chatview = findViewById(R.id.chatview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences editor = getSharedPreferences("settings", MODE_PRIVATE);
        uid = editor.getInt("uid", 0);
        Bundle i = getIntent().getExtras();
        eid = i.getInt("eid", 0);
        if(eid == 0) {
            Toast.makeText(ChatActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
            ChatActivity.this.finish();
        }
        String fname = i.getString("fname");
        String lname = i.getString("lname");
        getSupportActionBar().setTitle(fname + " " + lname);
        String cat = i.getString("cat");
        if(!cat.equals("NAN")) {
            Toast.makeText(ChatActivity.this, cat + "Specialist", Toast.LENGTH_LONG).show();
        }

        data = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chatview.setLayoutManager(layoutManager);
        chatview.setAdapter(chatAdapter);

        class Task extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                while (flag) {
                    SystemClock.sleep(1000);
                    Log.i("Hmmm", "asyncing");
                    try {
                        Log.i("Hmmm", "received");
                        String s = new NetworkManager().getDataFromURL(new URL("http://192.168.43.36/helpline/chats/inbox_" + uid + ".txt"));
                        Log.i("Hmmm", s);
                        boolean marker = true;
                        for(int i = data.size() - 1; i >= 0; i--) {
                            if(!data.get(i).isUser()) {
                                Log.i("Hmmmy", data.get(i).getMessage());
                                if(!data.get(i).getMessage().trim().equals(s.trim())) {
                                    sendMessage(s);
                                }
                                marker = false;
                                break;
                            }
                        }
                        if(marker) sendMessage(s);
                    } catch (IOException e) {
                        Log.i("Hmmm", "denied");
                        //return "NAN";
                    }
                }
                return "NAN";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!s.equals("NAN")) {

                }
            }

            void sendMessage(String s) {
                long time = System.currentTimeMillis();
                data.add(new ChatMessage(s, time,false));
                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                        chatview.scrollToPosition(data.size() - 1);
                    }
                });
            }
        }

        new Task().execute();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Hmmm", "Button clicked!");
                if(!messageText.getText().toString().equals("")) {
                    flag = false;
                    message = messageText.getText().toString();
                    Log.i("Hmmm", message);
                    messageText.setText("");
                    long time = System.currentTimeMillis();
                    data.add(new ChatMessage(message, time,true));
                    chatAdapter.notifyDataSetChanged();
                    chatview.scrollToPosition(data.size() - 1);

                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            try {
                                String rmessage = "http://192.168.43.36/helpline/message.php?from="
                                        +uid+"&to="+eid+"&msg="+message;
                                Log.i("Himmm", rmessage);
                                return new NetworkManager().getDataFromURL(new URL(rmessage));
                            } catch (IOException e) {
                                return "NAN";
                            }
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            flag = true;
                            new Task().execute();
                        }
                    }.execute();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        flag = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

        List<ChatMessage> data;

        public ChatAdapter(List<ChatMessage> loc) {
            data = loc;
        }

        @NonNull
        @Override
        public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if(viewType == 0) {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_layout1, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_layout2, parent, false);
            }
            return new ChatHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
            String message = data.get(position).getMessage();

            long time = data.get(position).getTimestamp();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            SimpleDateFormat myDateFormat = new SimpleDateFormat("EEE MMM dd yyyy hh:mm a"); //or ("dd.MM.yyyy"), If you want days before months.
            String formattedDate = myDateFormat.format(calendar.getTime());

            holder.tv_message.setText(message);
            holder.tv_time.setText(formattedDate);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(data.get(position).isUser()) return 1;
            else return 0;
        }

        class ChatHolder extends RecyclerView.ViewHolder {

            TextView tv_message, tv_time;

            ChatHolder(View itemView) {
                super(itemView);
                tv_time = itemView.findViewById(R.id.tv_time);
                tv_message = itemView.findViewById(R.id.tv_message);
            }
        }
    }
}
