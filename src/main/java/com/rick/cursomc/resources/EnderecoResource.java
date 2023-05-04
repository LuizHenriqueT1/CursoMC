package com.rick.cursomc.resources;

import com.rick.cursomc.domain.Endereco;
import com.rick.cursomc.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/enderecos")
public class EnderecoResource {

    @Autowired
    private EnderecoService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Endereco> enderecoFindById(@PathVariable Integer id) {
        Endereco obj = service.getFindById(id);
        return ResponseEntity.ok().body(obj);
    }
}
