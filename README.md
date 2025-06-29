# PassAPP – Sistema de Compra de Ingressos para Eventos (v0.5)

Trabalho Final de Programação para Dispositivos Móveis – FACOM/UFMS  
Professor(a): Ana Karina  
Aluno: Marcello Anthony Bezerra Martins  
RGA: 2018.1906.028-4  
Repositório GitHub: https://github.com/marcelloAnth/Prog-Mobile

---

## Visão Geral

O PassAPP é um aplicativo para dispositivos Android desenvolvido com o objetivo de facilitar a compra de ingressos para eventos. O sistema permite o cadastro de usuários e de eventos, controle de permissões baseado no perfil (usuário ou administrador), autenticação segura com senhas criptografadas e utilização de recursos como câmera e galeria para capturar imagens.

---

## Papéis de Usuário

| Papel         | Permissões                                                                 |
|---------------|----------------------------------------------------------------------------|
| Usuário       | Cadastrar-se com foto, visualizar eventos, comprar ingressos, trocar senha |
| Administrador | Gerenciar eventos, redefinir senhas de usuários, cadastrar novos eventos   |

---

## Requisitos Funcionais Implementados

- Cadastro de usuário com foto, login e senha
- Armazenamento seguro da senha utilizando hash com BCrypt
- Tela de login com diferenciação entre perfil de usuário e administrador
- Visualização de eventos para usuários autenticados
- Cadastro de eventos com imagem capturada por câmera ou selecionada da galeria
- Limite de compra de até 10 ingressos por usuário por evento
- Redefinição de senha pelo usuário ou por administrador
- Controle de acesso baseado em permissões
- Tratamento de erros (campos obrigatórios, login inválido, entradas incompatíveis)

---

## Requisitos de Sistema

- Banco de dados Room para persistência local
- Compatibilidade com Android 7.0 (API 24) ou superior
- Suporte a múltiplos tipos de entrada (texto, imagem)
- Separação de permissões por tipo de usuário
- Utilização de ViewBinding para melhorar a manutenção da interface

---

## Estrutura do Projeto

- `Activities/`: Telas principais do sistema (Login, Cadastro, Eventos)
- `Entities/`: Estrutura de dados (`Usuario`, `Evento`, `Compra`)
- `DAO/`: Interfaces de acesso ao banco de dados (Room)
- `Database/`: Classe singleton do banco de dados local (`AppDatabase`)
- `Security/`: Classe utilitária para criptografia de senhas (BCrypt)
- `UI/`: Classes de apoio como `UsuarioManager`

---

## Casos de Uso

| Código | Caso de Uso                     | Ator         |
|--------|----------------------------------|--------------|
| UC001  | Cadastrar Usuário                | Visitante    |
| UC002  | Visualizar Eventos               | Usuário      |
| UC003  | Redefinir Senha (Usuário)        | Usuário      |
| UC004  | Criar Evento                     | Administrador|
| UC005  | Redefinir Senha de Usuário       | Administrador|
| UC006  | Comprar Ingressos                | Usuário      |

---

## Observações Finais

O projeto encontra-se em conformidade com os requisitos propostos na disciplina.  
Caso precise de orientações adicionais sobre o funcionamento interno ou estrutura do sistema, consulte os comentários no código fonte disponível no repositório GitHub.

