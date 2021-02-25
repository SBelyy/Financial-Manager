package xyz.sbely.financemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.layout.CategoryLayout;
import xyz.sbely.financemanager.Constants;
import xyz.sbely.financemanager.db.MyDBManager;
import xyz.sbely.financemanager.model.Category;

public class AddCategoryActivity extends AppCompatActivity {

    CategoryLayout newCategoryLayout;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle argument = getIntent().getExtras();
        if (argument != null) {
            type = argument.getString("type");
        }

        initialCategoryList();
        newCategoryLayout = null;
    }

    private void initialCategoryList() {
        int CATEGORY_ROWS = 5;
        int CATEGORY_COLUMNS = 4;

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        List<Integer> imageIds = new ArrayList<>(Arrays.asList(Constants.IMAGE_NEW_CATEGORY_ID));
        for(Category category: Constants.DECREASE_CATEGORIES){
            imageIds.add(category.getImage());
        }
        for(Category category: Constants.INCREASE_CATEGORIES){
            if (!imageIds.contains(category.getImage()))
                imageIds.add(category.getImage());
        }

        int size = imageIds.size();
        for (int i = 0; i < CATEGORY_ROWS; i++) {

           TableRow tableRow = new TableRow(this);
           tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                   TableLayout.LayoutParams.WRAP_CONTENT));

           for (int j = 0; j < CATEGORY_COLUMNS; j++) {
               if(size - 1 < 0) continue;
               final CategoryLayout categoryLayout = new CategoryLayout(this, "", imageIds.get(size - 1));

               categoryLayout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       swap(categoryLayout);
                   }
               });
               size--;

               tableRow.addView(categoryLayout, j);
           }
          tableLayout.addView(tableRow, i);
       }
    }

    private void swap(CategoryLayout categoryLayout){
        if(newCategoryLayout != null)
            newCategoryLayout.visibleOff();
        newCategoryLayout = categoryLayout;
        newCategoryLayout.visibleOn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(AddCategoryActivity.this, ChangeAmountActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
            finish();
        } catch (Exception ignore){}
    }

    public void addCategory(View view) {
        EditText editText = findViewById(R.id.nameCategory);
        String name = editText.getText().toString();
        if(newCategoryLayout == null || name.isEmpty()){
            Toast.makeText(this, getString(R.string.errorAdd), Toast.LENGTH_SHORT).show();
        } else {
            MyDBManager dbManager = new MyDBManager(this);
            dbManager.openDb();
            dbManager.insertToCategoryDb(type, name, newCategoryLayout.getIdImage());
            dbManager.closeDb();
            onBackPressed();
        }
    }
}
