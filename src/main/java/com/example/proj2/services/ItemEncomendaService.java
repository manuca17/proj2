package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.ItemEncomenda;
import com.example.proj2.repository.ItemEncomendaRepository;

@Service
public class ItemEncomendaService {

    @Autowired
    private ItemEncomendaRepository repository;

    public List<ItemEncomenda> findAll() {
        return repository.findAll();
    }

    public Optional<ItemEncomenda> findById(Integer id) {
        return repository.findById(id);
    }

    public ItemEncomenda save(ItemEncomenda item) {
        return repository.save(item);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }


    public ItemEncomenda updateQuantity(Integer id, Integer quantity) {
        Optional<ItemEncomenda> existing = findById(id);
        if (existing.isPresent()) {
            ItemEncomenda i = existing.get();
            i.setQuantidade(quantity);
            return save(i);
        }
        return null;
    }
}