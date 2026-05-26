package app.dtos.filtering;

public record FilterCafeDTO(
        Boolean hasWifi,
        Boolean allowsPets,
        Boolean sellsFood,
        Boolean allowsStudentsDiscounts,
        Boolean isLgbtqFriendly,
        Boolean hasToilet,
        Boolean hasTerrace,
        Boolean allowsIntake,
        Boolean allowsTakeaway,
        Boolean supportsCardPayments
) {
}
