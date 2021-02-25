package xyz.sbely.financemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import xyz.sbely.financemanager.activity.DebtsActivity;
import xyz.sbely.financemanager.activity.ChangeAmountActivity;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.activity.StatisticsActivity;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed() {
        try {
            if(backPressedTime + 2000 > System.currentTimeMillis()){
                toast.cancel();
                finish();
                super.onBackPressed();
                return;
            } else {
                toast = Toast.makeText(getBaseContext(), getString(R.string.backToast), Toast.LENGTH_SHORT);
                toast.show();
            }
            backPressedTime = System.currentTimeMillis();
        } catch (Exception ignore){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void plusClick(View view) {
        try{
            Intent intent = new Intent(MainActivity.this, ChangeAmountActivity.class);
            intent.putExtra("type", MyDBConstants.INCOME);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

    public void removeClick(View view) {
        try{
            Intent intent = new Intent(MainActivity.this, ChangeAmountActivity.class);
            intent.putExtra("type", MyDBConstants.EXPENSE);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

    public void showStatistics(View view) {
        try{
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

    public void createDeptMe(View view) {
        goToDebts(MyDBConstants.I_GIVE);
    }

    public void createDeptI(View view) {
        goToDebts(MyDBConstants.GIVE_ME);
    }

    private void goToDebts(String state) {
        try{
            Intent intent = new Intent(MainActivity.this, DebtsActivity.class);
            intent.putExtra("state", state);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

}
