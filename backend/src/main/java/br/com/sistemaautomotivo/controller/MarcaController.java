
package br.com.sistemaautomotivo.controller;

import br.com.sistemaautomotivo.entity.Marca;
import br.com.sistemaautomotivo.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @PostMapping
    public Marca cadastrar(@Valid @RequestBody Marca marca) {
        return marcaService.cadastrar(marca);
    }

    @GetMapping
    public List<Marca> listarTodas() {
        return marcaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Marca buscarPorId(@PathVariable Long id) {
        return marcaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Marca atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Marca marca) {
        return marcaService.atualizar(id, marca);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        marcaService.excluir(id);
    }
}
