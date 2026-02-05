def call(Map configMap) {
    pipeline {
        agent {
            node {
                label 'agent-1'
            }
        }
        environment {
            appVersion = configMap.get('appVersion')
            REGION = 'us-east-1'
            PROJECT = configMap.get('project')
            COMPONENT = configMap.get('component')
            AWS_ACCOUNT_ID = '368903465982'
            deploy_to = configMap.get('deploy_to')
        }
        
        stages {
            stage ('Deploy Job') {
                steps {
                    script {
                        withAWS(region:'us-east-1',credentials:'aws-auth') {
                            sh """
                                echo "Triggering Deploy Job ..."
                                echo "Deploying application version ${appVersion} to ${deploy_to} environment..."
                                aws eks update-kubeconfig --region ${REGION} --name ${PROJECT}-${deploy_to}
                                kubectl get nodes
                            """
                        }                
                    }
                }
            }
        }
    }
}   