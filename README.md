# Challenge 3 - Gerenciamento de Usuários: MSAddress (CompassUOL)
O projeto consiste no desenvolvimento de uma API REST para um sistema de gerenciamento de usuários, utilizando as tecnologias e conhecimentos aprendidos até o momento durante essa jornada do programa de bolsas de estágio da Compass UOL | Back-end Journey (Spring Boot) - AWS Cloud Context.

O projeto foi organizado em formato de microsserviços, onde cada microsserviço é responsável por uma parte do sistema. Segue os microsserviços desenvolvidos e a funcionalidade de cada um:

| **Microsserviço**                                                                | **Funcionalidade**                                                                                                         |
|----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| [MSUser](https://github.com/quasemago/Compass_Challenge3_MSUser)                 | É responsável por armazenar e gerenciar os dados dos usuários, sendo integrado ao MSAddress e MSNotification.              |
| [MSAddress](https://github.com/quasemago/Compass_Challenge3_MSAddress)           | Encapsula a API ViaCEP e fornece um endpoint para consulta de endereço, tanto por CEP quanto por Id.                       |
| [MSNotification](https://github.com/quasemago/Compass_Challenge3_MSNotification) | Recebe e armazena notificações de eventos por parte dos usuários (MSUser), sendo eventos de cadastro, atualização e login. |

### Autor do Projeto
O projeto foi desenvolvido por **Bruno Patrick Formehl Ronning**.

| **E-mail**                           | **Usuário Github** |
|--------------------------------------|--------------------|
| bruno.ronning.pb@compasso.com.br     | quasemago          |

## Sumário
- [Challenge 3 - Gerenciamento de Usuários: MSAddress (CompassUOL)](#challenge-3---gerenciamento-de-usuários-msaddress-compassuol)
    - [Autor do Projeto](#autor-do-projeto)
    - [Tecnologias Utilizadas](#tecnologias-utilizadas)
      - [Dependências](#dependências)
    - [Microsserviço de Endereços (MSAddress)](#microsserviço-de-endereços-msaddress)
      - [Estrutura do banco de dados](#estrutura-do-banco-de-dados)
      - [Regras de Negócio](#regras-de-negócio)
      - [Endpoints](#endpoints)
      - [Payloads](#payloads)
      - [Fluxo de erros](#fluxo-de-erros)
    - [Como executar o projeto](#como-executar-o-projeto)
      - [Pré-configurações](#pré-configurações)
    - [Executando o projeto (Terminal)](#executando-o-projeto-terminal)
      - [Projeto em execução](#projeto-em-execução)
      - [Arquivo Swagger](#arquivo-swagger)
    - [Considerações Finais](#considerações-finais)
      - [Amazon AWS](#amazon-aws)
      - [Agradecimentos](#agradecimentos)

## Tecnologias Utilizadas
- Java JDK 17
- Spring Boot 3

### Dependências
- Spring Boot Test (inclui o JUni 5 e Mockito)
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring DevTools
- Spring Doc OpenAPI (Swagger)
- Spring Cloud OpenFeign
- Lombok
- JJwt (Java JSON Web Token)
- Banco de dados H2 (utilizado apenas nos testes unitários)
- Banco de dados MySQL

---
# Microsserviço de Endereços (MSAddress)
O microsserviço de endereços é responsável por encapsular a API ViaCEP e fornecer um endpoint para consulta de endereço, tanto por CEP quanto por Id.
Caso esse CEP não exista na base dados, consome a API ViaCEP e persiste o endereço para consultas posteriores.

Dessa forma, não é necessário fazer uma requisição para API ViaCEP toda vez que um endereço for consultado, pois o endereço já estará persistido no banco de dados.
Nesses casos, o microsserviço de usuários (MSUser) pode fazer uma requisição para o MSAddress passando um identificador (Id) do endereço em vez de passar o CEP, visto que ele já estará atrelado a um usuário existente.

Ademais, como o MSAddress é um microsserviço independente, podemos ter uma maior liberdade para definir onde e como os dados de endereços serão armazenados, podendo até utilizar algum serviço externo como o RDS da Amazon AWS.

### Estrutura do banco de dados
O banco de dados utilizado para armazenar os endereços é o MySQL. A estrutura do banco de dados é composta por uma única tabela chamada `addresses` com os seguintes campos:

![image](msaddress_db.png)

### Regras de Negócio
- Ao receber uma requisição para consultar um endereço por CEP, deve verificar se o endereço já está persistido no banco de dados, caso não esteja, consumir a API ViaCEP e persistir o endereço.

## Endpoints
A API Rest do MSAddress possui os seguintes endpoints:

| **Método** | **URL**            | **Descrição**                                                                                  |
|------------|--------------------|------------------------------------------------------------------------------------------------|
| `GET`      | /v1/address/:value | Recupera informações de um endereço, através do CEP ou Id. (Requer autenticação por Token JWT) |

É importante lembrar que o endpoint `/v1/address/:value` requer autenticação por Token JWT, que deve ser passado no _header_ da requisição.

## Payloads
A API Rest do MSAddress possui apenas um payload de resposta (Response), sendo:
- `AddressResponse`: Payload utilizado para responder às requisições de recuperação de endereço. Exemplo:
    ```json
    {
      "id": 1,
      "street": "Praça da Sé",
      "city": "São Paulo",
      "state": "SP",
      "cep": "01001-000"
    }
    ```

## Fluxo de erros
Para tratamento de exceções, a API possui um fluxo de erros padrão, que consiste em um payload de resposta chamado `ErrorMessage`, que possui as informações do código do erro, o _status_, e a mensagem.

Algumas situações tratadas pelo fluxo de erros são:

- Erro de validação:
    ```json
    {
      "code": 400,
      "status": "Bad Request",
      "message": "O valor informado não é um CEP válido (99999-000) ou um Id!"
    }
    ```

- Erro de autenticação:
    ```json
    {
      "code": 401,
      "status": "Unauthorized",
      "message": "Não foi possível processar a autenticação: Token JWT inválido ou expirado."
    }
    ```

---
# Como executar o projeto
O projeto foi desenvolvido utilizando a linguagem de programação Java, utilizando o Java Development Kit (JDK) na versão 17.
Portanto, para executar o projeto, é necessário ter o JDK 17 instalado na máquina, que pode ser baixado através do link: [Download Java JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)

### Pré-configurações
Antes de executar o projeto, é necessário fazer algumas configurações, como, por exemplo, a configuração do banco de dados MySQL.
Para isso, basta acessar o arquivo ``application.yml`` localizado na pasta ``src/main/resources`` e alterar as seguintes propriedades de acordo com suas preferências.

Porém, é recomendado utilizar variáveis de ambiente para configuração do projeto, evitando assim que informações sensíveis fiquem expostas no código-fonte, além de não ser necessário recompilar o projeto toda vez que for necessário alterar alguma configuração.
Caso você não configure as variáveis de ambiente, o projeto utilizará as configurações padrões.

**Segue a lista de propriedades que podem ser configuradas via variáveis de ambiente:**

| **Nome da variável**  | **Descrição**                                | **Valor Padrão**                                                                                  |
|-----------------------|----------------------------------------------|---------------------------------------------------------------------------------------------------|
| TOMCAT_PORT           | Porta para subir o servidor WEB Tomcat       | 8081                                                                                              |
| MYSQL_URL_HOST        | URL de conexão do banco MySQL                | jdbc:mysql://localhost:3306/compasschallenge3_msaddress?useSSL=false&allowPublicKeyRetrieval=true |
| MYSQL_USERNAME        | Usuário para conexão MySQL                   | root                                                                                              |
| MYSQL_PASSWORD        | Senha do usuário para conexão MySQL          | 123456                                                                                            |
| CHALLENGE3_JWT_SECRET | Chave secreta para autenticação do Token JWT | 909266500255685261705041354583000                                                                 |

## Executando o projeto (Terminal)
Para executar o projeto diretamente via terminal, além do JDK 17, é necessário ter o _apache maven_ instalado na máquina, que pode ser baixado através do link: [Download Apache Maven](https://maven.apache.org/download.cgi)

Após tudo instalado, basta abrir o terminal na pasta raiz do projeto, e executar o comando ``mvn clean install`` para que todas as dependências sejam baixadas. Após isso execute o comando ``mvn clean package`` para compilar nosso projeto.

Após a execução dos comandos acima, observe que será criado uma pasta chamada ``target`` na raiz do projeto, essa pasta contem o nosso projeto compilado, sendo nomeado de ``msaddress-1.0.jar``. Após entrar na pasta, basta executar o arquivo compilado do projeto utilizando o java.

Para executar o projeto, basta executar o comando ``java -jar msaddress-1.0.jar``.

### Projeto em execução
Com o projeto já em execução, basta acessar o endereço ``/docs-msaddress.html`` para acessar a documentação da API por meio do Swagger.
Por exemplo, se estiver executando o projeto localmente com as configurações padrões, o endereço será ``http://localhost:8081/docs-msaddress.html``.

### Arquivo Swagger
Para facilitar o uso da API, foi gerado um arquivo em JSON do Swagger da API, que pode ser baixado através do link [Download Swagger File](CompassUOL%20Challenge%203%20-%20MSAddress%20Bruno%20Patrick_Swagger.json), você pode importar esse arquivo no [Swagger Editor](https://editor.swagger.io/) para visualizar a documentação da API.

---
# Considerações Finais

### Amazon AWS
Durante o desenvolvimento do projeto, foi necessário realizar testes em um ambiente real, para garantir que a integração entre os microsserviços funcionasse corretamente, visto que até o momento tudo estava sendo testado de forma local.
Para isso, a abordagem utilizada foi hospedar cada microsserviço em uma instância do Amazon AWS EC2, e então realizar as requisições entre os microsserviços, simulando um ambiente real de produção.

Os microsserviços foram escalados da seguinte forma:

| **Microsserviço** | **Abordagem**                                                                                                                                                                                    |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MSUser            | Hospedado em uma instância do AWS EC2, utilizando um banco de dados MySQL local hospedado na própria instância.                                                                                  |
| MSAddress         | Hospedado em uma instância do AWS EC2, utilizando um banco de dados MySQL através do serviço RDS da AWS, proporcionando maior escalabilidade e disponibilidade.                                  |
| MSNotification    | Hospedado em uma instância do AWS EC2, utilizando um banco de dados MongoDB hospedado na própria instância. Além disso, o serviço de mensageria RabbitMQ também ficou hospedado nessa instância. |

Portanto, no final dos testes em um ambiente real de produção, foi possível observar que a integração entre os microsserviços funcionou corretamente, e que tudo estava funcionando corretamente, atendendo a todos os requisitos propostos.

Essa etapa no desenvolvimento foi muito relevante, pois contribuiu para o aprendizado de como é o processo de deploy de um projeto em um ambiente real, mesmo que tenha sido feito de forma manual e não automatizada.
Pois foi necessário configurar as instâncias, instalar as dependências, configurar o banco de dados, configurar o firewall para comunicação entre os serviços, entre outras configurações, evidenciando as dificuldades e desafios que podem ser encontrados em um ambiente real.

### Agradecimentos
O terceiro desafio (Challenge 3) do programa de bolsas de estágio da Compass UOL | Back-end Journey (Spring Boot) - AWS Cloud Context, representou uma grande oportunidade de aprendizado para aplicar os conhecimentos adquiridos até o momento no programa.

Ao utilizar tecnologias como Spring Boot, Swagger e JUnit e Mockito, foi possível criar uma API robusta, testada e bem documentada, seguindo boas práticas de desenvolvimento, garantindo assim a qualidade do projeto, além de facilitar o uso da API por parte de outros desenvolvedores.

Portanto, é importante destacar que esse desafio foi de grande importância, pois além de implementar uma API REST, que havia sido o foco do desafio anterior, foi necessário implementar uma integração entre microsserviços, utilizando de serviços de mensageria como o RabbitMQ e serviços de requisição HTTP como o OpenFeign.

Agradeço à Compass UOL pela oportunidade de participar do programa de bolsas de estágio, e por proporcionar desafios que contribuem para o meu crescimento profissional.

Atenciosamente, [Bruno Patrick Formehl Ronning](#autor-do-projeto).
