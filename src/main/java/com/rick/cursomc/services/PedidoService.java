package com.rick.cursomc.services;

import com.rick.cursomc.domain.*;
import com.rick.cursomc.enums.EstadoPagamento;
import com.rick.cursomc.repositories.*;
import com.rick.cursomc.security.UserSS;
import com.rick.cursomc.services.exceptions.AuthorizationException;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    public Pedido getPedidoFindById(Integer id) {
        Pedido obj = repository.findOne(id);
        if (obj == null) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Pedido.class.getName());
        }
        return obj;
    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        UserSS userLogged = UserService.authenticated();
        if (userLogged == null) {
            throw new AuthorizationException("Acesso negado");
        }

        PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
        Cliente cliente = clienteRepository.findOne(userLogged.getId());
        return repository.findByCliente(cliente, pageRequest);
    }

    @Transactional
    public Pedido insert(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
        obj.setCliente(clienteService.findID(obj.getCliente().getId()));
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preecherPagamentoComBoleto(pagto, obj.getInstante());
        }
        obj = repository.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        for (ItemPedido ip : obj.getItens()) {
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.find(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.save(obj.getItens());
        emailService.sendOrderConfirmationEmail(obj);
        return obj;
    }
}
