#!groovy
pipeline {
    agent {
        docker {
            image 'exfaust/seniorproject:1.0.5'
        }
    }
    stages {
        stage('detekt') {
            steps {
                checkout([
                        $class           : 'GitSCM',
                        branches         : [[name: '*/master']],
                        extensions       : [],
                        userRemoteConfigs: [[url: 'https://github.com/ExFaust/SeniorProject.git']]
                ])
            }
        }
        stage("init") {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew"
            }
        }
        stage("lint") {
            steps {
                sh "./gradlew lintDebug"
            }
        }
        stage("test") {
            steps {
                sh "./gradlew testDebugUnitTest"
            }
        }
        stage("build") {
            steps {
                sh "./gradlew assembleDebug"
            }
        }
        stage("publish") {
            steps {
                sh "./gradlew publishToMavenLocal"
            }
        }
    }
    post {
        failure {
            script {
                if (getContext(hudson.FilePath)) {
                    archiveArtifacts(artifacts: '**/build/reports/**', allowEmptyArchive: true)
                }
            }
        }
    }
}