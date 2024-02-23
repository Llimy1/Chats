package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import org.project.chats.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Object> createCategory(String type) {
        categoryService.createCategory(type);

        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }
}
