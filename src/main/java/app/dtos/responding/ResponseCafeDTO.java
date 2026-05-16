package app.dtos.responding;

public record ResponseCafeDTO(Long id, String name, String address, Boolean hasWifi, Boolean allowsPets,
                              Boolean sellsFood, Boolean allowsStudentsDiscounts, Boolean isLgbtqFriendly, Boolean hasTerrace, Boolean allowsIntake,
                              Boolean allowsTakeaway, Boolean supportsCardPayments) {
}
