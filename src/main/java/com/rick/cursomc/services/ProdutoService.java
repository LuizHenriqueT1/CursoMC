package com.rick.cursomc.services;

import com.rick.cursomc.domain.Produto;
import com.rick.cursomc.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> getListprodutos() {
        return repository.findAll();
    }

    public Produto createProduto(Produto produto) {
        produto.setId(null);
        return repository.save(produto);
    }
}
