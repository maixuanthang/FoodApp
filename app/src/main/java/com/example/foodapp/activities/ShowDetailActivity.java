package com.example.foodapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.Utils.Utils;
import com.example.foodapp.databinding.ActivityShowDetailBinding;
import com.example.foodapp.models.Cart;
import com.example.foodapp.models.MealDetail;
import com.example.foodapp.viewModel.ShowDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ShowDetailActivity extends AppCompatActivity {
    ShowDetailViewModel viewModel;
    ActivityShowDetailBinding binding;
    int amount = 1;
    MealDetail mealDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_show_detail);
        Paper.init(this);
        int id = getIntent().getIntExtra("id", 0);
        getData(id);
        eventClick();
        showToData(id);
    }

    private void showToData(int id) {
        if (Paper.book().read("cart") !=null){
            List<Cart> list = Paper.book().read("cart");
            Utils.cartList = list;
        }
        if (Utils.cartList.size()>0){
            for (int i = 0; i<Utils.cartList.size(); i++ ){
                if (Utils.cartList.get(i).getMealDetail().getId() == id) {
                    binding.txtamount.setText(Utils.cartList.get(i).getAmount() +"");

                   }
                }

        } else {
            binding.txtamount.setText(amount + "");
        }
    }

    private void eventClick() {
        binding.imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(binding.txtamount.getText().toString()) + 1;
                binding.txtamount.setText(String.valueOf(amount));
            }
        });
        binding.imagesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(binding.txtamount.getText().toString()) > 1){
                    amount = Integer.parseInt(binding.txtamount.getText().toString()) - 1;
                    binding.txtamount.setText(String.valueOf(amount));
                }
            }
        });
        binding.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(amount);
            }
        });

    }

    private void addToCart(int amount) {
        boolean checkExit = false;
        int n = 0;
        if (Utils.cartList != null){
            if (Utils.cartList.size()>0){
                for (int i = 0; i<Utils.cartList.size(); i++ ){
                    if (Utils.cartList.get(i).getMealDetail().getId() == mealDetail.getId()){
                        checkExit = true;
                        n=i;
                        break;
                    }
                }
            }
        } else{
            Utils.cartList = new ArrayList<>();
        }

        if (checkExit){
            Utils.cartList.get(n).setAmount(amount);
        }else {
            Cart cart = new Cart();
            cart.setMealDetail(mealDetail);
            cart.setAmount(amount);
            Utils.cartList.add(cart);
        }
        Toast.makeText(getApplicationContext(), "Adder to your cart", Toast.LENGTH_LONG).show();
        Paper.book().write("cart", Utils.cartList);
    }


    private void getData(int id) {

        viewModel = new ViewModelProvider(this).get(ShowDetailViewModel.class);
        viewModel.mealDetailModelMutableLiveData(id).observe(this,mealDetailModel -> {
            if (mealDetailModel.isSuccess()){
                mealDetail = mealDetailModel.getResult().get(0);
                Log.d("logg",mealDetailModel.getResult().get(0).getMeal());
                binding.txtnamefood.setText(mealDetail.getMeal());
                binding.txtprice.setText("$ "+mealDetail.getPrice());
                binding.txtdesc.setText(mealDetail.getInstructions());
                Glide.with(this).load(mealDetail.getStrmealthumb()).into(binding.image);
            }
        });
    }
}