package cat.itacademy.s04.t02.n01.fruit.service;

import cat.itacademy.s04.t02.n01.fruit.dtos.ProviderDTO;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceAlreadyExistsException;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceConflictException;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.mappers.ProviderMapper;
import cat.itacademy.s04.t02.n01.fruit.model.Provider;
import cat.itacademy.s04.t02.n01.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n01.fruit.repository.ProviderRepository;
import cat.itacademy.s04.t02.n01.fruit.serviceImpl.ProviderServiceImpl;
import org.junit.jupiter.api.DisplayName;
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
class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitRepository fruitRepository;

    @Mock
    private ProviderMapper providerMapper;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @Test
    void delete_ShouldThrowException_WhenProviderHasFruits() {
        Long providerId = 1L;

        when(providerRepository.existsById(providerId)).thenReturn(true);
        when(fruitRepository.existsByProviderId(providerId)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> {
            providerService.delete(providerId);
        });

        verify(providerRepository, never()).deleteById(providerId);
    }

    @Test
    void delete_ShouldThrowException_WhenProviderDoesNotExist() {
        Long providerId = 99L;

        when(providerRepository.existsById(providerId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.delete(providerId);
        });

        verify(providerRepository, never()).deleteById(providerId);
    }

    @Test
    void delete_ShouldDeleteProvider_WhenProviderExistsAndHasNoFruits() {
        Long providerId = 1L;

        when(providerRepository.existsById(providerId)).thenReturn(true);
        when(fruitRepository.existsByProviderId(providerId)).thenReturn(false);

        providerService.delete(providerId);

        verify(providerRepository, times(1)).deleteById(providerId);
    }

    @Test
    void update_ShouldReturnUpdatedProvider_WhenProviderExists() {
        Long providerId = 1L;

        Provider existingProvider = new Provider(providerId, "Minguella", "Pineda");
        Provider savedProvider = new Provider(providerId, "Minguella", "Mataró");
        ProviderDTO updateRequestDTO = new ProviderDTO(providerId, "Minguella", "Mataró");

        when(providerRepository.findById(providerId)).thenReturn(Optional.of(existingProvider));
        when(providerRepository.save(any(Provider.class))).thenReturn(savedProvider);
        when(providerMapper.convertToDTO(savedProvider)).thenReturn(updateRequestDTO);

        ProviderDTO result = providerService.update(providerId, updateRequestDTO);

        assertNotNull(result);
        assertEquals("Mataró", result.getCountry());

        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void update_ShouldThrowException_WhenProviderDoesNotExist() {
        Long fakeId = 99L;
        ProviderDTO updateRequestDTO = new ProviderDTO(fakeId, "Minguella", "Mataró");

        when(providerRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.update(fakeId, updateRequestDTO);
        });

        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void findById_ShouldReturnProvider_WhenProviderExists() {
        Long providerId = 1L;
        Provider existingProvider = new Provider(providerId, "Minguella", "Pineda");
        ProviderDTO expectedDTO = new ProviderDTO(providerId, "Minguella", "Pineda");

        when(providerRepository.findById(providerId)).thenReturn(Optional.of(existingProvider));
        when(providerMapper.convertToDTO(existingProvider)).thenReturn(expectedDTO);

        ProviderDTO result = providerService.findById(providerId);

        assertNotNull(result);
        assertEquals("Minguella", result.getName());
        verify(providerRepository, times(1)).findById(providerId);
    }

    @Test
    void findById_ShouldThrowException_WhenProviderDoesNotExist() {
        Long fakeId = 99L;
        when(providerRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.findById(fakeId);
        });

        verify(providerRepository, times(1)).findById(fakeId);
        verify(providerMapper, never()).convertToDTO(any());
    }

    @Test
    void findAll_ShouldReturnListOfProviders() {
        Provider provider1 = new Provider(1L, "Minguella", "Pineda");
        Provider provider2 = new Provider(2L, "Berto", "Poblet");
        List<Provider> providerList = List.of(provider1, provider2);

        ProviderDTO dto1 = new ProviderDTO(1L, "Minguella", "Pineda");
        ProviderDTO dto2 = new ProviderDTO(2L, "Berto", "Poblet");

        when(providerRepository.findAll()).thenReturn(providerList);
        when(providerMapper.convertToDTO(provider1)).thenReturn(dto1);
        when(providerMapper.convertToDTO(provider2)).thenReturn(dto2);

        List<ProviderDTO> result = providerService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Minguella", result.get(0).getName());

        verify(providerRepository, times(1)).findAll();
        verify(providerMapper, times(2)).convertToDTO(any(Provider.class));
    }

    @Test
    void create_ShouldReturnSavedProvider_WhenNameDoesNotExist() {
        ProviderDTO requestDTO = new ProviderDTO(null, "Minguella", "Pineda");
        Provider entityToSave = new Provider(null, "Minguella", "Pineda");
        Provider savedEntity = new Provider(1L, "Minguella", "Pineda");
        ProviderDTO responseDTO = new ProviderDTO(1L, "Minguella", "Pineda");

        when(providerRepository.existsByName("Minguella")).thenReturn(false);
        when(providerMapper.convertToEntity(requestDTO)).thenReturn(entityToSave);
        when(providerRepository.save(entityToSave)).thenReturn(savedEntity);
        when(providerMapper.convertToDTO(savedEntity)).thenReturn(responseDTO);

        ProviderDTO result = providerService.create(requestDTO);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        verify(providerRepository, times(1)).save(entityToSave);
    }

    @Test
    void create_ShouldThrowException_WhenProviderAlreadyExists() {
        ProviderDTO requestDTO = new ProviderDTO(null, "Minguella", "Pineda");

        when(providerRepository.existsByName("Minguella")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            providerService.create(requestDTO);
        });

        verify(providerMapper, never()).convertToEntity(any());
        verify(providerRepository, never()).save(any(Provider.class));
    }
}
