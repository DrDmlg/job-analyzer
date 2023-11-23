package com.example.analyzer.service;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.repository.VacancyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void createVacancy(Vacancy vacancy) {
        //TODO
        vacancyRepository.save(vacancy);
    }

    public Map<String, Long> getKeySkillsFromVacancy(String position, int depth) {
        Set<String> alternateUrls = new HashSet<>();

        String fullUrl = "https://api.hh.ru/vacancies" + "?" + String.format("text=%s&per_page=%d",
                position.replaceAll(" ", "+"),
                depth);

        try {
            String jsonString = restTemplate.getForObject(fullUrl, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(jsonString);

            JsonNode itemsNode = jsonNode.get("items");

            for (JsonNode item : itemsNode) {
                JsonNode alternateUrl = item.get("alternate_url");
                if (alternateUrl != null) {
                    String text = alternateUrl.asText();
                    String substring = text.substring(text.lastIndexOf("/") + 1).trim();
                    alternateUrls.add(substring);
                }
            }

            String apiUrl = "https://api.hh.ru/vacancies/";

            JsonNode keySkillsNode = null;

            StringJoiner stringJoiner = new StringJoiner(", ");
            for (String t : alternateUrls) {
                String s = apiUrl + t;
                String forObject = restTemplate.getForObject(s, String.class);
                JsonNode jsonNode1 = objectMapper.readTree(forObject);
                keySkillsNode = jsonNode1.get("key_skills");
                for (JsonNode key : keySkillsNode) {
                    stringJoiner.add(key.get("name").asText());
                }
            }

            String stringMap = stringJoiner.toString();

            return Stream.of(stringMap.split(", "))
                    .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));

        } catch (Exception e) {
            log.debug("Error");
        }

        return null;
    }
}


