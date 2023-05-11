package com.rick.cursomc.services;

import com.rick.cursomc.domain.ItemPedido;
import com.rick.cursomc.domain.PagamentoComBoleto;
import com.rick.cursomc.domain.Pedido;
import com.rick.cursomc.enums.EstadoPagamento;
import com.rick.cursomc.repositories.ItemPedidoRepository;
import com.rick.cursomc.repositories.PagamentoRepository;
import com.rick.cursomc.repositories.PedidoRepository;
import com.rick.cursomc.repositories.ProdutoRepository;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public Pedido getPedidoFindById(Integer id) {
        Optional<Pedido> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found: id "+ id));
    }

    public Pedido insert(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preecherPagamentoComBoleto(pagto, obj.getInstante());
        }
        obj =  repository.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        for (ItemPedido item : obj.getItens()) {
            item.setDesconto(0.0);
            item.setPreco(item.getProduto().getPreco());
            item.setProduto(produtoRepository.findById(item.getProduto().getId()).orElse(null));
            item.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        return obj;
    }
}
