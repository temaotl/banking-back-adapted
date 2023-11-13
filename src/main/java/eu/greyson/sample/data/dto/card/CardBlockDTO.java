package eu.greyson.sample.data.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardBlockDTO {

    @NotNull(message = "Blocked status must be specified")
    private Boolean blocked;
}
