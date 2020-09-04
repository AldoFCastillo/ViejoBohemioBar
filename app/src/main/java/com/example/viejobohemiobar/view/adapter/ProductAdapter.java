package com.example.viejobohemiobar.view.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
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
        @BindView(R.id.progressProductCell)
        ProgressBar progressBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                Result result = new Result();
                result.setResults(productList);
                listener.selection(getAdapterPosition(), result);
            });
        }

        public void bind(Product product) {
            String title = product.getTitle();
            textViewTitle.setText(title);
            textViewPrice.setText(product.getPrice());
            String url = product.getPicture();
            Glide.with(itemView).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageViewCell);
        }
    }



    public interface adapterListener {
        void selection(Integer adapterPosition, Result result);
    }
}