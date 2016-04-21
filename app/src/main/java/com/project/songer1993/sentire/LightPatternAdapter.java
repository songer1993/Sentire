package com.project.songer1993.sentire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by songer1993 on 10/04/2016.
 */
public class LightPatternAdapter extends ArrayAdapter<LightPattern> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public LightPatternAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final LightPattern currentPattern = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentPattern);
        //Setup the ui elements inside the item
        TextView tvName = (TextView) row.findViewById(R.id.tvPatternName);
        TextView tvType = (TextView) row.findViewById(R.id.tvPatternType);
        final TextView tvScore = (TextView) row.findViewById(R.id.tvPatternScore);
        ImageButton btnPlus = (ImageButton) row.findViewById(R.id.btnLike);
        ImageButton btnMinus = (ImageButton) row.findViewById(R.id.btnDislike);

        //Retrieve the values from the object
        tvName.setText(currentPattern.getName());
        tvType.setText(currentPattern.getType());
        tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), currentPattern.getValue(), Toast.LENGTH_SHORT).show();
                ConnectBT.bt.send(currentPattern.getValue(), true);
                return false;
            }
        });
        tvScore.setText(new Integer(currentPattern.getScore()).toString() + "pt");
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int new_score = currentPattern.getScore() + 1;
                tvScore.setText(new Integer(new_score).toString() + "pt");
                currentPattern.setScore(new_score);
                if (mContext instanceof BrowsePatterns) {
                    BrowsePatterns activity = (BrowsePatterns) mContext;
                    activity.updateLightPattern(currentPattern);
                }
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int new_score = currentPattern.getScore() - 1;
                tvScore.setText(new Integer(new_score).toString() + "pt");
                currentPattern.setScore(new_score);
                if (mContext instanceof BrowsePatterns) {
                    BrowsePatterns activity = (BrowsePatterns) mContext;
                    activity.updateLightPattern(currentPattern);
                }
            }
        });


        return row;
    }

}
