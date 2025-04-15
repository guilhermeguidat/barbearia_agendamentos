package com.barbearia.agendamentos.repository;

import com.barbearia.agendamentos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByTelefone(String telefone);
}