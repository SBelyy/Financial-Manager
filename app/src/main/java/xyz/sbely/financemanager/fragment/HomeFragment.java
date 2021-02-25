package xyz.sbely.financemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.db.MyDBManager;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setGeneralSum(root);
        return root;
    }

    private void setGeneralSum(View v) {
        MyDBManager myDbManager;
        TextView generalSum, iGive, giveMe;

        myDbManager = new MyDBManager(v.getContext());
        generalSum = v.findViewById(R.id.general_sum);
        iGive = v.findViewById(R.id.textIG);
        giveMe = v.findViewById(R.id.textGM);

        try {
            myDbManager.openDb();
            String sums[] = myDbManager.getSumFromDb();
            myDbManager.closeDb();

            generalSum.setText(sums[0]);
            iGive.setText(sums[1]);
            giveMe.setText(sums[2]);

            myDbManager.closeDb();
        }catch (Exception ignore){}
    }


}
