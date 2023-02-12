package com.example.parkify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Parcheggio> parcheggi;

    public MyAdapter(List<Parcheggio> parcheggi) {
        this.parcheggi = parcheggi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcheggio_ricerca, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parcheggio currentData = parcheggi.get(position);
        // Bind data to the view holder

        holder.nome.setText(currentData.getNomeParcheggio());
        holder.ratingBar.setRating(currentData.getRatingSicurezza());
    }

    @Override
    public int getItemCount() {
        return parcheggi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define your views here
        TextView nome;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize your views here

            nome = itemView.findViewById(R.id.text_view_name);
            ratingBar = itemView.findViewById(R.id.raating);
        }
    }
}
