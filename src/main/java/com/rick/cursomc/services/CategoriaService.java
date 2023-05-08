package com.rick.cursomc.services;

import com.rick.cursomc.domain.Categoria;
import com.rick.cursomc.repositories.CategoriaRepository;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;


    public Categoria getCategoriaFindById(Integer id) {
        Optional<Categoria> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found: id "+ id));
    }

    public List<Categoria> findAll() {
        return repository.findAll();
    }
}
