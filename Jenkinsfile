pipeline {
    agent any

    environment {
        APP_NAME     = "spring-app"
        DOCKER_IMAGE = "spring-app"
        USE_MINIKUBE_DIRECT = "true"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Setup Environment') {
            steps {
                sh '''
                    # Verify Java installation
                    echo "JAVA_HOME: $JAVA_HOME"
                    echo "Java version:"
                    java -version
                    echo "Maven version:"
                    ./mvnw --version
                '''
            }
        }
        
        stage('Build JAR') {
            steps {
                sh '''
                    # Create Maven home in workspace
                    mkdir -p ${WORKSPACE}/.m2/repository
                    
                    # Build with Maven wrapper using local repository
                    ./mvnw clean package -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2/repository
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .
                    docker images | grep $DOCKER_IMAGE
                '''
            }
        }

        stage('Install kubectl') {
            steps {
                sh '''
                    # Install kubectl
                    curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                    chmod +x kubectl
                    mv kubectl /usr/local/bin/
                    
                    # Verify installation
                    kubectl version --client
                '''
            }
        }

        stage('Install minkube') {
            steps {
                sh '''
                    # Check what's available
                    echo "Available tools:"
                    which docker || echo "Docker not found"
                    which kubectl || echo "kubectl not found"
                    which minikube || echo "minikube not found"
                    
                    # Install minikube if missing
                    if ! command -v minikube &> /dev/null; then
                        echo "Installing minikube..."
                        curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
                        install minikube-linux-amd64 /usr/local/bin/minikube
                        rm minikube-linux-amd64
                    fi
                    
                    # Verify installation
                    minikube version
                '''
            }
        }
        

        stage('Deploy') {
            steps {
                script {
                    
                        sh '''
                            kubectl -- apply -f deployment.yaml
                            kubectl -- rollout status deployment/$APP_NAME --timeout=300s
                            kubectl -- get pods
                        '''
                   
                }  
            }
        
        }
    }
    
    
    post {
        success { echo "Deployed ${DOCKER_IMAGE}" }
        failure { echo "Pipeline failed." }
        always  { sh 'docker image prune -f || true' }
    }
}