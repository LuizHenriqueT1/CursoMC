package com.rick.cursomc.services;

import com.rick.cursomc.domain.Pedido;
import com.rick.cursomc.repositories.PedidoRepository;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;


    public Pedido getPedidoFindById(Integer id) {
        Optional<Pedido> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found: id "+ id));
    }
}
