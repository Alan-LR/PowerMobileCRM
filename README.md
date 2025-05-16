# PowerCRM

Projeto desenvolvido como teste técnico para a Power Mobile.

## 📋 Descrição

O **PowerCRM** é um sistema simples de gerenciamento de **usuários** e **veículos**, com integração à **API pública da Tabela FIPE** para preenchimento automático do preço de veículos. O projeto inclui:

- Cadastro e listagem de usuários
- Cadastro e listagem de veículos
- Integração com a FIPE para preço de veículos
- Envio assíncrono via RabbitMQ
- Cache com Spring Cache
- Versionamento de banco com Flyway
- Persistência com PostgreSQL
- Testes unitários com JUnit e Mockito

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4**
- **PostgreSQL**
- **Docker + Docker Compose**
- **RabbitMQ**
- **Flyway**
- **Spring Data JPA**
- **JUnit 5 + Mockito**
- **Lombok**
- **Spring Cache**

---
🔧 Observações: 
 - O projeto já está configurado com Flyway para versionamento de schema do banco.
 - O cache é feito com @Cacheable nos métodos do serviço FIPE para evitar chamadas desnecessárias.
 - A fila do RabbitMQ simula o envio assíncrono da consulta de preço FIPE, permitindo desacoplamento entre serviços.

## 🧩 Estrutura do Projeto

```shell
src/
 └── main/
     ├── java/com/example/powermobilecrm
     │   ├── config/
     │   ├── controller/
     │   ├── dto/
     │   ├── entity/
     │   ├── messaging/
     │   ├── repository/
     │   └── service/
     └── resources/
         ├── application.properties
         └── db/migration/
```
---

## Como Executar a Aplicação Localmente  
🐳 Como subir a aplicação com Docker:
    Certifique-se de ter o Docker e Docker Compose instalados.
    Rode o seguinte comando na raiz do projeto:
    
    docker-compose up -d
    
**Para rodar a aplicação localmente, vá até a pasta do pom.xml e siga os passos abaixo:**

Instalar as dependências: Se você estiver usando o Maven:
 
    mvn clean install

Rodar a aplicação:
 
    mvn spring-boot:run

A aplicação estará disponível em 
  
    http://localhost:8080    

Acessar os endpoints:
Swagger UI:
 
    http://localhost:8080/swagger-ui/index.html

---
## Como Rodar os Testes

Os testes estão localizados no diretório src/test/java.

  Rodar todos os testes: Se você estiver usando o Maven:

    mvn test
---
## 📝 Notas Finais
Esta aplicação foi estruturada para simular um sistema de CRM com integração à API pública da Tabela FIPE e uso de mensageria assíncrona com RabbitMQ. Além disso:

- Permite o cadastro e a gestão de usuários e veículos;
- Integra-se com a API externa da FIPE para obter automaticamente o preço dos veículos;
- Utiliza o Spring Cache para evitar chamadas desnecessárias à API da FIPE;
- Simula o envio de dados de forma assíncrona com o RabbitMQ;
- O banco de dados é versionado com Flyway;
- Todo o ambiente pode ser facilmente executado via Docker Compose.

    
👨‍💻 Autor

Alan Ramalho
 ([LinkedIn](https://www.linkedin.com/in/alan-ramalho-942224247/))


