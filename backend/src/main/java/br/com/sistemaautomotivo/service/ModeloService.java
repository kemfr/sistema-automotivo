
package br.com.sistemaautomotivo.service;

import br.com.sistemaautomotivo.entity.Modelo;
import br.com.sistemaautomotivo.exception.ResourceNotFoundException;
import br.com.sistemaautomotivo.repository.ModeloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;

    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public Modelo cadastrar(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    public List<Modelo> listarTodos() {
        return modeloRepository.findAll();
    }

    public Modelo buscarPorId(Long id) {
        return modeloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modelo não encontrado."));
    }

    public Modelo atualizar(Long id, Modelo dadosAtualizados) {
        Modelo modelo = buscarPorId(id);
        modelo.setNome(dadosAtualizados.getNome());
        modelo.setMarca(dadosAtualizados.getMarca());

        return modeloRepository.save(modelo);
    }

    public void excluir(Long id) {
        Modelo modelo = buscarPorId(id);
        modeloRepository.delete(modelo);
    }
}
