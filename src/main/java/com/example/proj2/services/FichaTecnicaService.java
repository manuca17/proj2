package com.example.proj2.services;

import com.example.proj2.models.ProjetoPersonalizado;
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

    public List<FichaTecnica> findAll() {
        return repository.findAll();
    }

    public Optional<FichaTecnica> findById(Integer id) {
        return repository.findById(id);
    }

    public FichaTecnica save(FichaTecnica ficha) {
        return repository.save(ficha);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<FichaTecnica> findByIdProjeto(ProjetoPersonalizado idProjeto) {
        return repository.findByIdProjeto(idProjeto);
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