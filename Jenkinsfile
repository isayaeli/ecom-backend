
pipeline {
    agent any

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

        stage('Install Kubectl'){
            steps {
                sh '''
                mkdir -p /var/jenkins_home/bin
                curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                chmod +x kubectl
                mv kubectl /var/jenkins_home/bin/kubectl
                export PATH=/var/jenkins_home/bin:$PATH
                kubectl version --client
                '''
            }
        }



        
        stage('Deploy') {
    steps {
        sh '''
        /var/jenkins_home/bin/kubectl config use-context minikube
        minikube image load spring-app:latest
        /var/jenkins_home/bin/kubectl -n demo apply -f deployment.yaml
        '''
    }
}




    
        // stage('Deploy to Minikube') {
        //     steps {
        //         sh '''
        //             export KUBECONFIG=/var/jenkins_home/.kube/config
        //             kubectl config use-context minikube
                    
        //             echo "Current kubectl context:"
        //             kubectl config current-context
        //             echo "Available deployments:"
        //             kubectl get deployments

        //             # Update or create deployment
        //             if kubectl get deployment $APP_NAME > /dev/null 2>&1; then
        //                 echo "Updating existing deployment..."
        //                 kubectl set image deployment/$APP_NAME $APP_NAME=$DOCKER_IMAGE:$BUILD_NUMBER --record
        //             else
        //                 echo "Creating new deployment..."
        //                 kubectl apply -f deployment.yaml --validate=false
        //             fi
                    
        //             kubectl rollout status deployment/$APP_NAME --timeout=300s
        //             kubectl get pods
        //         '''
        //     }
        // }
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