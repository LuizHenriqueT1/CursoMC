package com.rick.cursomc.services;

import com.rick.cursomc.domain.Categoria;
import com.rick.cursomc.domain.dtos.CategoriaDTO;
import com.rick.cursomc.repositories.CategoriaRepository;
import com.rick.cursomc.services.exceptions.DataIntegrityViolationException;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repository.findAll(pageRequest);
    }

    public Categoria create(CategoriaDTO objDto) {
        objDto.setId(null);
        validaCategoria(objDto);
        Categoria newObj = new Categoria(objDto);
        return repository.save(newObj);
    }

    public Categoria update(Integer id, CategoriaDTO obj) {
        obj.setId(id);
        Categoria newObj = findByID(id);
        updateData(newObj, obj);
        return repository.save(newObj);
    }

    private void updateData(Categoria newObj, CategoriaDTO obj) {
        newObj.setNome(obj.getNome());
    }

    public void delete(Integer id) {
        findByID(id);
        try {
            repository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possivel excluir porque há entidades relacionadas");
        }
    }

    private void validaCategoria(CategoriaDTO objDto) {
        Optional<Categoria> obj = repository.findByNome(objDto.getNome());
        if (obj.isPresent() && obj.get().getId() != objDto.getId()) {
            throw new DataIntegrityViolationException("Categoria já está registrada: "+ objDto.getNome());
        }
    }
}
