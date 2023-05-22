<h1 align="center"> Cash Flow </h1>

# O Projeto

Projeto feito em Java 17 com objetivo de controlar lançamentos de fluxos de caixa (Créditos e Débitos), assim como, consultar os respectivos lançamentos e saldos diários ou total.

O projeto foi construindo expondo uma API Rest contendo os seguintes serviços: 

- Registrar lançamentos de débito
- Registrar lançamentos de crédito
- Consultar lançamentos
- Consultar lançamentos por dia
- Consultar saldo atual

Mais detalhes é possível ver na documentação do [Swagger](http://localhost:8080/swagger-ui/index.html#/) disponibilizada.
Obs.: Esta documentação ficará acessível quando a aplicação for iniciada.

# Estrutura

O Projeto possui três camadas como pode ser visto na imagem abaixo. 
 - A primeira camada (Controller) expoe os serviços Rest, recebendo as solicitações do usuário de acordo com o serviço acionado. 
 - A segunda camada (Service) é responsável por realizar toda parte negociável e fazer a ponte entre os Serviços Rest e a ultima camada referente a manipulação de dados (Banco de dados).
 - A terceira camada (Repository) é responsável por realizar todas as operações de manipulação de dados no banco de dados, sejam elas consultas ou operações de persistencia. 

![Workflow Diagram](https://github.com/csfinfor/cashflow/assets/133986549/73c3d725-d9fb-4a5e-b28b-59f01f2711ac)

## Instalação

Para o usuo do projeto é necessário que possua o [Docker Desktop](https://www.docker.com/products/docker-desktop/)

Uma vêz com ele instalado basta entrar na pasta raiz do projeto Lancamento e executar o seguinte comando: 

```bash
  docker compose up -d
```

Feito isso será levantado dois containers um referente ao banco de dados (Postgres) e outro referente a aplicação.

Dessa forma a aplicação ficará disponibilizada no endereço [http:8080/localhost/entrys](http:8080/localhost/entrys)

Obs.: Para rodar o projeto na sua IDE de desenvolvimento, é necessário trocar o valor da propriedade **spring.datasource.url** presente no arquivo application.properties para o valor:

```bash
  jdbc:postgresql://localhost:5432/cash_flow?createDatabaseIfNotExist=true&useSSL=false
```
