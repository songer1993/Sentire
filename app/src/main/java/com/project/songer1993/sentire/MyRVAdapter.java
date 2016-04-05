package com.project.songer1993.sentire;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by songer1993 on 03/04/2016.
 */
public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.FeatureViewHolder>{

    public static class FeatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        static CardView cv;
        static TextView cardName;
        static TextView cardDescription;
        static ImageView cardIcon;

        FeatureViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            cardName = (TextView)itemView.findViewById(R.id.card_name);
            cardDescription = (TextView)itemView.findViewById(R.id.card_description);
            cardIcon = (ImageView)itemView.findViewById(R.id.card_icon);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (getPosition()){
                case 0:
                    intent = new Intent(v.getContext(), DesignLibraryVibrationPatterns.class);
                    break;
                case 1:
                    intent = new Intent(v.getContext(), DesignRealtimeVibrationPatterns.class);
                    break;
                case 2:
                    intent = new Intent(v.getContext(), DesignLightPatterns.class);
                    break;
                case 3:
                    intent = new Intent(v.getContext(), SeeSavedPatterns.class);
                    break;
                case 4:
                    intent = new Intent(v.getContext(), Demo.class);
                    break;
                default:
                    intent = new Intent(v.getContext(), MainActivity.class);
                    break;

            }
            v.getContext().startActivity(intent);
        }
    }

    List<MainActivity.Feature> features;

    MyRVAdapter(List<MainActivity.Feature> features){
        this.features = features;
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    @Override
    public FeatureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_card_view, viewGroup, false);
        FeatureViewHolder pvh = new FeatureViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FeatureViewHolder personViewHolder, final int i) {
        FeatureViewHolder.cardName.setText(features.get(i).name);
        FeatureViewHolder.cardDescription.setText(features.get(i).description);
        FeatureViewHolder.cardIcon.setImageResource(features.get(i).iconID);
        FeatureViewHolder.cv.setCardBackgroundColor(features.get(i).color);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}