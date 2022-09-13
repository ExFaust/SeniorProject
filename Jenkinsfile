#!groovy
pipeline {
    agent {
        docker {
            image 'exfaust/seniorproject:1.0.8'
        }
    }
    parameters {
        string(name: 'branch', defaultValue: 'development', description: 'Branch to build')
    }
    stages {
        stage("branch") {
            steps {
                checkout([
                        $class           : 'GitSCM',
                        branches         : [[name: '*/CI/CD_homework']],
                        //branches         : [[name: 'release/*'], [name: 'feature/*'], [name: 'bugfix/*']],
                        extensions       : [],
                        userRemoteConfigs: [[url: 'https://github.com/ExFaust/SeniorProject.git']]
                ])
            }
        }
        stage("init") {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew --no-daemon"
            }
        }
        stage('detekt') {
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