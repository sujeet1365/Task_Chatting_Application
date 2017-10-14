package com.example.sujeet.chattingapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sujeet.chattingapp.Adapter.ChatListRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    RecyclerView recyclerview;
    List<User> userList;
    List<Chat> chatRoomList;
    String id_contact;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userList = new ArrayList<User>();
        chatRoomList = new ArrayList<Chat>();


//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        id_contact = sharedPreferences.getString("contact","");

        recyclerview = (RecyclerView) inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        // Inflate the layout for this fragment
        return recyclerview;
    }

    @Override
    public void onStart() {
        super.onStart();
        userList.clear();
        chatRoomList.clear();
        Query query = FirebaseDatabase.getInstance().getReference();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterate = dataSnapshot.getChildren().iterator();
                while(iterate.hasNext()){
                    DataSnapshot snapshot =(DataSnapshot) iterate.next();
                    if(snapshot.getKey().equals("chat")){
                        Iterator chatRoomIterator = snapshot.getChildren().iterator();
                        while (chatRoomIterator.hasNext()){
                            DataSnapshot chatRoomDatasnapshot = (DataSnapshot)chatRoomIterator.next();

                            Chat chat = new Chat();

                            String str = chatRoomDatasnapshot.getKey().toString();
                            String[] seperated = str.split(":");
                            String user_id_1 = seperated[0];
                            String user_id_2 = seperated[1];

                            if(user_id_1.equals(id_contact)){

                                chat.author = user_id_2;

                                Iterator chatIterator = chatRoomDatasnapshot.getChildren().iterator();
                                while (chatIterator.hasNext()){
                                    DataSnapshot snapshot1 = (DataSnapshot)chatIterator.next();

                                    if(user_id_2.equals(snapshot1.child("author").getValue().toString())){
                                        chat.message = snapshot1.child("message").getValue().toString();

                                    }else{
                                        chat.message = snapshot1.child("message").getValue().toString();

                                    }
                                }
                                chatRoomList.add(chat);

                            }else if(user_id_2.equals(id_contact)){

                                chat.author = user_id_1;

                                Iterator chatIterator = chatRoomDatasnapshot.getChildren().iterator();
                                while (chatIterator.hasNext()){
                                    DataSnapshot snapshot1 = (DataSnapshot)chatIterator.next();

                                    if(user_id_2.equals(snapshot1.child("author").getValue().toString())){
                                        chat.message = snapshot1.child("message").getValue().toString();

                                    }else{
                                        chat.message = snapshot1.child("message").getValue().toString();

                                    }
                                }
                                chatRoomList.add(chat);

                            }
                        }
                    }else if(snapshot.getKey().equals("group_chat"))
                    {
                        Iterator chatRoomIterator = snapshot.getChildren().iterator();
                        while (chatRoomIterator.hasNext()){
                            DataSnapshot chatRoomDatasnapshot = (DataSnapshot)chatRoomIterator.next();

                            Chat chat = new Chat();

                            String str = chatRoomDatasnapshot.getKey().toString();
                            String[] seperated = str.split(":");
//                            String user_id_1 = seperated[0];
//                            String user_id_2 = seperated[1];
                            String self="";
                            for(int i=0;i<seperated.length;i++)
                            {
                                if(seperated[i].equals(id_contact))
                                    self = seperated[i];
                            }

                            if(self.equals(id_contact)){

                                chat.author = str;

                                Iterator chatIterator = chatRoomDatasnapshot.getChildren().iterator();
                                while (chatIterator.hasNext()){
                                    DataSnapshot snapshot1 = (DataSnapshot)chatIterator.next();

//                                    if(seperated[0].equals(snapshot1.child("author").getValue().toString())){
                                        chat.message = snapshot1.child("message").getValue().toString();

//                                    }
//                                    else{
//                                        chat.message = snapshot1.child("message").getValue().toString();
//
//                                    }
                                }
                                chatRoomList.add(chat);

                            }
// else{
//
//                                chat.author = seperated[1];
//
//                                Iterator chatIterator = chatRoomDatasnapshot.getChildren().iterator();
//                                while (chatIterator.hasNext()){
//                                    DataSnapshot snapshot1 = (DataSnapshot)chatIterator.next();
//
//                                    if(seperated[0].equals(snapshot1.child("author").getValue().toString())){
//                                        chat.message = snapshot1.child("message").getValue().toString();
//
//                                    }else{
//                                        chat.message = snapshot1.child("message").getValue().toString();
//
//                                    }
//                                }
//                                chatRoomList.add(chat);
//
//                            }
                        }
                    }else if(snapshot.getKey().equals("users")){
                        Iterator iterator = snapshot.getChildren().iterator();
                        while (iterator.hasNext())
                        {
                            DataSnapshot dataSnapshot1 = (DataSnapshot) iterator.next();
                            Iterator iterator1 = dataSnapshot1.getChildren().iterator();
                            User user = new User();
                            user.contact = dataSnapshot1.getKey();
                            while (iterator1.hasNext())
                            {

                                DataSnapshot dataSnapshot2 = (DataSnapshot) iterator1.next();
                                if(dataSnapshot2.getKey().equals("name")){
                                    user.name = dataSnapshot2.getValue().toString();
                                }
//                                if(dataSnapshot2.getKey().equals("email")){
//                                    user.email = dataSnapshot2.getValue().toString();
//                                }
                                if(dataSnapshot2.getKey().equals("contact")){
                                    user.contact = dataSnapshot2.getValue().toString();
                                }
//                                if(dataSnapshot2.getKey().equals("picture_url")){
//                                    user.picture_url = dataSnapshot2.getValue().toString();
//                                }
                            }
                            userList.add(user);
                        }

                    }
                }
                for(User user:userList){
                    for(Chat chat : chatRoomList){

                        if(chat.author.equals(user.contact)){
                            chat.chat_user_name = user.name;
//                            chat.chat_dp_url = user.picture_url;
                        }else if(chat.author.length()>21)
                            chat.chat_user_name = "Group";
                    }
                }

                ChatListRecyclerAdapter chatListRecyclerAdapter = new ChatListRecyclerAdapter(getActivity(),chatRoomList);
                recyclerview.setAdapter(chatListRecyclerAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}