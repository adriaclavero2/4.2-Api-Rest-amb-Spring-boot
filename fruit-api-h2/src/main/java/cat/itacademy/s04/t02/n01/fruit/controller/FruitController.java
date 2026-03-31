package cat.itacademy.s04.t02.n01.fruit.controller;

import cat.itacademy.s04.t02.n01.fruit.dtos.FruitDTO;
import cat.itacademy.s04.t02.n01.fruit.service.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping()
    public ResponseEntity<FruitDTO> create(@Valid @RequestBody FruitDTO fruitDTO) {
        return new ResponseEntity<>(fruitService.create(fruitDTO), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<FruitDTO>> getFruits(@RequestParam(required = false) Long providerId) {
        if (providerId != null) {
            return ResponseEntity.ok(fruitService.findByProviderId(providerId));
        }
        return ResponseEntity.ok(fruitService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(fruitService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fruitService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitDTO> update(@PathVariable Long id, @Valid @RequestBody FruitDTO fruitDTO) {
        return new ResponseEntity<>(fruitService.update(id, fruitDTO), HttpStatus.OK);
    }
}