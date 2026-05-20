package app.dtos.updating;

import jakarta.validation.constraints.NotEmpty;


public record UpdateCafeDTO(
        @NotEmpty String name,
        @NotEmpty String address,
        Boolean hasWifi,
        Boolean allowsPets,
        Boolean sellsFood,
        Boolean allowsStudentsDiscounts,
        Boolean isLgbtqFriendly,
        Boolean hasTerrace,
        Boolean allowsIntake,
        Boolean allowsTakeaway,
        Boolean supportsCardPayments
) {
}
