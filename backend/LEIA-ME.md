# Lar de Patas — API (Spring Boot)

Requisitos: Java 17+, Maven 3.9+ e PostgreSQL (com Docker ou instalado direto).

## Executar
1. Banco: `docker compose up -d` (ou PostgreSQL instalado, veja abaixo)
2. API:   `mvn spring-boot:run`
3. Teste: http://localhost:8080/api/animais

**IMPORTANTE ao atualizar da versão sem login:** o modelo de dados mudou (animais agora pertencem a
uma conta). Rode UMA vez com `DDL_AUTO=create`, o que apaga e recria as tabelas:
`DDL_AUTO=create mvn spring-boot:run` (Linux/macOS) · no Windows (PowerShell): `$env:DDL_AUTO="create"; mvn spring-boot:run`.
Nas execuções seguintes, rode normalmente. NUNCA deixe DDL_AUTO=create fixo: apaga os dados a cada início.

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
| DDL_AUTO | `update` (padrão) ou `create` (apaga e recria, só uma vez) |
| PORT | Porta (a hospedagem define) |

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
Não feito: limite de tentativas de login, recuperação de senha, confirmação de e-mail, upload de fotos.
