package xyz.sbely.financemanager.fragment.statistics;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import xyz.sbely.financemanager.model.GraphPoint;

public class GraphicsPageFragment extends Fragment {

    private Handler handler;
    private List<PieEntry> entriesPieIncome;
    private List<PieEntry> entriesPieExpense;
    private LineData dataIncome;
    private LineData dataExpense;

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

        initialLineChartComponents(MyDBConstants.INCOME);
        initialLineChartComponents(MyDBConstants.EXPENSE);

        return graphicsView;
    }

    private void initialLineChartComponents(final String type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GraphPoint> points = dbManager.getPointForType(type);
                    List<Entry> entries = new ArrayList<>();

                    for(int i = 0; i < points.size(); i++){
                        for (int j = 0; j < points.size(); j++){
                            if (points.get(i).getDate() == points.get(j).getDate()) {
                                float sum = points.get(i).getSum() + points.get(j).getSum();
                                points.get(i).setSum(sum);
                                points.remove(points.get(j));
                            }
                        }
                    }

                    for (GraphPoint point : points) {
                        entries.add(new Entry(point.getDate(), point.getSum()));
                    }

                    LineDataSet dataSet;
                    if (type.equals(MyDBConstants.INCOME))
                        dataSet = new LineDataSet(entries, getString(R.string.income));
                    else
                        dataSet = new LineDataSet(entries, getString(R.string.costs));
                    dataSet.setDrawCircleHole(false);
                    dataSet.setCircleColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
                    dataSet.setDrawFilled(true);
                    dataSet.setFillColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
                    dataSet.setColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));

                    if (type.equals(MyDBConstants.INCOME)){
                        dataIncome = new LineData(dataSet);
                        handler.sendEmptyMessage(Constants.GRAPHICS_LINE_INC_SUCCESSFULLY_LOADED);
                    }
                    else{
                        dataExpense = new LineData(dataSet);
                        handler.sendEmptyMessage(Constants.GRAPHICS_LINE_EXP_SUCCESSFULLY_LOADED);
                    }

                } catch (Exception e) {
                    if(type.equals(MyDBConstants.INCOME))
                        handler.sendEmptyMessage(Constants.GRAPHICS_LINE_INC_UNSUCCESSFULLY_LOADED);
                    else
                        handler.sendEmptyMessage(Constants.GRAPHICS_LINE_EXP_UNSUCCESSFULLY_LOADED);

                    Log.d("GRAPHICS", "Load Line data failed");
                }
            }
        });
        thread.start();
    }

    private void createLineChart(String type) {
        try {
            LineChart lineChart;
            if (type.equals(MyDBConstants.INCOME)){
                lineChart = graphicsView.findViewById(R.id.line_chart_income);
                lineChart.setData(dataIncome);
            }
            else{
                lineChart = graphicsView.findViewById(R.id.line_chart_expense);
                lineChart.setData(dataExpense);
            }
            lineChart.getXAxis().setEnabled(false);
            lineChart.setNoDataText(getString(R.string.no_graphics));
            lineChart.setAutoScaleMinMaxEnabled(true);
            lineChart.getDescription().setText("");
            lineChart.invalidate();
        } catch (Exception e){
            Log.d("GRAPHICS", "LineChart create error");
        }
    }

    @Override
    public void onDestroy() {
        dbManager.closeDb();
        super.onDestroy();
    }

    private void createHandler() {
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                ProgressBar progress = null;
                LinearLayout layout;
                switch (msg.what) {
                    case Constants.GRAPHICS_PIE_EXP_SUCCESSFULLY_LOADED:
                        if (progress == null){
                            layout = graphicsView.findViewById(R.id.graphics_layout);
                            progress = graphicsView.findViewById(R.id.progress_graphics);
                            layout.removeView(progress);
                        }
                        createPieChartGraphics(MyDBConstants.EXPENSE);
                        break;
                    case Constants.GRAPHICS_PIE_EXP_UNSUCCESSFULLY_LOADED:
                        initialPieChartComponents(MyDBConstants.EXPENSE);
                        break;
                    case Constants.GRAPHICS_PIE_INC_SUCCESSFULLY_LOADED:
                        if (progress == null){
                            layout = graphicsView.findViewById(R.id.graphics_layout);
                            progress = graphicsView.findViewById(R.id.progress_graphics);
                            layout.removeView(progress);
                        }
                        createPieChartGraphics(MyDBConstants.INCOME);
                        break;
                    case Constants.GRAPHICS_PIE_INC_UNSUCCESSFULLY_LOADED:
                        initialPieChartComponents(MyDBConstants.INCOME);
                        break;
                    case Constants.GRAPHICS_LINE_INC_SUCCESSFULLY_LOADED:
                        createLineChart(MyDBConstants.INCOME);
                        break;
                    case Constants.GRAPHICS_LINE_INC_UNSUCCESSFULLY_LOADED:
                        initialLineChartComponents(MyDBConstants.INCOME);
                        break;
                    case Constants.GRAPHICS_LINE_EXP_SUCCESSFULLY_LOADED:
                        createLineChart(MyDBConstants.EXPENSE);
                        break;
                    case Constants.GRAPHICS_LINE_EXP_UNSUCCESSFULLY_LOADED:
                        initialLineChartComponents(MyDBConstants.EXPENSE);
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
                set = new PieDataSet(entriesPieIncome, "");
                pie = graphicsView.findViewById(R.id.pie_chart_income);
                pie.setCenterText(getString(R.string.income));
            } else {
                set = new PieDataSet(entriesPieExpense, "");
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
                        entriesPieIncome = entries;
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_INC_SUCCESSFULLY_LOADED);
                    }
                    else {
                        entriesPieExpense = entries;
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_EXP_SUCCESSFULLY_LOADED);
                    }

                } catch (Exception e){
                    if(type.equals(MyDBConstants.INCOME))
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_INC_UNSUCCESSFULLY_LOADED);
                    else
                        handler.sendEmptyMessage(Constants.GRAPHICS_PIE_EXP_UNSUCCESSFULLY_LOADED);

                    Log.d("GRAPHICS", "Load data failed");
                }
            }
        });
        t.start();
    }

}
