node {
    def builder = 'builder'
    def runtime = 'runtime'
    
    def imageStreamName = 'runtime'
    def imageStreamtag = 'latest'
    def dcName = 'js2i-demo-test'
    def containerNameInDC = 'js2i-demo'
    
    def token = sh returnStdout: true, script: '/usr/bin/oc whoami -t'
    def jenkins_namespace = "${env.JENKINS_NAMESPACE}"
    def workspace = "${env.WORKSPACE}"
    
    stage('Prepare build') {
        echo "Workspace is ${workspace}"
        echo "${jenkins_namespace}"
    }
    
    stage('Build fat jat') {
        try {
            openshiftBuild apiURL: '', authToken: '', bldCfg: "${builder}", buildName: '', checkForTriggeredDeployments: 'false', showBuildLogs: 'true', verbose: 'false', waitTime: '', waitUnit: 'sec', env: [[name: 'JENKINS_TOKEN', value: "${token}"], [name: 'JENKINS_NAMESPACE', value: "${jenkins_namespace}"], [name: 'WORKSPACE', value: "${workspace}"]]
        } finally {
            archive "target/**/*"
            junit 'target/surefire-reports/*.xml'
        }  
    }
    
    stage ('Build runtime image') {
        openshiftBuild apiURL: '', authToken: '', bldCfg: "${runtime}", buildName: '', checkForTriggeredDeployments: 'true', showBuildLogs: 'true', verbose: 'false', waitTime: '', waitUnit: 'sec'    
    }
    
    stage('Check image reference') {
        openshift.withCluster() {
            // Get exact reference for current tagged image
            def taggedImageReference = openshift.selector('istag',"${imageStreamName}:${imageStreamtag}").object().image.dockerImageReference.toString().trim()
            echo "Image reference in ImageStream is ${taggedImageReference}"
            def currentImageReference = ""
            retry = 0;
            // loop and wait 30s for the configuration deployment to be updated
            while ( !taggedImageReference.equalsIgnoreCase(currentImageReference) && retry < 15) {
                if (retry > 0) sleep(2)
                currentImageReference = openshift.raw('get', 'dc', "${dcName}", '-o', 'template', '--template=\'{{range $idx, $item := .spec.template.spec.containers }}{{if eq $item.name "' + "${containerNameInDC}" + '"}}{{$item.image}}{{end}}{{end}}\'').out.toString().trim()
                echo "Current reference in DC is ${currentImageReference}"
                retry = retry + 1
            }
            if (taggedImageReference != currentImageReference) {
                error 'Deployment config has not been updated in time'
            }
        }
    }
}
