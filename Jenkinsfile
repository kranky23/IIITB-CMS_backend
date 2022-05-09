pipeline {
    agent any
    tools {
        maven 'MAVEN'
    }
    environment {
        dockerImage = ''
        registry = 'akshathkaushal7/spe-project-backend'
    }
    stages {
        stage('Download the git repo') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/backend-refactored']], extensions: [], userRemoteConfigs: [[credentialsId: 'Akshath_Github_Private_Repo', url: 'git@github.com:akshathkaushal/IIITB-CMS-Backend.git']]])
            }
        }
        stage('Maven build and test') {
            steps {
            	script {
            		sh 'mvn -Dmaven.test.failure.ignore=true clean package'
            	}
            }
            post {
            	success {
            		junit '**/target/surefire-reports/TEST-*.xml'
            		archiveArtifacts 'target/*.jar '
            	}
            }
        }
        stage('Build docker image') {
        	steps {
        		script {
        			dockerImage = docker.build registry
        		}
        	}
        }
    }
}