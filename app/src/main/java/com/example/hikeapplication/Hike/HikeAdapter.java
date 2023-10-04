package com.example.hikeapplication.Hike;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hikeapplication.ConnectDb;
import com.example.hikeapplication.MainActivity;
import com.example.hikeapplication.R;
import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private final Activity activity;
    public List<Hike> hikes;
    private final List<Hike> hikeSearch;


    public HikeAdapter(Activity activity, Context context, List<Hike> hikes) {
        this.activity = activity;
        this.context = context;
        this.hikes = hikes;
        this.hikeSearch = hikes;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameHike;
        Button buttonDelete, buttonMore;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameHike = itemView.findViewById(R.id.nameHike);
            mainLayout = itemView.findViewById(R.id.mainLayoutHikes);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonMore = itemView.findViewById(R.id.buttonMore);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hikes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.MyViewHolder holder, int position) {
        Hike hike = hikes.get(position);
        holder.nameHike.setText(String.valueOf(hike.getName()));




    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String stringSearch = charSequence.toString();

                if (stringSearch.isEmpty()) {
                    hikes = hikeSearch;
                } else {
                    List<Hike> list = new ArrayList<>();
                    for (Hike hike : hikeSearch) {
                        if (hike.getName().toLowerCase().contains(stringSearch.toLowerCase())) {
                            list.add(hike);
                        }
                    }
                    hikes = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = hikes;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hikes = (List<Hike>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}