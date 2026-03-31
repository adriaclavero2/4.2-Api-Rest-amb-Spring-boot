package cat.itacademy.s04.t02.n01.fruit.serviceImpl;

import cat.itacademy.s04.t02.n01.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit.dtos.FruitDTO;
import cat.itacademy.s04.t02.n01.fruit.mappers.FruitMapper;
import cat.itacademy.s04.t02.n01.fruit.model.Provider;
import cat.itacademy.s04.t02.n01.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n01.fruit.repository.ProviderRepository;
import cat.itacademy.s04.t02.n01.fruit.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;
    private final FruitMapper fruitMapper;
    private final ProviderRepository providerRepository;

    public FruitServiceImpl(ProviderRepository providerRepository, FruitRepository fruitRepository, FruitMapper fruitMapper) {
        this.providerRepository = providerRepository;
        this.fruitRepository = fruitRepository;
        this.fruitMapper = fruitMapper;
    }

    @Override
    public FruitDTO create(FruitDTO fruitDTO) {
        Provider provider = providerRepository.findById(fruitDTO.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + fruitDTO.getProviderId()));

        Fruit fruit = fruitMapper.mapToEntity(fruitDTO);
        fruit.setProvider(provider);

        return fruitMapper.mapToDTO(fruitRepository.save(fruit));
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
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found"));
        return fruitMapper.mapToDTO(fruit);
    }

    @Override
    public void delete(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));
        fruitRepository.delete(fruit);
    }

    @Override
    public FruitDTO update(Long id, FruitDTO fruitDTO) {
        Fruit existingFruit = fruitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id: " + id));

        Provider provider = providerRepository.findById(fruitDTO.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + fruitDTO.getProviderId()));

        existingFruit.setName(fruitDTO.getName());
        existingFruit.setWeightInKilos(fruitDTO.getWeightInKilos());
        existingFruit.setProvider(provider);

        Fruit savedFruit = fruitRepository.save(existingFruit);

        return fruitMapper.mapToDTO(savedFruit);
    }

    @Override
    public List<FruitDTO> findByProviderId(Long providerId) {
        List<Fruit> fruits = fruitRepository.findByProviderId(providerId);

        return fruits.stream()
                .map(fruitMapper::mapToDTO)
                .toList();
    }
}