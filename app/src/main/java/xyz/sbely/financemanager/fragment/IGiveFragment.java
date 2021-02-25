package xyz.sbely.financemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.sbely.financemanager.activity.DebtInformationActivity;
import xyz.sbely.financemanager.model.Debt;
import xyz.sbely.financemanager.adapter.DebtAdapter;
import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBConstants;
import xyz.sbely.financemanager.db.MyDBManager;

public class IGiveFragment extends Fragment {

    private List<Debt> debts;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_i_give, container, false);
        createList(root);

        RecyclerView recyclerView = root.findViewById(R.id.list1);
        DebtAdapter.OnDebtClickListener onDebtClickListener = new DebtAdapter.OnDebtClickListener() {
            @Override
            public void onDebtClick(Debt debt) {
                try {
                    Intent intent = new Intent(root.getContext(), DebtInformationActivity.class);
                    intent.putExtra("name", debt.getNameD());
                    intent.putExtra("sum", debt.getSum());
                    //intent.putExtra("date", debt.getDate());
                    intent.putExtra("comment", debt.getComment());
                    intent.putExtra("id", debt.getId());
                    startActivity(intent);
                } catch (Exception ignore){}
            }
        };
        DebtAdapter adapter = new DebtAdapter(root.getContext(), debts, onDebtClickListener);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void createList(View view) {
        MyDBManager dbManager = new MyDBManager(view.getContext());
        List<String> list;
        TextView textView = view.findViewById(R.id.i);
        TextView textView1 = view.findViewById(R.id.numDebtI);

        try {
            dbManager.openDb();
            debts = dbManager.getFromDb(MyDBConstants.GIVE_ME);

            String[] sums= dbManager.getSumFromDb();
            textView.setText(sums[1]);
            textView1.setText(sums[3]);

            dbManager.closeDb();
        } catch (Exception ignore){}
    }

}
