def setDescription() { 
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
    }
	
    stages {
        stage('Setting Parameters') {
            steps {
				sh """ssh -tt root@"${Host}" << EOF
				ls -lh
				EOF"""
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