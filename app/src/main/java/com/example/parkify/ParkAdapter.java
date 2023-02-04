package com.example.progettoIUMcorretto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.MyHolder> {

    Context context;
    List<Parcheggi> arrayPark = new ArrayList<>();

    public ParkAdapter(Context context, List<Parcheggi> arrayPark) {
        this.context = context;
        this.arrayPark = arrayPark;
        layoutInflater = LayoutInflater.from(context);
    }

    LayoutInflater layoutInflater;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preview_search, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParkAdapter.MyHolder holder, int position) {
        holder.parkingName.setText((arrayPark.get(position).getParkingName()));
        holder.parkImg.setImageResource(arrayPark.get(position).getImageResource());
        holder.securityRate.setText(arrayPark.get(position).getSecurity());
        holder.dotColor.setImageResource(arrayPark.get(position).getColorSecurity());
        holder.generalRate.setText(arrayPark.get(position).getgRatings());
    }

    @Override
    public int getItemCount() {
        return arrayPark.size();
    }
    public void filteredList(List<Parcheggi> filterList){

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView parkingName, securityRate, generalRate;
        ImageView parkImg, dotColor;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Parcheggi parkk;
            for(Parcheggi park : arrayPark) {
                parkingName = itemView.findViewById(R.id.parking_name);
                parkImg = itemView.findViewById(R.id.park_img);
                securityRate = itemView.findViewById(R.id.security_ratings);
                generalRate = itemView.findViewById(R.id.rating_quality);
                dotColor = itemView.findViewById(R.id.dot_color);
            }
        }
    }
}