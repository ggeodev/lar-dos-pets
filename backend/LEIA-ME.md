# Lar de Patas — API (Spring Boot)

Requisitos: Java 17+, Maven 3.9+ e PostgreSQL (com Docker ou instalado direto).

## Executar
1. Banco: `docker compose up -d` (ou PostgreSQL instalado, veja abaixo)
2. API:   `mvn spring-boot:run`
3. Teste: http://localhost:8080/api/animais

**Banco e migrações:** o esquema é criado pelo Flyway (pasta `src/main/resources/db/migration`). Em um banco
novo e vazio nada precisa ser feito: as tabelas são criadas ao iniciar. Se o seu banco LOCAL foi criado por
versões antigas e dá erro ao iniciar, apague e recrie o banco (`DROP DATABASE adocao; CREATE DATABASE adocao OWNER adocao;`).

No perfil `dev` (padrão), animais, contas e pedidos fictícios são carregados se o banco estiver vazio.
Contas de teste (senha de todas: `demo1234`): patas@exemplo.com (ONG, tem Thor, Mel e Rex com pedidos),
maria@exemplo.com, joao@exemplo.com, vet@exemplo.com.

## Endpoints
| Método | Rota | Acesso | Para quê |
|---|---|---|---|
| GET | /api/animais | público | Catálogo. Filtros: especie, porte, sexo, cidade, destaque |
| GET | /api/animais/cidades | público | Opções do filtro de localização |
| GET | /api/animais/{id} | público | Perfil (animais adotados aparecem com status ADOTADO) |
| POST | /api/interesses | público | Formulário de interesse (409 se já pediu com o mesmo e-mail) |
| POST | /api/auth/registro | público | Criar conta (devolve token) |
| POST | /api/auth/login | público | Entrar (devolve token) |
| POST | /api/animais | login | Cadastrar animal na conta logada |
| GET | /api/meus-animais | login | Animais da conta, com total de interessados |
| GET | /api/meus-animais/{id}/interesses | login (dono) | Pedidos recebidos, com contato |
| PATCH | /api/meus-animais/{id}/status | login (dono) | `{"status":"ADOTADO"}` ou `{"status":"DISPONIVEL"}` |

Rotas com login exigem o cabeçalho `Authorization: Bearer <token>`. Sem token: 401.
Animal de outra conta responde 404 (não revela que existe).

Valores aceitos: especie = CACHORRO, GATO, COELHO, OUTRA · porte = PEQUENO, MEDIO, GRANDE ·
sexo = MACHO, FEMEA · moradia = CASA_COM_QUINTAL, CASA_SEM_QUINTAL, APARTAMENTO.

## Exemplos (curl)
```
# entrar e guardar o token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"email":"patas@exemplo.com","senha":"demo1234"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/meus-animais
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/meus-animais/1/interesses

curl -X POST http://localhost:8080/api/animais -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{
  "nome":"Frida","especie":"GATO","porte":"PEQUENO","sexo":"FEMEA","idade":"1 ano",
  "cidade":"Araraquara, SP","descricao":"Gata tranquila e carinhosa.","saude":"Vacinada"}'
```

## Variáveis de ambiente
| Variável | Para quê |
|---|---|
| DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD | Conexão com o PostgreSQL |
| JWT_SECRET | Texto longo e aleatório (mín. 32 caracteres) que assina os logins. Sem ele, um segredo temporário é gerado a cada início e os logins caem quando a API reinicia |
| JWT_HORAS | Validade do login em horas (padrão 12) |
| CORS_ORIGINS | Sites autorizados, ex.: https://seu-usuario.github.io |
| DDL_AUTO | Padrão `validate` (só confere). Use `none` apenas como socorro se o deploy falhar com "Schema-validation". Nunca `create`/`update` |
| PORT | Porta (a hospedagem define) |

## Testes automatizados
`mvn test` roda 7 testes unitários do token e 13 testes de integração da API (cadastro, login, permissões,
fluxo completo de adoção). Usam banco H2 em memória: não precisam de PostgreSQL nem Docker.
No GitHub, o workflow "Testes do backend" (aba Actions) roda tudo a cada push na pasta `backend`.

## Documentação interativa (Swagger)
Com a API rodando: http://localhost:8080/swagger-ui.html (no Render: `https://SEU-SERVICO.onrender.com/swagger-ui.html`).
Para rotas protegidas: faça login em POST /api/auth/login, copie o `token`, clique em **Authorize** e cole só o token.

## Migrações do banco (Flyway)
Mudanças no esquema entram como novos arquivos `V2__descricao.sql`, `V3__...`. Nunca edite uma migração já aplicada.
Um banco que já tinha as tabelas (criadas antes do Flyway) é "adotado" automaticamente na primeira execução.

## Sem Docker: PostgreSQL instalado direto (Ubuntu/Debian)
```
sudo apt update && sudo apt install -y postgresql openjdk-17-jdk maven
sudo -u postgres psql -c "CREATE USER adocao WITH PASSWORD 'adocao';"
sudo -u postgres psql -c "CREATE DATABASE adocao OWNER adocao;"
mvn spring-boot:run
```

## Estrutura
- model/ entidades (Usuario, Animal, Interesse) e enums · repository/ acesso ao banco
- service/ regras de negócio · controller/ rotas REST · dto/ entradas e saídas (com validação)
- security/ token JWT e filtro de login · config/ regras de acesso, CORS e dados de demonstração
- exception/ erros padronizados

## Segurança: o que está feito e o que não está
Feito: senhas com BCrypt, token JWT com validade, rotas protegidas, dono verificado em cada operação,
contato dos interessados visível só ao responsável, e-mail/telefone fora das respostas públicas.
Não feito: limite de tentativas de login, recuperação de senha, confirmação de e-mail, upload de fotos, paginação do catálogo.
