# DemoPlatform

Architecture microservices générée automatiquement avec GestoZip.

## Modules

- eureka-server
- api-gateway
- service-a
- service-b

## Lancement

```bash
docker compose up --build
```

## Accès

- Eureka : `http://localhost:8761`
- Gateway : `http://localhost:9000`

## Endpoints de test

- `GET /api/service-a/hello`
- `GET /api/service-b/hello`
