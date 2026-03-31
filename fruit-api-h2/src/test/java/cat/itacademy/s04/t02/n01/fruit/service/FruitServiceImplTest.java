package cat.itacademy.s04.t02.n01.fruit.service;

import cat.itacademy.s04.t02.n01.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit.dtos.FruitDTO;
import cat.itacademy.s04.t02.n01.fruit.mappers.FruitMapper;
import cat.itacademy.s04.t02.n01.fruit.model.Provider;
import cat.itacademy.s04.t02.n01.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n01.fruit.repository.ProviderRepository;
import cat.itacademy.s04.t02.n01.fruit.serviceImpl.FruitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @Mock
    private FruitMapper fruitMapper;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    void create_ShouldReturnFruitDTO_WhenSuccessful() {
        FruitDTO inputDTO = new FruitDTO(null, "Pera", 1.2, 1L);
        Provider mockProvider = new Provider(1L, "Proveedor Test", "España");
        Fruit fruitToSave = new Fruit(null, "Pera", 1.2, mockProvider);
        Fruit savedFruit = new Fruit(1L, "Pera", 1.2, mockProvider);
        FruitDTO expectedDTO = new FruitDTO(1L, "Pera", 1.2, 1L);

        when(providerRepository.findById(1L)).thenReturn(Optional.of(mockProvider));
        when(fruitMapper.mapToEntity(inputDTO)).thenReturn(fruitToSave);
        when(fruitRepository.save(fruitToSave)).thenReturn(savedFruit);
        when(fruitMapper.mapToDTO(savedFruit)).thenReturn(expectedDTO);

        FruitDTO result = fruitService.create(inputDTO);

        assertNotNull(result);
        verify(providerRepository).findById(1L);
        verify(fruitRepository).save(fruitToSave);
    }

    @Test
    void getById_ShouldReturnFruitDTO_WhenFruitExists() {
        Long id = 1L;
        Long providerId = 10L;

        Provider mockProvider = new Provider(providerId, "Proveedor Test", "España");

        Fruit fruit = new Fruit(id, "Manzana", 1.5, mockProvider);

        FruitDTO expectedDTO = new FruitDTO(id, "Manzana", 1.5, providerId);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(fruit));
        when(fruitMapper.mapToDTO(fruit)).thenReturn(expectedDTO);

        FruitDTO result = fruitService.getById(id);

        assertNotNull(result);
        assertEquals("Manzana", result.getName());
        assertEquals(providerId, result.getProviderId());
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenFruitExists() {
        Long id = 1L;
        Fruit fruit = new Fruit(id, "Manzana", 1.5, new Provider());

        when(fruitRepository.findById(id)).thenReturn(Optional.of(fruit));

        fruitService.delete(id);

        verify(fruitRepository, times(1)).delete(fruit);
    }

    @Test
    void update_ShouldReturnUpdatedFruitDTO_WhenFruitExists() {
        Long id = 1L;
        Long providerId = 1L;
        FruitDTO inputDTO = new FruitDTO(null, "Manzana Roja", 2.0, providerId);
        Fruit existingFruit = new Fruit(id, "Manzana", 1.5, new Provider());
        Provider newProvider = new Provider(providerId, "Nuevo Proveedor", "Francia");
        Fruit updatedFruit = new Fruit(id, "Manzana Roja", 2.0, newProvider);
        FruitDTO expectedDTO = new FruitDTO(id, "Manzana Roja", 2.0, providerId);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(existingFruit));
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(newProvider));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);
        when(fruitMapper.mapToDTO(updatedFruit)).thenReturn(expectedDTO);

        FruitDTO result = fruitService.update(id, inputDTO);

        assertEquals("Manzana Roja", result.getName());
    }

    @Test
    void getById_ShouldThrowException_WhenFruitDoesNotExist() {
        Long id = 99L;
        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fruitService.getById(id));
    }

    @Test
    void update_ShouldThrowException_WhenFruitDoesNotExist() {
        Long id = 99L;
        FruitDTO inputDTO = new FruitDTO(null, "Fantasma", 0.0, 1L);

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fruitService.update(id, inputDTO));

        verify(fruitRepository, never()).save(any(Fruit.class));
        verify(providerRepository, never()).findById(anyLong());
    }

    @Test
    void getAll_ShouldReturnListOfFruitDTOs() {
        Provider mockProvider = new Provider(1L, "Proveedor General", "España");

        List<Fruit> fruits = List.of(
                new Fruit(1L, "Manzana", 1.5, mockProvider),
                new Fruit(2L, "Pera", 1.2, mockProvider)
        );

        when(fruitRepository.findAll()).thenReturn(fruits);

        when(fruitMapper.mapToDTO(any(Fruit.class)))
                .thenReturn(new FruitDTO(1L, "Fruta Genérica", 1.0, 1L));

        List<FruitDTO> result = fruitService.getAll();

        assertEquals(2, result.size());
        verify(fruitRepository).findAll();
    }
}
