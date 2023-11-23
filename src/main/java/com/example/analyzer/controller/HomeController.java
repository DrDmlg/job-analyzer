package com.example.analyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "statistics";
    }

    @GetMapping("/resume-analysis")
    public String resumeAnalysis() {
        return "resume-analysis";
    }
}

