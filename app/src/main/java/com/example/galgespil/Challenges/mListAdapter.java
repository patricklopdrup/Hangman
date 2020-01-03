package com.example.galgespil.Challenges;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.galgespil.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import params.com.stepprogressview.StepProgressView;

public class mListAdapter extends RecyclerView.Adapter {

    private ChallengeLogic challengeLogic = new ChallengeLogic();
    private ArrayList<ChallengeObject> challenges = challengeLogic.getChallenges();

    private TextView challengeName, challengeDesc, skinName;
    private CheckBox checkBox;
    private StepProgressView progressView;

    //markers for the progressbar
    private ArrayList<Integer> markers;

    //holds the position on what checkbox is click in each group
    private int[] skinList = new int[ChallengeObject.SkinGroup.values().length];

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item_element, parent, false);

        //setting default value of -1 at every index.
        for (int i = 0; i < ChallengeObject.SkinGroup.values().length; i++) {
            skinList[i] = -1;
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
                if(skinList[curChallenge.getSkinGroup().ordinal()] == position || !curChallenge.isClickable()) {
                    skinList[curChallenge.getSkinGroup().ordinal()] = -1;
                } else {
                    skinList[curChallenge.getSkinGroup().ordinal()] = position;
                }

                //saving the array in sharedprefs everytime something is pressed.
                curChallenge.saveSkinsToSharedPrefs(holder.itemView.getContext(), curChallenge.getSKIN_KEY(), skinList);

                System.out.println("pos er: " + position);
                System.out.println("object: " + curChallenge);
                System.out.println("keyboard: " + skinList[ChallengeObject.SkinGroup.KEYBOARD_SKIN.ordinal()]);
                System.out.println("man: " + skinList[ChallengeObject.SkinGroup.MAN_SKIN.ordinal()]);
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
