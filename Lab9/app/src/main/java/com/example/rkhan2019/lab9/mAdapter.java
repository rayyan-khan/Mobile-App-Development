package com.example.rkhan2019.lab9;

import android.support.annotation.*;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.*;
import android.widget.*;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class mAdapter extends RecyclerView.Adapter<mAdapter.MyViewHolder> {

    private ArrayList<String> mArray;

    public class MyViewHolder extends RecyclerView.ViewHolder{ // error here
        public TextView t;

        public MyViewHolder(View itemView) {
            super(itemView);
            t = (TextView) itemView.findViewById(R.id.textView2);
        }
    }

    public mAdapter(ArrayList<String> a){

        mArray = a;
    }

    public mAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewTypes){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String s = mArray.get(position);
        holder.t.setText(s);
    }

    @Override
    public int getItemCount() {
        return mArray.size();
    }

}
