package com.example.galgespil.Challenges;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.galgespil.R;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import params.com.stepprogressview.StepProgressView;

public class ChallengeAdapter extends RecyclerView.Adapter {

    private ChallengeLogic challengeLogic = new ChallengeLogic();
    private ArrayList<ChallengeObject> challenges = challengeLogic.getChallenges();

    private TextView challengeName, challengeDesc, skinName;
    private CheckBox checkBox;
    private StepProgressView progressView;

    //markers for the progressbar
    private ArrayList<Integer> markers;

    //holds the position on what checkbox is click in each group
    private int[] skinList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_list_element, parent, false);

        int[] tempSkinList = challengeLogic.getChosenSkinList(itemView.getContext(), challengeLogic.getSKIN_KEY());
        if (tempSkinList.length == 0) {
            //setting default value of -1 at every index.
            skinList = new int[ChallengeObject.SkinGroup.values().length];
            for (int i = 0; i < ChallengeObject.SkinGroup.values().length; i++) {
                skinList[i] = -1;
            }
            System.out.println("skinlist i if: "+Arrays.toString(skinList));
        } else {
            skinList = tempSkinList;
        }

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

        ChallengeObject curChallenge = challenges.get(position);

        //setting progressbar
        int progression = challengeLogic.getProgressionList(holder.itemView.getContext()).get(position);
        int limit = curChallenge.getLimit();
        setMarkers(progression, limit);

        //setting text for textViews
        challengeName.setText(curChallenge.getName());
        challengeDesc.setText(curChallenge.getDesc());
        skinName.setText(holder.itemView.getResources().getString(R.string.unlock_skin, challenges.get(position).getSkinDesc()));

        //setting the background color for every second one
        if(position % 2 == 0) {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.default_btn));
        }

        //setting text for checkbox
        if (progression >= limit) {
            checkBox.setText(holder.itemView.getResources().getString(R.string.skin_unlocked));
            //flagging the boolean clicked in the challenge object
            curChallenge.setClickable(true);
        } else {
            checkBox.setText(holder.itemView.getResources().getString(R.string.skin_locked));
            curChallenge.setClickable(false);
        }

        //.ordinal return the index for the enum and we use that to find it in the array
        int selectedSkin = skinList[curChallenge.getSkinGroup().ordinal()];
        checkBox.setChecked(position == selectedSkin);

        //onclickListener so it's possible to group different checkboxes
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if challenge is locked
                if (!curChallenge.isClickable()) {
                    System.out.println("ikke clickable");
                    //check you click on challenge that is already checked
                } else if (skinList[curChallenge.getSkinGroup().ordinal()] == position) {
                    skinList[curChallenge.getSkinGroup().ordinal()] = -1;
                    //otherwise set which one is clicked
                } else {
                    skinList[curChallenge.getSkinGroup().ordinal()] = position;

                }

                //saving the array in sharedprefs everytime something is pressed.
                challengeLogic.saveSkinsToSharedPrefs(holder.itemView.getContext(), challengeLogic.getSKIN_KEY(), skinList);

                System.out.println("pos er: " + position);
                System.out.println("object: " + curChallenge);
                System.out.println("hele arrayet: " + Arrays.toString(skinList));
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

    public int[] getSkinList() {
        return skinList;
    }
}
