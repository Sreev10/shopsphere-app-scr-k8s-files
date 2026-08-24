# After Phase 2

Do not add Kubernetes manifests or CI/CD yet.

The next phase is **Phase 3 — Dockerization**:

1. Write one Dockerfile per Spring Boot service.
2. Write the frontend Dockerfile.
3. Build each image locally.
4. Run the application as containers.
5. Verify service-to-service communication.
6. Tag images for Google Artifact Registry.
7. Push images after local validation.

Only after that should we proceed to Kubernetes deployment manifests/Helm and connect the application to the GKE infrastructure.
