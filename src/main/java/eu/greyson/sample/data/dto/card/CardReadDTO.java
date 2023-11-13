package eu.greyson.sample.data.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardReadDTO {
    private Long id;

    @NotBlank(message = "Card name is mandatory")
    private String name;

    @NotNull(message = "Blocked status must be specified")
    private Boolean blocked;

    private LocalDate dateLocked;
    
}
