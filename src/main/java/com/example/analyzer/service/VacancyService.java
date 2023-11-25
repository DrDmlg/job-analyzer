package com.example.analyzer.service;

import com.example.analyzer.model.Vacancy;
import com.example.analyzer.repository.VacancyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        String apiUrl =  "https://api.hh.ru/vacancies";

        String fullUrl = buildFullUrlUsingParameters(apiUrl, position, depth);
        String jsonData = requestDataFromApi(fullUrl, String.class);
        JsonNode jsonTree = getJsonTree(jsonData);
        JsonNode jsonItemsNodeArray = getArrayNodeItemsFromTree(jsonTree, "items");

        String alternateUrl = getSpecificNode(jsonItemsNodeArray, "alternate_url");
        Set<String> vacancyId = extractVacancyId(alternateUrl);

        List<String> specificVacancyUrl = buildFullUrlSpecificVacancy("https://api.hh.ru/vacancies/", vacancyId);
        List<String> strings = requestDataSpecificVacancy(specificVacancyUrl);

        String specificNode = getSpecificNode(strings, "key_skills");

        return sortAndConvertToMap(specificNode);
    }

    private String buildFullUrlUsingParameters(String apiUrl, String position, int depth) {
        return apiUrl + "?" + String.format("text=%s&per_page=%d",
                position.replaceAll(" ", "+"),
                depth);
    }

    private <T> T requestDataFromApi(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    private JsonNode getJsonTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    private JsonNode getArrayNodeItemsFromTree(JsonNode jsonTree, String fieldName) {
        return jsonTree.get(fieldName);
    }

    private String getSpecificNode(JsonNode jsonArray, String specificNode) {
        StringJoiner joiner = new StringJoiner(", ");
        for (JsonNode item : jsonArray) {
            JsonNode node = item.get(specificNode);
            if (node != null) {
                joiner.add(node.asText());
            }
        }
        return joiner.toString().trim();
    }

    private Set<String> extractVacancyId(String urls) {
        return Arrays.stream(urls.split(", "))
                .map(url -> url.substring(url.lastIndexOf("/") + 1).trim())
                .collect(Collectors.toSet());
    }

    private List<String> buildFullUrlSpecificVacancy(String url, Set<String> vacancyId) {
        return vacancyId.stream()
                .map(id -> url.concat(id))
                .collect(Collectors.toList());
    }

    private List<String> requestDataSpecificVacancy(List<String> specificVacancyUrl) {
        return specificVacancyUrl.stream()
                .map(url -> requestDataFromApi(url, String.class))
                .toList();
    }

    private String getSpecificNode(List<String> jsonArray, String specificNode) {
        JsonNode jsonNodeName = null;
        StringJoiner joiner = new StringJoiner(", ");
        for (String item : jsonArray) {
            jsonNodeName = getJsonTree(item).get(specificNode);
            for (JsonNode key : jsonNodeName) {
               joiner.add(key.get("name").asText());
            }
        }
        return joiner.toString().trim();
    }

    private LinkedHashMap<String, Long> sortAndConvertToMap(String stringMap) {
        return Stream.of(stringMap.split(", "))
                .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}