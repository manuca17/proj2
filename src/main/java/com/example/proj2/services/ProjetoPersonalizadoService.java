package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.models.FichaTecnica;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.repository.ArtigoCatalogoRepository;
import com.example.proj2.repository.FichaTecnicaRepository;
import com.example.proj2.repository.ProjetoPersonalizadoRepository;
import com.example.proj2.models.Utilizador;
import com.example.proj2.models.Artesa;

@Service
public class ProjetoPersonalizadoService {

    @Autowired
    private ProjetoPersonalizadoRepository repository;

    @Autowired
    private ArtigoCatalogoRepository artigoRepository;

    @Autowired
    private FichaTecnicaRepository fichaRepository;

    public List<ProjetoPersonalizado> findAll() {
        return repository.findAll();
    }

    public Optional<ProjetoPersonalizado> findById(Integer id) {
        return repository.findById(id);
    }

    public ProjetoPersonalizado save(ProjetoPersonalizado projeto) {
        if (projeto != null && projeto.getId() == null && projeto.getDataCriacao() == null) {
            projeto.setDataCriacao(Instant.now());
        }
        return repository.save(projeto);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<ProjetoPersonalizado> findByIdUtilizador(Utilizador utilizador) {
        return repository.findByIdUtilizador(utilizador);
    }

    public List<ProjetoPersonalizado> findByUtilizadorId(Integer utilizadorId) {
        return repository.findByIdUtilizadorId(utilizadorId);
    }

    public List<ProjetoPersonalizado> findByIdArtesa(Artesa artesao) {
        return repository.findByIdArtesa(artesao);
    }

    @Transactional
    public ProjetoPersonalizado updateEstado(Integer id, String estado) {
        Optional<ProjetoPersonalizado> existing = findById(id);
        if (existing.isPresent()) {
            ProjetoPersonalizado p = existing.get();
            p.setEstadoAtual(estado);
            ProjetoPersonalizado saved = save(p);
            if ("concluido".equalsIgnoreCase(estado)) {
                concluirProjeto(saved);
            }
            return saved;
        }
        return null;
    }

    private void concluirProjeto(ProjetoPersonalizado projeto) {
        ArtigoCatalogo artigo = new ArtigoCatalogo();
        artigo.setNome(projeto.getTituloProjeto());
        artigo.setPrecoUnitario(BigDecimal.ZERO);
        artigo.setStock(1);
        artigo.setVisivel(false);
        ArtigoCatalogo savedArtigo = artigoRepository.save(artigo);

        List<FichaTecnica> fichas = fichaRepository.findByIdProjeto(projeto);
        for (FichaTecnica ficha : fichas) {
            ficha.setIdProjeto(null);
            ficha.setArtigoCatalogo(savedArtigo);
            fichaRepository.save(ficha);
        }
    }

    public ProjetoPersonalizado assignArtesa(Integer id,Artesa artesao) {
        Optional<ProjetoPersonalizado> existing = findById(id);
        if (existing.isPresent()) {
            ProjetoPersonalizado p = existing.get();
            p.setIdArtesa(artesao);
            return save(p);
        }
        return null;
    }
}