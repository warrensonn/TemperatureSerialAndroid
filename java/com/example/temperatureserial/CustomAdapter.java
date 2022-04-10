package com.example.temperatureserial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList moment, temperature;

    CustomAdapter(Context context, ArrayList moment, ArrayList temperature) {
        this.context = context;
        this.moment = moment;
        this.temperature = temperature;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.moment_txt.setText(String.valueOf(moment.get(position)));
        holder.temperature_txt.setText(String.valueOf(temperature.get(position)));
    }

    @Override
    public int getItemCount() {
        return moment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView moment_txt, temperature_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moment_txt = itemView.findViewById(R.id.moment_txt);
            temperature_txt = itemView.findViewById(R.id.temperature_txt);
        }
    }
}
