# Publicando o projeto

GitHub Pages hospeda só o frontend. A API e o banco ficam em outro serviço (ex.: Render).

## Primeira publicação
1. Suba a pasta inteira (frontend, backend, .github) para um repositório PÚBLICO no GitHub.
2. Render: crie um PostgreSQL e um Web Service (Root Directory = backend, ambiente Docker) com as variáveis:
   DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, CORS_ORIGINS (https://SEU-USUARIO.github.io)
   e JWT_SECRET (texto longo e aleatório, no mínimo 32 caracteres; ex.: gerado em um gerenciador de senhas).
3. Edite `frontend/js/api.js` e troque API_PRODUCAO pelo endereço https do backend (termina em /api).
4. GitHub: Settings > Pages > Source = "GitHub Actions" e rode o workflow na aba Actions.

## Atualizando de uma versão anterior (sem login) para esta
O modelo do banco mudou. Faça UMA vez:
1. No Render, adicione a variável `DDL_AUTO` = `create` e faça o deploy.
2. Abra /api/animais: devem aparecer os animais. (Isso apagou e recriou as tabelas.)
3. **Apague a variável DDL_AUTO** e salve. Se ela ficar, cada reinício apaga todos os dados.
Depois suba os arquivos novos para o GitHub (push) para o Pages e o Render atualizarem.
