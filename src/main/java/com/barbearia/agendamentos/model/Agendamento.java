package com.barbearia.agendamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento ManyToOne com a entidade Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotBlank(message = "O serviço não pode ser vazio") // Validação de campo não vazio
    private String servico;

    @NotNull(message = "O horário de início não pode ser nulo") // Validação de horário de início
    private LocalDateTime horarioInicio;

    @NotNull(message = "O horário de término não pode ser nulo") // Validação de horário de término
    private LocalDateTime horarioTermino;

    // Método de validação para garantir que o horário de término seja posterior ao de início
    public void validarHorarios() {
        if (horarioTermino.isBefore(horarioInicio)) {
            throw new IllegalArgumentException("O horário de término não pode ser antes do horário de início.");
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public LocalDateTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalDateTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalDateTime getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(LocalDateTime horarioTermino) {
        this.horarioTermino = horarioTermino;
    }
}