package cat.itacademy.s04.t02.n01.fruit.controller;


import cat.itacademy.s04.t02.n01.fruit.dtos.ProviderDTO;
import cat.itacademy.s04.t02.n01.fruit.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {
    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    public ResponseEntity<ProviderDTO> create(@Valid @RequestBody ProviderDTO providerDTO) {
        ProviderDTO created = providerService.create(providerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProviderDTO>> findAll() {
        List<ProviderDTO> providers = providerService.findAll();
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDTO> findById(@PathVariable Long id) {
        ProviderDTO provider = providerService.findById(id);
        return ResponseEntity.ok(provider);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody ProviderDTO providerDTO) {
        ProviderDTO updated = providerService.update(id, providerDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    }
