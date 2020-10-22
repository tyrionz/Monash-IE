package com.NHAS.Infantime.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.Facility;

import java.util.List;

public class FacilityRecyclerViewAdapter extends RecyclerView.Adapter<FacilityRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView facilityTextView;
        public ImageView locationAvailableImageView;

        public ViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView);
            this.facilityTextView = view.findViewById(R.id.facilityTextView);
            this.locationAvailableImageView = view.findViewById(R.id.locationAvailableImageView);
        }
    }

    private List<Facility> facilityList;
    private Context context;

    public FacilityRecyclerViewAdapter(List<Facility> facilityList, Context context) {
        this.facilityList = facilityList;
        this.context = context;
    }

    @NonNull
    @Override
    public FacilityRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View diseaseView = inflater.inflate(R.layout.rv_facility_layout, parent, false);
        return new ViewHolder(diseaseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Facility thisFacility = facilityList.get(position);

        TextView facilityName = holder.facilityTextView;
        facilityName.setText(thisFacility.getName());

        ImageView locationAvailableImageView = holder.locationAvailableImageView;

        CardView cardView = holder.cardView;
        cardView.setClickable(true);
        locationAvailableImageView.setImageResource(R.drawable.ic_location);
        locationAvailableImageView.setColorFilter(ContextCompat.getColor(context, R.color.warningGreen));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    context.getPackageManager().getPackageInfo("com.google.android.apps.maps", 0);
                    new AlertDialog.Builder(context)
                            .setTitle("Open Google Map?")
                            .setMessage("This will open Google map and view the location.")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String searchLocation = "";
                                            if (!thisFacility.getAddress().equals("null")) {
                                                searchLocation = thisFacility.getAddress();
                                            } else {
                                                searchLocation = thisFacility.getName();
                                            }


                                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=10&q=" + searchLocation);
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            context.startActivity(mapIntent);
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                            .show();
                } catch (PackageManager.NameNotFoundException e) {
                    new AlertDialog.Builder(context)
                            .setTitle("Google Map not installed.")
                            .setMessage("Please install Google map to view the location")
                            .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }


}
