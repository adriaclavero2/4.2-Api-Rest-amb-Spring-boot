package cat.itacademy.s04.t02.n01.fruit.dtos;

import jakarta.validation.constraints.NotBlank;

public class ProviderDTO {

    private Long id;

    @NotBlank(message = "Provider name is required and cannot be empty.")
    private String name;

    @NotBlank(message = "Country is required.")
    private String country;

    public ProviderDTO() {
    }

    public ProviderDTO(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
