
---

### README for Project 2: ShopSphere Microservices & Kubernetes Deployment

# ShopSphere Microservices Platform

ShopSphere is a cloud-native e-commerce application composed of a React frontend and Spring Boot/Java microservices, designed to deploy on Kubernetes with automated CI/CD via Jenkins.

---

## Services Overview

| Component | Stack | Port | Description |
| :--- | :--- | :--- | :--- |
| **frontend** | React, Vite, Nginx | `80` / `5173` | Customer web interface |
| **user-service** | Java, Spring Boot, Maven | `8081` | Authentication, customer profiles, and authorization |
| **product-service** | Java, Spring Boot, Maven | `8082` | Product catalog, categories, and inventory |
| **order-service** | Java, Spring Boot, Maven | `8083` | Order creation, tracking, and checkout workflows |
| **payment-service** | Java, Spring Boot, Maven | `8084` | Payment processing integration |

---

## Repository Structure

```text
shopsphere-app-scr-k8s-files/
├── Jenkinsfile                          # Pipeline definition for build, test, & deploy
├── application-scr/                     # Application source code
│   ├── docker-compose.yml               # Local multi-service orchestration
│   ├── frontend/                        # React frontend
│   │   ├── Dockerfile
│   │   ├── package.json
│   │   └── src/
│   ├── services/                        # Backend microservices
│   │   ├── user-service/
│   │   ├── product-service/
│   │   ├── order-service/
│   │   └── payment-service/
│   ├── contracts/                       # API definitions & OpenAPI specifications
│   ├── docs/                            # Architecture diagrams & technical docs
│   └── scripts/                         # Testing and verification utilities
└── kubernetes/                          # Kubernetes deployment manifests
    ├── namespace/                       # Namespace definitions
    ├── config/                          # ConfigMaps and Secrets
    ├── ingress/                         # Ingress rules for external access
    ├── frontend/                        # Frontend Deployment & Service
    ├── user-service/                    # User service manifests
    ├── product-service/                 # Product service manifests
    ├── order-service/                   # Order service manifests
    └── payment-service/                 # Payment service manifests


Local Development with Docker Compose
To spin up all services and dependencies locally:

cd application-scr

# Copy environment template files
find . -name ".env.example" -exec sh -c 'cp "$1" "${1%.example}"' _ {} \;

# Run all containers
docker-compose up --build

Access the frontend at http://localhost:5173 (or port configured in docker-compose.yml).

Kubernetes Deployment
Prerequisites
⚬	Kubernetes cluster running (e.g., GKE, Minikube, kind)
⚬	kubectl configured with cluster credentials
⚬	NGINX Ingress Controller installed in the cluster
Deployment Steps

# 1. Create namespace
kubectl apply -f kubernetes/namespace/

# 2. Apply shared configs and secrets
kubectl apply -f kubernetes/config/

# 3. Deploy microservices
kubectl apply -f kubernetes/user-service/
kubectl apply -f kubernetes/product-service/
kubectl apply -f kubernetes/order-service/
kubectl apply -f kubernetes/payment-service/

# 4. Deploy frontend
kubectl apply -f kubernetes/frontend/

# 5. Apply Ingress routing
kubectl apply -f kubernetes/ingress/

Verification

# Check running pods
kubectl get pods -n shopsphere

# Inspect services and ingress endpoints
kubectl get svc,ingress -n shopsphere

# Execute smoke tests
./application-scr/scripts/smoke-test.sh

vv

