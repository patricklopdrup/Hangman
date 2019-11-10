package com.example.galgespil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SettingsDialog extends AppCompatDialogFragment {
    private Game game = new Game();
    private ToggleButton toggle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.frag_setting_dialog, null);
        toggle = view.findViewById(R.id.toggleButton);

        int keyboardChoise = game.getKeyboardChoise(game.getKeyboardKey(), getContext());
        System.out.println("valgt: " + keyboardChoise);
        if(keyboardChoise == 0) {
            System.out.println("qwerty");
            toggle.setChecked(true);
        } else if(keyboardChoise == 1) {
            System.out.println("abc");
            toggle.setChecked(false);
        }

        builder.setView(view)
                .setTitle(R.string.setting)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int keyboard = toggle.isChecked() ? 0 : 1;
                        System.out.println("her er den: " + keyboard);
                        game.saveKeyboardChoise(game.getKeyboardKey(), keyboard, getContext());
                    }
                });

        return builder.create();
    }
}
