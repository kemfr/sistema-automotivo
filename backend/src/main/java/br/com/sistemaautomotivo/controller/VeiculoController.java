
package br.com.sistemaautomotivo.controller;

import br.com.sistemaautomotivo.entity.Veiculo;
import br.com.sistemaautomotivo.entity.StatusVeiculo;
import br.com.sistemaautomotivo.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping("/filtro")
    public List<Veiculo> filtrar(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) StatusVeiculo status) {
        return veiculoService.filtrar(marca, modelo, precoMin, precoMax, ano, status);
    }

    @PostMapping
    public Veiculo cadastrar(@Valid @RequestBody Veiculo veiculo) {
        return veiculoService.cadastrar(veiculo);
    }

    @GetMapping
    public List<Veiculo> listarTodos() {
        return veiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Veiculo buscarPorId(@PathVariable Long id) {
        return veiculoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Veiculo atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Veiculo veiculo) {
        return veiculoService.atualizar(id, veiculo);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        veiculoService.excluir(id);
    }
}
