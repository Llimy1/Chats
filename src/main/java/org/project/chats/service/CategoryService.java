package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import org.project.chats.domain.Category;
import org.project.chats.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(String type) {
        Category category = Category.createCategory(type);

        categoryRepository.save(category);
    }
}
