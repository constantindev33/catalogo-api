# Catálogo API

API REST para gerenciamento de produtos, desenvolvida com Java e Spring Boot como projeto de estudos e portfólio.

O projeto implementa um CRUD completo, validação dos dados, tratamento de exceções e testes automatizados.

## Funcionalidades

- Cadastrar um produto
- Listar todos os produtos
- Buscar um produto pelo ID
- Atualizar um produto
- Excluir um produto
- Validar os dados recebidos
- Retornar erros padronizados
- Consultar o banco pelo console H2
- Executar testes automatizados

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- H2 Database
- Maven
- JUnit 5
- Mockito
- MockMvc

## Arquitetura

O projeto está dividido nas seguintes camadas:

```text
Controller → Service → Repository → Banco de dados
```

- **Controller:** recebe as requisições HTTP.
- **Service:** concentra as regras da aplicação.
- **Repository:** realiza o acesso ao banco de dados.
- **Entity:** representa a tabela de produtos.
- **DTOs:** representam os dados de entrada e saída.
- **Exception Handler:** padroniza as respostas de erro.

## Endpoints

| Método   | Endpoint             | Descrição                | Status de sucesso |
| -------- | -------------------- | ------------------------ | ----------------- |
| `GET`    | `/api/produtos`      | Lista os produtos        | `200 OK`          |
| `GET`    | `/api/produtos/{id}` | Busca um produto pelo ID | `200 OK`          |
| `POST`   | `/api/produtos`      | Cadastra um produto      | `201 Created`     |
| `PUT`    | `/api/produtos/{id}` | Atualiza um produto      | `200 OK`          |
| `DELETE` | `/api/produtos/{id}` | Exclui um produto        | `204 No Content`  |

## Exemplo de requisição

### Cadastrar produto

```http
POST /api/produtos
Content-Type: application/json
```

```json
{
  "nome": "Teclado",
  "descricao": "Teclado mecânico",
  "preco": 199.9,
  "estoque": 10
}
```

### Exemplo de resposta

```json
{
  "id": 1,
  "nome": "Teclado",
  "descricao": "Teclado mecânico",
  "preco": 199.9,
  "estoque": 10
}
```

## Validações

A API valida os seguintes dados:

- O nome é obrigatório.
- O nome deve possuir no máximo 120 caracteres.
- A descrição deve possuir no máximo 500 caracteres.
- O preço é obrigatório e não pode ser negativo.
- O estoque é obrigatório e não pode ser negativo.
- O preço aceita no máximo duas casas decimais.

Quando os dados são inválidos, a API retorna o status:

```text
400 Bad Request
```

## Como executar

### Pré-requisitos

- Java 21
- Git

Clone o repositório:

```powershell
git clone https://github.com/constantindev33/catalogo-api.git
```

Entre na pasta:

```powershell
cd catalogo-api
```

No Windows, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

## Banco H2

O projeto utiliza um banco H2 em memória para facilitar a execução durante o desenvolvimento.

Console H2:

```text
http://localhost:8080/h2-console
```

Dados de acesso:

```text
JDBC URL: jdbc:h2:mem:catalogodb
User Name: sa
Password:
```

A senha deve permanecer vazia.

> Como o banco está em memória, os dados são apagados quando a aplicação é encerrada.

## Testes

Para executar os testes automatizados:

```powershell
.\mvnw.cmd test
```

O projeto possui testes para:

- Operações do repositório com H2
- Regras do service com Mockito
- Endpoints HTTP com MockMvc
- Validação dos dados
- Tratamento de produto inexistente
- Operações de criação, busca, atualização e exclusão

Atualmente, o projeto possui 17 testes automatizados.

## Melhorias futuras

- PostgreSQL
- Migrações com Flyway
- Documentação com OpenAPI/Swagger
- Docker
- Paginação e ordenação
- Filtros para busca de produtos
- Deploy da aplicação

## Autor

Desenvolvido por [Constantin](https://github.com/constantindev33).
