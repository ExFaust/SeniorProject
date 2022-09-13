#!groovy
pipeline {
    agent {
        docker {
            image 'exfaust/seniorproject:1.0.8'
        }
    }
    environment{
        ANDROID_HOME = "/sdk"
    }
    stages {
        stage("init") {
            steps {
                sh 'chmod 755 ./gradlew'
                sh "chmod +x gradlew"
                sh "./gradlew --no-daemon"
            }
        }
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