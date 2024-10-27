package jpabook.jpashop.service;

import jpabook.jpashop.domian.Category;
import jpabook.jpashop.domian.Item;
import jpabook.jpashop.exception.IdNotFoundException;
import jpabook.jpashop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 등록
     * @param
     */
    @Transactional
    public Long saveCategory(Category category) {
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = findCategoryOrThrow(categoryId);
        categoryRepository.softDelete(categoryId);
    }

    @Transactional
    public void restoreCategory(Long categoryId) {
        Category category = findCategoryOrThrow(categoryId);
        categoryRepository.restore(categoryId);
    }

    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    public Category findOne(Long categoryId) {
        return categoryRepository.findOne(categoryId);
    }

    private Category findCategoryOrThrow(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) {
            throw new IdNotFoundException(String.format("해당 아이디가 존재하지 않습니다. (id: %d)", categoryId));
        }
        return category;
    }
}
