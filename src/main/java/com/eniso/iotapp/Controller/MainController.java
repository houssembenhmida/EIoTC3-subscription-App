package com.eniso.iotapp.Controller;

import com.eniso.iotapp.Entity.Industrial;
import com.eniso.iotapp.Entity.Student;
import com.eniso.iotapp.Entity.Workshop;
import com.eniso.iotapp.Repository.IndusRepository;
import com.eniso.iotapp.Repository.StudentRepository;
import com.eniso.iotapp.Util.ExcelWriter;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(path = "/")
public class MainController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private IndusRepository indusRepository;

    @PostMapping(path = "/addStudent")
    public String addStudent(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String phoneNumber,
                             @RequestParam String email,
                             @RequestParam String establishment,
                             @RequestParam Workshop workshop) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setPhoneNumber(phoneNumber);
        student.setEmail(email);
        student.setEstablishment(establishment);
        student.setWorkshop(workshop);
        studentRepository.save(student);
        return "redirect:/";
    }

    @PostMapping(path = "/addIndus")
    public String addIndus(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String society) {
        Industrial industrial = new Industrial();
        industrial.setFirstName(firstName);
        industrial.setLastName(lastName);
        industrial.setEmail(email);
        industrial.setSociety(society);
        indusRepository.save(industrial);
        return "redirect:/";
    }

    @GetMapping(path = "/student")
    public String studentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student";
    }

    @GetMapping(path = "/indus")
    public String indusForm(Model model) {
        model.addAttribute("indus", new Industrial());
        return "indus";
    }

    @GetMapping(path = "/getFiles")
    public @ResponseBody
    String getFiles() {
        ExcelWriter.backupFiles();
        List<Student> students = (List<Student>) studentRepository.findAll();
        Map<Workshop, List<Student>> map = new HashedMap<>();
        for (Workshop workshop : Workshop.values()) {
            map.put(workshop, new ArrayList<>());
        }

        for (Student student : students) {
            if (map.containsKey(student.getWorkshop())) {
                map.get(student.getWorkshop()).add(student);
            }
        }

        for (Workshop workshop : Workshop.values()) {
            if (!map.get(workshop).isEmpty()) {
                ExcelWriter excelWriter = new ExcelWriter(map.get(workshop), workshop.getFullName(), workshop.getFullName());
                excelWriter.writeToFile();
            }
        }
        return "Files are saved under /files folder.";
    }

    @GetMapping(path = "/")
    public String main(Model model) {
        return "main";
    }
}
