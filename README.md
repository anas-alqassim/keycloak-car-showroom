# Development guide

## Starting Keycloak on local machine
run ```docker compose -f docker-compose.postgres.yml up``` to start the server with a Postgres database.


## Added extension
- Using email OTP for resting password and verifying account

## Automatic realm import
Keycloak will start with `--import-realm` handle which means it will only import the realm on clean installation.

# Keycloak Admin Console
Keycloak Admin Console is available at http://localhost:8080/auth/admin/master/console/

Default credentials are `admin:admin`

