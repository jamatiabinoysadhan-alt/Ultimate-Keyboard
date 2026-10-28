package com.ultimatekeyboard;

import android.inputmethodservice.InputMethodService;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

public class UltimateKeyboardService extends InputMethodService {

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private Button makeKey(String label, int weight, LinearLayout row) {
        Button key = new Button(this);
        key.setText(label);
        key.setTextColor(Color.rgb(25, 28, 32));
        key.setTextSize(17);
        key.setAllCaps(false);
        key.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        key.setPadding(0, 0, 0, 0);
        key.setMinWidth(0);
        key.setMinimumWidth(0);
        key.setMinHeight(0);
        key.setMinimumHeight(0);
        key.setBackground(background(Color.WHITE, 7));

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(0, dp(46), weight);
        params.setMargins(dp(2), dp(3), dp(2), dp(3));
        row.addView(key, params);

        key.setOnClickListener(v -> pressKey(label));
        return key;
    }

    private void pressKey(String key) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        switch (key) {
            case "⌫":
                ic.deleteSurroundingText(1, 0);
                break;
            case "Enter":
                ic.commitText("\n", 1);
                break;
            case "Space":
                ic.commitText(" ", 1);
                break;
            case "?123":
                setInputView(onCreateInputViewNumbers());
                break;
            case "ABC":
                setInputView(onCreateInputView());
                break;
            default:
                ic.commitText(key, 1);
                break;
        }
    }

    private LinearLayout createRoot() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(3), dp(5), dp(3), dp(5));
        root.setBackgroundColor(Color.rgb(218, 222, 230));
        return root;
    }

    private void addRow(LinearLayout root, String[] keys) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER);
        for (String key : keys) {
            int weight = key.equals("Space") ? 5 :
                (key.equals("⌫") || key.equals("Enter") ||
                 key.equals("?123") || key.equals("ABC")) ? 2 : 1;
            makeKey(key, weight, row);
        }
        root.addView(row, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public View onCreateInputView() {
        LinearLayout root = createRoot();
        addRow(root, new String[]{"q","w","e","r","t","y","u","i","o","p"});
        addRow(root, new String[]{"a","s","d","f","g","h","j","k","l"});
        addRow(root, new String[]{"⇧","z","x","c","v","b","n","m","⌫"});
        addRow(root, new String[]{"?123",",","Space",".","Enter"});
        return root;
    }

    private View onCreateInputViewNumbers() {
        LinearLayout root = createRoot();
        addRow(root, new String[]{"1","2","3","4","5","6","7","8","9","0"});
        addRow(root, new String[]{"@","#","$","%","&","-","+","(",")","/"});
        addRow(root, new String[]{"ABC","*","\"","'",":",";","!","?","⌫"});
        addRow(root, new String[]{"?123",",","Space",".","Enter"});
        return root;
    }
}
