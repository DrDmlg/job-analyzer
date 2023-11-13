package com.example.analyzer.repository;

import com.example.analyzer.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

}
