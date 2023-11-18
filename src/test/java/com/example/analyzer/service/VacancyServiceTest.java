package com.example.analyzer.service;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.repository.VacancyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class VacancyServiceTest {

    @Test
    public void testGetAllVacancies() {

        VacancyRepository vacancyRepository = Mockito.mock(VacancyRepository.class);

        VacancyService vacancyService = new VacancyService(vacancyRepository);

        Vacancy vacancy = Vacancy.builder()
                .jobTitle("Software Engineer")
                .companyName("Example Company")
                .location("City")
                .salary(90000)
                .experience("2 years")
                .technologies(new HashSet<>(Arrays.asList("java", "Spring")))
                .build();

        List<Vacancy> mockVacancies = new ArrayList<>();
        mockVacancies.add(vacancy);

        Mockito.when(vacancyRepository.findAll()).thenReturn(mockVacancies);

        List<Vacancy> result = vacancyService.getAllVacancies();

        assertEquals(mockVacancies, result);
    }

    @Test
    public void testCreateVacancy() {
        VacancyRepository vacancyRepository = Mockito.mock(VacancyRepository.class);
        VacancyService vacancyService = new VacancyService(vacancyRepository);

        Vacancy vacancy = Vacancy.builder()
                .jobTitle("Software Engineer")
                .companyName("Example Company")
                .location("City")
                .salary(90000)
                .experience("2 years")
                .technologies(new HashSet<>(Arrays.asList("java", "Spring")))
                .build();

        vacancyService.createVacancy(vacancy);

        Mockito.verify(vacancyRepository, Mockito.times(1)).save(vacancy);
    }

}