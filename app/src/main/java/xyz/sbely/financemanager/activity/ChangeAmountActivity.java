package xyz.sbely.financemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.text.format.DateUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import xyz.sbely.financemanager.MainActivity;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.layout.CategoryLayout;
import xyz.sbely.financemanager.Constants;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;
import xyz.sbely.financemanager.model.Category;

public class ChangeAmountActivity extends AppCompatActivity {

    private CategoryLayout enableCategoryLayout;
    private String type;

    private MyDBManager dbManager;
    private TextView currentData;
    private TextView currentTime;
    private Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_amount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new MyDBManager(this);

        currentTime = findViewById(R.id.textTime);
        currentData = findViewById(R.id.textDate);

        Bundle argument = getIntent().getExtras();
        if (argument != null) {
            type = argument.getString("type");
        }

        setInitialDateTime();
        createCategory();
    }

    private void createCategory() {
        LinearLayout linearLayout = findViewById(R.id.mainLayout);

        Category[] categories;

        if(type.equals(MyDBConstants.INCOME)){
            categories = Constants.INCREASE_CATEGORIES;
        }else {
            categories = Constants.DECREASE_CATEGORIES;

            TextView tv = findViewById(R.id.layoutTitle);
            tv.setText(getString(R.string.choose_spend));

            EditText ed = findViewById(R.id.enterSum2);
            ed.setHint(getString(R.string.enter_spend));
        }


        List<CategoryLayout> categoryLayouts = new ArrayList<>();

        for(Category category: categories){
            final CategoryLayout categoryLayout = new CategoryLayout(this, getString(category.getName()),
                    category.getImage());
            categoryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swap(categoryLayout);
                }
            });
            linearLayout.addView(categoryLayout);
            categoryLayouts.add(categoryLayout);
        }

        if (!categoryLayouts.isEmpty()){
            enableCategoryLayout = categoryLayouts.get(0);
            enableCategoryLayout.visibleOn();
        }

        try {
            dbManager.openDb();
            ArrayList<CategoryLayout> newCategoryLayout = dbManager.getCategoryFromDb(type);
            if (!newCategoryLayout.isEmpty())
            for (final CategoryLayout category : newCategoryLayout){
                category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swap(category);
                    }
                });
                linearLayout.addView(category);
            }
        } catch (Exception e) { }


        CategoryLayout categoryLayout = new CategoryLayout(this, getString(R.string.add), R.drawable.button_add_category);
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
        linearLayout.addView(categoryLayout);
    }

    private void addCategory() {
        try {
            Intent intent = new Intent(ChangeAmountActivity.this, AddCategoryActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

    private void swap(CategoryLayout categoryLayout) {
        enableCategoryLayout.visibleOff();
        categoryLayout.visibleOn();
        enableCategoryLayout = categoryLayout;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onSupportNavigateUp(){
        goBack();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
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

    public void setDate(View view) {
        new DatePickerDialog(ChangeAmountActivity.this, R.style.TimePickerTheme, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setTime(View view) {
        new TimePickerDialog(ChangeAmountActivity.this, R.style.TimePickerTheme, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime () {
        currentData.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_SHOW_YEAR));
        currentTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
    }

    public void donePress(View view) {
        try {
            EditText editSum = findViewById(R.id.enterSum2);
            EditText editComments = findViewById(R.id.enterComment2);

            Double sum = Double.parseDouble(editSum.getText().toString());

            String date = "" + dateAndTime.getTimeInMillis();
            String source = enableCategoryLayout.getName();
            dbManager.insertToDb(type, source, date,
                editSum.getText().toString(), editComments.getText().toString(), enableCategoryLayout.getIdImage());

            goBack();
        }catch (Exception ign){
            System.out.println(ign.getMessage());
        }
    }

    private void goBack() {
        try {
            Intent intent = new Intent(ChangeAmountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }
}
