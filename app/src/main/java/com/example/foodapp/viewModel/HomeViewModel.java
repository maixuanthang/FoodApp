package com.example.foodapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.models.CategoryModel;
import com.example.foodapp.models.MealModel;
import com.example.foodapp.repository.CategoryRepository;
import com.example.foodapp.repository.MealRepository;

public class HomeViewModel extends ViewModel {
    private CategoryRepository categoryRepository;
    private MealRepository mealRepository;

    public HomeViewModel() {
        categoryRepository = new CategoryRepository();
        mealRepository = new MealRepository();
    }
    public MutableLiveData<CategoryModel> categoryModelMutableLiveData(){
        return categoryRepository.getCategory();
    }

    public MutableLiveData<MealModel> mealModelMutableLiveData(int idcate){
        return mealRepository.getMeals(idcate);
    }
}
