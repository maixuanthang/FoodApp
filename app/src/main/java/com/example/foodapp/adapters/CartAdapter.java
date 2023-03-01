package com.example.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Utils.Utils;
import com.example.foodapp.databinding.ItemCartBinding;
import com.example.foodapp.listener.ChangeNumListener;
import com.example.foodapp.models.Cart;

import java.util.List;

import io.paperdb.Paper;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<Cart> cartList;
    private ChangeNumListener changeNumListener;

    public CartAdapter(Context context, List<Cart> cartList, ChangeNumListener changeNumListener) {
        this.context = context;
        this.cartList = cartList;
        this.changeNumListener = changeNumListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding cartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(cartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.binding.txtname.setText(cart.getMealDetail().getMeal());
        Glide.with(context).load(cart.getMealDetail().getStrmealthumb()).into(holder.binding.imageCart);
        holder.binding.txtprice.setText(cart.getMealDetail().getPrice() + "");
        holder.binding.imagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(holder.getAdapterPosition());
                notifyDataSetChanged();
                changeNumListener.change();
            }
        });
        holder.binding.imagSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subToCart(holder.getAdapterPosition());
                notifyDataSetChanged();
                changeNumListener.change();
            }
        });
        holder.binding.txtamount.setText(cart.getAmount() + "");
        holder.binding.txtprice2.setText("$"+ String.valueOf(cart.getAmount() * cart.getMealDetail().getPrice()));



    }

    private void subToCart(int adapterPosition) {
        if (Utils.cartList.get(adapterPosition).getAmount() ==1){
            Utils.cartList.remove(adapterPosition);
        }else {
            Utils.cartList.get(adapterPosition).setAmount(Utils.cartList.get(adapterPosition).getAmount() - 1);
        }
        Paper.book().write("cart", Utils.cartList);

    }

    private void addToCart(int adapterPosition) {
        Utils.cartList.get(adapterPosition).setAmount(Utils.cartList.get(adapterPosition).getAmount() + 1);
        Paper.book().write("cart", Utils.cartList);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemCartBinding binding;

        public MyViewHolder( ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
