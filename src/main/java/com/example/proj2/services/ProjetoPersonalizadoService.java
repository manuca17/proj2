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
import com.example.proj2.repository.ArtesaRepository;
import com.example.proj2.repository.FichaTecnicaRepository;
import com.example.proj2.repository.ProjetoPersonalizadoRepository;
import com.example.proj2.repository.UtilizadorRepository;
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

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private ArtesaRepository artesaRepository;

    public List<ProjetoPersonalizado> findAll() {
        return repository.findAll();
    }

    public Optional<ProjetoPersonalizado> findById(Integer id) {
        return repository.findById(id);
    }

    public ProjetoPersonalizado save(ProjetoPersonalizado projeto) {
        if (projeto != null && projeto.getId() == null) {
            if (projeto.getDataCriacao() == null) {
                projeto.setDataCriacao(Instant.now());
            }
            if (projeto.getEstadoAtual() == null || projeto.getEstadoAtual().isBlank()) {
                projeto.setEstadoAtual("briefing");
            }
            // Resolve detached JPA entities so the FK columns are persisted correctly
            if (projeto.getIdUtilizador() != null && projeto.getIdUtilizador().getId() != null) {
                utilizadorRepository.findById(projeto.getIdUtilizador().getId())
                    .ifPresent(projeto::setIdUtilizador);
            }
            if (projeto.getIdArtesa() != null && projeto.getIdArtesa().getId() != null) {
                artesaRepository.findById(projeto.getIdArtesa().getId())
                    .ifPresent(projeto::setIdArtesa);
            }
        }
        ProjetoPersonalizado saved = repository.save(projeto);
        return repository.findByIdWithRelations(saved.getId()).orElse(saved);
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
            if ("concluido".equalsIgnoreCase(estado) || "completed".equalsIgnoreCase(estado)) {
                concluirProjeto(saved);
            }
            return saved;
        }
        return null;
    }

    private void concluirProjeto(ProjetoPersonalizado projeto) {
        System.out.println(">>> concluirProjeto chamado para projeto id=" + projeto.getId() + " titulo=" + projeto.getTituloProjeto());

        ArtigoCatalogo artigo = new ArtigoCatalogo();
        artigo.setNome(projeto.getTituloProjeto());
        artigo.setPrecoUnitario(BigDecimal.ZERO);
        artigo.setStock(1);
        artigo.setVisivel(false);
        artigo.setIdProjetoOrigem(projeto);
        ArtigoCatalogo savedArtigo = artigoRepository.save(artigo);
        System.out.println(">>> Artigo criado id=" + savedArtigo.getId());

        List<FichaTecnica> fichas = fichaRepository.findByIdProjetoId(projeto.getId());
        System.out.println(">>> Fichas encontradas: " + fichas.size());
        for (FichaTecnica ficha : fichas) {
            ficha.setIdProjeto(null);
            ficha.setArtigoCatalogo(savedArtigo);
            fichaRepository.save(ficha);
            System.out.println(">>> Ficha id=" + ficha.getId() + " transferida para artigo id=" + savedArtigo.getId());
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