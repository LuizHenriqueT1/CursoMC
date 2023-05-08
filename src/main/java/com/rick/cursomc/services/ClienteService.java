package com.rick.cursomc.services;

import com.rick.cursomc.domain.Cliente;
import com.rick.cursomc.domain.dtos.ClienteDTO;
import com.rick.cursomc.repositories.ClienteRepository;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
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
    private ClienteRepository repository;

    public Cliente findByID(Integer id) {
        Optional<Cliente> cliente = repository.findById(id);
        return cliente.orElseThrow(() -> new ObjectNotFoundException("Object Not Found: id " + id));
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Cliente create(ClienteDTO objDto) {
        objDto.setId(null);
        Cliente newObj = new Cliente(objDto);
        return repository.save(newObj);
    }

    public Cliente update(Integer id, Cliente obj) {
        obj.setId(id);
        Cliente newObj = findByID(id);
        updateData(newObj, obj);
        return repository.save(newObj);
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repository.findAll(pageRequest);
    }

    //Recebe newObj que é o objeto Cliente que terá seus dados atualizados
    //obj é o objeto Cliente que contém os novos dados
    private void updateData(Cliente newObj, Cliente obj) {
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }

//    public Cliente fromDto(ClienteDTO objDto) {
//        return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
//    }
}
