# Phase 2 requirements

## Application requirements

- React frontend with product browsing, registration and checkout flow.
- Spring Boot REST microservices.
- PostgreSQL persistence for products, users and orders.
- Flyway database migrations.
- Actuator health endpoints for DevOps probes.
- Environment-variable based configuration.
- REST API contracts documented in `contracts/`.
- Sample seed products for testing.
- Unit-test-ready Maven projects.

## DevOps-friendly requirements

- Every service is independently buildable with Maven.
- Frontend is independently buildable with npm.
- No credentials are hard-coded except the deliberately fake local-development defaults in examples.
- Ports are explicit and stable.
- Health endpoints exist before containerization.
- No Terraform code is changed by this phase.
- Dockerfiles are intentionally postponed to Phase 3.
- Kubernetes manifests/Helm are intentionally postponed to the Kubernetes phase.

## Security note

The registration service currently stores a clearly marked demo password representation rather than a production authentication system. JWT/OAuth2, secret rotation, TLS, authorization and production identity integration should be implemented as a later security-focused exercise before production use.
