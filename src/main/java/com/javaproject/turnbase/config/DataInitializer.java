package com.javaproject.turnbase.config;

import com.javaproject.turnbase.service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MonsterService monsterService;

    @Override
    public void run(String... args) throws Exception {
        monsterService.saveMonstersToDatabase();
    }
}
