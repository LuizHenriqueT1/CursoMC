package com.rick.cursomc.services;

import com.rick.cursomc.domain.Cidade;
import com.rick.cursomc.domain.Cliente;
import com.rick.cursomc.domain.Endereco;
import com.rick.cursomc.domain.dtos.ClienteDTO;
import com.rick.cursomc.domain.dtos.ClienteNewDto;
import com.rick.cursomc.enums.Perfil;
import com.rick.cursomc.enums.TipoCliente;
import com.rick.cursomc.repositories.CidadeRepository;
import com.rick.cursomc.repositories.ClienteRepository;
import com.rick.cursomc.repositories.EnderecoRepository;
import com.rick.cursomc.security.UserSS;
import com.rick.cursomc.services.exceptions.AuthorizationException;
import com.rick.cursomc.services.exceptions.DataIntegrityViolationException;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private S3Service s3Service;

    public Cliente findID(Integer id) {
        Cliente obj = clienteRepository.findOne(id);
        UserSS userLogged = UserService.authenticated();
        if (userLogged == null || !userLogged.hasRole(Perfil.ADMIN) && !id.equals(userLogged.getId())) {
            throw new AuthorizationException("Acesso negado");
        }
        if (obj == null) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Cliente.class.getName());
        }
        return obj;
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
        PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return clienteRepository.findAll(pageRequest);
    }

    public URI uploadProfilePicture(MultipartFile multipartFile) {
        return s3Service.uploadFile(multipartFile);
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
            clienteRepository.delete(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possivel excluir porque há entidades relacionadas");
        }
    }

    public Cliente fromDTO(ClienteDTO objDto) {
        return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
    }

    public Cliente fromDto(ClienteNewDto objDto) {
        Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
                TipoCliente.toEnum(objDto.getTipo()), passwordEncoder.encode(objDto.getSenha()));

        Cidade cidade = cidadeRepository.findOne(objDto.getCidadeId());

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
