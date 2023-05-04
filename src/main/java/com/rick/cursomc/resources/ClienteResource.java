package com.rick.cursomc.resources;

import com.rick.cursomc.domain.Cliente;
import com.rick.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cliente> clienteFindById(@PathVariable Integer id) {
        Cliente obj = service.getFindById(id);
        return ResponseEntity.ok().body(obj);
    }
}
