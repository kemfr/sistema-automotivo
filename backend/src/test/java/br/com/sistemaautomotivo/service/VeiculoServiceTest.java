package br.com.sistemaautomotivo.service;

import br.com.sistemaautomotivo.entity.StatusVeiculo;
import br.com.sistemaautomotivo.entity.Veiculo;
import br.com.sistemaautomotivo.repository.VeiculoRepository;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    @Test
    void filtraComTodosOsCriteriosRecebidos() {
        List<Veiculo> veiculos = List.of(new Veiculo());
        BigDecimal precoMin = new BigDecimal("50000");
        BigDecimal precoMax = new BigDecimal("150000");
        when(veiculoRepository.buscarPorFiltros(
                "Toyota", "Corolla", precoMin, precoMax, 2024, StatusVeiculo.DISPONIVEL))
                .thenReturn(veiculos);

        List<Veiculo> resultado = veiculoService.filtrar(
                "Toyota", "Corolla", precoMin, precoMax, 2024, StatusVeiculo.DISPONIVEL);

        assertSame(veiculos, resultado);
        verify(veiculoRepository).buscarPorFiltros(
                "Toyota", "Corolla", precoMin, precoMax, 2024, StatusVeiculo.DISPONIVEL);
    }

    @Test
    void rejeitaFaixaDePrecoInvertida() {
        assertThrows(IllegalArgumentException.class, () -> veiculoService.filtrar(
                null, null, new BigDecimal("150000"), new BigDecimal("50000"), null, null));

        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void rejeitaPrecoNegativoNoFiltro() {
        assertThrows(IllegalArgumentException.class, () -> veiculoService.filtrar(
                null, null, new BigDecimal("-1"), null, null, null));

        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void rejeitaAnoInvalidoNoFiltro() {
        assertThrows(IllegalArgumentException.class, () -> veiculoService.filtrar(
                null, null, null, null, 1885, null));

        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void rejeitaAnoDeFabricacaoFuturo() {
        Veiculo veiculo = new Veiculo();
        veiculo.setAnoFabricacao(Year.now().getValue() + 1);

        assertThrows(IllegalArgumentException.class, () -> veiculoService.cadastrar(veiculo));

        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void rejeitaPrecoEQuilometragemNegativos() {
        Veiculo veiculo = new Veiculo();
        veiculo.setAnoFabricacao(2024);
        veiculo.setCor("Prata");
        veiculo.setPreco(new BigDecimal("-0.01"));
        veiculo.setQuilometragem(-1);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Set<String> invalidFields = factory.getValidator().validate(veiculo).stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .collect(Collectors.toSet());

            assertTrue(invalidFields.contains("preco"));
            assertTrue(invalidFields.contains("quilometragem"));
        }
    }
}
