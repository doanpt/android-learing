package musicplayer.group3.dev.musicplayer.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.adapter.PagerArtistAdapter;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.common.SharePreferencesController;

public class DetailArtistActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private int idArtist;
    private String nameArtist;

    //Bottom View
    private View viewBottom;
    private LinearLayout llDetailTitleSong;
    private ImageView imvImageSong;
    private ImageView imvNext;
    private ImageView imvPrevious;
    private ImageView imvPausePlay;
    private TextView tvTitleSong;
    private TextView tvNameArtist;
    private boolean isPauseOrPlay;
    //Top Menu
    private ImageView imvBack;
    private TextView tvSeach;
    private TextView tvSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artist);
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        idArtist = intent.getIntExtra(Const.KEY_ID_ARTIST, -1);
        nameArtist = intent.getStringExtra(Const.KEY_NAME_ARTIST);
    }

    private void initView() {
        isPauseOrPlay = false;
        PagerArtistAdapter pagerArtistAdapter = new PagerArtistAdapter(getSupportFragmentManager(), 2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        assert viewPager != null;
        viewPager.setAdapter(pagerArtistAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        assert tabLayout != null;
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //init menu top
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvBack.setOnClickListener(this);
        tvSeach = (TextView) findViewById(R.id.tv_action_search);
        tvSetting = (TextView) findViewById(R.id.tv_action_setting);
        tvSeach.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

        //Bottom Menu
        viewBottom = (View) findViewById(R.id.bottom_menu);
        llDetailTitleSong = (LinearLayout) findViewById(R.id.ll_detail_title_song);
        llDetailTitleSong.setOnClickListener(this);
        imvImageSong = (ImageView) findViewById(R.id.imv_image_song);
        imvImageSong.setOnClickListener(this);
        imvNext = (ImageView) findViewById(R.id.imv_next);
        imvPrevious = (ImageView) findViewById(R.id.imv_previous);
        imvPausePlay = (ImageView) findViewById(R.id.imv_pause_play);
        imvNext.setOnClickListener(this);
        imvPrevious.setOnClickListener(this);
        imvPausePlay.setOnClickListener(this);

        tvTitleSong = (TextView) findViewById(R.id.tv_bottom_title_song);
        tvTitleSong.setSelected(true);
        tvNameArtist = (TextView) findViewById(R.id.tv_bottom_name_artist);
        showBottomLayout(false);
    }

    public void showBottomLayout(boolean isFirstRun) {
        if (isFirstRun) {
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
        }
    }

    public int getIdArtist() {
        return idArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imv_back:
                finish();
                //actionFinish();
                break;
            case R.id.tv_action_search:
                actionSearch();
                break;
            case R.id.tv_action_setting:
                //actionSetting();
                //Snackbar.make(v, "Please input Setting", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.imv_image_song:
                actionShowDetailSong(v);
                break;
            case R.id.ll_detail_title_song:
                actionShowDetailSong(v);
                break;
            case R.id.imv_next:

                break;
            case R.id.imv_previous:

                break;
            case R.id.imv_pause_play:
                if (!isPauseOrPlay) {
                    imvPausePlay.setImageResource(R.drawable.ic_play);
                    isPauseOrPlay = true;

                } else {
                    imvPausePlay.setImageResource(R.drawable.ic_pause);
                    isPauseOrPlay = false;

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQUEST_CODE_ACTION_SEARCH_DETAIL_ARTIST:
                if (resultCode == RESULT_OK) {
                    //TODO play music, update animation


                    //setinfor
                    showBottomLayout(true);
                    //set title
//                    setInforBottomLayout(data.getStringExtra(Const.KEY_ACTION_SEARCH_SONG_NAME),
//                            data.getStringExtra(Const.KEY_ACTION_SEARCH_ARTIST_NAME));
                }
                break;
        }
    }

    private void actionSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE_ACTION_SEARCH_DETAIL_ARTIST);
    }

    private void actionShowDetailSong(View view) {
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
    }

    public void setInforBottomLayout(String nameSong, String nameArtist) {
        tvTitleSong.setText(nameSong);
        tvNameArtist.setText(nameArtist);
    }
}
