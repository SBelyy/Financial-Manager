package xyz.sbely.financemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;

public class EditDebtActivity extends AppCompatActivity {

    private String operator;
    private String name;
    private String sum;
    private String comment;
    private String id;

    private MyDBManager dbManager;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_debt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new MyDBManager(this);
        componentInitialization();
    }

    private void componentInitialization() {
        Bundle argument = getIntent().getExtras();

        if(argument != null){
            operator = argument.getString("operator");
            name = argument.getString("name");
            comment = argument.getString("comment");
            sum = argument.getString("sum");
            id = argument.getString("id");

            editText = findViewById(R.id.editRefillSum);
            if(operator.equals("+")) {
                editText.setHint(getString(R.string.enter_refill));
            } else {
                editText.setHint(getString(R.string.enter_reduction));
            }
        }
    }

    @Override
    public void onBackPressed() {
        toBack();
    }

    @Override
    public boolean onSupportNavigateUp(){
        toBack();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    public void backToInform(View view) {
        toBack();
    }

    public void done(View view) {
        try {
            Double sumEdit = Double.parseDouble(editText.getText().toString());
            EditText commentEdit = findViewById(R.id.editReductionComment);
            BigDecimal newSum = new BigDecimal(sum);

            dbManager.openDb();

            String date = "" + System.currentTimeMillis();
            if(operator.equals("+")) {
                dbManager.insertToHistoryDb(id, MyDBConstants.REFILL, date,
                        editText.getText().toString(), commentEdit.getText().toString());
                newSum = newSum.add(BigDecimal.valueOf(sumEdit));
            } else {
                dbManager.insertToHistoryDb(id, MyDBConstants.REDUCTION, date,
                        editText.getText().toString(), commentEdit.getText().toString());
                newSum = newSum.subtract(BigDecimal.valueOf(sumEdit));
            }

            sum = newSum + "";
            dbManager.updateDbSum(id, sum);

            dbManager.closeDb();
            toBack();
        } catch (Exception ignore){}
    }

    private void toBack(){
        try {
            Intent intent = new Intent(EditDebtActivity.this, DebtInformationActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("sum", sum);
            intent.putExtra("comment", comment);
            intent.putExtra("id", id);
            startActivity(intent);
        } catch (Exception ignore){}
    }
}
