package com.example.foodapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.foodapp.R;
import com.example.foodapp.adapters.CategoryAdapter;
import com.example.foodapp.adapters.PopularAdapter;
import com.example.foodapp.databinding.ActivityHomeBinding;
import com.example.foodapp.listener.CategoryListener;
import com.example.foodapp.listener.EventClickListener;
import com.example.foodapp.models.Category;
import com.example.foodapp.models.Meals;
import com.example.foodapp.viewModel.HomeViewModel;

public class HomeActivity extends AppCompatActivity implements CategoryListener, EventClickListener {
    HomeViewModel homeViewModel;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initView();
        initData();
        

    }

    private void initView() {
        binding.rcCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false );
        binding.rcCategory.setLayoutManager(layoutManager);

        binding.rcPopular.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,3);
        binding.rcPopular.setLayoutManager(layoutManager1);

        binding.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
    }

    private void initData() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.categoryModelMutableLiveData().observe(this, categoryModel ->{
            if (categoryModel.isSuccess()){
                CategoryAdapter adapter = new CategoryAdapter(categoryModel.getResult(), this);
                binding.rcCategory.setAdapter(adapter);
            }
        } );
        homeViewModel.mealModelMutableLiveData(1).observe(this,mealModel ->{
            if (mealModel.isSuccess()){
                PopularAdapter adapter = new PopularAdapter(mealModel.getResult(), this);
                binding.rcPopular.setAdapter(adapter);
            }
        } );
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        intent.putExtra("idcate", category.getId());
        intent.putExtra("namecate", category.getCategory());
        startActivity(intent);
    }

    @Override
    public void onPopularClick(Meals meals) {
        Intent intent = new Intent(getApplicationContext(), ShowDetailActivity.class);
        intent.putExtra("id", meals.getIdMeal());
        startActivity(intent);

    }
}