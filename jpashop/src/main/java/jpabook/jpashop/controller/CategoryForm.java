package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryForm {
    private Long id;
    private String name;
    private String description;

    public static CategoryForm createCategoryForm(Long id, String name, String description) {
        CategoryForm form = new CategoryForm();
        form.id = id;
        form.name = name;
        form.description = description;
        return form;
    }
}
