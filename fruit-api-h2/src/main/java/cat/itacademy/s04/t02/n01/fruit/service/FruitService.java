package cat.itacademy.s04.t02.n01.fruit.service;

import cat.itacademy.s04.t02.n01.fruit.dtos.FruitDTO;

import java.util.List;

public interface FruitService {
    FruitDTO create(FruitDTO fruitDTO);

    List<FruitDTO> getAll();

    FruitDTO getById(Long id);

    void delete(Long id);

    FruitDTO update(Long id, FruitDTO fruitDTO);

    List<FruitDTO> findByProviderId(Long providerId);
}
