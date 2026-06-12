package com.example.proj2.services;

import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.repository.ArtigoCatalogoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.FichaTecnica;
import com.example.proj2.repository.FichaTecnicaRepository;

@Service
public class FichaTecnicaService {

    @Autowired
    private FichaTecnicaRepository repository;

    @Autowired
    private ArtigoCatalogoRepository artigoCatalogoRepository;

    public List<FichaTecnica> findAll() {
        return repository.findAll();
    }

    public Optional<FichaTecnica> findById(Integer id) {
        return repository.findById(id);
    }

    public FichaTecnica save(FichaTecnica ficha) {
        // Se a ficha está a ser associada a um projeto já concluído,
        // redireciona automaticamente para o artigo gerado na conclusão.
        if (ficha.getIdProjeto() != null) {
            ProjetoPersonalizado projeto = ficha.getIdProjeto();
            if ("concluido".equalsIgnoreCase(projeto.getEstadoAtual())) {
                Optional<ArtigoCatalogo> artigo = artigoCatalogoRepository.findByIdProjetoOrigem(projeto);
                if (artigo.isPresent()) {
                    ficha.setIdProjeto(null);
                    ficha.setArtigoCatalogo(artigo.get());
                }
            }
        }
        return repository.save(ficha);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<FichaTecnica> findByIdProjeto(ProjetoPersonalizado idProjeto) {
        return repository.findByIdProjeto(idProjeto);
    }

    public List<FichaTecnica> findByArtigoCatalogo(ArtigoCatalogo artigoCatalogo) {
        return repository.findByArtigoCatalogo(artigoCatalogo);
    }

    public FichaTecnica updateTechnicalDetails(Integer id, FichaTecnica updated) {
        Optional<FichaTecnica> existing = findById(id);
        if (existing.isPresent()) {
            FichaTecnica f = existing.get();
            f.setTipoBarro(updated.getTipoBarro());
            f.setCorVidrado(updated.getCorVidrado());
            f.setTemperaturaCozedura(updated.getTemperaturaCozedura());
            f.setTempoSecagem(updated.getTempoSecagem());
            f.setObservacoes(updated.getObservacoes());
            f.setFotoDesign(updated.getFotoDesign());
            f.setFotoPrototipo(updated.getFotoPrototipo());
            f.setRefMolde(updated.getRefMolde());
            return save(f);
        }
        return null;
    }
}