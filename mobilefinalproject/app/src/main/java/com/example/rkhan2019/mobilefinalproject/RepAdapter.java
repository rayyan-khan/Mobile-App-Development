package com.example.rkhan2019.mobilefinalproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RepAdapter extends RecyclerView.Adapter<RepAdapter.MyViewHolder> {

    private List<Representative> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, status, contact;

        public MyViewHolder(View view) {
            super(view);
            TextView name = view.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public RepAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RepAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
