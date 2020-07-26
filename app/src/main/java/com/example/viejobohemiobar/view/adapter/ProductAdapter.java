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
import com.example.viejobohemiobar.model.pojo.Product;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter {

    private adapterListener listener;
    private List<Product> productList;


    public ProductAdapter(adapterListener listener, List<Product> products) {
        this.listener = listener;
        this.productList = products;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_cell, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        ProductViewHolder productViewHolder = (ProductViewHolder) holder;
        productViewHolder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    public class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewCell)
        ImageView imageViewCell;
        @BindView(R.id.textViewTitleCell)
        TextView textViewTitle;
        @BindView(R.id.textViewPriceCell)
        TextView textViewPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.selection(getAdapterPosition());
                }
            });
        }

        public void bind(Product product) {
            String title = product.getTitle();
            textViewTitle.setText(title);
            textViewPrice.setText(product.getPrice());
            String url = product.getPicture();
            Glide.with(itemView).load(url).into(imageViewCell);
        }
    }

    private String getPriceWithFormat(Product product) {
        String stringPrice = product.getPrice();
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
        Double c = Double.parseDouble(stringPrice);
        String price = formatoImporte.format(c);
        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price ="$"+ price.substring(1, centsIndex);
            }
        }
        return price;
    }


    public interface adapterListener {
        void selection(Integer adapterPosition);
    }
}

