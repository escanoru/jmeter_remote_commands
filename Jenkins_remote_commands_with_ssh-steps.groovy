def setDescription() {
  def item = Jenkins.instance.getItemByFullName(env.JOB_NAME) 
  item.setDescription("<h3 style=\"color:#138D75 \">Job to install SmartConnetors</h3>") 
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
		description: '<h4>Target hosts separated by comma, e.g 10.0.0.2, 10.0.0.3, 10.0.0.4</h4>'
		)
        password(
		name: 'Host_Password', 
		defaultValue: 'arst@dm1n', 
		description: '<h4>Host root\'s password. The default password is <span style=\"color:red\">arst@dm1n</span>, you can change it by clicking on \"Change Password\".</h4>'
		)		
  }		
	
    stages {		
        stage('Starting SSH-Steps') {	
            steps {
			  script {
				    def remote = [:]
					remote.name = ${params.Host}
					remote.host = ${params.Host}
					remote.user = 'root'
					remote.password = ${params.Host_Password}
					remote.allowAnyHosts = true
					stage('Uninstalling previous connector (if exists)') {
						sshCommand failOnError: false, remote: remote, command: "printf \"\n\" | /opt/arcsight_smart_connector_syslogd_tcp_514/current/UninstallerData/Uninstall_ArcSightAgents -i console"
						}
					stage('Removing previous SmartConnetor folder (if exists)') {
						sshCommand remote: remote, command: "rm -rf /opt/arcsight_smart_connector_syslogd_tcp_514/"
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