/* def setDescription() { 
  def item = Jenkins.instance.getItemByFullName(env.JOB_NAME) 
  item.setDescription("<h3><span style=\"color:green\">Jmeter pipeline to send events to Kafka topics (MS Windows events collected from the arcsight.com domain controller) </h3> \n<h3>Dashboard: <a href=\"https://15.214.145.90:8083/d/H3TCoAjWz/th-kafka-metrics-single-instance-node-metrics?orgId=5\">TH Kafka Metrics (Single Instance) + Node Metrics</a></span></h3>") 
  item.save()
  }
setDescription()


// Functions
def jmeter_command() {
    sh '''
	/opt/jmeter/bin/jmeter.sh -n -t ${WORKSPACE}/Jmeter_Pepperbox_MS_WindowsEvents.jmx
	'''
}

jmeter_instances_1 = [
    "Single Instance": {
        jmeter_command()
    }
]

jmeter_instances_2 = [
    "Parallel Instances-1": {
        jmeter_command()
    },
	"Parallel Instances-2": {
        jmeter_command()
    }
]
*/

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
  remote.name = 'test'
  remote.host = '${Host}'
  remote.user = 'root'
  remote.password = '${Host_Password}'
  remote.allowAnyHosts = true
	
    stages {
        stage('Testing ssh-steps') {
            steps {
			  sshCommand remote: remote, command: "ls -lrt"
			  sshCommand remote: remote, command: "for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done"
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