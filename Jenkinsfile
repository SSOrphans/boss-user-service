node {
    try {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'aws-cli', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            stage('Checkout') {
                echo "Checking out $serviceName"
                checkout scm
            }
            withEnv(['serviceName=boss-user', "commitHash=${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"]) {
                stage('Build') {
                    withMaven(jdk: 'openjdk-11') {
                        echo "Building $serviceName with maven"
                        sh 'mvn clean package'
                    }
                }
            }
        }
    }
    catch (exc) {
        echo "$exc"
    } finally {
        stage('Cleanup') {
            echo "cleanup"
            sh 'rm -rf *'
        }
    }
}