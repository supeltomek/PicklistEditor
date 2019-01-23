package com.ts.mobilepicklist.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ts250231 on 2015-07-01.
 */
public class CategoryList {


    public List<Category> categoryList = new ArrayList<Category>();
    private List<String> categoryNamesList = new ArrayList<String>();
    private final AtomicInteger counter = new AtomicInteger();

    private String selectedCategoryNames = "";
    private String selectedCategoryIds = "";


    public CategoryList(String categoryString){
        String categoryNames = categoryString.split("#")[0];
        readCategoryNames(categoryNames);
        if(categoryString.split("#").length > 1){
            String categoryTreeString = categoryString.split("#")[1];
            prepareCategoryTreeSimple(categoryList, categoryTreeString);
            setUniqueCategoryID(categoryList);
        }
    }



    private void readCategoryNames(String categoryNamesString){
        String[] categoriesWithID = categoryNamesString.split(";");
        for (String s : categoriesWithID){
            categoryNamesList.add(s.split(":")[1]);
        }
    }

    private String getCategoryNameByID(int categoryID){
        if(categoryNamesList.size() > 0){
            return categoryNamesList.get(categoryID-1);
        }
        return null;
    }


    public String getSelectedCategoryName(List<Category> categoryList){
        for(int i = 0; i<categoryList.size();i++){
            if(categoryList.get(i).isSelected){
                selectedCategoryNames += categoryList.get(i).name + ";";
            }
            if(categoryList.get(i).subCategory.size()>0){
                getSelectedCategoryName(categoryList.get(i).subCategory);
            }
        }

        return selectedCategoryNames;
    }

    public String getSelectedCategoryIds(List<Category> categoryList){
        for(int i = 0; i<categoryList.size();i++){
            if(categoryList.get(i).isSelected){
                selectedCategoryIds += Integer.toString(categoryList.get(i).CategoryID) + ";";
            }
            if(categoryList.get(i).subCategory.size()>0){
                getSelectedCategoryIds(categoryList.get(i).subCategory);
            }
        }

        return selectedCategoryIds;
    }

    private void prepareCategoryTreeSimple(List<Category> categoryList, String categoryString){
        categoryString = categoryString.replace(";", "");

        int categoryStringLenth = categoryString.length();
        for(int i = 0; i<categoryStringLenth; i++){
            if(categoryStringLenth> i + 2){
                if(categoryString.charAt(i+2) == '}'){
                    categoryList.add(new Category(Character.getNumericValue(categoryString.charAt(i))));
                    setNameForCategoryID(categoryList, Character.getNumericValue(categoryString.charAt(i)));
                    i=i+2;
                }
                else if(Character.isDigit(categoryString.charAt(i + 2))){
                    categoryList.add(new Category(Character.getNumericValue(categoryString.charAt(i))));
                    setNameForCategoryID(categoryList, Character.getNumericValue(categoryString.charAt(i)));
                    for(int j=0; j<categoryList.size(); j++){
                        if(categoryList.get(j).CategoryID == Character.getNumericValue(categoryString.charAt(i))){
                            prepareCategoryTreeSimple(categoryList.get(j).subCategory, getSubCat(i+2, categoryString));
                            String tmpStringToReplace = getSubCat(i+2, categoryString);
                            tmpStringToReplace = tmpStringToReplace.replace("{", "\\{");
                            tmpStringToReplace = tmpStringToReplace.replace("}", "\\}");
                            categoryString = categoryString.substring(0, i+2) + categoryString.substring(i+2).replaceFirst(tmpStringToReplace, "");
                            categoryStringLenth = categoryString.length();
                            i=i+2;
                        }
                    }
                }
            }
        }
    }

    private void setNameForCategoryID(List<Category> categoryList, int categoryID){
        for(int i = 0; i<categoryList.size(); i++){
            if(categoryList.get(i).CategoryID == categoryID) {
                categoryList.get(i).name = getCategoryNameByID(categoryID);
            }
        }
    }
    private String getSubCat(int beginPosition, String parentCat){
        String subCat = "";
        parentCat = parentCat.substring(beginPosition);
        int position = parentCat.indexOf("}}");
        for(int i=0;i<parentCat.length();i++) {
            if (parentCat.length() > position + 2) {
                if (Character.isDigit(parentCat.charAt(position + 2))) {
                    return parentCat.substring(0, position + 1);
                }
            } else if (parentCat.length() == position + 2) {
                return parentCat.substring(0, position + 1);
            }
            position++;
        }

        return subCat;
    }

    private void setUniqueCategoryID(List<Category> categoryList){
        try {
            for (int i = 0; i < categoryList.size(); i++) {
                categoryList.get(i).uniqueID = counter.incrementAndGet();
                if (categoryList.get(i).subCategory != null) {
                    setUniqueCategoryID(categoryList.get(i).subCategory);
                }
            }
        }catch (Exception ex){
            Log.d("CATEGORYLIST", ex.getMessage());
        }
    }

    public List<Category> getParentListByUniqIdChild(int uniqueChildId, List<Category> topLevelCategoryList){
        List<Category> parentList = new ArrayList<Category>();

        for(int i = 0; i<topLevelCategoryList.size();i++){
            if(topLevelCategoryList.get(i).uniqueID == uniqueChildId) {
                parentList = topLevelCategoryList;
                break;
            }
            if(topLevelCategoryList.get(i).subCategory != null){
                for(int j = 0; j<topLevelCategoryList.get(i).subCategory.size(); j++){
                    if(topLevelCategoryList.get(i).subCategory.get(j).uniqueID == uniqueChildId){
                        parentList = topLevelCategoryList;
                        return parentList;
                    }
                    else {
                        parentList = getParentListByUniqIdChild(uniqueChildId, topLevelCategoryList.get(i).subCategory);
                        if(parentList.size()>0){
                            return parentList;
                        }
                    }
                }
            }

        }

        return parentList;
    }

}