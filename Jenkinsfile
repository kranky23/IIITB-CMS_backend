pipeline {
    agent any
    tools {
        maven 'MAVEN'
    }
    stages {
        stage('Download the git repo') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/backend-refactored']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/akshathkaushal/IIITB-CMS-Backend.git']]])
            }
        }
    }
}