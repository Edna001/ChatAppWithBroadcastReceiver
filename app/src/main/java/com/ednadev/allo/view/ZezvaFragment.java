package com.ednadev.allo.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ednadev.allo.R;
import com.ednadev.allo.adapter.ChatArrayAdapter;
import com.ednadev.allo.model.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ZezvaFragment extends Fragment {

    private Button zezvaSend;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView messages;
    private EditText chatInput;
    private ArrayList<ChatMessage> messageArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zezva, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        zezvaSend = view.findViewById(R.id.zezvaSend);
        messages = view.findViewById(R.id.zezvaMessages);
        chatInput = view.findViewById(R.id.zezvaInput);

        chatArrayAdapter = new ChatArrayAdapter(getContext(), R.layout.right_message);
        messages.setAdapter(chatArrayAdapter);

        loadChat();

        zezvaSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("event-zezva");
                intent.putExtra("zezva", chatInput.getText().toString());
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

                ChatMessage chatMessage = new ChatMessage(false, chatInput.getText().toString());
                chatArrayAdapter.add(chatMessage);
                messageArrayList.add(chatMessage);
                saveChat();
                chatInput.setText("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("event-mzia"));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("mzia");
            ChatMessage chatMessage = new ChatMessage(true, message);
            chatArrayAdapter.add(chatMessage);
            messageArrayList.add(chatMessage);
            saveChat();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    private void saveChat() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messageArrayList);
        editor.putString("zezvaChat", json);
        editor.apply();
        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
    }

    private void loadChat() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("zezvaChat", null);
        Type type = new TypeToken<ArrayList<ChatMessage>>() {}.getType();
        ArrayList<ChatMessage> arrayList = gson.fromJson(json, type);

        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                chatArrayAdapter.add(arrayList.get(i));
            }
        }
    }
}