package com.rick.cursomc.services;

import com.rick.cursomc.domain.Categoria;
import com.rick.cursomc.domain.dtos.CategoriaDTO;
import com.rick.cursomc.repositories.CategoriaRepository;
import com.rick.cursomc.services.exceptions.DataIntegrityViolationException;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;


    public Categoria findByID(Integer id) {
        Optional<Categoria> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found: id "+ id));
    }

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Categoria create(CategoriaDTO objDto) {
        objDto.setId(null);
        validaCategoria(objDto);
        Categoria newObj = new Categoria(objDto);
        return repository.save(newObj);
    }

    public Categoria update(Integer id, CategoriaDTO objDto) {
        objDto.setId(id);
        findByID(id);
        Categoria oldObj = new Categoria(objDto);
        return repository.save(oldObj);
    }

    private void validaCategoria(CategoriaDTO objDto) {
        Optional<Categoria> obj = repository.findByNome(objDto.getNome());
        if (obj.isPresent() && obj.get().getId() != objDto.getId()) {
            throw new DataIntegrityViolationException("Categoria já está registrada: "+ objDto.getNome());
        }
    }
}
