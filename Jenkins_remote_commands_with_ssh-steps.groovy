def setDescription() {
  def item = Jenkins.instance.getItemByFullName(env.JOB_NAME) 
  item.setDescription("<h3>Pipeline to install SmartConnetors</h3>") 
  item.save()
  }
setDescription()

// Declarative //
pipeline {
	agent any
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
        stage('Starting SSH-Steps') {	
            steps {
			  script {
				    def remote = [:]
					remote.name = '15.214.139.152'
					remote.host = '15.214.139.152'
					remote.user = 'root'
					remote.password = 'arst@dm1n'
					remote.allowAnyHosts = true
					stage('Uninstalling previous connector (if exists)') {
						sshCommand failOnError: false, remote: remote, command: "printf \"\n\" | /opt/arcsight_smart_connector_syslogd_tcp_514/current/UninstallerData/Uninstall_ArcSightAgents -i console"
						}
					stage('Removing previous SmartConnetor folder (if exists)') {
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