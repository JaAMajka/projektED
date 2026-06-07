package app.services;

import app.BeverageType;
import app.models.GlobalStats;
import app.projections.AvgPriceProjection;
import app.repositories.GlobalStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class GlobalStatsService {
    private final GlobalStatsRepository globalStatsRepository;

    public void updatePriceAverages(List<AvgPriceProjection> avgPricePerTypeGlobal){
        GlobalStats globalStats = globalStatsRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new GlobalStats());
        Map<BeverageType, BigDecimal> pricesMap = avgPricePerTypeGlobal.stream()
                        .collect(Collectors.toMap(
                                AvgPriceProjection::getType,
                                AvgPriceProjection::getAvgPrice
                        ));

        globalStats.setAvgPriceBaggedTea(pricesMap.getOrDefault(BeverageType.BAGGED_TEA, null));
        globalStats.setAvgPriceOther(pricesMap.getOrDefault(BeverageType.OTHER, null));
        globalStats.setAvgPriceCoffeeDrinks(pricesMap.getOrDefault(BeverageType.COFFEE_DRINKS, null));
        globalStats.setAvgPriceLeafTea(pricesMap.getOrDefault(BeverageType.LEAF_TEA, null));
        globalStats.setAvgPriceFruityDrink(pricesMap.getOrDefault(BeverageType.FRUITY_DRINK, null));
        globalStats.setAvgPricePureCoffee(pricesMap.getOrDefault(BeverageType.PURE_COFFEE, null));
        globalStats.setAvgPriceMatcha(pricesMap.getOrDefault(BeverageType.MATCHA, null));
        globalStatsRepository.save(globalStats);

    }
}
