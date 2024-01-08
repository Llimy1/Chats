package org.project.shoppingmall.controller;

import org.project.shoppingmall.dto.request.SignupRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    @PostMapping("/signup")
    public void signup(SignupRequestDto signupRequestDto) {

    }

}
