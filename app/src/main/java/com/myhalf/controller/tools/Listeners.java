package com.myhalf.controller.tools;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/*
 * Created by Adir on 3/17/2018.
 */

public class Listeners {
    public static final String TAG = "Listeners";


    public static void attachEditTextToSeekBar(Activity activity, SeekBar seekBar, EditText editText) {
        attachEditTextToSeekBar(activity,seekBar, editText, null);
    }

    /**
     * @param seekBarMinValue is null if u support VERSION_CODES.O and implements SeekBar.minValue().
     */
    public static void attachEditTextToSeekBar(Activity activity, SeekBar seekBar, EditText editText, @Nullable Integer seekBarMinValue) {
        try {
            if (seekBarMinValue == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
                new AttachEdittextToSeekbar(activity,seekBar, editText, seekBar.getMin(), seekBar.getMax(), true);
            else if (seekBarMinValue != null)
                new AttachEdittextToSeekbar(activity,seekBar, editText, seekBarMinValue, seekBar.getMax() + seekBarMinValue, false);
            else
                throw new Exception("Minimum Value of Seekbar Not Inserted!, u must either add your minimum value, or support versoin O");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private static class AttachEdittextToSeekbar implements SeekBar.OnSeekBarChangeListener, TextWatcher {
        SeekBar mSeekBar;
        EditText mEditText;
        private int sbRealMin, sbRealMax;
        private boolean sbAutoMin;
        Activity mActivity;

        private Timer timer = new Timer();
        private final long DELAY = 3000; // milliseconds

        public AttachEdittextToSeekbar(Activity activity,SeekBar seekBar, EditText editText, int min, int max, boolean sbAutoMin) {
            mSeekBar = seekBar;
            mEditText = editText;
            mEditText.addTextChangedListener(this);
            mSeekBar.setOnSeekBarChangeListener(this);
            sbRealMin = min;
            sbRealMax = max;
            this.sbAutoMin = sbAutoMin;
            mActivity = activity;

        }


        //-----------------------text-watcher------------------------------
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            String s1 = mEditText.getText().toString();
            if (s1 != "") {// then set the value on the editText to the seekbar
                InsertToSeekBar(s1);
            } else {
                mSeekBar.setProgress(minSeekBar());
                mEditText.setText(String.valueOf(sbRealMin));
            }

        }


        private void InsertToSeekBar(String s1) {
            try {
                final int value = Integer.valueOf(s1);
                if (value >= sbRealMin && value <= sbRealMax && value != mSeekBar.getProgress()) {
                    scheduleEditTextChange(value);
                } else if (value < sbRealMin) {
                    scheduleEditTextChange(0);
                } else if (value > sbRealMax) {
                    scheduleEditTextChange(maxSeekBar());
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


        private void scheduleEditTextChange(final int value) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mEditText.setSelection(mEditText.getText().length());
                                    mSeekBar.setProgress(progressUpdateValue(value));
                                }
                            });
                            Log.d(TAG, "set seekbar");
                        }
                    },
                    DELAY - 1500
            );
        }


        //-----------------------seekbar-change------------------------------
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mEditText.setText(String.valueOf(editTextUpdateValue(progress)));
            Log.d(TAG, "set text");

        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        //----------------------------------------inner-tools----------------------
        private int minSeekBar() {
            if (sbAutoMin && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                return mSeekBar.getMin();
            return 0;
        }

        private int maxSeekBar() {
            if (sbAutoMin && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                return mSeekBar.getMax();
            return mSeekBar.getMax();
        }

        private int progressUpdateValue(int value) {
            if (sbAutoMin)
                return value;
            else
                return value-sbRealMin;
        }

        private int editTextUpdateValue(int progress) {
            if (sbAutoMin)
                return progress;
            return progress+ sbRealMin;
        }
    }
}
