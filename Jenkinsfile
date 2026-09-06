pipeline {
    agent any
    options {
        timestamps()
        diasbleConcurrentBuilds()
        skipDefaultCheckout(true)
    }
    environment {
        PROJECT_ID = 'gke-project-1-500610'
        REGION = 'asia-south1'
        GKE_CLUSTER = 'shopsphere-dev-gke'
        ARTIFACT_REPOSITORY = 'dev-shopsphere'
        REGISTRY = 'asia-south1-docker.pkg.dev/gke-project-1-500610/dev-shopsphere'
        NAMESPACE = 'shopspahere'
        DB_NAME = 'ecommerce'
        DB_HOST = '<CLOUD_SQL_PRIVATE_IP>'
    }
    stages{
        stage ('checkout') {
            steps {
                checkout scm
                scrit {
                    env.IMAGE_TAG = sh (
                        script: 'git rev-parse --short-12 HEAD',
                        returnStdout: true
                    ).trim()
                    echo "Buildiing Git Commit: ${env.IMAGE_TAG}"
                    
                }
            }
        }

        stage ('Verify Tools') {
            steps {
                sh '''
                    set -e
                    echo "==== Java ===="
                    java -version

                    echo "=== Maven ==="
                    mvn -version

                    echo "=== Node ==="
                    node --version

                    echo "=== NPM ==="
                    npm --version

                    echo "=== Docker ==="
                    docker --version

                    echo "=== Googlr Cloud ==="
                    gcloud --version 

                    echo "=== kubectl ==="
                    kubectl version --client
                '''    
            }
        }

        stage ('Backend Tests') {
            steps {
                sh '''
                    set -e
                    echo "Testing product-service"
                    cd application-scr/services/product-service
                    mvn -B clean test
                    cd ../../..

                    echo "Testing user-service"
                    cd application-scr/services/user-service
                    mvn -B clean test
                    cd ../../..

                    echo "Testing order-service"
                    cd application-scr/services/order-service
                    mvn -B clean test
                    cd ../../..

                    echo "Testing payment-service"
                    cd application-scr/services/payment-service
                    mvn -B clean test 
                    cd ../../..
                '''    

            }
        }
        stage ('Frontend Test') {
            steps {
                sh '''
                    set -e
                    cd application-scr/frontend
                    npm ci
                    npm run build
                '''    
            }
        }
        stage ('GCP Authentication') {
            steps {
                sh '''
                    gcloud config set project "$gke-project-1-500610"
                    echo "Current GCP identity:"
                    gcloud auth list
                    echo "Current project:"
                    gcloud config get-value project
                '''    
            }
        }
        stage ('Configure Artifact Registry') {
            steps {
                sh '''
                    set -e
                    gcloud auth configure-docker \
                        "$REGION-docker.pkg.dev" \
                        --quiet
                    echo "Artifact Registry configured"
                '''        
            }
        }
        stage ('Build Backend Images') {
            steps {
                sh '''
                    set -e
                    echo "Building product-service"
                    docker build \
                        -t "$REGISTRY/product-service:$IMAGE_TAG" \
                        -t "$REGISTRY/product-serice:latest" \
                        application-scr/services/product-service

                    echo "Building user-service"
                    docker build \
                        -t "$REGISTRY/user-service:$IMAGE_TAG" \
                        -t "$REGISTRY/user-service:latest" \
                        application-scr/services/user-service

                    echo 'Building order-service"
                    docker build \
                        -t "$REGISTRY/order-service:$IMAGE_TAG" \
                        -t "$REGISTRY/order-service:latest" \
                        application-scr/services/order-service

                    echo 'Building payment-service"
                    docker build \
                        -t "$REGISTRY/payment-service
                '''        

            }
        }
        stage ('build Frontend Image') {
            steps {
                sh '''
                    set -e
                    cd application-scr/frontend
                    cat > .env.production <<EOF
VITE_PRODUCT_API=/product
VITE_USER_API=/user
VITE_ORDER_API=/order
VITE_PAYMENT_API=/payment
EOF
                    npm ci
                    npm run build
                    cd ../..
                    docker build \
                    -t "$REGISTRY/frontend:$IMAGE_TAG" \
                    -t "$REGISTRY/frontend:latest" \
                    application-scr/frontend
                '''    

            }
        }
        stage ('Push Images') {
            steps {
                sh '''
                    set -e
                    echo "Pushing product-service"
                    docker push \
                        "$REGISTRY/product-service:$IMAGE_TAG"
                    docker push \
                        "$REGISTRY/product-service:latest"

                    echo  "Pushing user-service"
                    docker push \
                        "$REGISTRY/user-service:$IMAGE_TAG"
                    docker push \
                        "$REGISTRY/user-service:latest"

                    echo "Pushing order-service"    
                    docker push \
                        "$REGISTRY/order-service:$IMAGE_TAG"
                    docker push \
                        "$REGISTRY/order-service:latest" 

                    echo "Pushing payment-service"
                    docker push \
                        "$REGISTRY/payment-service:$IMAGE_TAG"
                    docker push \
                        "$REGISTRY/payment-service:latest"

                    echo " Pushing frontend"
                    docker push \
                        "$REGISTRY/frontend:$IMAGE_TAG"
                    docker push \
                        "$REGISTRY/frontend:latest"
                '''            

            }
        }

        stage ('Connect to GKE') {
            steps {
                sh '''
                    set -e
                    gcloud container clusters get-credentials \
                        "$GKE_CLUSTER" \
                        --region "$REGION" \
                         --project "$PROJECT_ID"
                    echo "=== GKE Cluster ==="
                    kubectl cluster-info
                    echo "=== Nodes ==="
                    kubectl get nodes
                '''    
            }
        }

        stage ('Deploy Namespace') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/namespace/namesapce.yaml
                '''        
            }
        }

        stage ('Deply ConfigMap') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kuberenets/configmap/configmap.yaml
                '''        
            }
        }

        stage ('Deploy Database Secret') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId; 'shopsphere-db'
                        usernameVariable: 'DB_USERNAME'
                        passwordVariable: 'DB_PASSWORD'
                    )
                ]) {
                    sh '''
                    set -e
                    kubectl -n "$NAMESAPCE" \
                        create secret generic shopsphere-secrets \
                        --from-literal=POSTGRES_USER="$DB_USERNAME" \
                        --from-literal=POSTGRES_PASSWORD="$DB_PASSWORD" \
                        --dry-run=client \
                        -o yaml |
                        kubectl apply -f -
                    '''    
                }
            }
        }
        stage ('Deploy frontend') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/frontend/deployment.yaml
                    kubectl apply \
                        -f kubernetes/frontend/service.yaml
                    kubectl -n "$NAMESPACE" set image \
                        deployment/frontend \
                        frontend="$REGISTRY/frontend:$IMAGE_TAG"
                '''        

            }
        }
        stage ('Deploy Product Service') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/product-service/deployment.yaml
                    kubectl apply \
                        -f kubernetes/product-service/service.yaml
                    kubectl -n "$NAMESPACE" set image \
                        deployment/product-service \
                        product-service="$REGISTRY/product-service:$IMAGE_TAG"
                '''                
            }
        }

        stage ('Deploy User Service') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/user-service/deployment.yaml
                    kubectl apply \
                        -f kubernetes/user-service/service.yaml
                    kubectl -n "$NAMESPACE" set image \
                        deployment/user-service \
                        user-service="$REGISTRY/user-service:$IMAGE_TAG"
                '''                
            }
        }
        stage ('Deploy Order Service') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/order-service/deployment.yaml
                    kubectl apply \
                        -f kubernetes/order-service/service.yaml
                    kubectl -n "$NAMESPACE" set image \
                        deployment/order-service \
                        order-service="$REGISTRY/order-service:$IMAGE_TAG"
                '''                
            }
        }

        stage ('Deploy payment service') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/payment-service/deployment.yaml
                    kubectl apply \
                        -f kubernetes/oayments-service/service.yaml
                    kubectl -n "$NAMESPACE" set image 
                        deployment/payment-service \
                        payment-service="$REGISTRY/payment-service:$IMAGE_TAG"
                '''                
            }
        }

        stage ('Deploy ingress') {
            steps {
                sh '''
                    set -e
                    kubectl apply \
                        -f kubernetes/ingress/ingress.yaml
                '''        
            }
        }

        stage ('Verify Deployment') {
            steps {
                sh '''
                    set -e
                    echo "=== FRONTEND ==="
                    kubectl -n "$NAMESPACE" rollout status \
                        deployment/frontend \
                        --timeout=5m

                    echo "=== PRODUCT ==="
                    kubectl -n "$NAMESAPCE" rollout status \
                        deployment/product-service \
                        --timeout=5m    
                    echo "===USER ==="
                    kubectl -n "$NAMESPACE" rollout status \
                        deployment/user-service \
                        --timeout=5m
                    echo "=== ORDER ==="
                    kubecl -n "$NAMESPACE" rollout status \
                        deployment/order-service \
                        --timeout=5m
                    echo "=== PAYMENT ==="
                    kubectl -n "$NAMESPACE" rollout status \
                        deployment/payment-service \
                        --timeout=5m 
                '''                  
            }
        }
        stage ('Deployment Summary') {
            steps {
                sh '''
                    echo ""
                    echo "============"
                    echo " SHOPSPHERE DEPLOYMENT"
                    echo "============="
                    echo ""
                    echo "====PODS====="
                    kubectl -n "$NAMESPACE" get pods -o wide
                    echo ""
                    echo "========== SERVICES ========="
                    kubectl -n "$NAMESPACE" get services
                    echo ""
                    echo "======== INGRESS ======"
                    kubectl -n "$NAMESPACE" get ingress
                    echo ""
                    echo "====== DEPLOYMENTS ====="
                    kubectl -n "$NAMESPACE" get deployments
                    echo ""
                    echo "Image tag:"
                    echo "$IMAGE_TAG"
                '''        
            }
        }
    }
    // stages closes
    post {
            success {
                echo """
                ======================================
                SHOPSPHERE DEPLOYMENT SUCCESSFUL
                ======================================
                Image tag: ${env.IMAGE_TAG}
                GKE Cluster: ${env.GKE_CLUSTER}
                Namespace: ${env.NAMESPACE}
                """
            }
            failure {
                echo """
                =================================
                SHOPSPHERE DEPLOYMENT FAILED
                =================================
                check the failed Jenkins stage.
                useful commands:
                kubectl -n shopsphere get pods
                kubectl -n shopsphere get events --sort-by=.lastTimestamp
                kubectl -n shopsphere describe pods
                """
            }
            always {
                sh '''
                    echo "====== FINAL POD STATUS ======="
                    kubectl -n shopsphere get pods 2>/dev/null || true
                    echo "============= FINAL INGRESS STATUS ========="
                    kubectl -n shopsphere get ingress 2>/dev/null || true
                '''    
            }

    }
}

// pipeline closes