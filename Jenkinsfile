
pipeline {
    agent {
        docker {
            image 'lachlanevenson/k8s-kubectl:v1.30.0'
        }
    }

    environment {
        APP_NAME     = "spring-app"
        DOCKER_IMAGE = "spring-app"
        MAVEN_HOME   = "${WORKSPACE}/.m2"
        // Use which java to dynamically find JAVA_HOME
        JAVA_HOME    = sh(script: 'dirname $(dirname $(readlink -f $(which java)))', returnStdout: true).trim()
        PATH         = "${JAVA_HOME}/bin:${PATH}"
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

       
     stage('Deploy') {
            steps {
                sh '''
                kubectl config use-context minikube
                minikube image load spring-app:latest
                kubectl -n demo apply -f k8s/deployment.yaml
                kubectl -n demo apply -f k8s/service.yaml
                '''
            }
        }

    }
    
    // post {
    //     always {
    //         sh '''
    //             echo "Build completed with status: $currentBuild.result"
    //             # Cleanup Minikube docker env to avoid conflicts
    //             eval $(minikube docker-env -u) 2>/dev/null || true
    //         '''
    //     }
    // }
}