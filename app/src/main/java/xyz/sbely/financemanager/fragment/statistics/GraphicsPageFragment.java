package xyz.sbely.financemanager.fragment.statistics;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.sbely.financemanager.Constants;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;
import xyz.sbely.financemanager.layout.CategoryLayout;
import xyz.sbely.financemanager.model.Category;

public class GraphicsPageFragment extends Fragment {

    private Handler handler;
    private List<PieEntry> entriesIncome;
    private List<PieEntry> entriesExpense;

    private View graphicsView;
    private MyDBManager dbManager;
    private int[] colors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        graphicsView = inflater.inflate(R.layout.fragment_graphics, container, false);
        dbManager = new MyDBManager(graphicsView.getContext());
        dbManager.openDb();

        colors = Constants.colors;
        createHandler();
        initialPieChartComponents(MyDBConstants.INCOME);
        initialPieChartComponents(MyDBConstants.EXPENSE);
        return graphicsView;
    }

    private void createHandler() {
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                ProgressBar progress;
                switch (msg.what) {
                    case Constants.GRAPHICS_PIE_EXP_SUCCESSFULLY_LOADED:
                        createPieChartGraphics(MyDBConstants.EXPENSE);
                        break;
                    case Constants.GRAPHICS_PIE_EXP_UNCCESSFULLY_LOADED:
                        initialPieChartComponents(MyDBConstants.EXPENSE);
                        break;
                    case Constants.GRAPHICS_PIE_INC_SUCCESSFULLY_LOADED:
                        createPieChartGraphics(MyDBConstants.INCOME);
                        break;
                    case Constants.GRAPHICS_PIE_INC_UNCCESSFULLY_LOADED:
                        initialPieChartComponents(MyDBConstants.INCOME);
                        break;
                }
            }
        };
    }

    private void createPieChartGraphics(String type){
        try {
            PieDataSet set;
            PieChart pie;
            if (type.equals(MyDBConstants.INCOME)) {
                set = new PieDataSet(entriesIncome, "");
                pie = graphicsView.findViewById(R.id.pie_chart_income);
                pie.setCenterText(getString(R.string.income));
            } else {
                set = new PieDataSet(entriesExpense, "");
                pie = graphicsView.findViewById(R.id.pie_chart_expense);
                pie.setCenterText(getString(R.string.costs));
            }
            set.setColors(colors, getContext());
            PieData data = new PieData(set);

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

    private void initialPieChartComponents(final String type) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
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

                    if(type.equals(MyDBConstants.INCOME)){
                        entriesIncome = entries;
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_INC_SUCCESSFULLY_LOADED);
                    }
                    else {
                        entriesExpense = entries;
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_EXP_SUCCESSFULLY_LOADED);
                    }

                } catch (Exception e){
                    if(type.equals(MyDBConstants.INCOME))
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_INC_UNCCESSFULLY_LOADED);
                    else
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_EXP_UNCCESSFULLY_LOADED);

                    Log.d("GRAPHICS", "Load data failed");
                }
            }
        });
        t.start();
    }
}
