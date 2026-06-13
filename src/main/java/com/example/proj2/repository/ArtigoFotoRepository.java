package com.example.proj2.repository;

import com.example.proj2.models.ArtigoFoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtigoFotoRepository extends JpaRepository<ArtigoFoto, Integer> {
    List<ArtigoFoto> findByIdArtigoIdOrderByOrdemAsc(Integer artigoId);
}
