package cat.itacademy.s04.t02.n01.fruit.controller;

import cat.itacademy.s04.t02.n01.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit.dtos.FruitDTO;
import cat.itacademy.s04.t02.n01.fruit.service.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FruitController.class)
public class FruitControllerTest {

    @MockitoBean
    private FruitService fruitService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /fruits - Debería retornar 201 cuando el DTO es válido")
    void create_ShouldReturnHTTP201_WhenInputIsValid() throws Exception {
        FruitDTO inputDTO = new FruitDTO(null, "Cereza", 0.2, 1L);
        FruitDTO savedDTO = new FruitDTO(1L, "Cereza", 0.2, 1L);

        when(fruitService.create(any(FruitDTO.class))).thenReturn(savedDTO);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /fruits - Debería retornar 400 cuando los datos son inválidos")
    void create_ShouldReturnHTTP400_WhenDataIsInvalid() throws Exception {
        FruitDTO invalidDTO = new FruitDTO(null, "", -1.0, null);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void read_ShouldReturnHTTP200_WhenGetAll() throws Exception {
        List<FruitDTO> fruits = Arrays.asList(
                new FruitDTO(1L, "Manzana", 1.2, 1L),
                new FruitDTO(2L, "Plátano", 1.3, 2L)
        );
        when(fruitService.getAll()).thenReturn(fruits);

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void read_ShouldReturnHTTP200_WhenGetByProvider() throws Exception {
        List<FruitDTO> fruits = Arrays.asList(new FruitDTO(1L, "Manzana", 1.2, 1L));

        when(fruitService.findByProviderId(1L)).thenReturn(fruits);

        mockMvc.perform(get("/fruits").param("providerId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void read_ShouldReturnHTTP200_WhenGetOneExists() throws Exception {
        FruitDTO foundFruit = new FruitDTO(1L, "Manzana", 0.5, 1L);
        when(fruitService.getById(1L)).thenReturn(foundFruit);

        mockMvc.perform(get("/fruits/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void read_ShouldReturnHTTP404_WhenIdDoesNotExist() throws Exception {
        when(fruitService.getById(99L))
                .thenThrow(new ResourceNotFoundException("Fruit not found with id: 99"));

        mockMvc.perform(get("/fruits/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_ShouldReturnHTTP200_WhenUpdateIsSuccessful() throws Exception {
        FruitDTO updateDTO = new FruitDTO(null, "Manzana Roja", 0.6, 1L);
        FruitDTO updatedDTO = new FruitDTO(1L, "Manzana Roja", 0.6, 1L);

        when(fruitService.update(eq(1L), any(FruitDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/fruits/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_ShouldReturnHTTP404_WhenIdNotFound() throws Exception {
        FruitDTO updateDTO = new FruitDTO(null, "Fresa", 0.1, 1L);

        when(fruitService.update(eq(99L), any(FruitDTO.class)))
                .thenThrow(new ResourceNotFoundException("Fruit not found with id: 99"));

        mockMvc.perform(put("/fruits/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ShouldReturnHTTP204_WhenIdExists() throws Exception {
        doNothing().when(fruitService).delete(1L);

        mockMvc.perform(delete("/fruits/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnHTTP404_WhenIdDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Fruit not found with id: 99"))
                .when(fruitService).delete(99L);

        mockMvc.perform(delete("/fruits/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}