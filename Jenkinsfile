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
                        branches         : [[name: "*/${params.branch}"]],
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
                script {
                    if (params.branch.contains('release/')) {
                        sh "./gradlew testReleaseUnitTest"
                    }
                    else {
                        sh "./gradlew testDebugUnitTest"
                    }
                }
            }
        }
        stage("build") {
            steps {
                script {
                    if (params.branch.contains('release/')) {
                        sh "./gradlew assembleRelease"
                    }
                    else {
                        sh "./gradlew assembleDebug"
                    }
                }
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