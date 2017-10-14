package com.example.sujeet.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujeet.chattingapp.Activity.ChatActivity;
import com.example.sujeet.chattingapp.Activity.MainActivity;
import com.example.sujeet.chattingapp.R;
import com.example.sujeet.chattingapp.User;

import java.util.ArrayList;
import java.util.List;


public class Contact_ListAdapter extends ArrayAdapter {

    ArrayList<User> userList;
    ArrayList grp_list;
    TextView txt_chat_name;
    CheckBox checkBox;

    public Contact_ListAdapter(Context context, ArrayList arrayList) {
        super(context, 0, arrayList);
        userList = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_list, parent, false);
        }



        txt_chat_name = (TextView) listItemView.findViewById(R.id.txt_user_name);
        txt_chat_name.setText(userList.get(position).name);
        CheckBox cb = (CheckBox) listItemView.findViewById(R.id.cbox);
////        cb.performClick();
//        if (cb.isChecked()) {
////            MainActivity.grp_list.add(userList.get(position).contact);
//            Toast.makeText(getContext(),userList.get(position).contact, Toast.LENGTH_SHORT).show();
//        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.grp_list.add(userList.get(position).contact);
            }
        });
            return listItemView;
        }
    }

