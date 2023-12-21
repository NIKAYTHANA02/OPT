pipeline {
	agent any

	// check for new source changes and re-trigger the pipeline if found.

	environment {
		VERSION = "$BUILD_NUMBER"
		PROJECT = "dropline-otpservice"
		IMAGE = "dropline-otpservice:latest"

		UAT_ECRURL = "679430529045.dkr.ecr.ap-south-1.amazonaws.com/dropline-otpservice"
		UAT_CLUSTER_NAME = "dropline-mobileapp"
		UAT_SERVICE_NAME = "dropline-otpservice-svc"
		UAT_TASK_DEFINITION = "dropline-otpservice-td:1"
		KUBE_CONFIG = "--kubeconfig /var/lib/jenkins/.kube/config_dropline_uat"

		PP_ECRURL = ""
		PP_CLUSTER_NAME = ""
		PP_SERVICE_NAME = ""
		PP_TASK_DEFINITION = ""

		PROD_ECRURL = "109420162169.dkr.ecr.ap-south-1.amazonaws.com/dropline-otpservice-prod"
		PROD_CLUSTER_NAME = "abfl-cross-products"
		PROD_SERVICE_NAME = "dropline-otpservice-service"
		PROD_TASK_DEFINITION = "dropline-otpservice-td:1"
		KUBE_PROD = "--kubeconfig /var/lib/jenkins/.kube/config_dropline_prod"					
	}

	stages {
		stage ('DEPLOYMENT APPROVAL') {
            when { anyOf { branch 'PROD'; branch 'PROD' }}
			steps {
    		script {
            timeout(time: 4, unit: 'HOURS') {
            input id: 'CustomID', 
    		message: 'Deployment Step', 
    		ok: 'Proceed', 
    		parameters: [password(defaultValue: 'admin', description: 'Please confirm if you want to proceed with the deployemnt. You need to be admin to confirm.', name: 'admin')], 
			submitter: 'admin', 
			submitterParameter: 'approvingSubmitter'
               }
             }
		   }
        }

		stage('FORTIFY ANALYSIS')	{		
			when { anyOf { branch 'UAT'; branch 'PREPROD'; branch 'PROD' }}
            steps {
                script {
                    fortifyTranslate addJVMOptions: '', buildID: '$BUILD_NUMBER', excludeList: '', logFile: '', maxHeap: '', projectScanType: fortifyOther(otherIncludesList: '*', otherOptions: '')
                    fortifyScan addJVMOptions: '', addOptions: '', buildID: '$BUILD_NUMBER', customRulepacks: '', logFile: '', maxHeap: '', resultsFile: ''
                    fortifyUpload appName: 'dropline-otpservice', appVersion: 'latest', failureCriteria: '', filterSet: '', pollingInterval: '', resultsFile: ''
			    }
            }
		}
        
		stage('APPLICATION BUILD') {
			when { anyOf { branch 'UAT'; branch 'PREPROD'; branch 'PROD' }}
			steps {
				script {
					sh '''
				 	/opt/maven/bin/mvn clean install
					cp target/OTP-Service.war OTP-Service.war
					'''
				}
			}
		}

		stage('IMAGE BUILD') {
			when { anyOf { branch 'UAT'; branch 'PREPROD'; branch 'PROD' }}
			steps {
				script {
				 	docker.build("$IMAGE")
				}
			}
		}

		stage('IMAGE PUSH') {
			steps {
				script {					
					if ( env.BRANCH_NAME == 'UAT' || env.BRANCH_NAME == 'uat' ) {
						sh '''
						$(aws ecr get-login --no-include-email --region ap-south-1 --registry-ids 679430529045) &&
						docker tag $IMAGE $UAT_ECRURL:uat && docker push $UAT_ECRURL:uat
						'''	
					}	
					else if ( env.BRANCH_NAME == 'PREPROD'  || env.BRANCH_NAME == 'preprod' ) {
						sh '''
						$(aws ecr get-login --no-include-email --region ap-south-1 --registry-ids 679430529045) &&
						docker tag $IMAGE $PP_ECRURL:preprod && docker push $PP_ECRURL:preprod 
						'''	
					}	
					else if ( env.BRANCH_NAME == 'PROD' || env.BRANCH_NAME == 'prod' ) {
						sh '''
						$(aws ecr get-login --no-include-email --region ap-south-1 --registry-ids 109420162169) &&
						docker tag $IMAGE $PROD_ECRURL:prod && docker push $PROD_ECRURL:prod
						'''
					}
					else {
						echo "This branch is not set for Deployment"
					}
				}
			}
		}

		stage('DEPLOYMENT ') {
			steps {
				script {
					if ( env.BRANCH_NAME == 'UAT_BACKUP' || env.BRANCH_NAME == 'UAT_BACKUP' ) {
						sh '''
						sleep 60 &&
						aws ecs update-service --cluster $UAT_CLUSTER_NAME --service $UAT_SERVICE_NAME --force-new-deployment --task-definition $UAT_TASK_DEFINITION	
						'''
					}	
					else if ( env.BRANCH_NAME == 'PREPROD' || env.BRANCH_NAME == 'preprod' ) {
						sh '''
						sleep 60 &&
						aws ecs update-service --cluster $PP_CLUSTER_NAME --service $PP_SERVICE_NAME --force-new-deployment --task-definition $PP_TASK_DEFINITION
						'''	
					}	
					else if ( env.BRANCH_NAME == 'PROD_Backup' || env.BRANCH_NAME == 'PROD_Backup' ) {
						sh '''
						sleep 60 &&
						aws ecs update-service --cluster $PROD_CLUSTER_NAME --service $PROD_SERVICE_NAME --force-new-deployment --task-definition $PROD_TASK_DEFINITION --profile abflprod
						'''	
					}
					else {
						echo "This branch is not set for Deployment"
					}	
				}
			}
		}
		stage('DEPLOYMENT_EKS') {
			steps {
				script {
					if ( env.BRANCH_NAME == 'UAT' || env.BRANCH_NAME == 'uat' ) {
						sh '''
						sleep 60 &&
						kubectl rollout restart deployment otpservice -n abfl-dropline $KUBE_CONFIG
						'''
					}
					else if ( env.BRANCH_NAME == 'PROD' || env.BRANCH_NAME == 'prod' ) {
						sh '''
						sleep 60 &&
						kubectl rollout restart deployment otpservice -n abfl-dropline-prod $KUBE_PROD	
						'''
					}
					else {
						echo "This branch is not set for Deployment"
					}
				}
			}
		}	
	    
	}
		
		
        
	// remove docker images to save space

	post {
		always {
			sh "docker rmi $IMAGE | true"
		}
	}
}
