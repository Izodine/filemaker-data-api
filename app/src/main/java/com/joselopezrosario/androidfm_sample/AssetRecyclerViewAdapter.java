package com.joselopezrosario.androidfm_sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joselopezrosario.androidfm.FmRecord;

import java.util.List;

public class AssetRecyclerViewAdapter extends RecyclerView.Adapter<AssetRecyclerViewAdapter.AssetViewHolder> {

    private List<FmRecord> records;

    public void updateData(List<FmRecord> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_list_item,
                parent, false);

        return new AssetViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder assetViewModel, int position) {
        FmRecord currentRecord = records.get(position);

        assetViewModel.titleText.setText(currentRecord.getValue("Item"));
        assetViewModel.assignedToText.setText(currentRecord.getValue("Assigned To"));
        assetViewModel.categoryText.setText(currentRecord.getValue("Category"));
        assetViewModel.verboseStatusText.setText(currentRecord.getValue("Verbose Status"));

        String dateDue = currentRecord.getValue("Date Due");
        if(dateDue.isEmpty()) {
            assetViewModel.dueDateText.setVisibility(View.GONE);
        } else {
            assetViewModel.dueDateText.setText(currentRecord.getValue("Date Due"));
        }

        assetViewModel.locationText.setText(currentRecord.getValue("Location"));
    }

    @Override
    public int getItemCount() {
        return records==null ? 0 : records.size();
    }

    final static class AssetViewHolder extends RecyclerView.ViewHolder{

        protected ImageView listImage;
        protected TextView titleText;
        protected TextView assignedToText;
        protected TextView categoryText;
        protected TextView verboseStatusText;
        protected TextView dueDateText;
        protected TextView locationText;

        public AssetViewHolder(@NonNull View itemView) {
            super(itemView);
            listImage = itemView.findViewById(R.id.asset_list_imageview);
            titleText = itemView.findViewById(R.id.asset_title_textview);
            assignedToText = itemView.findViewById(R.id.assigned_to_textview);
            categoryText = itemView.findViewById(R.id.category_textview);
            verboseStatusText = itemView.findViewById(R.id.verbose_status_textview);
            dueDateText = itemView.findViewById(R.id.due_date_textview);
            locationText = itemView.findViewById(R.id.location_textview);

        }
    }

}
