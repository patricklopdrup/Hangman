package com.example.galgespil.Challenges;

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

    TextView challengeName, challengeDesc;
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
        checkBox = holder.itemView.findViewById(R.id.checkBox_challenge);
        progressView = holder.itemView.findViewById(R.id.challenge_progression);

        setMarkers(position);

        challengeName.setText(challengeLogic.getChallengeNames().get(position));
        challengeDesc.setText(challengeLogic.getChallengeDescriptions().get(position));
    }

    @Override
    public int getItemCount() {
        System.out.println("return size: " + challengeLogic.getChallengeDescriptions().size());
        return challengeLogic.getChallengeDescriptions().size();
    }

    public void setMarkers(int position) {
        markers = new ArrayList<>();
        int limit = challengeLogic.getChallengeLimits().get(position);
        //if limit is more than 10 we don't show all numbers only every second
        int constant = limit <= 10 ? 1 : 2;
        for(int i = 0; i <= limit; i += constant) {
            markers.add(i);
        }
        progressView.setTotalProgress(limit);
        progressView.setMarkers(markers);


        progressView.setCurrentProgress(3);
    }
}
