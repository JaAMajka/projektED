package app.services;

import app.BeverageType;
import app.models.CafeReadModel;
import app.projections.AvgPriceProjection;
import app.repositories.CafeReadModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CafeReadModelService {
    private final CafeReadModelRepository cafeReadModelRepository;

    void updatePriceData(Long cafeId, Map<BeverageType, BigDecimal> avgPrices) {
        CafeReadModel cafeReadModel = cafeReadModelRepository.findByCafeId(cafeId).orElseThrow();
        cafeReadModel.setAvgBaggedTeaPrice(avgPrices.getOrDefault(BeverageType.BAGGED_TEA, null));
        cafeReadModel.setAvgCoffeePrice(avgPrices.getOrDefault(BeverageType.PURE_COFFEE, null));
        cafeReadModel.setAvgMatchaPrice(avgPrices.getOrDefault(BeverageType.MATCHA, null));
        cafeReadModel.setAvgFruityDrinkPrice(avgPrices.getOrDefault(BeverageType.FRUITY_DRINK, null));
        cafeReadModel.setAvgLeafTeaPrice(avgPrices.getOrDefault(BeverageType.LEAF_TEA, null));
        cafeReadModel.setAvgWhiteCoffeePrice(avgPrices.getOrDefault(BeverageType.COFFEE_DRINKS, null));
        cafeReadModel.setAvgOtherPrice(avgPrices.getOrDefault(BeverageType.OTHER, null));
        cafeReadModelRepository.save(cafeReadModel);
    }

    public void recalculatePriceAverages(Long cafeId, List<AvgPriceProjection> averages) {
        Map<BeverageType, BigDecimal> avgPrices = averages.stream()
                .collect(Collectors.toMap(
                        AvgPriceProjection::getType,
                        AvgPriceProjection::getAvgPrice
                ));
        updatePriceData(cafeId, avgPrices);
    }


    public void recalculateRateData(
            Long cafeId, BigDecimal avgAtmosphere,
            BigDecimal avgBeverage,
            BigDecimal avgService,
            BigDecimal stdAtmosphere,
            BigDecimal stdBeverage,
            BigDecimal stdService
    )
    {
        CafeReadModel cafeReadModel = cafeReadModelRepository.findByCafeId(cafeId).orElseThrow();
        cafeReadModel.setAvgAtmosphereScore(avgAtmosphere);
        cafeReadModel.setAvgBeverageScore(avgBeverage);
        cafeReadModel.setAvgServiceScore(avgService);
        cafeReadModel.setStdDevAtmosphere(stdAtmosphere);
        cafeReadModel.setStdDevBeverage(stdBeverage);
        cafeReadModel.setStdDevService(stdService);
        cafeReadModelRepository.save(cafeReadModel);
    }

    public void updateHasIcedPropertyIfIcedItemAdded(Long cafeId, Boolean hasIcedItems){
        CafeReadModel cafeReadModel = cafeReadModelRepository.findByCafeId(cafeId).orElseThrow();
        if(hasIcedItems){
            cafeReadModel.setHasIcedItems(Boolean.TRUE);
        }

        cafeReadModelRepository.save(cafeReadModel);
    }


}
