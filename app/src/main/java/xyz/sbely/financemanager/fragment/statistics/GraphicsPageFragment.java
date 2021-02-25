package xyz.sbely.financemanager.fragment.statistics;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;

import xyz.sbely.financemanager.Constants;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;
import xyz.sbely.financemanager.layout.CategoryLayout;
import xyz.sbely.financemanager.model.Category;

public class GraphicsPageFragment extends Fragment {

    private View graphicsView;
    private MyDBManager dbManager;
    int[] colors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        graphicsView = inflater.inflate(R.layout.fragment_graphics, container, false);
        dbManager = new MyDBManager(graphicsView.getContext());
        dbManager.openDb();

        colors = Constants.colors;
        initialPieChart(MyDBConstants.INCOME);
        initialPieChart(MyDBConstants.EXPENSE);
        return graphicsView;
    }

    private void initialPieChart(String type) {
        try {
            List<PieEntry> entries = new ArrayList<>();

            List<Category> categories;
            if(type.equals(MyDBConstants.INCOME))
                categories = new ArrayList<>(Arrays.asList(Constants.INCREASE_CATEGORIES));
            else
                categories = new ArrayList<>(Arrays.asList(Constants.DECREASE_CATEGORIES));

            ArrayList<CategoryLayout> newCategories = dbManager.getCategoryFromDb(type);
            for (final CategoryLayout category : newCategories) {
                categories.add(new Category(category.getName(), category.getIdImage()));
            }

            for (Category category : categories) {
                String nameCategory;

                if(!(category.getName() == null))
                    nameCategory = getString(category.getName());
                else
                    nameCategory = category.getStringName();

                float sum = Float.parseFloat(dbManager.getTotalSumFromDb(nameCategory));
                if(sum > 0)
                    entries.add(new PieEntry(sum, nameCategory));
            }

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(colors, getContext());
            PieData data = new PieData(set);

            PieChart pie;
            if(type.equals(MyDBConstants.INCOME)){
                pie = graphicsView.findViewById(R.id.pie_chart_income);
                pie.setCenterText(getString(R.string.income));
            } else {
                pie = graphicsView.findViewById(R.id.pie_chart_expense);
                pie.setCenterText(getString(R.string.costs));
            }

            pie.setCenterTextSize(20);
            pie.setData(data);
            pie.setNoDataText(getString(R.string.no_graphics));
            pie.setUsePercentValues(true);

            pie.getLegend().setWordWrapEnabled(true);
            pie.getDescription().setText("");

            pie.invalidate(); // refresh
        } catch (Exception e){
            Log.d("GRAPHICS", "Pie Diagram error");
        }
    }
}
