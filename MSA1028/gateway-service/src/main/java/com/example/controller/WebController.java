package com.example.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "🎬 MSA 영화 리뷰 프로젝트에 오신 것을 환영합니다!");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "로그인");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "회원가입");
        return "register";
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("message", "영화 목록");
        return "movies";
    }

    @GetMapping("/reviews")
    public String reviews(Model model) {
        model.addAttribute("message", "리뷰 목록");
        return "reviews";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "admin";
    }

}
