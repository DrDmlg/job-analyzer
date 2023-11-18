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

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @Autowired
    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Vacancy>> getAllVacancies() {
        List<Vacancy> vacancies = vacancyService.getAllVacancies();
        return new ResponseEntity<>(vacancies, HttpStatus.OK);
    }

    @PostMapping("/create-vacancy")
    public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
        vacancyService.createVacancy(vacancy);
        return new ResponseEntity<>(vacancy, HttpStatus.CREATED);
    }

    @GetMapping("/analyze")
    public ResponseEntity<List<String>> analyzeTechnologies(@RequestParam String vacancyUrl) {
        //:TODO
        List<String> technologies = vacancyService.analyzeTechnologies(vacancyUrl);
        technologies.forEach(System.out::println);
        return new ResponseEntity<>(technologies, HttpStatus.OK);
    }

    @PostMapping("/get-skills")
    public String getKeySkillsFromVacancy(@RequestParam String inputText, Model model) {
        String vacancySkills = vacancyService.getKeySkillsFromVacancy(inputText);
        model.addAttribute("outputText", "Found by me: " + vacancySkills);
        return "result";
    }
}
