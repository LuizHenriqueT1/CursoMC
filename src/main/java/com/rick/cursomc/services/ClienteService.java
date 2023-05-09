package com.rick.cursomc.services;

import com.rick.cursomc.domain.Cidade;
import com.rick.cursomc.domain.Cliente;
import com.rick.cursomc.domain.Endereco;
import com.rick.cursomc.domain.dtos.ClienteDTO;
import com.rick.cursomc.domain.dtos.ClienteNewDto;
import com.rick.cursomc.enums.TipoCliente;
import com.rick.cursomc.repositories.CidadeRepository;
import com.rick.cursomc.repositories.ClienteRepository;
import com.rick.cursomc.repositories.EnderecoRepository;
import com.rick.cursomc.services.exceptions.DataIntegrityViolationException;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente findID(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElseThrow(() -> new ObjectNotFoundException("Object Not Found: id " + id));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente insert(Cliente obj) {
        obj.setId(null);
        obj = clienteRepository.save(obj);
        for (Endereco endereco : obj.getEnderecos()) {
            endereco.setCliente(obj);
            enderecoRepository.save(endereco);
        }
        return obj;
    }

    public Cliente update(Cliente obj) {
        Cliente newObj = findID(obj.getId());
        updateData(newObj, obj);
        return clienteRepository.save(newObj);
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clienteRepository.findAll(pageRequest);
    }

    //Recebe newObj que é o objeto Cliente que terá seus dados atualizados
    //obj é o objeto Cliente que contém os novos dados
    private void updateData(Cliente newObj, Cliente obj) {
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }

    public void delete(Integer id) {
        findID(id);
        try {
            clienteRepository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possivel excluir porque há entidades relacionadas");
        }
    }

    public Cliente fromDTO(ClienteDTO objDto) {
        return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
    }

    public Cliente fromDto(ClienteNewDto objDto) {
        Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
                TipoCliente.toEnum(objDto.getTipo()));

        Cidade cidade = cidadeRepository.findById(objDto.getCidadeId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Cidade não encontrada com ID: " + objDto.getCidadeId()));

        Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
                objDto.getBairro(), objDto.getCep(), cliente, cidade);

        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(objDto.getTelefone1());
        if (objDto.getTelefone2() !=null) {
            cliente.getTelefones().add(objDto.getTelefone2());
        }
        if (objDto.getTelefone3() !=null) {
            cliente.getTelefones().add(objDto.getTelefone3());
        }
        return cliente;
    }
}
