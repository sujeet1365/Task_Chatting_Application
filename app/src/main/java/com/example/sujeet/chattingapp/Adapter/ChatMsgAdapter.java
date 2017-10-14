package com.example.sujeet.chattingapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.android.volley.toolbox.ImageLoader;
//import com.google.firebase.auth.FirebaseAuth;
import com.example.sujeet.chattingapp.Activity.ChatActivity;
import com.example.sujeet.chattingapp.Chat;
import com.example.sujeet.chattingapp.HomeActivity;
import com.example.sujeet.chattingapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

/**
 * Created by angad on 16/9/16.
 */
public class ChatMsgAdapter extends BaseAdapter {

    Context context;
    //public List<CartItem> itemList;
    public LayoutInflater inflater;
    public MyViewHolder myViewHolder;
   // public ImageLoader imageLoader;
    public Dialog dialog;
    List<Chat> chatList;
    String currunt_user_id;

    DatabaseReference databaseReference;
    //FirebaseUser user;

    public ChatMsgAdapter(Context context, List<Chat> chatList){
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.chatList = chatList;

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SharedPreferences sharedPreferences = context.getSharedPreferences("Login",Context.MODE_PRIVATE);

        currunt_user_id = sharedPreferences.getString("contact","");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //user = FirebaseAuth.getInstance().getCurrentUser();
      //  imageLoader = AppController.getInstance().getImageLoader();
    }
    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        myViewHolder = null;
        if (view == null){
            view = inflater.inflate(R.layout.chat_message, viewGroup, false);
            myViewHolder = new MyViewHolder();

            myViewHolder.bubble_layout_parent = (LinearLayout) view.findViewById(R.id.bubble_layout_parent);
            myViewHolder.bubble_layout = (LinearLayout) view.findViewById(R.id.bubble_layout);
            myViewHolder.message = (TextView) view.findViewById(R.id.message);

            view.setTag(myViewHolder);

        }else {
            myViewHolder = (MyViewHolder) view.getTag();
        }

        Chat chat = chatList.get(i);

        if (chat.author.equals(currunt_user_id)){
            myViewHolder.bubble_layout_parent.setGravity(Gravity.RIGHT);
            myViewHolder.bubble_layout.setBackgroundResource(R.drawable.chat_text_box_bg_right);
        }else {
            myViewHolder.bubble_layout_parent.setGravity(Gravity.LEFT);
            myViewHolder.bubble_layout.setBackgroundResource(R.drawable.chat_text_box_bg_left);
        }

        myViewHolder.message.setText(chat.message);



        return view;
    }
    private class MyViewHolder {

        LinearLayout bubble_layout_parent;
        LinearLayout bubble_layout;
        TextView message;
    }

}
