package app.filtering;

import java.math.BigDecimal;

import java.util.List;

public record CafeSearchCriteria(
        String name,
        String city,
        Boolean isStudentFriendly,
        Boolean isPetFriendly,
        Boolean hasWifi,
        Boolean hasTerrace,
        Boolean allowsIntake,
        Boolean allowsTakeaway,
        Boolean supportsCardPayments,
        Boolean isLgbtqFriendly,
        Boolean sellsFood,
        Boolean hasToilet,

        Boolean isOpenNow,
        List<String> targetOpenDays,


        String desiredBeverageType,
        Boolean requiresIcedOption,
        Boolean requiresAppendageOption,


        BigDecimal maxPriceForCoffee,
        BigDecimal maxPriceForTea,
        BigDecimal maxPriceForMatcha,
        BigDecimal maxPriceForOtherBeverage,


        Integer minAtmosphereScore,
        Integer minServiceScore,
        Integer minBeverageScore
) {
}
