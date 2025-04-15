package com.barbearia.agendamentos.controller;

import com.barbearia.agendamentos.model.Cliente;
import com.barbearia.agendamentos.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> criar(@RequestBody @Valid Cliente cliente) {
        // Verifica se já existe um cliente com o mesmo telefone
        if (clienteRepository.existsByTelefone(cliente.getTelefone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null); // Conflito, já existe o cliente com o mesmo telefone
        }
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }
}