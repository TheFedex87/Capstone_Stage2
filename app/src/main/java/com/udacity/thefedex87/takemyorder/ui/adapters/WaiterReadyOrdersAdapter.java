package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 12/07/2018.
 */

//Adapter used to display the list of Ready orders inside waiter activity
public class WaiterReadyOrdersAdapter extends RecyclerView.Adapter<WaiterReadyOrdersAdapter.WaiterReadyOrdersViewHolder> {
    private List<WaiterReadyOrder> readyOrders;
    private Context context;
    private WaiterReadyOrderAdapterClick waiterReadyOrderAdapterClick;

    public WaiterReadyOrdersAdapter(WaiterReadyOrderAdapterClick waiterReadyOrderAdapterClick){
        this.waiterReadyOrderAdapterClick = waiterReadyOrderAdapterClick;
    }

    public void swapReadyOrders(List<WaiterReadyOrder> readyOrders){
        this.readyOrders = readyOrders;
        notifyDataSetChanged();
    }

    public interface WaiterReadyOrderAdapterClick {
        void takeReadyOrderClick(WaiterReadyOrder readyOrder);
    }

    @NonNull
    @Override
    public WaiterReadyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiter_ready_order, parent, false);
        return new WaiterReadyOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaiterReadyOrdersViewHolder holder, int position) {
        holder.waiterReadyOrderText.setText(context.getString(R.string.waiter_order_ready_text, readyOrders.get(position).getTableId()));
        holder.waiterReadyOrderId.setText(readyOrders.get(position).getId());
    }

    @Override
    public int getItemCount() {
        if (readyOrders == null) return 0;
        return readyOrders.size();
    }

    class WaiterReadyOrdersViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.waiter_ready_order_text)
        TextView waiterReadyOrderText;

        @BindView(R.id.waiter_ready_order_id)
        TextView waiterReadyOrderId;

        @BindView(R.id.take_request)
        ImageView takeRequest;

        public WaiterReadyOrdersViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            takeRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    waiterReadyOrderAdapterClick.takeReadyOrderClick(readyOrders.get(getAdapterPosition()));
                }
            });
        }
    }
}
