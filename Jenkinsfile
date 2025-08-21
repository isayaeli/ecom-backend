// pipeline {
//     agent {
//         docker {
//             image 'docker:24.0-dind'
//             args '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.kube:/root/.kube'
//         }
//     }

//     environment {
//         APP_NAME     = "spring-app"
//         DOCKER_IMAGE = "spring-app"
//     }

//     stages {
//         stage('Checkout') {
//             steps {
//                 checkout scm
//             }
//         }
//         stage('Build JAR') {
//             steps {
//                 sh '''
//                     export HOME=$WORKSPACE
//                     export MAVEN_USER_HOME=$WORKSPACE/.m2
//                     mkdir -p $MAVEN_USER_HOME
//                     ./mvnw clean package -DskipTests
//                 '''
//             }
//         }

//         stage('Build Docker Image in Minikube') {
//             steps {
//                 sh '''
//                   # Use Minikube's Docker daemon
//                   #eval $(minikube docker-env)
//                   docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .
//                 '''
//             }
//         }

//         stage('Deploy to Minikube') {
//             steps {
//                 sh '''
//                   # Update deployment if exists, otherwise create it
//                   kubectl set image deployment/$APP_NAME $APP_NAME=$DOCKER_IMAGE:$BUILD_NUMBER --record || \
//                   kubectl apply -f k8s/deployment.yaml
                  
//                   # Wait until rollout finishes
//                   kubectl rollout status deployment/$APP_NAME
//                 '''
//             }
//         }
//     }
// }


// pipeline {
//     agent any

//     environment {
//         APP_NAME     = "spring-app"
//         DOCKER_IMAGE = "spring-app"
//         MAVEN_HOME   = "${WORKSPACE}/.m2"  // Use workspace instead of root
//         JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64" 
//     }

//     stages {
//         stage('Checkout') {
//             steps {
//                 checkout scm
//             }
//         }
        
//         stage('Build JAR') {
//             steps {
//                 sh '''
//                     # Create Maven home in workspace (where we have write permissions)
//                     mkdir -p ${WORKSPACE}/.m2/repository
                    
//                     # Build with Maven wrapper using local repository
//                     ./mvnw clean package -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2/repository
//                 '''
//             }
//         }

//         stage('Build Docker Image') {
//             steps {
//                 sh '''
//                     eval $(minikube docker-env)
//                     docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .
//                 '''
//             }
//         }
        

//         stage('Deploy to Minikube') {
//             steps {
//                 sh '''
//                     export KUBECONFIG=/var/jenkins_home/.kube/config
//                     kubectl config use-context minikube
//                     echo "Current kubectl context:"
//                     kubectl config current-context

//                     # Use Minikube's Docker daemon if needed
//                     eval $(minikube docker-env 2>/dev/null) || true
                    
//                     # Update deployment
//                     kubectl set image deployment/$APP_NAME $APP_NAME=$DOCKER_IMAGE:$BUILD_NUMBER --record || \\
//                     kubectl apply -f deployment.yaml --validate=false
                    
//                     kubectl rollout status deployment/$APP_NAME
//                 '''
//             }
//         }
//     }
// }



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

       
        // stage('Build Docker Image') {
        //     steps {
        //         sh '''
        //             docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .
        //             docker images | grep $DOCKER_IMAGE
        //         '''
        //     }
        // }

    
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