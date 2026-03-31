package cat.itacademy.s04.t02.n01.fruit.service;

import cat.itacademy.s04.t02.n01.fruit.dtos.ProviderDTO;

import java.util.List;

public interface ProviderService {
    ProviderDTO create(ProviderDTO providerDTO);
    List<ProviderDTO> findAll();
    ProviderDTO findById(Long id);
    ProviderDTO update(Long id, ProviderDTO providerDTO);
    void delete(Long id);
}
