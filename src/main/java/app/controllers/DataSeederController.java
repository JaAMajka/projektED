package app.controllers;

import app.services.DataSeederService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/seed")
@RequiredArgsConstructor
public class DataSeederController {
    private final DataSeederService dataSeederService;
    @PostMapping
    void seedUsers(){
        dataSeederService.seedUsers();
    }
}
