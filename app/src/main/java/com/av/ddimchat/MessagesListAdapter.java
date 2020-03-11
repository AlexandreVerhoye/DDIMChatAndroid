package com.av.ddimchat;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private List<Message> listMessages;

    public MessagesListAdapter(Context context, List<Message> listMessages) {
        this.listMessages = listMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType){
            case(1):
                layout=R.layout.message_item;
                break;
            case (2):
                layout=R.layout.message_item;
                break;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username_user_textview;
        TextView message_user_textview;

        public ViewHolder(View itemView){
            super(itemView);

            username_user_textview = itemView.findViewById(R.id.username_user);
            message_user_textview = itemView.findViewById(R.id.message_user);
        }

        public void setUsername(String username){
            if(null == username_user_textview) return;
            username_user_textview.setText(username);
        }

        public void setMessage(String message){
            if(null == message_user_textview) return;
            message_user_textview.setText(message);
        }
    }
}
