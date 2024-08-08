package com.javaproject.turnbase.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.turnbase.entity.Class;
import com.javaproject.turnbase.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    private RestTemplate restTemplate = new RestTemplate();

    public List<Class> fetchClassesFromAPI() {
        String url = "https://www.dnd5eapi.co/api/classes";
        ClassResponse response = restTemplate.getForObject(url, ClassResponse.class);
        if (response != null && response.getResults() != null) {
            List<Class> classes = response.getResults();
            for (Class characterClass : classes) {
                fetchAndSetClassDetails(characterClass);
                classRepository.save(characterClass);
            }
            return classes;
        } else {
            return List.of();
        }
    }

    private void fetchAndSetClassDetails(Class characterClass) {
        String url = "https://www.dnd5eapi.co/api/classes/" + characterClass.getIndex();
        String rawDetails = restTemplate.getForObject(url, String.class);
        parseAndSetClassDetails(characterClass, rawDetails);
    }

    private void parseAndSetClassDetails(Class characterClass, String rawDetails) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(rawDetails);

            // Set hit die
            characterClass.setHitDie(rootNode.path("hit_die").asInt());

            // Set proficiencies
            List<Class.Proficiency> proficiencies = mapper.convertValue(
                    rootNode.path("proficiencies"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Class.Proficiency.class)
            );
            characterClass.setProficiencies(proficiencies);

            // Set starting equipment,
            List<Class.StartingEquipment> startingEquipment = mapper.convertValue(
                    rootNode.path("starting_equipment"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Class.StartingEquipment.class)
            );
            characterClass.setStartingEquipment(startingEquipment);

            // Set subbclasses
            List<Class.Reference> subclasses = mapper.convertValue(
                    rootNode.path("subclasses"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Class.Reference.class)
            );
            characterClass.setSubclasses(subclasses);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    public Class getClassDetails(String classIndex) {
        String url = "https://www.dnd5eapi.co/api/classes/" + classIndex;
        Class classDetails = restTemplate.getForObject(url, Class.class);

        if (classDetails != null) {
            fetchAndSetClassDetails(classDetails);
            classRepository.save(classDetails);
        }

        return classDetails;
    }

    public String getClassDetailsRaw(String classIndex) {
        String url = "https://www.dnd5eapi.co/api/classes/" + classIndex;
        return restTemplate.getForObject(url, String.class);
    }

    public Class getClassById(String id) {
        return classRepository.findById(id).orElse(null);
    }

    public Class createClass(Class newClass) {
        return classRepository.save(newClass);
    }
}

class ClassResponse {
    private List<Class> results;

    public List<Class> getResults() {
        return results;
    }

    public void setResults(List<Class> results) {
        this.results = results;
    }
}
