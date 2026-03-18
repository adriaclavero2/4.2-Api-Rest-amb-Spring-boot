package cat.itacademy.s04.t02.n01.fruit.service;

import cat.itacademy.s04.t02.n01.fruit.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit.model.FruitDTO;
import cat.itacademy.s04.t02.n01.fruit.model.FruitMapper;
import cat.itacademy.s04.t02.n01.fruit.repository.FruitRepository;
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

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    void create_ShouldReturnFruitDTO_WhenSuccessful() {
        FruitDTO inputDTO = new FruitDTO(null, "Pera", 1.2);
        Fruit fruitToSave = new Fruit(null, "Pera", 1.2);
        Fruit savedFruit = new Fruit(1L, "Pera", 1.2);
        FruitDTO expectedDTO = new FruitDTO(1L, "Pera", 1.2);

        when(fruitMapper.mapToEntity(inputDTO)).thenReturn(fruitToSave);
        when(fruitRepository.save(fruitToSave)).thenReturn(savedFruit);
        when(fruitMapper.mapToDTO(savedFruit)).thenReturn(expectedDTO);

        FruitDTO result = fruitService.create(inputDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(fruitRepository, times(1)).save(fruitToSave);
    }

    @Test
    void getById_ShouldReturnFruitDTO_WhenFruitExists() {
        Long id = 1L;
        Fruit fruit = new Fruit(id, "Manzana", 1.5);
        FruitDTO expectedDTO = new FruitDTO(id, "Manzana", 1.5);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(fruit));
        when(fruitMapper.mapToDTO(fruit)).thenReturn(expectedDTO);

        FruitDTO result = fruitService.getById(id);

        assertNotNull(result);
        assertEquals("Manzana", result.getName());
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenFruitExists() {
        Long id = 1L;

        fruitService.delete(id);

        verify(fruitRepository, times(1)).deleteById(id);
    }

    @Test
    void update_ShouldReturnUpdatedFruitDTO_WhenFruitExists() {
        Long id = 1L;
        FruitDTO inputDTO = new FruitDTO(null, "Manzana Roja", 2.0);
        Fruit existingFruit = new Fruit(id, "Manzana", 1.5);
        Fruit updatedFruit = new Fruit(id, "Manzana Roja", 2.0);
        FruitDTO expectedDTO = new FruitDTO(id, "Manzana Roja", 2.0);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(existingFruit));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);
        when(fruitMapper.mapToDTO(updatedFruit)).thenReturn(expectedDTO);

        FruitDTO result = fruitService.update(id, inputDTO);

        assertNotNull(result);
        assertEquals("Manzana Roja", result.getName());
        verify(fruitRepository).save(any(Fruit.class));
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
        FruitDTO inputDTO = new FruitDTO(null, "Fantasma", 0.0);
        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fruitService.update(id, inputDTO));
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    void getAll_ShouldReturnListOfFruitDTOs() {
        List<Fruit> fruits = List.of(
                new Fruit(1L, "Manzana", 1.5),
                new Fruit(2L, "Pera", 1.2)
        );

        when(fruitRepository.findAll()).thenReturn(fruits);
        when(fruitMapper.mapToDTO(any(Fruit.class))).thenReturn(new FruitDTO());

        List<FruitDTO> result = fruitService.getAll();

        assertEquals(2, result.size()); // ¿Nos devuelve los dos elementos?
        verify(fruitRepository, times(1)).findAll();
    }
}
