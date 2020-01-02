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

    private ChallengeLogic challengeLogic = new ChallengeLogic();
    ArrayList<ChallengeObject> challenges = challengeLogic.getChallenges();

    private TextView challengeName, challengeDesc, skinName;
    private CheckBox checkBox;
    private StepProgressView progressView;

    //markers for the progressbar
    private ArrayList<Integer> markers;

    //holds the position on what checkbox is click in each group
    private int selectedKeyboardSkin = -1;
    private int selectedManSkin = -1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item_element, parent, false);
        return new RecyclerView.ViewHolder(itemView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        challengeName = holder.itemView.findViewById(R.id.challenge_name);
        challengeDesc = holder.itemView.findViewById(R.id.challenge_desc);
        skinName = holder.itemView.findViewById(R.id.challenge_skin_name);
        checkBox = holder.itemView.findViewById(R.id.checkBox_challenge);
        progressView = holder.itemView.findViewById(R.id.challenge_progression);

        //setting progressbar
        int progression = challengeLogic.getProgressionList(holder.itemView.getContext()).get(position);
        int limit = challenges.get(position).getLimit();
        setMarkers(progression, limit);

        //setting text for textViews
        challengeName.setText(challenges.get(position).getName());
        challengeDesc.setText(challenges.get(position).getDesc());
        skinName.setText(holder.itemView.getResources().getString(R.string.unlock_skin, challenges.get(position).getSkinDesc()));

        //setting text for checkbox
        if (progression >= limit) {
            checkBox.setText(holder.itemView.getResources().getString(R.string.skin_unlocked));
            //flagging the boolean clicked in the challenge object
            challenges.get(position).setClicked(true);
        } else {
            checkBox.setText(holder.itemView.getResources().getString(R.string.skin_locked));
            challenges.get(position).setClicked(false);
        }

        //making the first half the "keyboard skins" and the other half the "man skins"
        if (position < getItemCount() / 2) {
            checkBox.setChecked(position == selectedKeyboardSkin);
        } else {
            checkBox.setChecked(position == selectedManSkin);
        }


        //onclickListener so it's possible to group different checkboxes
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pos er: " + position);
                if (position < getItemCount() / 2) {
                    //if already click on we set it to -1 (default) otherwise it's sat to "position"
                    if (selectedKeyboardSkin == position || !challenges.get(position).isClicked()) selectedKeyboardSkin = -1;
                        //challenges.get(position).setSkinChosen(false);
                    else selectedKeyboardSkin = position;
                        //challenges.get(position).setSkinChosen(true);
                } else {
                    if (selectedManSkin == position || !challenges.get(position).isClicked()) selectedManSkin = -1;
                    else selectedManSkin = position;
                }
                System.out.println("keyboard: " + selectedKeyboardSkin);
                System.out.println("man: " + selectedManSkin);
                //recreating the list when something change
                notifyDataSetChanged();
            }
        };
        //onclickListener on both the checkbox and the whole view
        //checkbox problem solved with: http://joshskeen.com/building-a-radiogroup-recyclerview/ and https://stackoverflow.com/questions/39127008/how-can-i-select-only-one-checkbox-in-recyclerview-and-notifydataset-changed
        checkBox.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void setMarkers(int progression, int progressionLimit) {
        markers = new ArrayList<>();
        //if limit is more than 10 we don't show all numbers only every second
        int constant = progressionLimit <= 10 ? 1 : 2;
        for (int i = 0; i <= progressionLimit; i += constant) {
            markers.add(i);
        }
        progressView.setTotalProgress(progressionLimit);
        progressView.setMarkers(markers);

        //setting the progression from sharedprefs
        progressView.setCurrentProgress(progression);
    }
}
