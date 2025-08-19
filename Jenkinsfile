pipeline {
    agent {
        docker {
            image 'docker:24.0-dind'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.kube:/root/.kube'
        }
    }

    environment {
        APP_NAME     = "spring-app"
        DOCKER_IMAGE = "spring-app"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build JAR') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH
                    export HOME=$WORKSPACE
                    export MAVEN_USER_HOME=$WORKSPACE/.m2
                    mkdir -p $MAVEN_USER_HOME
                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image in Minikube') {
            steps {
                sh '''
                  # Use Minikube's Docker daemon
                  #eval $(minikube docker-env)
                  docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .
                '''
            }
        }

        stage('Deploy to Minikube') {
            steps {
                sh '''
                  # Update deployment if exists, otherwise create it
                  kubectl set image deployment/$APP_NAME $APP_NAME=$DOCKER_IMAGE:$BUILD_NUMBER --record || \
                  kubectl apply -f k8s/deployment.yaml
                  
                  # Wait until rollout finishes
                  kubectl rollout status deployment/$APP_NAME
                '''
            }
        }
    }
}
