package com.example.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";
    private boolean mAnswerIstrue;
    private Button btn_showanswer;
    private TextView mAnswerTextView;
    private boolean saved=false;
    private int API;
    private TextView txt_showAPI;
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }
    public static boolean wasAnswerShown(Intent result){
         boolean test = result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
        return test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIstrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        btn_showanswer = (Button) findViewById(R.id.btn_showanswer);
        txt_showAPI = (TextView) findViewById(R.id.txt_showAPI);
        API = Build.VERSION.SDK_INT;
        txt_showAPI.setText("API "+API);
        if(savedInstanceState != null){
            if((saved=savedInstanceState.getBoolean("save"))) {
                setAnswerShownResult(saved);
            }
        }
        btn_showanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIstrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = btn_showanswer.getWidth() / 2;
                    int cy = btn_showanswer.getHeight() / 2;
                    float radius = btn_showanswer.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(btn_showanswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            btn_showanswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else {
                mAnswerTextView.setVisibility(View.VISIBLE);
                btn_showanswer.setVisibility(View.INVISIBLE);
            }
                saved = true;
                setAnswerShownResult(saved);
            }

        });
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i("CheatActivity", "onSaveInstanceState");
        savedInstanceState.putBoolean("save",saved);
    }
}
