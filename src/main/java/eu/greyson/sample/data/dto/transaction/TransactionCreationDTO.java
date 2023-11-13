package eu.greyson.sample.data.dto.transaction;


import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class TransactionCreationDTO {
    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Debtor is mandatory")
    @Size(max = 100, message = "Debtor name must be up to 100 characters long")
    private String debtor;
}
