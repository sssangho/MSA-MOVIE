package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "MSA 프로젝트에 오신 것을 환영합니다");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "로그인");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "회원 가입");
        return "register";
    }

    @GetMapping("/movies")
    public String products(Model model) {
        model.addAttribute("message", "영화 목록");
        return "movies";
    }

    @GetMapping("/reviews")
    public String orders(Model model) {
        model.addAttribute("message", "리뷰 목록");
        return "reviews";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "admin";
    }

    @GetMapping("/customers")
    public String customer(Model model) {
        model.addAttribute("message", "고객 목록");
        return "customers";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }
} 