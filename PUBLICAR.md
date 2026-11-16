# Publicando o projeto

GitHub Pages hospeda só o frontend. A API e o banco ficam em outro serviço (ex.: Render).

## Primeira publicação
1. Suba a pasta inteira (frontend, backend, .github) para um repositório PÚBLICO no GitHub.
2. Render: crie um PostgreSQL e um Web Service (Root Directory = backend, ambiente Docker) com as variáveis:
   DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, CORS_ORIGINS (https://SEU-USUARIO.github.io)
   e JWT_SECRET (texto longo e aleatório, no mínimo 32 caracteres; ex.: gerado em um gerenciador de senhas).
3. Edite `frontend/js/api.js` e troque API_PRODUCAO pelo endereço https do backend (termina em /api).
4. GitHub: Settings > Pages > Source = "GitHub Actions" e rode o workflow na aba Actions.

## Atualizando (versões com Flyway e Swagger)
1. No Render, em Environment, **apague a variável `DDL_AUTO`** se ela ainda existir (ela não é mais necessária).
2. Suba os arquivos novos para o GitHub (push). O Render faz o deploy sozinho.
3. Na primeira subida o Flyway "adota" as tabelas que já existem (nenhum dado é apagado) e passa a controlar o esquema.
4. Confira: `/api/animais` mostra os animais e `/swagger-ui.html` abre a documentação.
5. Se o deploy falhar com erro contendo "Schema-validation" nos Logs, defina `DDL_AUTO` = `none`, faça o deploy
   (a API sobe sem conferir o esquema) e me envie o log do erro.
Os testes do backend rodam no GitHub (aba Actions, workflow "Testes do backend"). Eles não afetam o deploy.
