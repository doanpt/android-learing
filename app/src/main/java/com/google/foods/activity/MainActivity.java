package com.google.foods.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.foods.R;
import com.google.foods.adapter.FoodFragmentAdapter;
import com.google.foods.customeview.DialogNotification;
import com.google.foods.dialog.DialogNetworkConnection;
import com.google.foods.fragment.DrinksFragment;
import com.google.foods.fragment.Set30Fragment;
import com.google.foods.fragment.Set35Fragment;
import com.google.foods.fragment.Set40Fragment;
import com.google.foods.listenner.OnFoodClickItem;
import com.google.foods.models.ItemFood;
import com.google.foods.models.ItemUser;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.Keys;
import com.google.foods.utils.UserInfor;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFoodClickItem {
    private static final int REQUEST_SIGNUP = 22;
    private static final int REQUEST_LOGIN = 11;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvUserInfor;
    private ArrayList<ItemFood> arrFoodChoose;

    private DialogNetworkConnection dialogNetworkConnection;
    private boolean isNetworkConnected;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        registerBroadcastReciver();
        setContentView(R.layout.activity_main);
        initView();
        arrFoodChoose = new ArrayList<>();
        setUserInfor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    private void registerBroadcastReciver() {
        dialogNetworkConnection = new DialogNetworkConnection(this);
        dialogNetworkConnection.setCancelable(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonValue.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case CommonValue.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (isNetworkConnected) {
                        dialogNetworkConnection.dismiss();
                    } else {
                        dialogNetworkConnection.show();
                    }
                    break;
            }

        }
    };

    private void checkResultLogin(Intent intent) {
        boolean isLogin = intent.getBooleanExtra(Keys.KEY_LOGIN_SUCCESS, false);
        if (isLogin) {
            Snackbar.make(tabLayout, UserInfor.getInstance(this).getUserName() + " " + getResources().getString(R.string.login_successfull), Snackbar.LENGTH_LONG).show();
        }

    }

    private void setUserInfor() {
        String userPhoneNumber = UserInfor.getInstance(this).getPhoneNumber();
        String userName = UserInfor.getInstance(this).getUserName();
        if (userPhoneNumber == CommonValue.BLANK) {
            tvUserInfor.setText(CommonValue.STRING_USER);
        } else {
            tvUserInfor.setText(userName + "\n" + userPhoneNumber);
        }
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        inflateMenu();

        View headerNavigation = (View) navigationView.getHeaderView(0);
        tvUserInfor = (TextView) headerNavigation.findViewById(R.id.tv_user_infor);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.food_viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.float_btn_order);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmFoodOrder();
            }
        });
    }

    private void inflateMenu() {
        if (UserInfor.getInstance(this).isUserLogin()) {
            navigationView.getMenu().removeGroup(R.id.group_login);
            navigationView.getMenu().removeItem(R.id.item_menu_login_about);
            navigationView.inflateMenu(R.menu.menu_mode_user_logout);
        } else {
            navigationView.getMenu().removeGroup(R.id.group_logout);
            navigationView.getMenu().removeItem(R.id.item_menu_login_about);
            navigationView.inflateMenu(R.menu.menu_mode_user_login);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        FoodFragmentAdapter adapter = new FoodFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(Set30Fragment.getInstance(MainActivity.this), "Set30");
        adapter.addFragment(Set35Fragment.getInstance(MainActivity.this), "Set35");
        adapter.addFragment(Set40Fragment.getInstance(MainActivity.this), "Set40");
        adapter.addFragment(DrinksFragment.getInstance(MainActivity.this), "Drinks");
        viewPager.setAdapter(adapter);
    }

    /*
    * This method use to check if user not choose drink then show question to ask user, else start confirm activity
    * */
    private void confirmFoodOrder() {
        boolean isHaveFoods = checkContainFood();
        boolean isHaveDrinks = checkContainDrink();
        if (!isHaveFoods && !isHaveDrinks) {
            DialogNotification notification = new DialogNotification(this);
            notification.setContentMessage(getString(R.string.question_ask_order_food));
            notification.setHiddenBtnExit();
            notification.show();
        } else if (!isHaveFoods || !isHaveDrinks) {
            DialogNotification notification = new DialogNotification(this);
            notification.setTextBtnExit(getString(R.string.text_no));
            notification.setTextBtnOK(getString(R.string.text_yes));
            notification.setContentMessage(getString(R.string.question_ask_order_drink));
            notification.setOnClickButtonOKDialogListener(new DialogNotification.OnClickButtonOKDialogListener() {
                @Override
                public void onClickButtonOK() {
                    viewPager.setCurrentItem(3, true);
                }
            });
            notification.show();

        } else {
            startConfirmOrderActivity();
        }
    }

    private void startConfirmOrderActivity() {
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        intent.putParcelableArrayListExtra(Keys.KEY_ORDER_LIST, arrFoodChoose);
        startActivity(intent);
    }

    /**
     * this method use to check whether the user has selected a drink or not.
     */
    private boolean checkContainDrink() {
        for (ItemFood item : arrFoodChoose) {
            if (item.getType() <= CommonValue.VALUE_FOOD_20_VND) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method use to check whether the user has selected a food or not.
     */
    private boolean checkContainFood() {
        for (ItemFood item : arrFoodChoose) {
            if (item.getType() >= CommonValue.VALUE_FOOD_20_VND) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btn_order) {
            confirmFoodOrder();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else if (id == R.id.nav_sign_up) {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            intent.putExtra(Keys.KEY_REQUEST_SIGNIN_ACCOUNT, CommonValue.BLANK);
            startActivityForResult(intent, REQUEST_SIGNUP);
        } else if (id == R.id.nav_about_me) {
            Toast.makeText(MainActivity.this, "About App", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_log_out_user) {

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.string_logouting));
            progressDialog.show();

            UserInfor.getInstance(this).setUserLogout();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Snackbar.make(tabLayout, getResources().getString(R.string.logout_successfull), Snackbar.LENGTH_LONG).show();
                            inflateMenu();
                            setUserInfor();
                        }
                    }, 1000);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    inflateMenu();
                    setUserInfor();
                    checkResultLogin(data);
                }
                break;
            case REQUEST_SIGNUP:
                if (resultCode == RESULT_OK) {
                    inflateMenu();
                    setUserInfor();
                    checkResultLogin(data);
                }
                break;
        }
    }

    /**
     * this method to add food which the user choose to list order food
     *
     * @param foodItem
     */
    @Override
    public void onItemClick(ItemFood foodItem) {
        //Issue #22 fixed START
        if (arrFoodChoose.contains(foodItem)) {
            for (Iterator<ItemFood> it = arrFoodChoose.iterator(); it.hasNext(); ) {
                ItemFood s = it.next();
                if (s.getOrderQuantity() == 0) {
                    it.remove();
                } else if (foodItem.getIdFood() == s.getIdFood()) {
                    s.setOrderQuantity(foodItem.getOrderQuantity());
                }
            }
        } else {
            arrFoodChoose.add(foodItem);
        }
        //Issue #22 fixed END
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        Set30Fragment.releaseInstance();
        Set35Fragment.releaseInstance();
        Set40Fragment.releaseInstance();
        DrinksFragment.releaseInstance();
        unregisterReceiver(receiver);
    }

    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }
}
