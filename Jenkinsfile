node {
    try {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'aws-cli', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                withEnv(["AWS_ELB_DNS=${sh(script: 'aws elbv2 --region us-east-2 describe-load-balancers --query LoadBalancers[*].DNSName --output text', returnStdout: true).trim()}",
                        'serviceName=boss-user', "commitHash=${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"]) {

                stage('Checkout') {
                    echo "Checking out $serviceName"
                    checkout scm
                    sh 'git submodule update --init'
                }

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