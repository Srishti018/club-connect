package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.entity.Registration;
import com.cbit.club_connect.clubconnect.Repository.RegistrationRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationRepository registrationRepository;

    public RegistrationController(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    @PostMapping
    public Registration createRegistration(@RequestBody Registration registration) {
        return registrationRepository.save(registration);
    }

    @GetMapping("/{id}")
    public Registration getRegistrationById(@PathVariable Long id) {
        return registrationRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistration(@PathVariable Long id) {
        registrationRepository.deleteById(id);
    }
}