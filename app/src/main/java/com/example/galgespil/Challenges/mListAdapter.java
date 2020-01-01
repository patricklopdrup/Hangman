package com.example.galgespil.Challenges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.galgespil.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import params.com.stepprogressview.StepProgressView;

public class mListAdapter extends RecyclerView.Adapter {

    ChallengePage challenge = new ChallengePage();
    ChallengeLogic challengeLogic = new ChallengeLogic();

    TextView challengeName, challengeDesc, skinName;
    CheckBox checkBox;
    StepProgressView progressView;

    ArrayList<Integer> markers;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item_element, parent, false);
        return new RecyclerView.ViewHolder(itemView) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        challengeName = holder.itemView.findViewById(R.id.challenge_name);
        challengeDesc = holder.itemView.findViewById(R.id.challenge_desc);
        skinName = holder.itemView.findViewById(R.id.challenge_skin_name);
        checkBox = holder.itemView.findViewById(R.id.checkBox_challenge);
        progressView = holder.itemView.findViewById(R.id.challenge_progression);

        //setting progressionbar
        setMarkers(holder.itemView.getContext(), position);

        //setting text for textViews
        challengeName.setText(challengeLogic.getChallengeNames().get(position));
        challengeDesc.setText(challengeLogic.getChallengeDescriptions().get(position));
        skinName.setText(holder.itemView.getResources().getString(R.string.unlock_skin, challengeLogic.getSkinNames().get(position)));
    }

    @Override
    public int getItemCount() {
        return challengeLogic.getChallengeDescriptions().size();
    }

    public void setMarkers(Context context, int position) {
        markers = new ArrayList<>();
        int limit = challengeLogic.getChallengeLimits().get(position);
        //if limit is more than 10 we don't show all numbers only every second
        int constant = limit <= 10 ? 1 : 2;
        for(int i = 0; i <= limit; i += constant) {
            markers.add(i);
        }
        progressView.setTotalProgress(limit);
        progressView.setMarkers(markers);

        //setting the progression from sharedprefs
        progressView.setCurrentProgress(challengeLogic.getProgressionList(context).get(position));
    }
}
