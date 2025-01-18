pipeline {
    agent {
        docker {
            image 'maven:3.8.6-openjdk-11'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        REPO_URL = 'https://github.com/MrChhoke/Jobify'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                git url: env.REPO_URL
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests with Testcontainers...'
                sh 'mvn test -Dtestcontainers.reuse.enable=true'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
