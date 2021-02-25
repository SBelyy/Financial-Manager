package xyz.sbely.financemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import xyz.sbely.financemanager.MainActivity;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;

public class DebtsActivity extends AppCompatActivity {

    private String type;

    private MyDBManager dbManager;

    private Calendar calendar;
    private TextView date;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getString(R.string.i_give);
        initialState();
        dbManager = new MyDBManager(this);

        calendar = Calendar.getInstance();
        date = findViewById(R.id.textDate2);
        time = findViewById(R.id.textTime2);
        setInitialDateTime();
    }

    private void initialState() {
        Bundle argument = getIntent().getExtras();

        if (argument != null) {
            String state = argument.getString("state");
            if (state.equals(MyDBConstants.GIVE_ME)) {
                Switch sw = findViewById(R.id.swith);
                sw.setChecked(true);
                View view = sw;
                switch_click(view);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        goBack();
        return true;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDb();
    }

    public void switch_click(View view) {
        TextView iGive = findViewById(R.id.i_give);
        TextView giveMe = findViewById(R.id.give_me);
        if(type.equals(getString(R.string.i_give))) {
            iGive.setTextColor(getColor(R.color.colorBlack60));
            giveMe.setTextColor(getColor(R.color.colorPrimary));
            type = getString(R.string.give_me);
        } else {
            iGive.setTextColor(getColor(R.color.colorPrimary));
            giveMe.setTextColor(getColor(R.color.colorBlack60));
            type = getString(R.string.i_give);
        }
    }

    public void enterTime(View view) {
        new TimePickerDialog(DebtsActivity.this, R.style.TimePickerTheme, t,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    public void enterDate(View view) {
        new DatePickerDialog(DebtsActivity.this, R.style.TimePickerTheme, d,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {
        date.setText(DateUtils.formatDateTime(this, calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        time.setText(DateUtils.formatDateTime(this, calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
    }

    public void doneClick(View view) {
        try{
            EditText name = findViewById(R.id.editName);
            EditText editSum = findViewById(R.id.editSum);
            EditText comment = findViewById(R.id.editComment);

            Double sum = Double.parseDouble(editSum.getText().toString());

            String date = "" + calendar.getTimeInMillis();
            if(type.equals(getString(R.string.i_give))) {
                dbManager.insertToDb(MyDBConstants.I_GIVE, name.getText().toString(), date,
                        editSum.getText().toString(), comment.getText().toString(), -1);
            }else {
                dbManager.insertToDb(MyDBConstants.GIVE_ME, name.getText().toString(), date,
                        editSum.getText().toString(), comment.getText().toString(), -1);
            }

            String idn = dbManager.getLastIdFromDb();
            dbManager.insertToHistoryDb(idn, MyDBConstants.CREATE_DEBT, date,
                    editSum.getText().toString(), comment.getText().toString());

            goBack();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void goBack() {
        try {
            Intent intent = new Intent(DebtsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

}
