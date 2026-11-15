
package br.com.sistemaautomotivo.service;

import br.com.sistemaautomotivo.entity.Marca;
import br.com.sistemaautomotivo.exception.ResourceNotFoundException;
import br.com.sistemaautomotivo.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public Marca cadastrar(Marca marca) {
        return marcaRepository.save(marca);
    }

    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    public Marca buscarPorId(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada."));
    }

    public Marca atualizar(Long id, Marca dadosAtualizados) {
        Marca marca = buscarPorId(id);
        marca.setNome(dadosAtualizados.getNome());

        return marcaRepository.save(marca);
    }

    public void excluir(Long id) {
        Marca marca = buscarPorId(id);
        marcaRepository.delete(marca);
    }
}
