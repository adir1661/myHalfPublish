package com.myhalf.controller.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhalf.R;

import java.util.ArrayList;
/*
* this adapter for chat list @see ChatListFragment
* */

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ChatMessage> objects;
    private Context mContext;

    public ChatListRecyclerViewAdapter(ArrayList<ChatMessage> objects) {
        this.objects = objects;
    }

    public ChatListRecyclerViewAdapter(ArrayList<ChatMessage> objects, Context context) {
        this.objects = objects;
        this.mContext = context;
    }

    @Override
    public ChatListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatListRecyclerViewAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ChatListRecyclerViewAdapter.ViewHolder holder, int position) {


        ChatMessage chatMessage = objects.get(position);

        int defaultImage = mContext.getResources().getIdentifier(chatMessage.getMessegeImageUri(), null, mContext.getPackageName());
        Drawable drawable = mContext.getResources().getDrawable(defaultImage);

        holder.name.setText(chatMessage.getMessageUser());
        holder.imageView.setImageDrawable(drawable);
        holder.date.setText(String.valueOf(chatMessage.getMessageTime()));
        holder.lastMessage.setText(chatMessage.getMessageText());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
// here u should change setting from the user to one line in chat list
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView lastMessage;
        TextView date;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.time);
            lastMessage = (TextView) itemView.findViewById(R.id.last_sentence);
            imageView = (ImageView) itemView.findViewById(R.id.circleImageView);
        }
    }
}