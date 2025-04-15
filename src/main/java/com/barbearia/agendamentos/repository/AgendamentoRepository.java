package com.barbearia.agendamentos.repository;

import com.barbearia.agendamentos.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByHorarioInicioBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByHorarioInicio(LocalDateTime horarioInicio);
}