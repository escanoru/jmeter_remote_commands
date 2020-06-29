/*
pipeline {
  agent any
  parameters {
        string(
		name: 'Target_Host', 
		description: '<h4>Node ip separated by comma where Telegraf will be installed, e.g 15.214.x.x, 15.214.x.x, 15.214.x.x, 15.214.x.x.</h4>'
		)
    }
*/

node {
  parameters {
        string(
		name: 'Target_Host', 
		description: '<h4>Node ip separated by comma where Telegraf will be installed, e.g 15.214.x.x, 15.214.x.x, 15.214.x.x, 15.214.x.x.</h4>'
		)
  def remote = [:]
  remote.name = '15.214.139.152'
  remote.host = '15.214.139.152'
  remote.user = 'root'
  remote.password = 'arst@dm1n'
  remote.allowAnyHosts = true
  stage('Uninstalling previous connector (if exists)') {
    sshCommand remote: remote, command: "printf \"\n\" | /opt/arcsight_smart_connector_syslogd_tcp_514/current/UninstallerData/Uninstall_ArcSightAgents -i console"
  }
  stage('Removing previous connector folder (if exists)') {
    sshCommand remote: remote, command: "rm -rf /opt/arcsight_smart_connector_syslogd_tcp_514/"
  }  
}