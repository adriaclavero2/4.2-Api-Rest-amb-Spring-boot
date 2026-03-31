package cat.itacademy.s04.t02.n01.fruit.controller;

import cat.itacademy.s04.t02.n01.fruit.dtos.ProviderDTO;
import cat.itacademy.s04.t02.n01.fruit.exception.GlobalExceptionHandler;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceAlreadyExistsException;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceConflictException;
import cat.itacademy.s04.t02.n01.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.service.ProviderService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@WebMvcTest(ProviderController.class)
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProviderService providerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProviders_ShouldReturnHTTP200OK() throws Exception {
        List<ProviderDTO> providerDTOList = List.of(
                new ProviderDTO(1L, "Minguella", "Pineda"),
                new ProviderDTO(2L, "Balsenys", "Barcelona")
        );

        when(providerService.findAll()).thenReturn(providerDTOList);

        mockMvc.perform(get("/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getProviderById_ShouldReturnHTTP200OK() throws Exception {
        ProviderDTO providerDTO = new ProviderDTO(1L, "Minguella", "Pineda");

        when(providerService.findById(1L)).thenReturn(providerDTO);

        mockMvc.perform(get("/providers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Minguella"));
    }

    @Test
    void findDoesntExistIdProvider_ShouldReturnHTTP404NotFound() throws Exception {

        when(providerService.findById(99L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/providers/{id}", 99L))
                .andExpect(status().isNotFound());

    }

    @Test
    void createNewProvider_ShouldReturnHTTP201Created() throws Exception {
        ProviderDTO provider1 = new ProviderDTO(null, "Minguella", "Pineda");
        ProviderDTO provider2 = new ProviderDTO(1L, "Minguella", "Pineda");

        when(providerService.create(any(ProviderDTO.class))).thenReturn(provider2);

        mockMvc.perform(post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(provider1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Minguella"));
    }

    @Test
    @DisplayName("Debe devolver 409 Conflict cuando el nombre del proveedor ya existe")
    void createDuplicateProvider_ShouldReturnHTTP409Conflict() throws Exception {
        ProviderDTO provider1 = new ProviderDTO(null, "Minguella", "Pineda");

        when(providerService.create(any(ProviderDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("Name already exists"));

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(provider1)))
                .andExpect(status().isConflict()); // Ahora sí coincidirá con el 409
    }

    @Test
    void updateProviderCountry_ShouldReturnHTTP200OK() throws Exception {

        Long providerID = 1L;
        ProviderDTO updatedProvider = new ProviderDTO(providerID, "Minguella", "Mataró");

        when(providerService.update(eq(providerID), any(ProviderDTO.class))).thenReturn(updatedProvider);

        mockMvc.perform(put("/providers/{id}", providerID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProvider)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Mataró"));

    }

    @Test
    void updateNonExistProvider_ShouldReturnHTTP404NotFound() throws Exception {

        Long fakeId = 99L;
        ProviderDTO providerToUpdate = new ProviderDTO(fakeId, "Minguella", "Pineda");

        when(providerService.update(eq(fakeId), any(ProviderDTO.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/providers/{id}", fakeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(providerToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delateProvider_ShouldReturn204NoContent() throws Exception {

        Long providerId = 1L;
        doNothing().when(providerService).delete(providerId);

        mockMvc.perform(delete("/providers/{id}", providerId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNonExistProvider_ShouldReturn404NotFound() throws Exception {

        Long fakeId = 99L;

        doThrow(ResourceNotFoundException.class).when(providerService).delete(fakeId);

        mockMvc.perform(delete("/providers/{id}", fakeId))
                .andExpect(status().isNotFound());
    }
}
