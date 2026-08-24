#!/usr/bin/env bash
set -euo pipefail
curl -fsS http://localhost:8081/actuator/health >/dev/null
curl -fsS http://localhost:8082/actuator/health >/dev/null
curl -fsS http://localhost:8083/actuator/health >/dev/null
curl -fsS http://localhost:8084/actuator/health >/dev/null
echo "All ShopSphere services are healthy."
