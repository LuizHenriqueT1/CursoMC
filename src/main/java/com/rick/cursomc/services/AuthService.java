package com.rick.cursomc.services;

import com.rick.cursomc.domain.Cliente;
import com.rick.cursomc.repositories.ClienteRepository;
import com.rick.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new ObjectNotFoundException("Email não encontrado");
        }

        String newPass = newPassword();
        cliente.setSenha(bCryptPasswordEncoder.encode(newPass));

        clienteRepository.save(cliente);
        emailService.sendNewPasswordEmail(cliente, newPass);
    }

    private String newPassword() {
        char[] vet = new char[10];
        for (int i=0; i<10; i++) {
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {
        int opt = random.nextInt();
        if (opt == 0) {
            return (char) (random.nextInt(10) + 48); // gera um digito
        }
        else if (opt == 1) {
            return (char) (random.nextInt(26) + 65); // gera uma letra maiuscula
        }
        else {
            return (char) (random.nextInt(26) + 97); // gera uma letra minuscula
        }
    }
}
