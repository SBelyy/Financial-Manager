package xyz.sbely.financemanager.fragment.statistics;

import android.app.Dialog;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.sbely.financemanager.Constants;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;
import xyz.sbely.financemanager.layout.CategoryLayout;
import xyz.sbely.financemanager.model.Category;
import xyz.sbely.financemanager.model.Report;

public class ReportPageFragment extends Fragment {

    private Handler handler;
    private LayoutInflater inflater;
    private View reportView;

    private List<View> incomeView;
    private List<View> expenseView;
    private List<View> reportsViewList;

    private MyDBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        reportView = inflater.inflate(R.layout.fragment_report, container, false);
        this.inflater = inflater;

        dbManager = new MyDBManager(reportView.getContext());
        dbManager.openDb();

        createHandler();
        createIncomeView();
        createExpenseView();
        createReport();

        return reportView;
    }

    private void createHandler() {
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                ProgressBar progress;
                switch (msg.what){
                    case Constants.INCOME_SUCCESSFULLY_LOADED:
                        progress = reportView.findViewById(R.id.progress_income);
                        LinearLayout layout = reportView.findViewById(R.id.income_layout);
                        layout.removeView(progress);
                        for (View view: incomeView){
                            layout.addView(view);
                        }
                        break;
                    case Constants.INCOME_UNSUCCESSFULLY_LOADED:
                        createIncomeView();
                        break;
                    case Constants.EXPENSE_SUCCESSFULLY_LOADED:
                        progress = reportView.findViewById(R.id.progress_costs);
                        LinearLayout layout1 = reportView.findViewById(R.id.costs_layout);
                        layout1.removeView(progress);
                        for (View view: expenseView){
                            layout1.addView(view);
                        }
                        break;
                    case Constants.EXPENSE_UNSUCCESSFULLY_LOADED:
                        createExpenseView();
                        break;
                    case Constants.REPORT_SUCCESSFULLY_LOADED:
                        progress = reportView.findViewById(R.id.progress_report);
                        LinearLayout layout2 = reportView.findViewById(R.id.report_layout);
                        layout2.removeView(progress);
                        for (View view: reportsViewList){
                            layout2.addView(view);
                        }
                        break;
                    case Constants.REPORT_UNSUCCESSFULLY_LOADED:
                        createReport();
                        break;
                }
            };
        };

    }

    private void createReport() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Report> reports = dbManager.getReportFromDb();
                    reportsViewList = new ArrayList<>();

                    long dateInMillis = 0;
                    if (!reports.isEmpty()){
                        dateInMillis = Long.parseLong(reports.get(0).getDate());
                        addDateView(dateInMillis);
                    }

                    for (final Report report: reports) {
                        View view = inflater.inflate(R.layout.fragment_report_history, null);

                        TextView name = view.findViewById(R.id.name_report);
                        name.setText(report.getNameD());

                        ImageView icon = view.findViewById(R.id.icon_report);
                        icon.setImageResource(report.getId());
                        icon.setColorFilter(getResources().getColor(R.color.colorPrimary, view.getContext().getTheme()));

                        TextView sum = view.findViewById(R.id.sum_report);
                        sum.setText(report.getSum());
                        if(report.getType().equals(MyDBConstants.INCOME))
                            sum.setTextColor(getResources().getColor(R.color.colorGreen, view.getContext().getTheme()));

                        TextView comment = view.findViewById(R.id.comment_report);
                        comment.setText(report.getComment());

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialogStats(report);
                            }
                        });

                        long difference = dateInMillis - Long.parseLong(report.getDate());
                        int days = (int) (difference / (24 * 60 * 60 * 1000));
                        if(days >= 1) {
                            dateInMillis = Long.parseLong(report.getDate());
                            addDateView(dateInMillis);
                        }
                        reportsViewList.add(view);
                    }

                    handler.sendEmptyMessage(Constants.REPORT_SUCCESSFULLY_LOADED);
                } catch (Exception e) {
                    handler.sendEmptyMessage(Constants.REPORT_UNSUCCESSFULLY_LOADED);
                    Log.d("REPORT", "Failed loaded");
                }
            }
        });
        t.start();
    }

    private void showDialogStats(Report report) {
        try {
            Dialog dialog = new Dialog(reportView.getContext());
            dialog.setContentView(R.layout.fragment_dialog);

            TextView tv = dialog.findViewById(R.id.date_dialog);
            tv.setText(DateUtils.formatDateTime(reportView.getContext(), Long.parseLong(report.getDate()),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME));

            tv = dialog.findViewById(R.id.name_dialog);
            tv.setText(report.getNameD());

            tv = dialog.findViewById(R.id.comment_dialog);
            tv.setText(report.getComment());

            tv = dialog.findViewById(R.id.sum_dialog);
            tv.setText(report.getSum());
            if(report.getType().equals(MyDBConstants.INCOME))
                tv.setTextColor(getResources().getColor(R.color.colorGreen, dialog.getContext().getTheme()));

            dialog.show();
        } catch (Exception e){
            Log.d("REPORT", "Show dialog failed");
        }
    }

    private void addDateView(long time){
        try {
            TextView date = new TextView(reportView.getContext());
            date.setText(DateUtils.formatDateTime(reportView.getContext(), time, DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME));
            date.setTextSize(18);
            reportsViewList.add(date);
        } catch (Exception e) {
            Log.d("REPORT", "Add date failed");
        }
    }

    private void createIncomeView() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                incomeView = createView(MyDBConstants.INCOME);
            }
        });
        t.start();
    }

    private void createExpenseView() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                expenseView = createView(MyDBConstants.EXPENSE);
            }
        });
        t.start();
    }

    private List<View> createView(String type) {
        List<View> viewCategory = new ArrayList<>();
        try {
            List<Category> totalCategory;
            int colorId;

            if(type.equals(MyDBConstants.EXPENSE)){
                totalCategory = new ArrayList<>(Arrays.asList(Constants.DECREASE_CATEGORIES));
                colorId = R.color.colorRed;
            }
            else {
                totalCategory = new ArrayList<>(Arrays.asList(Constants.INCREASE_CATEGORIES));
                colorId = R.color.colorGreen;
            }

            ArrayList<CategoryLayout> newCategoryLayout = dbManager.getCategoryFromDb(type);
            if (!newCategoryLayout.isEmpty())
                for (final CategoryLayout category : newCategoryLayout){
                    totalCategory.add(new Category(category.getName(), category.getIdImage()));
                }

            for (Category category : totalCategory){
                View refill = inflater.inflate(R.layout.fragment_refil_costs, null);
                String nameCategory;

                if(!(category.getName() == null))
                    nameCategory = getString(category.getName());
                else
                    nameCategory = category.getStringName();

                TextView name = refill.findViewById(R.id.name_refill_costs);
                name.setText(nameCategory);

                ImageView icon = refill.findViewById(R.id.icon_refill_costs);
                icon.setImageResource(category.getImage());
                icon.setColorFilter(getResources().getColor(R.color.colorPrimary, refill.getContext().getTheme()));

                TextView totalSum = refill.findViewById(R.id.sum_refill_costs);
                totalSum.setTextColor(getResources().getColor(colorId, refill.getContext().getTheme()));
                totalSum.setText(dbManager.getTotalSumFromDb(nameCategory));

                viewCategory.add(refill);
            }

            if(type.equals(MyDBConstants.EXPENSE))
                handler.sendEmptyMessage(Constants.EXPENSE_SUCCESSFULLY_LOADED);
            else
                handler.sendEmptyMessage(Constants.INCOME_SUCCESSFULLY_LOADED);
        } catch (Exception e) {
            if(type.equals(MyDBConstants.EXPENSE))
                handler.sendEmptyMessage(Constants.EXPENSE_UNSUCCESSFULLY_LOADED);
            else
                handler.sendEmptyMessage(Constants.INCOME_UNSUCCESSFULLY_LOADED);
            Log.d(type, "Failed loaded");
        }

        return viewCategory;
    }

    @Override
    public void onDestroy() {
        dbManager.closeDb();
        super.onDestroy();
    }
}
