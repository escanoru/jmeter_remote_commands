def setDescription() {
  def item = Jenkins.instance.getItemByFullName(env.JOB_NAME) 
  item.setDescription("<h3><span style=\"color:green\">Jmeter pipeline to send events to Kafka topics (MS Windows events collected from the arcsight.com domain controller) </h3> \n<h3>Dashboard: <a href=\"https://15.214.145.90:8083/d/H3TCoAjWz/th-kafka-metrics-single-instance-node-metrics?orgId=5\">TH Kafka Metrics (Single Instance) + Node Metrics</a></span></h3>") 
  item.save()
  }
setDescription()

// Declarative //
pipeline {
	agent { label 'jmeter_slave' }
	options {
		ansiColor('xterm')
		buildDiscarder(logRotator(daysToKeepStr: '180'))
		}
  parameters {
        string(
		name: 'Host',
		defaultValue: '15.214.',
		description: '<h4>Kafka broker nodes separated by comma, e.g 15.214.x.x, 15.214.x.x, 15.214.x.x, 15.214.x.x</h4>'
		)
  }		
	
    stages {
        stage('Setting Parameters') {
            steps {
                sh '''
				echo "empty"
				'''
            }
        }		
        stage('Uninstalling previous connector (if exists)') {	
            steps {
			  script {
				    def remote = [:]
					remote.name = '15.214.139.152'
					remote.host = '15.214.139.152'
					remote.user = 'root'
					remote.password = 'arst@dm1n'
					remote.allowAnyHosts = true
					stage('Uninstalling previous connector (if exists)') {
						sshCommand remote: remote, command: "printf \"\n\" | /opt/arcsight_smart_connector_syslogd_tcp_514/current/UninstallerData/Uninstall_ArcSightAgents -i console"
						}
                }
            }
        }
    }
	
    post {
        always {
            echo 'Clenning up the workspace'
            deleteDir()
        }
	}
}