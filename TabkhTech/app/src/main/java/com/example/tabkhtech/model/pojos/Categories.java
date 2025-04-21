package com.example.tabkhtech.model.pojos;

import java.util.List;

public class Categories {
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }


    private class Category {
        private String strCategory;
        private String strCategoryThumb;

        public String getStrCategoryDescription() {
            return strCategoryDescription;
        }

        public void setStrCategoryDescription(String strCategoryDescription) {
            this.strCategoryDescription = strCategoryDescription;
        }

        private String strCategoryDescription;

        public Category(String strCategory, String strCategoryThumb, String strCategoryDescription) {
            this.strCategory = strCategory;
            this.strCategoryThumb = strCategoryThumb;
            this.strCategoryDescription = strCategoryDescription;
        }

        public String getStrCategory() {
            return strCategory;
        }

        @Override
        public String toString() {
            return "Category{" +
                    "strCategory='" + strCategory + '\'' +
                    ", strCategoryThumb='" + strCategoryThumb + '\'' +
                    '}';
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrCategoryThumb() {
            return strCategoryThumb;
        }

        public void setStrCategoryThumb(String strCategoryThumb) {
            this.strCategoryThumb = strCategoryThumb;
        }
    }

}
