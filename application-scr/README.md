# ShopSphere — Phase 2 Application Source Code

This repository is the **application layer** for the ShopSphere DevOps practice project.
It is intentionally separated from the Terraform infrastructure already completed.

## Architecture

- Frontend: React + Vite
- Backend: Spring Boot microservices
  - product-service: product catalogue
  - user-service: user registration
  - order-service: order creation and retrieval
  - payment-service: mock payment processing
- Database: PostgreSQL (one database, service-owned schemas)
- API style: REST/JSON
- Dockerfiles/Kubernetes/Helm/Jenkins are deliberately **not** included in Phase 2; those are handled in later DevOps phases.

## Runtime ports

| Component | Port |
|---|---:|
| Frontend | 5173 |
| Product service | 8081 |
| User service | 8082 |
| Order service | 8083 |
| Payment service | 8084 |
| PostgreSQL | 5432 |

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 24 LTS + npm
- PostgreSQL 15+ (or the PostgreSQL-compatible Cloud SQL instance later)

Spring Boot 3.5.16 is used for predictable training compatibility. Node.js 24 LTS is the frontend runtime target.

## Local database

Create a database named `shopsphere` and a user with permission to create schemas/tables.
Each service uses its own schema:

- products
- users
- orders

Example environment variables are in `.env.example` files in each service.

## Start backend services

Open four terminals:

```bash
cd services/product-service && mvn spring-boot:run
cd services/user-service && mvn spring-boot:run
cd services/order-service && mvn spring-boot:run
cd services/payment-service && mvn spring-boot:run
```

Then start the frontend:

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173.

## Health checks

Each Spring Boot service exposes:

- `/actuator/health`
- `/actuator/info`

## API summary

### Product service
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`

### User service
- `POST /api/users/register`
- `GET /api/users/{id}`

### Order service
- `POST /api/orders`
- `GET /api/orders/user/{userId}`

### Payment service
- `POST /api/payments`
- `GET /api/payments/health`

## DevOps phase boundary

Phase 2 = application source code and local runtime requirements.

Next:

1. Phase 3 — Dockerization: Dockerfiles, images, networks, container configuration, and local container testing.
2. Later phases — Kubernetes manifests/Helm, GKE deployment, CI/CD, security scanning, GitOps, monitoring, and troubleshooting.
