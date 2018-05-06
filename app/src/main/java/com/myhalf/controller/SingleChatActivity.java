package com.myhalf.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.myhalf.R;
import com.myhalf.controller.tools.MessageAdapter;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.UserSeeker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by CardUser on 11/10/2017.
 */
//TODO: make it fragment inside nevigation drawer

public class SingleChatActivity extends AppCompatActivity {
    UserSeeker activityUser = myUser.getUserSeeker();
    UserSeeker otherUser;
    String token="";
    private static final String TAG = "SingleChatActivity" ;
    private ListView listView;
    private View btnSend;
    private EditText etInput;
    boolean myMessage = true;
    private List<ChatBubble> ChatBubbles;
    private ArrayAdapter<ChatBubble> adapter;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);


        Bundle chatData = getIntent().getExtras();
        if (chatData != null) {
            otherUser = (UserSeeker) chatData.getSerializable(Finals.App.USER_SEEKER);
            String name = otherUser.getAboutMe().getName();

            TextView textView = (TextView) findViewById(R.id.tvOtherUserTitle);
            textView.setText(name);
            token = otherUser.getFirebaseToken();
//            String imgUrl=chatData.getString("img");
//            int defaultImage = getResources().getIdentifier(imgUrl, null, getPackageName());
//            Drawable drawable = getResources().getDrawable(defaultImage);
//
//            CircleImageView circleImageView=(CircleImageView)findViewById(R.id.circleImageView);
//            circleImageView.setImageDrawable(drawable);
        }else {
            token ="dXHm45SqagA:APA91bGSR-IW5UzYDZU9obUu4jhJlWnWqICq9q-P9UIHqzyxApgsgUqhM139wqxy7tyMnW2h8Ee4tTTodZD4n7KyRw_z0N4nRDZgfnOauBazl_tRsnOQWA96OVvoMh42Zn1KN3Zgtv9g";
        }

        ChatBubbles = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = findViewById(R.id.btn_chat_send);
        etInput = (EditText) findViewById(R.id.etInput);

        //set ListView adapter first
        adapter = new MessageAdapter(this, R.layout.left_chat_bubble, ChatBubbles);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == "GET_MESSAGE") {
                    myMessage = false;
                    String name = intent.getExtras().getString("name");
                    String text = intent.getExtras().getString("text");
                    ChatBubble ChatBubble = new ChatBubble(text, myMessage);
                    ChatBubbles.add(ChatBubble);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        mIntentFilter = new IntentFilter("GET_MESSAGE");

        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
//                Toast.makeText(SingleChatActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendMessage() {
        if (etInput.getText().toString().trim().equals("")) {
            Toast.makeText(SingleChatActivity.this, "Please input some text...", Toast.LENGTH_SHORT).show();
        } else {
            myMessage = true;// make the text on the  left side
            //add message to list
            ChatBubble ChatBubble = new ChatBubble(etInput.getText().toString(), myMessage);
            ChatBubbles.add(ChatBubble);
            adapter.notifyDataSetChanged();
            sendFCMPush(activityUser.getAboutMe().getName(), etInput.getText().toString(),token);
            etInput.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }


    @Override
    protected void onPause() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onPause();
    }
    private void sendFCMPush(String name ,String message,String FCM_RECEIVER_TOKEN ) {// methud to send the message via firebase throug http post request

        final String Legacy_SERVER_KEY = Finals.FireBase.LEGACY_SERVER_KEY;
        String msg = message;
        String title = name;
        String token = FCM_RECEIVER_TOKEN;

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("FCM Sent:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, Finals.FireBase.FCM_PUSH_URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }
}


