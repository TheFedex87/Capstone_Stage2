package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 12/07/2018.
 */

public class WaiterCallsAdapter extends RecyclerView.Adapter<WaiterCallsAdapter.WaiterCallsViewHolder> {
    private List<WaiterCall> calls;

    public void swapCalls(List<WaiterCall> calls){
        this.calls = calls;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WaiterCallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiter_call, parent, false);
        return new WaiterCallsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaiterCallsViewHolder holder, int position) {
        holder.waiterCallText.setText(calls.get(position).getTableId());
        holder.waiterCallId.setText(calls.get(position).getId());
    }

    @Override
    public int getItemCount() {
        if (calls == null) return 0;
        return calls.size();
    }

    class WaiterCallsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.waiter_call_text)
        TextView waiterCallText;

        @BindView(R.id.waiter_call_id)
        TextView waiterCallId;

        public WaiterCallsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
