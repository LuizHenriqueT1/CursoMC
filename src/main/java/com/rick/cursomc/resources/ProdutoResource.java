package com.rick.cursomc.resources;

import com.rick.cursomc.domain.Produto;
import com.rick.cursomc.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> listProdutos() {
        List<Produto> list = service.getListprodutos();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody Produto produto) {
        Produto newObj = service.createProduto(produto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
