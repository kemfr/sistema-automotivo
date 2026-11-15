# Sistema Automotivo — Gestão de Estoque de Veículos

Projeto acadêmico de Ciência da Computação para cadastro e consulta de marcas, modelos e veículos. O backend disponibiliza uma API REST para administrar o estoque e localizar veículos por critérios combinados.

## Funcionalidades

- CRUD de marcas.
- CRUD de modelos associados a uma marca.
- CRUD de veículos associados a um modelo.
- Filtros de veículos por marca, modelo, preço mínimo, preço máximo, ano de fabricação e status.
- Validação de campos obrigatórios, preço e quilometragem não negativos e ano de fabricação.
- Respostas de erro legíveis para dados inválidos e registros não encontrados.

Os status aceitos para veículos são `DISPONIVEL`, `VENDIDO` e `DESCONTINUADO`.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC (API REST)
- Spring Data JPA e Hibernate
- MySQL 8
- Maven, via Maven Wrapper

## Arquitetura

O código do backend está em `backend/src/main/java/br/com/sistemaautomotivo` e é organizado por responsabilidade:

- `entity`: entidades JPA `Marca`, `Modelo` e `Veiculo`, além do enum `StatusVeiculo`.
- `repository`: interfaces Spring Data JPA para persistência e consulta. `VeiculoRepository` também contém a consulta dos filtros.
- `service`: operações de negócio e acesso aos repositories.
- `controller`: endpoints REST que recebem requisições e delegam as operações aos services.
- `exception`: exceção de recurso não encontrado e tratamento global de erros da API.

O relacionamento do domínio é: cada modelo pertence a uma marca e cada veículo pertence a um modelo.

## Pré-requisitos

- JDK 21 instalado e disponível no `PATH`.
- MySQL 8 acessível localmente. A configuração incluída aponta para `localhost:3307`.
- MySQL Workbench ou cliente MySQL de linha de comando para executar o script SQL.
- Windows PowerShell para os comandos abaixo.

## Banco de dados

Na raiz do projeto, execute `database/schema.sql` em uma instalação limpa do MySQL. O script cria o schema `sistema_automotivo` e as tabelas `marcas`, `modelos` e `veiculos`.

Uma opção pelo cliente MySQL no PowerShell, a partir da raiz do projeto:

```powershell
Get-Content -Raw .\database\schema.sql | mysql -u root -p --default-character-set=utf8mb4
```

Informe a senha do MySQL quando solicitada. Alternativamente, abra `database/schema.sql` no MySQL Workbench e execute o script. Ele define as tabelas, chaves e constraints; o Hibernate está configurado com `ddl-auto=validate`, portanto espera que a estrutura exista e seja compatível.

## Configuração da conexão

O arquivo `backend/src/main/resources/application.properties` contém a configuração da aplicação:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/sistema_automotivo?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

A aplicação usa a variável de ambiente `DB_PASSWORD` para obter a senha; não coloque credenciais no código nem as publique. Para defini-la no terminal atual do Windows PowerShell:

```powershell
$env:DB_PASSWORD = "sua_senha_local_do_mysql"
```

Se o MySQL estiver em outra porta ou usar outro usuário, ajuste a conexão localmente em `application.properties`, sem publicar dados sensíveis.

## Executar o backend no Windows

Abra o PowerShell na raiz do projeto, configure `DB_PASSWORD` na sessão e execute:

```powershell
Set-Location .\backend
.\mvnw.cmd spring-boot:run
```

Por padrão, a API fica disponível em `http://localhost:8080`.

## Endpoints

Todos os endpoints recebem e retornam JSON quando aplicável. Para atualizar ou cadastrar, envie `Content-Type: application/json`.

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/marcas` | Cadastra uma marca |
| `GET` | `/api/marcas` | Lista marcas |
| `GET` | `/api/marcas/{id}` | Busca marca pelo ID |
| `PUT` | `/api/marcas/{id}` | Atualiza marca |
| `DELETE` | `/api/marcas/{id}` | Exclui marca |
| `POST` | `/api/modelos` | Cadastra um modelo |
| `GET` | `/api/modelos` | Lista modelos |
| `GET` | `/api/modelos/{id}` | Busca modelo pelo ID |
| `PUT` | `/api/modelos/{id}` | Atualiza modelo |
| `DELETE` | `/api/modelos/{id}` | Exclui modelo |
| `POST` | `/api/veiculos` | Cadastra um veículo |
| `GET` | `/api/veiculos` | Lista veículos |
| `GET` | `/api/veiculos/{id}` | Busca veículo pelo ID |
| `PUT` | `/api/veiculos/{id}` | Atualiza veículo |
| `DELETE` | `/api/veiculos/{id}` | Exclui veículo |
| `GET` | `/api/veiculos/filtro` | Lista veículos conforme os filtros opcionais enviados |

### Exemplos de cadastro

Marca:

```json
{
  "nome": "Toyota"
}
```

Modelo (a marca referenciada precisa existir):

```json
{
  "nome": "Corolla",
  "marca": {
    "id": 1
  }
}
```

Veículo (o modelo referenciado precisa existir):

```json
{
  "modelo": {
    "id": 1
  },
  "anoFabricacao": 2024,
  "cor": "Prata",
  "preco": 125000.00,
  "quilometragem": 0,
  "status": "DISPONIVEL"
}
```

`quilometragem` e `status` têm valores iniciais na entidade (`0` e `DISPONIVEL`), mas podem ser enviados explicitamente como no exemplo.

### Consultas com filtros

Todos os parâmetros são opcionais e podem ser combinados:

```http
GET http://localhost:8080/api/veiculos/filtro?marca=Toyota&modelo=Corolla&precoMin=50000&precoMax=150000&ano=2024&status=DISPONIVEL
```

Exemplo usando apenas parte dos filtros:

```http
GET http://localhost:8080/api/veiculos/filtro?marca=Toyota&precoMax=100000
```

Marca e modelo são comparados sem diferenciar maiúsculas de minúsculas. Valores válidos de status: `DISPONIVEL`, `VENDIDO` e `DESCONTINUADO`.

## Testes automatizados

O teste de contexto do Spring usa o datasource configurado em `application.properties`. Portanto, o MySQL deve estar acessível e o schema criado antes de executar a suíte.

A partir do PowerShell, na raiz:

```powershell
$env:DB_PASSWORD = "sua_senha_local_do_mysql"
Set-Location .\backend
.\mvnw.cmd test
```

Para limpar e compilar o backend:

```powershell
.\mvnw.cmd clean compile
```

Os testes existentes incluem o carregamento do contexto da aplicação e testes unitários de regras de filtro e validação do serviço de veículos.
