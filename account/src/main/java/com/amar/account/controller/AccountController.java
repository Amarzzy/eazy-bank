package com.amar.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    /**
     *
     * @return Sting
     */
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }
}
