package com.example.analyzer.service;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.repository.VacancyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public void createVacancy(Vacancy vacancy) {
        vacancyRepository.save(vacancy);
    }

    public List<String> analyzeTechnologies(String vacancyUrl) {
        log.debug(vacancyUrl);
        //:TODO
        return Arrays.asList("Java", "Spring", "Hibernate");
    }
}