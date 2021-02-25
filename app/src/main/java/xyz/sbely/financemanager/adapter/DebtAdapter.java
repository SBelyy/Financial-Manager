package xyz.sbely.financemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.model.Debt;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Debt> list;
    private OnDebtClickListener onDebtClickListener;

    public DebtAdapter(Context context, List<Debt> list, OnDebtClickListener onDebtClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.onDebtClickListener = onDebtClickListener;
    }

    @Override
    public DebtAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_depts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DebtAdapter.ViewHolder holder, int position) {
        Debt debt = list.get(position);
        holder.date.setText(debt.getDate());
        holder.name.setText(debt.getNameD());
        holder.sum.setText(debt.getSum());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView date, name, sum;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            sum = itemView.findViewById(R.id.sum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Debt debt = list.get(getLayoutPosition());
                    onDebtClickListener.onDebtClick(debt);
                }
            });
        }
    }

    public interface OnDebtClickListener {
        void onDebtClick(Debt debt);
    }

}
