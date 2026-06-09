package app.controllers;

import app.services.BackFillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/backfill")
@RequiredArgsConstructor
public class BackfillController {
    private final BackFillService backFillService;
    @PostMapping
    public void backfill(){
        backFillService.backFillReadModel();
    }

}
