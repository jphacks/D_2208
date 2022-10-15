pipeline {
    agent any

    environment {
        JENKINS_NODE_COOKIE = 'dontKillMe'
    }

    stages {
        stage("checkout") {
            steps {
                checkout scm
            }
        }

        stage("backend-build") {
            agent {
                docker {
                    image "openjdk:11-jdk"
                    reuseNode true
                }
            }
            steps {
                sh "./gradlew bootJar"
            }
        }

        stage("deploy") {
            steps {
                sh "docker-compose up -d"
                sh "docker run --rm -d -v $PWD/app/build/libs:/app -p ${PORT}:8080 openjdk:11-jdk java -jar -Dspring.profiles.active=${ENVIRONMENT} app/smartpointer_1.0.0_SNAPSHOT.jar"
            }
        }
    }

    post {
        always {
            discordSend link: env.BUILD_URL,
                        result: currentBuild.currentResult,
                        title: JOB_NAME,
                        webhookURL: DISCORD_WEBHOOK_URL
        }
    }
}