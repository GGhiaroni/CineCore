# CineCore | Plataforma de Catálogo e Streaming de Filmes

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-4169e1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200.svg?style=for-the-badge&logo=Flyway&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Sobre o Projeto

O CineCore é uma API RESTful robusta projetada para gerenciar um catálogo de filmes e simular o backend de uma plataforma de streaming. A aplicação permite a consulta detalhada de filmes baseada em categorias, datas de lançamento e avaliações, além de gerenciar a identidade e o acesso dos usuários de forma segura.

Este projeto foi construído com foco em clean code, arquitetura em camadas (Controller, Service, Repository), segurança de ponta a ponta e otimização de consultas ao banco de dados.

## Arquitetura e Decisões Técnicas

* **Ambiente de Desenvolvimento Conteinerizado:** Utilização do Docker para o provisionamento do banco de dados PostgreSQL, garantindo o isolamento do ambiente e a consistência na execução da aplicação em diferentes máquinas, sem a necessidade de instalações locais complexas.
* **Autenticação Stateless com JWT:** A aplicação não armazena sessões de usuários no servidor. A segurança é gerenciada através de JSON Web Tokens (JWT), interceptando cada requisição HTTP por meio de um SecurityFilter customizado acoplado ao Spring Security para validação de identidade.
* **Criptografia de Dados Sensíveis:** Utilização do algoritmo BCryptPasswordEncoder para o hash de senhas antes da persistência no banco de dados, garantindo a proteção das credenciais dos usuários contra vazamentos.
* **Delegação de Processamento ao Banco de Dados:** Uso estratégico de métodos de interface do Spring Data JPA (como paginação nativa com `Pageable` e consultas literais) para delegar ordenações e limites (LIMIT/OFFSET) diretamente ao PostgreSQL, evitando sobrecarga de memória na JVM.
* **Controle de Versão de Schema:** Implementação do Flyway para garantir a previsibilidade e a rastreabilidade das alterações estruturais do banco de dados (Migrations) em todos os ambientes.

## Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework Principal:** Spring Boot 4.0.4
* **Persistência de Dados:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL
* **Gerenciamento de Banco:** Flyway Migrations
* **Conteinerização:** Docker
* **Segurança e Autenticação:** Spring Security
* **Geração e Validação de Tokens:** Java JWT (Auth0)
* **Redução de Boilerplate:** Lombok
* **Validação de Dados:** Spring Boot Starter Validation
* **Design de API:** Spring Web MVC

## Como Executar

Para rodar o projeto localmente, certifique-se de ter o Docker e o Java 17+ instalados na sua máquina.

1. Clone o repositório para o seu ambiente local:
```bash
git clone [https://github.com/seu-usuario/cinecore.git](https://github.com/seu-usuario/cinecore.git)
```

2. Navegue até o diretório do projeto:
```bash
cd cinecore
```

3. Suba o container do banco de dados PostgreSQL usando o Docker Compose (certifique-se de ter o arquivo `docker-compose.yml` configurado na raiz):
```bash
docker-compose up -d
```

4. Compile e execute a aplicação utilizando o Maven Wrapper:
```bash
./mvnw spring-boot:run
```
*Nota: O Flyway executará automaticamente as migrations no banco de dados assim que a aplicação iniciar e conectar com o PostgreSQL.*

## Variáveis de Ambiente

Para rodar o projeto, é necessário configurar as variáveis de ambiente. Você pode configurá-las diretamente no seu ambiente ou criar um arquivo baseando-se no `application.properties` ou `application.yml`. 

Principais variáveis necessárias:
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=cinecore_db
DB_USER=postgres
DB_PASSWORD=sua_senha_aqui
JWT_SECRET=sua_chave_secreta_para_geracao_de_tokens
JWT_EXPIRATION=86400000 # Tempo em milissegundos
```

## 📚 Documentação da API (Swagger)

A API do CineCore está totalmente documentada utilizando a especificação OpenAPI 3 (Swagger). A documentação interativa permite testar todos os endpoints diretamente pelo navegador, visualizar os schemas de requisição/resposta e verificar os métodos de autenticação necessários.

Para acessar a interface do Swagger UI, rode a aplicação localmente e acesse a seguinte URL no seu navegador:

**http://localhost:8080/swagger.html**

*(Caso tenha configurado uma porta diferente, substitua o `8080`)*

<img width="1409" height="782" alt="Captura de Tela 2026-03-31 às 09 13 38" src="https://github.com/user-attachments/assets/875d77a1-29f3-4bfc-9647-ebacdfe2ff00" />


## Principais Endpoints

Abaixo estão as rotas principais da API. Recomenda-se utilizar plataformas como Postman ou Insomnia para os testes.

### Autenticação e Login
* `POST /cinecore/auth/register` - Cadastra um novo usuário no banco de dados.
* `POST /cinecore/auth/login` - Autentica um usuário e retorna o token JWT.

### Categorias => todos os endpoints requerem um token JWT válido e usuário autenticado
* `GET /cinecore/category` - Retorna a lista paginada de categorias do catálogo.
* `GET /cinecore/category/{id}` - Retorna os detalhes de uma categoria específico pelo ID fornecido.
* `POST /cinecore/category` - Adiciona uma nova categoria passando o NOME da categoria no body.
* `PUT /cinecore/category/{id}` - Atualiza os dados de uma categoria existente, informando o novo NOME no body.
* `DELETE /cinecore/category/{id}` - Remove um filme do catálogo.

### Streamings => todos os endpoints requerem um token JWT válido e usuário autenticado
* `GET /cinecore/streaming` - Retorna a lista paginada de todos os streamings cadastrados.
* `GET /cinecore/streaming/{id}` - Retorna um streaming específico a partir do ID fornecido.
* `POST /cinecore/streaming` - Adiciona um novo streaming passando o NOME no body.
* `PUT /cinecore/streaming/{id}` - Atualiza o nome de um streaming existente, informando seu novo NOME no body.
* `DELETE /cinecore/streaming/{id}` - Remove um streaming do catálogo.
  
### Filmes => todos os endpoints requerem um token JWT válido e usuário autenticado
* `GET /cinecore/movie` - Retorna a lista paginada de filmes do catálogo.
* `GET /cinecore/movie/{id}` - Retorna os detalhes de um filme específico a partir do ID fornecido.
* `GET /cinecore/movie/top-rated?limit=10` - Retorna os 10 filmes mais bem avaliados do catálogo. Para alterar o tamanho da lista, basta fornecer um número diferente no parâmetro "limit".
* `GET /cinecore/movie/latest?limit=5` - Retorna os 5 últimos filmes lançados do catálogo. Para alterar o tamanho da lista, basta fornecer um número diferente no parâmetro "limit".
* `GET /cinecore/movie/search?categoryId=5` - Retorna todos os filmes cadastrados na categoria 5. Para alterar a categoria, basta informar uma nova.
* `POST /cinecore/movie` - Adiciona um novo filme ao catálogo.
* `PUT /cinecore/movie/{id}` - Atualiza os dados de um filme existente.
* `DELETE /cinecore/movie/{id}` - Remove um filme do catálogo.
