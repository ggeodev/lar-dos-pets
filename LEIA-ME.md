# Lar de Patas — plataforma de adoção responsável

## Como rodar tudo
1. Banco + API (pasta backend):
   - `cd backend`
   - `docker compose up -d`
   - `mvn spring-boot:run`        (API em http://localhost:8080)
2. Site (pasta frontend):
   - Abra `frontend/index.html` no navegador, ou, na pasta frontend, rode
     `python3 -m http.server 5500` e acesse http://localhost:5500

Se o site mostrar "Não foi possível conectar ao servidor", a API não está rodando
ou o endereço em frontend/js/api.js (API_URL) está diferente.

Detalhes da API: backend/LEIA-ME.md
