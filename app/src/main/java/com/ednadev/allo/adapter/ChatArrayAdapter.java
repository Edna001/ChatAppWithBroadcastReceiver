package com.ednadev.allo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ednadev.allo.R;
import com.ednadev.allo.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private View row;
    private TextView chatText;
    private ArrayList<ChatMessage> chatMessageList = new ArrayList<>();
    private Context context;

    @Override
    public void add(@Nullable ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(@NonNull Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.chatMessageList.size();
    }

    @Nullable
    @Override
    public ChatMessage getItem(int position) {
        return this.chatMessageList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage chatMessage = getItem(position);
        row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessage.left) {
            row = inflater.inflate(R.layout.left_message, parent, false);
        } else {
            row = inflater.inflate(R.layout.right_message, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.chatMessage);
        chatText.setText(chatMessage.message);
        return row;
    }

    public ArrayList<ChatMessage> getItems() {
        return chatMessageList;
    }
}
