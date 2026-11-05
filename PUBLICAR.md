# Publicando o projeto

GitHub Pages hospeda só sites estáticos (o frontend). A API Spring Boot e o PostgreSQL
precisam de outro serviço. Passos resumidos:

1. Suba a pasta inteira (frontend, backend, .github) para um repositório PÚBLICO no GitHub.
2. Backend + banco em um serviço de hospedagem (ex.: Render): crie um PostgreSQL, depois um
   Web Service apontando para a pasta `backend` (Root Directory = backend, ambiente Docker),
   com as variáveis DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD e CORS_ORIGINS.
3. Edite `frontend/js/api.js` e troque API_PRODUCAO pelo endereço https do seu backend (termina em /api).
4. No GitHub: Settings > Pages > Source = "GitHub Actions". Faça push na main: o workflow publica.
5. O site ficará em https://SEU-USUARIO.github.io/NOME-DO-REPOSITORIO/
