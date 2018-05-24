package com.myhalf.controller.tools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhalf.R;
import com.myhalf.controller.activities.ChatBubble;
import com.myhalf.model.backend.Finals;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatBubble> {

    private Activity activity;
    private List<ChatBubble> messages;

    public MessageAdapter(Activity context, int resource, List<ChatBubble> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        ChatBubble ChatBubble = getItem(position);
        int viewType = getItemViewType(position);

        if (ChatBubble.isMyMessage()) {
            layoutResource = R.layout.left_chat_bubble;
        }
        else {
            layoutResource = R.layout.right_chat_bubble;
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        //set message content
        holder.msg.setText(ChatBubble.getContent() + "\n"+ChatBubble.getMessageTime().toString() );
        ImageView imageView= holder.getImageView();
        Storage.getFromStorage(activity, Finals.FireBase.storage.MAIN_PICTURE,imageView,ChatBubble.getSender());
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime. Value 2 is returned because of left and right views.
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        if (getItem(position).isMyMessage())
            return 0;
        else
            return 1;
    }

    private class ViewHolder {
        private TextView msg;
        private ImageView imageView;

        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.txt_msg);
            imageView = v.findViewById(R.id.circleImageView);
        }

        public ImageView getImageView() {
            return imageView;
        }
        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}