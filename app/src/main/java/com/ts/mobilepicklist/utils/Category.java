package com.ts.mobilepicklist.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts250231 on 2015-07-08.
 */
public class Category{
    int CategoryID;
    int uniqueID;
    String name;
    boolean isSelected = false;
    List<Category> subCategory = new ArrayList<Category>();

    public Category(int categoryID){
        this.CategoryID = categoryID;
    }

}