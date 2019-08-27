package com.alium.niboexample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alium.niboexample.R;
import com.alium.niboexample.model.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.HolderView>{

    private Context context;
    private List<Trip> tripList;

    public TripAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip,viewGroup,false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.HolderView holderView, int position) {

        holderView.origin.setText(tripList.get(position).getOrigin());
        holderView.destination.setText(tripList.get(position).getDestination());
        holderView.jstart_date.setText(tripList.get(position).getTripStartDate());
        holderView.jstart_time.setText(tripList.get(position).getTripStartTime());
        holderView.jend_date.setText(tripList.get(position).getTripEndDate());
        holderView.distance.setText(tripList.get(position).getDistance());
        holderView.status.setText(tripList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

        TextView origin,destination,jstart_date,jstart_time,jend_date,distance,status;

        public HolderView(@NonNull View itemView) {
            super(itemView);

            origin=(TextView) itemView.findViewById(R.id.origin);
            destination=(TextView) itemView.findViewById(R.id.destination);
            jstart_date=(TextView) itemView.findViewById(R.id.jstart_date);
            jstart_time=(TextView) itemView.findViewById(R.id.jstart_time);
            jend_date=(TextView) itemView.findViewById(R.id.jend_date);
            distance=(TextView) itemView.findViewById(R.id.distance);
            status=(TextView) itemView.findViewById(R.id.status);

        }
    }
}
