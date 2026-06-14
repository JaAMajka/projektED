package app.controllers;

import app.projections.RateExportProjection;
import app.repositories.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/export")
@RequiredArgsConstructor
public class ExportController {
    private final RateRepository rateRepository;

    @GetMapping("/rates")
    public List<RateExportProjection> exportRates() {
        return rateRepository.findAllForExport();
    }
}
