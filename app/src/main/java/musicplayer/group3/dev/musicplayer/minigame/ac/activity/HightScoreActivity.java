package musicplayer.group3.dev.musicplayer.minigame.ac.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.common.Const;

public class HightScoreActivity extends Activity implements OnClickListener {
    private static final String TAG = "GameOverAct";
    private TextView tvUsername, tvScore, tvStatusPlay, tvBestScore;
    private ImageView imvStatusPlay;
    private Button btnPlayAgain;
    private LinearLayout llInfor;
    private LinearLayout llBestScore;
    private Animation mAnimationLayoutInfor, mAnimationButton;

    private boolean isStatus;
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_activity);
        getValue();
        initViews();
    }

    private void initViews() {
        mAnimationLayoutInfor = AnimationUtils.loadAnimation(this,
                R.anim.animation_translate);
        llInfor = (LinearLayout) findViewById(R.id.ll_infor);
        llInfor.startAnimation(mAnimationLayoutInfor);
        btnPlayAgain = (Button) findViewById(R.id.btn_play_again);
        btnPlayAgain.setOnClickListener(this);
        btnPlayAgain.setVisibility(View.GONE);

        mAnimationButton = AnimationUtils.loadAnimation(this,
                R.anim.animation_scale_button_game_over);

        mAnimationLayoutInfor.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                btnPlayAgain.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnPlayAgain.setVisibility(View.VISIBLE);
                btnPlayAgain.setAnimation(mAnimationButton);
                mAnimationButton.start();
            }
        });
        tvUsername = (TextView) findViewById(R.id.tv_username_game_over);
        tvScore = (TextView) findViewById(R.id.tv_score_game_over);
        tvScore.setText(score);
        tvBestScore = (TextView) findViewById(R.id.tv_best_score);
        tvStatusPlay = (TextView) findViewById(R.id.tv_state_play);
        imvStatusPlay = (ImageView) findViewById(R.id.imv_status_play);

        llBestScore = (LinearLayout) findViewById(R.id.ll_best_score);
        if (isStatus) {
            llBestScore.setVisibility(View.GONE);
            tvStatusPlay.setText("Hight Score");
            tvStatusPlay.setTextColor(Color.RED);
            tvUsername.setTextColor(Color.GREEN);
            tvUsername.setTextSize(30);
            tvUsername.setSelected(true);
            tvScore.setTextColor(Color.GREEN);
            tvScore.setTextSize(40);
            llInfor.setBackgroundResource(R.drawable.boder_frame_hight_score);
            imvStatusPlay.setImageResource(R.drawable.ic_likee);
        } else {
            llBestScore.setVisibility(View.VISIBLE);
            tvStatusPlay.setText("Game Over");
            tvStatusPlay.setTextColor(Color.WHITE);
            tvUsername.setTextColor(Color.GREEN);
            tvUsername.setTextSize(20);
            tvUsername.setSelected(true);
            tvScore.setTextColor(Color.GREEN);
            tvScore.setTextSize(30);
            llInfor.setBackgroundResource(R.drawable.boder_frame_game_over);
            imvStatusPlay.setImageResource(R.drawable.ic_game_over);
            tvBestScore.setText(readHightScore()+"");
        }

    }

    public void getValue() {
        Intent intent = getIntent();
        this.score = intent.getStringExtra(Const.KEY_SCORE);
        this.isStatus = intent.getBooleanExtra(Const.KEY_STATUS, false);
    }

    public int readHightScore() {
        SharedPreferences pre = getSharedPreferences(Const.FILE_SAVE_HIGHT_SCORE, MODE_PRIVATE);
        return pre.getInt(Const.KEY_HIGHT_SCORE, -1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_again:
                Intent dataReturn = new Intent();
                Log.i(TAG, "Play Again");
                setResult(RESULT_OK, dataReturn);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "CANCELED");
        setResult(RESULT_CANCELED);
        super.onBackPressed();

    }

}
