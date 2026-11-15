
package br.com.sistemaautomotivo.controller;

import br.com.sistemaautomotivo.entity.Modelo;
import br.com.sistemaautomotivo.service.ModeloService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @PostMapping
    public Modelo cadastrar(@Valid @RequestBody Modelo modelo) {
        return modeloService.cadastrar(modelo);
    }

    @GetMapping
    public List<Modelo> listarTodos() {
        return modeloService.listarTodos();
    }

    @GetMapping("/{id}")
    public Modelo buscarPorId(@PathVariable Long id) {
        return modeloService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Modelo atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Modelo modelo) {
        return modeloService.atualizar(id, modelo);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        modeloService.excluir(id);
    }
}
