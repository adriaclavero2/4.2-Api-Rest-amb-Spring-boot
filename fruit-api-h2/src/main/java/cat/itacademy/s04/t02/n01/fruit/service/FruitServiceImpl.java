package cat.itacademy.s04.t02.n01.fruit.service;

import cat.itacademy.s04.t02.n01.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit.model.FruitDTO;
import cat.itacademy.s04.t02.n01.fruit.model.FruitMapper;
import cat.itacademy.s04.t02.n01.fruit.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class FruitServiceImpl implements FruitService {

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private FruitMapper fruitMapper;

    @Override
    public FruitDTO create(FruitDTO fruitDTO) {
        Fruit fruit = fruitMapper.mapToEntity(fruitDTO);

        Fruit savedFruit = fruitRepository.save(fruit);

        return fruitMapper.mapToDTO(savedFruit);
    }

    @Override
    public List<FruitDTO> getAll() {
        return fruitRepository.findAll()
                .stream()
                .map(fruitMapper::mapToDTO) // Mucho más legible
                .collect(Collectors.toList());
    }

    @Override
    public FruitDTO getById(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit not found"));
        return fruitMapper.mapToDTO(fruit);
    }

    @Override
    public void delete(Long id) {
        fruitRepository.findById(id)
                .ifPresentOrElse(
                        fruit -> fruitRepository.delete(fruit),
                        () -> { throw new FruitNotFoundException("Id not found: " + id);}
                );
    }

    @Override
    public FruitDTO update(Long id, FruitDTO fruitDTO) {
        Fruit existingFruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit not found with id: " + id));
        existingFruit.setName(fruitDTO.getName());
        existingFruit.setWeightInKilos(fruitDTO.getWeightInKilos());

        Fruit savedFruit = fruitRepository.save(existingFruit);

        return fruitMapper.mapToDTO(savedFruit);
    }
}