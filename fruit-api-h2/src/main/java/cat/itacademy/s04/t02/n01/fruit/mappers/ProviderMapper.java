package cat.itacademy.s04.t02.n01.fruit.mappers;

import cat.itacademy.s04.t02.n01.fruit.model.Provider;
import cat.itacademy.s04.t02.n01.fruit.dtos.ProviderDTO;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDTO convertToDTO(Provider provider) {
        if (provider == null) return null;
        ProviderDTO dto = new ProviderDTO();
        dto.setId(provider.getId());
        dto.setName(provider.getName());
        dto.setCountry(provider.getCountry());
        return dto;
    }

    public Provider convertToEntity(ProviderDTO dto) {
        if (dto == null) return null;
        Provider provider = new Provider();
        provider.setId(dto.getId());
        provider.setName(dto.getName());
        provider.setCountry(dto.getCountry());
        return provider;
    }
}