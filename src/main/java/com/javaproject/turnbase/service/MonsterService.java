package com.javaproject.turnbase.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.turnbase.entity.Monster;
import com.javaproject.turnbase.repository.MonsterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class MonsterService {

    private final RestTemplate restTemplate;

    @Autowired
    private MonsterRepository monsterRepository;

    public MonsterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getMonstersFromAPI() {
        String url = "https://www.dnd5eapi.co/api/monsters";
        return restTemplate.getForObject(url, String.class);
    }

    public void saveMonstersToDatabase() {
        String jsonResponse = getMonstersFromAPI();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<Monster> monsters = objectMapper.convertValue(rootNode.path("results"), new TypeReference<>(){});
            monsterRepository.saveAll(monsters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMonsterDetails(String endpoint) {
        String apiUrl = "https://www.dnd5eapi.co" + endpoint;
        return restTemplate.getForObject(apiUrl, String.class);
    }

    public Monster saveMonster(Monster monster) {
        return monsterRepository.save(monster);
    }

    public List<Monster> getAllFetchedMonsters() {
        return monsterRepository.findAll();
    }
}
