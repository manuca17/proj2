package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.ItemEncomenda;
import com.example.proj2.models.Utilizador;
import com.example.proj2.repository.EncomendaCatalogoRepository;
import com.example.proj2.repository.ItemEncomendaRepository;

@Service
public class EncomendaCatalogoService {

    @Autowired
    private EncomendaCatalogoRepository repository;

    @Autowired
    private ItemEncomendaRepository itemRepository;

    public List<EncomendaCatalogo> findAll() {
        return repository.findAll();
    }

    public Optional<EncomendaCatalogo> findById(Integer id) {
        return repository.findById(id);
    }

    public EncomendaCatalogo save(EncomendaCatalogo encomenda) {
        return repository.save(encomenda);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<EncomendaCatalogo> findByIdUtilizador(Utilizador idUtilizador) {
        return repository.findByIdUtilizador(idUtilizador);
    }

    public EncomendaCatalogo updateEstado(Integer id, String estado) {
        Optional<EncomendaCatalogo> existing = findById(id);
        if (existing.isPresent()) {
            EncomendaCatalogo e = existing.get();
            e.setEstado(estado);
            return save(e);
        }
        return null;
    }

    @Transactional
    public BigDecimal calculateValorFinal(Integer id) {
        Optional<EncomendaCatalogo> encomenda = findById(id);
        if (encomenda.isPresent()) {
            List<ItemEncomenda> items = itemRepository.findByIdEncomendaWithArtigo(encomenda.get());
            return items.stream()
                .map(item -> item.getIdArtigo().getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public ItemEncomenda addItem(Integer encomendaId, ItemEncomenda item) {
        Optional<EncomendaCatalogo> encomenda = findById(encomendaId);
        if (encomenda.isPresent()) {
            item.setIdEncomenda(encomenda.get());
            return itemRepository.save(item);
        }
        return null;
    }

    public void removeItem(Integer itemId) {
        itemRepository.deleteById(itemId);
    }
}