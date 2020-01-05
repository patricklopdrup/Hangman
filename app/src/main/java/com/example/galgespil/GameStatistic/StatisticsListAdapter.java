package com.example.galgespil.GameStatistic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.galgespil.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsListAdapter extends RecyclerView.Adapter {

    private GameStatLogic gameStatLogic = new GameStatLogic();
    private List<StatDisplayObject> statObjectList = gameStatLogic.getStatNames();
    private List<StatDisplayObject> statValues;

    private TextView statName, statInfo;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item_element, parent, false);

        //statObjectList = gameStatLogic.getStats(gameStatLogic.getGameStats(itemView.getContext()));

        return new RecyclerView.ViewHolder(itemView) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        statName = holder.itemView.findViewById(R.id.stat_name);
        statInfo = holder.itemView.findViewById(R.id.stat_info);

        GameStatObject obj = gameStatLogic.getGameStats(holder.itemView.getContext());
        //getting the values. Can't do it in global variable because I need context
        statValues = gameStatLogic.getStatValues(holder.itemView.getContext());

        //mergin the 2 Lists. We take the values and adding them to the global statObjectList
        for (int i = 0; i < statObjectList.size(); i++) {
            statObjectList.get(i).statInfo = statValues.get(i).statInfo;
            statObjectList.get(i).statHelpInfo = statValues.get(i).statHelpInfo;
        }

        statName.setText(statObjectList.get(position).statName);
        if(statObjectList.get(position).statInfo == -1.0) {
            statInfo.setText(statObjectList.get(position).statHelpInfo);
        } else if(statObjectList.get(position).statHelpInfo == null) {
            statInfo.setText(Double.toString(statObjectList.get(position).statInfo));
        } else {
            statInfo.setText(statObjectList.get(position).statInfo + " " + statObjectList.get(position).statHelpInfo);
        }
    }

    @Override
    public int getItemCount() {
        return statObjectList.size();
    }
}
