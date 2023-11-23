package com.example.analyzer.controller;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.service.VacancyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/vacancy")
public class VacancyController {

    private final VacancyService vacancyService;

    @Autowired
    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping("/create-vacancy")
    public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
        vacancyService.createVacancy(vacancy);
        return new ResponseEntity<>(vacancy, HttpStatus.CREATED);
    }

    @GetMapping("/statistics")
    public String getKeySkillsFromVacancy(@RequestParam String position, @RequestParam int depth, Model model) {
        Map<String, Long> listWord = vacancyService.getKeySkillsFromVacancy(position, depth);
        model.addAttribute("outputText", listWord);
        return "statistics";
    }
}
