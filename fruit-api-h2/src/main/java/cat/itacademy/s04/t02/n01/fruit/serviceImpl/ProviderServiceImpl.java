package cat.itacademy.s04.t02.n01.fruit.serviceImpl;

import cat.itacademy.s04.t02.n01.fruit.exception.ResourceAlreadyExistsException;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceConflictException;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.mappers.ProviderMapper;
import cat.itacademy.s04.t02.n01.fruit.model.Provider;
import cat.itacademy.s04.t02.n01.fruit.dtos.ProviderDTO;
import cat.itacademy.s04.t02.n01.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n01.fruit.repository.ProviderRepository;
import cat.itacademy.s04.t02.n01.fruit.service.ProviderService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;
    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(ProviderRepository providerRepository, FruitRepository fruitRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.fruitRepository = fruitRepository;
        this.providerMapper = providerMapper;
    }

    @Override
    public ProviderDTO create(ProviderDTO providerDTO) {
        if (providerRepository.existsByName(providerDTO.getName())) {
            throw new ResourceAlreadyExistsException("Name already exists.");
        }

        Provider provider = providerMapper.convertToEntity(providerDTO);
        Provider saved = providerRepository.save(provider);

        return providerMapper.convertToDTO(saved);
    }

    @Override
    public List<ProviderDTO> findAll() {
        List<Provider> providers = providerRepository.findAll();

        return providers.stream()
                .map(providerMapper::convertToDTO)
                .toList();
    }

    @Override
    public ProviderDTO findById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Provider not found with id: " + id));
        return providerMapper.convertToDTO(provider);
    }

    @Override
    public ProviderDTO update(Long id, ProviderDTO providerDTO) {
        Provider existingProvider = providerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Provider not found with id: " + id));

        if (!existingProvider.getName().equals(providerDTO.getName()) &&
             providerRepository.existsByName(providerDTO.getName())) {
            throw new ResourceAlreadyExistsException("The name: " + providerDTO.getName() + " is already in use.");
        }

        existingProvider.setName(providerDTO.getName());
        existingProvider.setCountry(providerDTO.getCountry());

        Provider updated = providerRepository.save(existingProvider);
        return providerMapper.convertToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Provider not found with id: " + id);
        }
        if (this.fruitRepository.existsByProviderId(id)) {
            throw new ResourceConflictException("Cannot delete provider: It has associated fruits.");
        }

        providerRepository.deleteById(id);
    }
}



