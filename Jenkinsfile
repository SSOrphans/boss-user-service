pipeline {
    agent any
    environment {
        serviceName = "ssor-user"
    }
    tools {
        git 'git'
        maven 'maven'
    }
    stages {
        stage('Init submodule') {
            steps {
                sh 'git submodule update --init'
            }
        }
        stage('Maven Build') {
            steps {
                echo "Building $serviceName with maven"
                sh 'mvn clean package'
            }
        }
        stage('Quality Analysis') {
            steps {
                echo "Performing Quality Analysis for $serviceName"
            }
        }
        stage('Quality Gate') {
            steps {
                echo "Waiting for Quality Analysis"
            }
        }
        stage('Docker Build') {
            environment {
                commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            }
            steps {
                echo "Building and deploying docker image for $serviceName"
                withCredentials([string(credentialsId: 'awsRepo', variable: 'awsRepo')]) {
                    script {
                        docker.build('$serviceName-repo:$commitHash')
                        docker.withRegistry('https://$awsRepo', 'ecr:us-east-2:aws-credentials') {
                            docker.image('$serviceName-repo:$commitHash').push('$commitHash')
                        }
                    }
                }
            }    
        }
        stage('Deploy') {
            environment {
                commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                targetGroup = sh(script: 'aws elbv2 describe-target-groups --region=us-east-2 --query "TargetGroups[?TargetGroupName==\'ssor-tg\'].TargetGroupArn" --output text', returnStdout: true).trim()
            }
            steps {
                echo 'Deploying cloudformation..'
                withCredentials([string(credentialsId: 'awsAccountId', variable: 'awsAccountId'), string(credentialsId: 'awsRepo', variable: 'awsRepo')]) {
                    sh 'aws cloudformation deploy --stack-name $serviceName-stack --template-file ./ecs.yaml '+
                    '--parameter-overrides ApplicationName=$serviceName ApplicationEnvironment=dev '+
                    'ECRRepositoryUri=$awsRepo/$serviceName-repo:$commitHash '+
                    'ExecutionRoleArn=arn:aws:iam::$awsAccountId:role/ecsTaskExecutionRole '+
                    'TargetGroupArn=$targetGroup '+
                    '--role-arn arn:aws:iam::$awsAccountId:role/awsCloudFormationRole '+
                    '--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM --region us-east-2'
                }
            }
        }
    }
    post {
        cleanup {
            sh 'docker system prune -f'
            cleanWs()
        }
    }
}