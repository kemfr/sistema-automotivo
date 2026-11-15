
package br.com.sistemaautomotivo.service;

import br.com.sistemaautomotivo.entity.Veiculo;
import br.com.sistemaautomotivo.entity.StatusVeiculo;
import br.com.sistemaautomotivo.exception.ResourceNotFoundException;
import br.com.sistemaautomotivo.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private static final int PRIMEIRO_ANO_VALIDO = 1886;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        validarAnoFabricacao(veiculo);
        return veiculoRepository.save(veiculo);
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado."));
    }

    public List<Veiculo> filtrar(
            String marca,
            String modelo,
            BigDecimal precoMin,
            BigDecimal precoMax,
            Integer ano,
            StatusVeiculo status) {
        if ((precoMin != null && precoMin.signum() < 0)
                || (precoMax != null && precoMax.signum() < 0)) {
            throw new IllegalArgumentException("Os limites de preço não podem ser negativos.");
        }
        if (precoMin != null && precoMax != null && precoMin.compareTo(precoMax) > 0) {
            throw new IllegalArgumentException("O preço mínimo não pode ser maior que o preço máximo.");
        }
        validarAnoFabricacao(ano);

        return veiculoRepository.buscarPorFiltros(marca, modelo, precoMin, precoMax, ano, status);
    }

    public Veiculo atualizar(Long id, Veiculo dadosAtualizados) {
        validarAnoFabricacao(dadosAtualizados);
        Veiculo veiculo = buscarPorId(id);

        veiculo.setModelo(dadosAtualizados.getModelo());
        veiculo.setAnoFabricacao(dadosAtualizados.getAnoFabricacao());
        veiculo.setCor(dadosAtualizados.getCor());
        veiculo.setPreco(dadosAtualizados.getPreco());
        veiculo.setQuilometragem(dadosAtualizados.getQuilometragem());
        veiculo.setStatus(dadosAtualizados.getStatus());

        return veiculoRepository.save(veiculo);
    }

    public void excluir(Long id) {
        Veiculo veiculo = buscarPorId(id);
        veiculoRepository.delete(veiculo);
    }

    private void validarAnoFabricacao(Veiculo veiculo) {
        validarAnoFabricacao(veiculo.getAnoFabricacao());
    }

    private void validarAnoFabricacao(Integer ano) {
        if (ano != null && (ano < PRIMEIRO_ANO_VALIDO || ano > Year.now().getValue())) {
            throw new IllegalArgumentException(
                    "O ano de fabricação deve estar entre 1886 e " + Year.now().getValue() + ".");
        }
    }
}
