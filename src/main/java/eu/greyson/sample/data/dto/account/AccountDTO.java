package eu.greyson.sample.data.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;

    @NotBlank(message = "IBAN is mandatory")
    private String iban;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;

    @NotBlank(message = "Currency is mandatory")
    private String currency;


    private Long userId;
}
