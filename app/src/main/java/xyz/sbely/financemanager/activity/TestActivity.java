package xyz.sbely.financemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBManager;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        MyDBManager dbManager = new MyDBManager(this);
        TextView text = findViewById(R.id.textTest);
        TextView text2 = findViewById(R.id.textTest2);
        dbManager.openDb();
        List<String> values = dbManager.getHistoryFromDb("6");
        String idn = dbManager.getLastIdFromDb();
        dbManager.closeDb();

        text.setText("");
        for (String ids: values) {
            text.append(ids + " ");
        }

        text2.setText(idn);

    }

}
