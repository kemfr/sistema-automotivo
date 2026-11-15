
package br.com.sistemaautomotivo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    @NotNull(message = "O modelo do veículo é obrigatório.")
    private Modelo modelo;

    @Column(name = "ano_fabricacao", nullable = false)
    @NotNull(message = "O ano de fabricação é obrigatório.")
    @Min(value = 1886, message = "O ano de fabricação deve ser igual ou posterior a 1886.")
    private Integer anoFabricacao;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "A cor do veículo é obrigatória.")
    private String cor;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "O preço do veículo é obrigatório.")
    @PositiveOrZero(message = "O preço não pode ser negativo.")
    private BigDecimal preco;

    @Column(nullable = false)
    @NotNull(message = "A quilometragem é obrigatória.")
    @PositiveOrZero(message = "A quilometragem não pode ser negativa.")
    private Integer quilometragem = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O status do veículo é obrigatório.")
    private StatusVeiculo status = StatusVeiculo.DISPONIVEL;

    public Veiculo() {
    }

    public Long getId() {
        return id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Integer quilometragem) {
        this.quilometragem = quilometragem;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }
}
