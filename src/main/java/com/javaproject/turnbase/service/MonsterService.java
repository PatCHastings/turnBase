package com.javaproject.turnbase.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.turnbase.entity.Monster;
import com.javaproject.turnbase.repository.MonsterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonsterService extends AbstractCharacterService<Monster> {

    private final RestTemplate restTemplate;

    @Autowired
    private MonsterRepository monsterRepository;

    @Autowired
    public MonsterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected JpaRepository<Monster, Long> getRepository() {
        return monsterRepository;
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
            List<JsonNode> monsterNodes = objectMapper.convertValue(rootNode.path("results"), new TypeReference<>(){});

            List<Monster> monsters = new ArrayList<>();

            for (JsonNode monsterNode : monsterNodes) {
                // Fetch additional details for the monster by using its index
                String index = monsterNode.get("index").asText();
                String monsterDetailsResponse = getMonsterDetails("/api/monsters/" + index);
                JsonNode monsterDetailsNode = objectMapper.readTree(monsterDetailsResponse);

                // Map the monster details into a Monster entity
                Monster monster = new Monster();
                monster.setIndex(monsterDetailsNode.get("index").asText());
                monster.setName(monsterDetailsNode.get("name").asText());
                monster.setHealth(monsterDetailsNode.get("hit_points").asInt());

                // Extract challengeRating and set it to the Monster entity
                if (monsterDetailsNode.has("challenge_rating")) {
                    monster.setChallengeRating(monsterDetailsNode.get("challenge_rating").asInt());
                }

                // You can also map other fields like actions, armorClass, etc., similar to how you did for enemy

                // Add the monster to the list
                monsters.add(monster);
            }

            // Save all the monsters to the repository
            monsterRepository.saveAll(monsters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Monster> getAllMonstersWithChallengeRating() {
        String jsonResponse = getMonstersFromAPI();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<Monster> allMonsters = objectMapper.convertValue(rootNode.path("results"), new TypeReference<>() {});
            // Fetch challenge ratings for all monsters
            for (Monster monster : allMonsters) {
                String monsterDetails = getMonsterDetails("/api/monsters/" + monster.getIndex());
                Monster detailedMonster = objectMapper.readValue(monsterDetails, Monster.class);
                monster.setChallengeRating(detailedMonster.getChallengeRating());
            }
            return allMonsters;
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public String getMonsterDetails(String endpoint) {
        String apiUrl = "https://www.dnd5eapi.co" + endpoint;
        return restTemplate.getForObject(apiUrl, String.class);
    }
    public String getMonsterDetailsRaw(String index) {
        String apiUrl = "https://www.dnd5eapi.co/api/monsters/" + index;
        return restTemplate.getForObject(apiUrl, String.class);
    }

    public Monster saveMonster(Monster monster) {
        return monsterRepository.save(monster);
    }

    public List<Monster> getAllFetchedMonsters() {
        return monsterRepository.findAll();
    }
}
