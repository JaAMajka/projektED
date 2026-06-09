package app.services;

import app.BeverageType;
import app.projections.AvgPricePerCafeProjection;
import app.projections.GlobalStatsRateProjection;
import app.repositories.MenuItemRepository;
import app.repositories.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackFillService {
    private final MenuItemRepository menuItemRepository;
    private final RateRepository rateRepository;
    private final CafeReadModelService cafeReadModelService;


    public void backFillReadModel(){
        List<GlobalStatsRateProjection> rates = rateRepository.findStatsForAllRates();
        rates.
                forEach(
                        (GlobalStatsRateProjection ->
                                cafeReadModelService.recalculateRateData(
                                        GlobalStatsRateProjection.getCafeId(),
                                        GlobalStatsRateProjection.getAvgAtmosphereScore(),
                                        GlobalStatsRateProjection.getAvgBeverageScore(),
                                        GlobalStatsRateProjection.getAvgServiceScore(),
                                        GlobalStatsRateProjection.getStdDevAtmosphere(),
                                        GlobalStatsRateProjection.getStdDevBeverage(),
                                        GlobalStatsRateProjection.getStdDevService()
                                )
                        )
                );
        List<AvgPricePerCafeProjection> prices = menuItemRepository.findAvgPricePerTypeForAllCafes();
        prices.stream()
                .collect(Collectors.groupingBy(AvgPricePerCafeProjection::getCafeId))
                .forEach((cafeId, projections) -> {
                    Map<BeverageType, BigDecimal> avgPrices = projections.stream()
                            .collect(Collectors.toMap(
                                    AvgPricePerCafeProjection::getType,
                                    AvgPricePerCafeProjection::getAvgPrice
                            ));
                    cafeReadModelService.updatePriceData(cafeId, avgPrices);
                });
    }



}
