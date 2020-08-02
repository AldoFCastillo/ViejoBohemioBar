package com.example.viejobohemiobar.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter {

    private listener listener;
    private List<Order> orderList;

    public OrderAdapter(listener listener, List<Order> orderList) {
        this.listener = listener;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_cell, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        orderViewHolder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewCellOrder)
        ImageView imageViewCellOrder;
        @BindView(R.id.textViewTitleCellOrder)
        TextView textViewTitleCellOrder;
        @BindView(R.id.textViewTotalCellOrder)
        TextView textViewTotalCellOrder;
        @BindView(R.id.textViewPaymentCellOrder)
        TextView textViewPaymentCellOrder;
        @BindView(R.id.textViewDetailsCellOrder)
        TextView textViewDetailsCellOrder;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderList(orderList);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.orderAdapterListener(getAdapterPosition(), orderLog);
                }
            });


        }

        public void bind(Order order) {
            Glide.with(itemView).load(order.getResult().getResults().get(0).getPicture()).into(imageViewCellOrder);
            String title = "Pedido Mesa " + order.getTable() + " (" + order.getTime() + "hs)";
            textViewTitleCellOrder.setText(title);
            textViewTotalCellOrder.setText(order.getTotal());
            String payment;
            if (order.getCash()) {
                payment = "Efectivo";
            } else payment = "Tarjeta/otro";
            textViewPaymentCellOrder.setText(payment);
            String wait = "";
            if (order.getCallWait()) {
                wait = "Solicita camarero/a!!! | ";
            }
            wait = wait.concat("Productos: " + order.getResult().getResults().size());
            textViewDetailsCellOrder.setText(wait);
        }
    }

    public interface listener {
        void orderAdapterListener(Integer adapterPosition, OrderLog orderLog);
    }
}
