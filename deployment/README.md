# Deploying 3.semesterProjekt to DigitalOcean

This project builds and publishes `rav3n2594/coding-learning-platform:latest` using GitHub Actions when changes are pushed to `master`. The Java API listens on port 7070.

## Before editing your Droplet

SSH into your server and locate your existing deployment directory (in the course tutorial: `/home/jetty/deployment`). Do **not** delete or recreate your existing PostgreSQL data volumes.

```bash
cd /home/jetty/deployment
pwd
docker compose ps
cp docker-compose.yml docker-compose.yml.backup
```

Make sure your Docker Hub image is available and that the existing Compose file defines `backend` and `frontend` networks, and a PostgreSQL service whose Compose service name is `db`.

## Extend the existing Compose file

Edit the existing `docker-compose.yml` and add the service from [digitalocean-service.example.yml](digitalocean-service.example.yml) under the existing `services:` mapping. Do not add a second `services:` key or replace your existing Postgres/Caddy services.

The example uses the real healthcheck route `/auth/healthcheck`, not the tutorial's `/api/auth/healthcheck`.

The example publishes port 7070 directly for initial diagnostics. Once Caddy proxies the API, restrict that port as appropriate for your setup.

## Environment variables

Add these to the existing Droplet `.env` file, using the real database name and credentials from **your existing** PostgreSQL installation:

```dotenv
DB_NAME=your_existing_database_name
DB_USERNAME=postgres
DB_PASSWORD=replace_with_existing_postgres_password
CONNECTION_STR=jdbc:postgresql://db:5432/
SECRET_KEY=replace_with_a_strong_random_secret
ISSUER=semesterprojekt
TOKEN_EXPIRE_TIME=1800000
```

The Compose example sets `DEPLOYED=true` explicitly. Use `db` in `CONNECTION_STR` only if the existing PostgreSQL service is actually named `db`; containers must share a Docker network. Use the existing database password rather than changing it in the Compose file.

Do not commit the real `.env` file or secrets to GitHub.

## Start without disrupting PostgreSQL

Instead of the tutorial's `docker compose down`, which stops *all* services, validate the configuration and start only the new service:

```bash
docker compose config --quiet
docker compose pull semesterprojekt-api
docker compose up -d --no-deps semesterprojekt-api
docker compose ps
docker compose logs --tail=100 semesterprojekt-api
curl -i http://localhost:7070/auth/healthcheck
```

If the healthcheck succeeds, it proves the HTTP server is responsive; it does **not** prove database connectivity. Confirm a DB-dependent endpoint separately, and inspect logs if it fails.

If the database does not exist, confirm the correct name and create only the required empty database with the appropriate credentials—never overwrite your existing DB.

## Security and operations

- Avoid publishing port 7070 publicly after configuring Caddy HTTPS.
- Use a randomly generated token signing key; never use a tutorial example secret in production.
- Keep persistent Postgres volumes and backups intact.
- For future releases, pull the latest image and run `docker compose up -d --no-deps semesterprojekt-api`.
  Watchtower deployment test - October 28, 2026