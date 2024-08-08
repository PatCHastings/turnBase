package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Class;
import com.javaproject.turnbase.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "http://localhost:5174")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping("/fetch")
    public List<Class> fetchClassesFromAPI() {
        return classService.fetchClassesFromAPI();
    }

    @GetMapping
    public List<Class> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/{index}")
    public Class getClassByIndex(@PathVariable String index) {
        return classService.getClassById(index);
    }

    @GetMapping("/details/{classIndex}")
    public Class getClassDetails(@PathVariable String classIndex) {
        return classService.getClassDetails(classIndex);
    }

    @GetMapping("/raw/{classIndex}")
    public String getClassDetailsRaw(@PathVariable String classIndex) {
        return classService.getClassDetailsRaw(classIndex);
    }

    @PostMapping
    public Class createClass(@RequestBody Class newClass) {
        return classService.createClass(newClass);
    }
}
