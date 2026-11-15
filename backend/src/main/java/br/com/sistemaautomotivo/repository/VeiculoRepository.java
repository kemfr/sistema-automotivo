
package br.com.sistemaautomotivo.repository;

import br.com.sistemaautomotivo.entity.Veiculo;
import br.com.sistemaautomotivo.entity.StatusVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("""
            select v from Veiculo v
            join v.modelo modelo
            join modelo.marca marca
            where (:marca is null or lower(marca.nome) = lower(:marca))
              and (:modelo is null or lower(modelo.nome) = lower(:modelo))
              and (:precoMin is null or v.preco >= :precoMin)
              and (:precoMax is null or v.preco <= :precoMax)
              and (:ano is null or v.anoFabricacao = :ano)
              and (:status is null or v.status = :status)
            """)
    List<Veiculo> buscarPorFiltros(
            @Param("marca") String marca,
            @Param("modelo") String modelo,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax,
            @Param("ano") Integer ano,
            @Param("status") StatusVeiculo status);
}
