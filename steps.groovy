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
		description: '<h4>Target host where the SmartConnector will be installed separated by comma, e.g 15.214.x.x, 15.214.x.x, 15.214.x.x, 15.214.x.x</h4>'
		)
        password(
		name: 'Host_Password', 
		defaultValue: 'arst@dm1n', 
		description: '<h4>Host root\'s password. The default password is <span style=\"color:red\">arst@dm1n</span>, you can change it by clicking on \"Change Password\".</h4>'
		)		
    }
	
  def remote = [:]
  remote.name = '15.214.139.152'
  remote.host = '15.214.139.152'
  remote.user = 'root'
  remote.password = 'arst@dm1n'
  remote.allowAnyHosts = true
  stage('Remote SSH') {
    sshCommand remote: remote, command: "ls -lrt"
    sshCommand remote: remote, command: "for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done"
	
    post {
        always {
            echo 'Clenning up the workspace'
            deleteDir()
        }
	}	
}