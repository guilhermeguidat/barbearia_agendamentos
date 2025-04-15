package com.barbearia.agendamentos.controller;

import com.barbearia.agendamentos.model.Agendamento;
import com.barbearia.agendamentos.model.Cliente;
import com.barbearia.agendamentos.repository.AgendamentoRepository;
import com.barbearia.agendamentos.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Retorna todos os agendamentos existentes
    @GetMapping
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    // Cria um novo agendamento
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Agendamento agendamento) {
        try {
            agendamento.validarHorarios(); // Validação customizada do horário (ex: horário válido)

            boolean ocupado = agendamentoRepository.existsByHorarioInicio(agendamento.getHorarioInicio());
            if (ocupado) {
                throw new RuntimeException("Horário já agendado.");
            }

            Cliente cliente = agendamento.getCliente();
            if (cliente.getId() == null) {
                // Se cliente ainda não existir, salva no banco
                cliente = clienteRepository.save(cliente);
                agendamento.setCliente(cliente);
            }

            Agendamento criado = agendamentoRepository.save(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }

    // Lista horários disponíveis de um determinado dia
    @GetMapping("/horarios-disponiveis")
    public List<LocalDateTime> listarHorariosDisponiveis(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        List<Agendamento> agendamentos = agendamentoRepository.findByHorarioInicioBetween(
                data.atTime(8, 0), data.atTime(18, 0)
        );

        Set<LocalTime> horariosOcupados = agendamentos.stream()
                .map(a -> a.getHorarioInicio().toLocalTime())
                .collect(Collectors.toSet());

        List<LocalDateTime> horariosDisponiveis = new ArrayList<>();
        for (int hora = 8; hora < 18; hora++) {
            LocalTime horario = LocalTime.of(hora, 0);
            if (!horariosOcupados.contains(horario)) {
                horariosDisponiveis.add(data.atTime(horario));
            }
        }

        return horariosDisponiveis;
    }

    // Lista agendamentos de um dia específico
    @GetMapping("/agendamentos-dia")
    public List<Agendamento> listarAgendamentosPorData(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(23, 59, 59);

        return agendamentoRepository.findByHorarioInicioBetween(inicioDoDia, fimDoDia);
    }

    // Exclui um agendamento com base no ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (!agendamentoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Agendamento não encontrado."));
        }

        agendamentoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}