# Lar de Patas — API (Spring Boot)

Requisitos: Java 17+, Maven 3.9+ (ou use o IDE) e Docker (para o PostgreSQL).

## Executar
1. Subir o banco:        `docker compose up -d`
2. Rodar a API:          `mvn spring-boot:run`
3. Testar no navegador:  http://localhost:8080/api/animais

Sem Docker? Crie um banco `adocao` no PostgreSQL local e ajuste usuário/senha
com as variáveis DB_HOST, DB_PORT, DB_NAME, DB_USER e DB_PASSWORD.

No perfil `dev` (padrão), 8 animais fictícios são carregados se o banco estiver vazio.
As tabelas `animais` e `interesses` são criadas automaticamente.

## Endpoints
| Método | Rota | Para quê |
|---|---|---|
| GET | /api/animais | Catálogo. Filtros opcionais: especie, porte, sexo, cidade, destaque |
| GET | /api/animais/cidades | Opções do filtro de localização |
| GET | /api/animais/{id} | Perfil do animal |
| POST | /api/animais | Cadastro de animal (201 + Location) |
| POST | /api/interesses | Formulário de interesse (201) |

Valores aceitos: especie = CACHORRO, GATO, COELHO, OUTRA · porte = PEQUENO, MEDIO, GRANDE ·
sexo = MACHO, FEMEA · moradia = CASA_COM_QUINTAL, CASA_SEM_QUINTAL, APARTAMENTO.

## Exemplos (curl)
```
curl "http://localhost:8080/api/animais?especie=GATO&porte=PEQUENO"

curl -X POST http://localhost:8080/api/animais -H "Content-Type: application/json" -d '{
  "nome":"Frida","especie":"GATO","porte":"PEQUENO","sexo":"FEMEA","idade":"1 ano",
  "cidade":"Araraquara, SP","descricao":"Gata tranquila e carinhosa.","saude":"Vacinada",
  "responsavelNome":"Ana Lima","responsavelEmail":"ana@exemplo.com","responsavelTelefone":"(16) 99999-0000"}'

curl -X POST http://localhost:8080/api/interesses -H "Content-Type: application/json" -d '{
  "animalId":1,"nome":"Carlos Dias","cidade":"Araraquara","email":"carlos@exemplo.com",
  "telefone":"(16) 98888-0000","moradia":"CASA_COM_QUINTAL","possuiOutrosAnimais":false,
  "motivo":"Sempre tive cães e tenho espaço.","aceitouTermo":true}'
```

## Estrutura
- model/       entidades JPA (Animal, Interesse) e enums
- repository/  acesso ao banco (Spring Data JPA) e filtros do catálogo (AnimalSpecs)
- service/     regras de negócio
- controller/  rotas REST
- dto/         formatos de entrada/saída da API (com validação)
- exception/   tratamento padronizado de erros
- config/      CORS e dados iniciais de desenvolvimento

## Ainda não existe (próximas etapas)
Login/autenticação (hoje qualquer pessoa pode cadastrar animais), upload de fotos,
painel para o responsável ver os interesses recebidos, migrações Flyway e testes automatizados.
