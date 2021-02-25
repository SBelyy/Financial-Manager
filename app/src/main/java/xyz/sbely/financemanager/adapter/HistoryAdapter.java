package xyz.sbely.financemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.model.HistoryDebt;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<HistoryDebt> list;

    public HistoryAdapter(Context context, List<HistoryDebt> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_depts_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryDebt debt = list.get(position);
        holder.date.setText(debt.getDate());
        holder.comment.setText(debt.getComment());
        holder.sum.setText(debt.getSum());
        holder.type.setText(debt.getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView type, sum, comment, date;

        public ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            sum = itemView.findViewById(R.id.sumHistory);
            comment = itemView.findViewById(R.id.commentHistory);
            date = itemView.findViewById(R.id.dateHistory);
        }

    }
}
