# API - Barbearia

### 📦 Agendamentos

| Método | Rota                         | Acesso   | Descrição                    |
|--------|------------------------------|----------|------------------------------|
| GET    | /agendamentos                | Admin    | Lista todos os agendamentos |
| GET    | /agendamentos/dia?data=...   | Admin    | Lista agendamentos do dia   |
| POST   | /agendamentos                | Público  | Cria novo agendamento       |
| DELETE | /agendamentos/{id}           | Admin    | Exclui um agendamento       |

### 🕑 Horários Disponíveis

| Método | Rota                           | Acesso  | Descrição                    |
|--------|--------------------------------|---------|------------------------------|
| GET    | /horarios-disponiveis?data=... | Público | Lista horários livres do dia |

---

### ✅ Acesso Admin

| Usuário | Senha   |
|---------|---------|
| admin   | 123456  |

Use autenticação `Basic Auth` nas requisições protegidas.

