package com.example.sujeet.chattingapp.Activity;

/**
 * Created by Sujeet on 14-10-2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

//import com.google.firebase.auth.FirebaseAuth;
import com.example.sujeet.chattingapp.Adapter.ChatMsgAdapter;
import com.example.sujeet.chattingapp.Chat;
import com.example.sujeet.chattingapp.HomeActivity;
import com.example.sujeet.chattingapp.R;
import com.example.sujeet.chattingapp.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupChatActivity extends AppCompatActivity {

    String other_user_uid;
    ListView listView;
    List<Chat> chatList;
    ChatMsgAdapter chatMsgAdapter;
    ImageButton sendButton;
    DatabaseReference databaseReference;
    String name = "";
    String currentUserId = "";
    ValueEventListener valueEventListener;
    String chatRoomId = "";
    SharedPreferences sharedPreferences;
    ArrayList groupData;
    StringBuilder contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat);

        chatList = new ArrayList<Chat>();
        contacts = new StringBuilder("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("contact","");
        Set<String> set = sharedPreferences.getStringSet("list",null);
//        groupData = new ArrayList();
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        groupData.addAll(set);
//        Intent intent = getIntent();
//        if(intent != null){
//            name = intent.getStringExtra("name");
            name = "Group";
//            groupData = intent.getStringArrayListExtra("groupList");
//            other_user_uid = intent.getStringExtra("other_user_uid");


            if(!name.equals("")){
                setTitle(name);
            }
//        }
        for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
            String f = it.next();
            contacts.append(f);
            contacts.append(":");
        }

        listView = (ListView)findViewById(R.id.listView1);
        sendButton = (ImageButton)findViewById(R.id.sendMessageButton1);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList.clear();

                if(dataSnapshot.getValue() != null){
                    DataSnapshot child;
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()){
                        child = (DataSnapshot)iterator.next();
                        if(child.getKey().equals("group_chat")){
                            Iterator chatRoomIterator = child.getChildren().iterator();
                            while (chatRoomIterator.hasNext()){
                                DataSnapshot chatRoomSnaphot = (DataSnapshot)chatRoomIterator.next();
                                if(chatRoomSnaphot.getKey().equals(contacts)){

                                    chatRoomId = contacts.toString();

                                    Iterator iterator1 = chatRoomSnaphot.getChildren().iterator();
                                    while (iterator1.hasNext()){
                                        DataSnapshot snapshot = (DataSnapshot)iterator1.next();

                                        Chat chat = snapshot.getValue(Chat.class);
                                        chatList.add(chat);
                                    }
                                }
//                                else if(chatRoomSnaphot.getKey().equals(other_user_uid+":"+currentUserId)){
//
//                                    chatRoomId = other_user_uid+":"+currentUserId+"";
//
//                                    Iterator iterator1 =chatRoomSnaphot.getChildren().iterator();
//                                    while (iterator1.hasNext()){
//                                        DataSnapshot snapshot = (DataSnapshot)iterator1.next();
//
//                                        Chat chat = snapshot.getValue(Chat.class);
//                                        chatList.add(chat);
//                                    }
//                                }
                            }
                        }
                    }
                }

                ChatMsgAdapter chatMsgAdapter = new ChatMsgAdapter(GroupChatActivity.this,chatList);
                listView.setAdapter(chatMsgAdapter);
                listView.setSelection(chatList.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);

        valueEventListener = eventListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(valueEventListener != null){
            databaseReference.child("group_chat").removeEventListener(valueEventListener);
        }
    }
    //Ask to Sir
    public  void sendMessage(){
        String timeStamp = "";
        EditText inputText = (EditText)findViewById(R.id.messageEditText1);
        String input = inputText.getText().toString();
        if(chatRoomId.equals("")){
            chatRoomId = contacts.toString();
        }
        if(!input.equals("") && !chatRoomId.equals("")){
            Long timeStampLong = System.currentTimeMillis()/1000;
            timeStamp = timeStampLong.toString();
            Chat chat = new Chat(currentUserId,input,timeStamp,true);
            Map<String,Object> postValues = chat.tomap();

            Map<String,Object> childUpdates = new HashMap<>();
            childUpdates.put("/group_chat/"+chatRoomId+"/"+timeStamp,postValues);

            databaseReference.updateChildren(childUpdates);

            inputText.setText("");
        }
    }
}
