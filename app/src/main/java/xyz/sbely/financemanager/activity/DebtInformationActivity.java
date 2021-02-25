package xyz.sbely.financemanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.sbely.financemanager.MainActivity;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;
import xyz.sbely.financemanager.adapter.HistoryAdapter;
import xyz.sbely.financemanager.model.HistoryDebt;

public class DebtInformationActivity extends AppCompatActivity {

    private TextView name;
    private TextView comment;
    private TextView sum;
    private String id;

    private List<HistoryDebt> list;
    private MyDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        componentInitialization();
        createHistory();
        RecyclerView recyclerView = findViewById(R.id.historyDebt);
        HistoryAdapter adapter = new HistoryAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void createHistory() {
        list = new ArrayList<HistoryDebt>();
        dbManager = new MyDBManager(this);
        List<String> values;

        try {
            dbManager.openDb();
            values = dbManager.getHistoryFromDb(id);
            dbManager.closeDb();

            for (int i = 0; i < values.size(); i += 4) {
                String type;

                if(values.get(i).equals(MyDBConstants.REFILL)){
                    type = getString(R.string.refill_debt);
                } else if (values.get(i).equals(MyDBConstants.REDUCTION)) {
                    type = getString(R.string.reduction_debt);
                } else {
                    type = getString(R.string.create_debt);
                }

                list.add(new HistoryDebt(type, values.get(i + 1), values.get(i + 2), values.get(i + 3)));
            }

        } catch (Exception ignore){}
    }

    private void componentInitialization() {
        Bundle argument = getIntent().getExtras();

        name = findViewById(R.id.nameDebt);
        comment = findViewById(R.id.commentDebt);
        sum = findViewById(R.id.sumDept);

        if(argument != null){
            name.setText(argument.getString("name"));
            comment.setText(argument.getString("comment"));
            sum.setText(argument.getString("sum"));
            id = argument.getString("id");
        }

    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBack();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void editDeptAdd(View view) {
        openEditDept("+");
    }

    public void editDeptRemove(View view) {
        openEditDept("-");
    }

    private void openEditDept(String operator){
        try {
            Intent intent = new Intent(DebtInformationActivity.this, EditDebtActivity.class);
            intent.putExtra("operator", operator);
            intent.putExtra("name", name.getText());
            intent.putExtra("sum", sum.getText());
            intent.putExtra("comment", comment.getText());
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

    public void deleteDebt(View view) {
        dbManager.openDb();
        dbManager.deleteRow(id);
        dbManager.closeDb();
        onBack();
    }

    public void onBack() {
        try {
            Intent intent = new Intent(DebtInformationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }
}
