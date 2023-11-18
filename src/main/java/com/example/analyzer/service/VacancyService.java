package com.example.analyzer.service;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.repository.VacancyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public VacancyService(VacancyRepository vacancyRepository, RestTemplate restTemplate) {
        this.vacancyRepository = vacancyRepository;
        this.restTemplate = restTemplate;
    }

    public List<Vacancy> getAllVacancies() {
        //TODO
        return vacancyRepository.findAll();
    }

    public void createVacancy(Vacancy vacancy) {
        //TODO
        vacancyRepository.save(vacancy);
    }

    public List<String> analyzeTechnologies(String vacancyUrl) {
        log.debug(vacancyUrl);
        //TODO
        return Arrays.asList("Java", "Spring", "Hibernate");
    }

    public String getKeySkillsFromVacancy(String vacancyId) {
        try {
            String id = vacancyId.substring(vacancyId.lastIndexOf("/") + 1).trim();
            String apiUrl = "https://api.hh.ru/vacancies/" + id;

            String jsonString = restTemplate.getForObject(apiUrl, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode keySkillsNode = jsonNode.get("key_skills");

            if (keySkillsNode.isArray()) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                for (JsonNode key : keySkillsNode) {
                    stringJoiner.add(key.get("name").asText());
                }
                return stringJoiner.toString();
            } else {
                return keySkillsNode.asText();
            }
        } catch (Exception e) {
            log.debug("Error while extracting vacancy  details form {}: {}", vacancyId, e.getMessage());
            //:TODO
            return null;
        }
    }
}


