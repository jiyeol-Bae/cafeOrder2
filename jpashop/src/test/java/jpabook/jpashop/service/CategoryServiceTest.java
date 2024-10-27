package jpabook.jpashop.service;

import jpabook.jpashop.domian.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;

    @Test
    public void 카테고리저장_아이디리턴() throws Exception {
        // given
        Category category = Category.createCategory("테스트 카테고리", "테스트 설명");

        // when
        Long savedId = categoryService.saveCategory(category);

        // then
        Category foundCategory = categoryService.findOne(savedId);
        assertNotNull(foundCategory, "카테고리가 제대로 저장됐는지 확인");
        assertEquals("테스트 카테고리", foundCategory.getName(), "이름이 올바르게 저장됐는지 확인");
        assertEquals("테스트 설명", foundCategory.getDescription(), "설명이 올바르게 저장됐는지 확인");
        assertFalse(foundCategory.getIsDeleted(), "삭제상태가 아님을 확인");
    }
}