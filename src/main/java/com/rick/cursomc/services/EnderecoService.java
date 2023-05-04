package com.rick.cursomc.services;

import com.rick.cursomc.domain.Cliente;
import com.rick.cursomc.domain.Endereco;
import com.rick.cursomc.repositories.EnderecoRepository;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    public Endereco getFindById(Integer id) {
        Optional<Endereco> endereco = repository.findById(id);
        return endereco.orElseThrow(() -> new ObjectNotFoundException("Object Not Found: id " + id));
    }
}
