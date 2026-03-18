package cat.itacademy.s04.t02.n01.fruit.model;

import org.springframework.stereotype.Component;

@Component
public class FruitMapper {

    public Fruit mapToEntity(FruitDTO dto) {
        Fruit entity = new Fruit();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setWeightInKilos(dto.getWeightInKilos());
        return entity;
    }

    public FruitDTO mapToDTO(Fruit fruit) {
        FruitDTO dto = new FruitDTO();
        dto.setId(fruit.getId());
        dto.setName(fruit.getName());
        dto.setWeightInKilos(fruit.getWeightInKilos());
        return dto;
    }
}