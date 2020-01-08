package com.example.galgespil.Statistic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.galgespil.MyKeyboard;
import com.example.galgespil.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

public class StatPressedKeysFrag extends Fragment {

    private StatLogic statLogic = new StatLogic();
    private MyKeyboard myKeyboard = new MyKeyboard();

    private BarChart barChart;
    private BarChartModel barChartModel;
    private ArrayList<BarChartModel> barChartModels;
    private int[] letterUsage;
    private String[] alphabet = myKeyboard.abc();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.stat_show_letters_pressed_scrollview, container, false);

        barChart = layout.findViewById(R.id.bar_chart_letters_pressed);

        //creating a list of the bars
        barChartModels = new ArrayList<>();
        //array of how many times the letters has been pressed
        letterUsage = statLogic.getStats(getContext()).getGuessedLetters();

        //finding the most used letter
        int mostPressedLetter = 0;
        for(int i: letterUsage) {
            if(i > mostPressedLetter) {
                mostPressedLetter = i;
            }
        }
        //setting the barMaxValue 25% higher than mostPressedLetter
        mostPressedLetter = (int)(mostPressedLetter * 1.25);
        barChart.setBarMaxValue(mostPressedLetter);
        System.out.println("temp er: " + mostPressedLetter);

        //creating all the barChartModels and putting them in an array
        for(int i = 0; i < letterUsage.length; i++) {
            barChartModel = new BarChartModel();
            barChartModel.setBarValue(letterUsage[i]);
            barChartModel.setBarText(alphabet[i].toUpperCase() + ": " + letterUsage[i]);

            barChartModels.add(barChartModel);
        }

        barChart.addBar(barChartModels);

        return layout;
    }
}
