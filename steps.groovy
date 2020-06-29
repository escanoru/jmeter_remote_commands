node {
  def remote = [:]
  remote.name = '15.214.139.152'
  remote.host = '15.214.139.152'
  remote.user = 'root'
  remote.password = 'arst@dm1n'
  remote.allowAnyHosts = true
  stage('Remote SSH') {
    sshCommand remote: remote, command: "ls -lrt"
    sshCommand remote: remote, command: "for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done"
  }
}