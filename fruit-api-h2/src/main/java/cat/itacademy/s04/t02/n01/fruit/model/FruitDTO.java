package cat.itacademy.s04.t02.n01.fruit.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FruitDTO {
    private Long id;

    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Positive(message = "Weight must be greater than 0")
    private double weightInKilos;
}
