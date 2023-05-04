package com.rick.cursomc.resources;

import com.rick.cursomc.domain.Categoria;
import com.rick.cursomc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Categoria> categoriaFindById (@PathVariable Integer id) {
        Categoria obj = service.getCategoriaFindById(id);
        return ResponseEntity.ok().body(obj);
    }
}
