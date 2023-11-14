package com.example.analyzer.controller;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.service.VacancyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSOutput;

import java.util.List;


@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @Autowired
    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllVacancies() {
        List<Vacancy> vacancies = vacancyService.getAllVacancies();
        return new ResponseEntity<>(vacancies, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
        vacancyService.createVacancy(vacancy);
        return new ResponseEntity<>(vacancy, HttpStatus.CREATED);
    }

    @PostMapping("/analyze")
    public ResponseEntity<List<String>> analyzeTechnologies(@RequestParam String vacancyUrl) {
            //:TODO
        List<String> technologies = vacancyService.analyzeTechnologies(vacancyUrl);
        technologies.forEach(System.out::println);
        return new ResponseEntity<>(technologies, HttpStatus.OK);
    }
}
