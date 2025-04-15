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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Agendamento agendamento) {
        try {
            agendamento.validarHorarios();

            // Verifica se o horário do agendamento já está ocupado
            boolean ocupado = agendamentoRepository.existsByHorarioInicio(agendamento.getHorarioInicio());

            if (ocupado) {
                throw new RuntimeException("Horário já agendado.");
            }

            // Verifica se o cliente já existe. Se não, cria um novo cliente.
            Cliente cliente = agendamento.getCliente();
            if (cliente.getId() == null) {
                cliente = clienteRepository.save(cliente); // Salvando o cliente
                agendamento.setCliente(cliente); // Associando o cliente ao agendamento
            }

            // Salva o agendamento no banco de dados
            Agendamento criado = agendamentoRepository.save(agendamento);

            // Retorna status 201 (Created) com o objeto criado no corpo da resposta
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);

        } catch (RuntimeException e) {
            // Em caso de erro (horário já ocupado, etc.), retorna status 409 (Conflict) com o erro
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/horarios-disponiveis")
    public List<LocalDateTime> listarHorariosDisponiveis(@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        // Obter todos os agendamentos para o dia solicitado
        List<Agendamento> agendamentos = agendamentoRepository.findByHorarioInicioBetween(
                data.atTime(8, 0), data.atTime(18, 0)
        );

        // Obter os horários já ocupados
        Set<LocalTime> horariosOcupados = agendamentos.stream()
                .map(a -> a.getHorarioInicio().toLocalTime())
                .collect(Collectors.toSet());

        // Gerar uma lista de horários livres para o intervalo de 8h às 18h
        List<LocalDateTime> horariosDisponiveis = new ArrayList<>();
        for (int hora = 8; hora < 18; hora++) {
            LocalTime horario = LocalTime.of(hora, 0);
            if (!horariosOcupados.contains(horario)) {
                horariosDisponiveis.add(data.atTime(horario));
            }
        }
        return horariosDisponiveis;
    }

}