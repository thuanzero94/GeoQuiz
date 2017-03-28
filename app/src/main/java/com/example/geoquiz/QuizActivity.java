package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private Button btn_true;
    private Button btn_false;
    private Button btn_next;
    private Button btn_cheat;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_1, true),
            new Question(R.string.question_2,true),
            new Question(R.string.question_3,true),
            new Question(R.string.question_4, false),
            new Question(R.string.question_5,true),
            new Question(R.string.question_6, true),
    };
    private int mCurrentIndex = 0;
    private static final int REQUEST_CODE_CHEAT=0;
    private boolean mIsCheater;
    private void UpdateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void CheckAnswer(boolean userPressTrue){
        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressTrue == answerTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        btn_true = (Button) findViewById(R.id.btn_true);
        btn_false = (Button) findViewById(R.id.btn_false);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_cheat = (Button) findViewById(R.id.btn_cheat);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                mIsCheater = false;
                UpdateQuestion();
            }
        });
        btn_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CheckAnswer(true);
            }
        });
        btn_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                CheckAnswer(false);
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean("cheater");
        }
        UpdateQuestion();

        btn_cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean("cheater",mIsCheater);
    }

}
