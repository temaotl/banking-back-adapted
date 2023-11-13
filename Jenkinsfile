node('docker-agent-jdk17') {
    String stackName = params.stackName
    String imageName = "${stackName}/${stackName}-be"
    String containerName = "${stackName}-be"
    String networkName = "${stackName}-network"

    def mvnHome
    def mvnEnv = "-DskipTests"

    // do not change
    def proxyNetworkName = "proxy-network"

    stage('Preparation') { // for display purposes
        // Get some code from a GitHub repository
        git branch: repoBranch,
                credentialsId: 'bitbucket',
                url: repoUrl
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'M3'
    }
    stage('Build') {
        jdk = tool name: 'openjdk-17'
        env.JAVA_HOME = "${jdk}"

        echo "jdk installation path is: ${jdk}"

        // Run the maven build
        withEnv(["MVN_HOME=$mvnHome"]) {
            if (isUnix()) {
                sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean package'
            } else {
                bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
            }
        }
    }

    stage('Sonar') {
        // Run the maven build
        withEnv(["MVN_HOME=$mvnHome"]) {
            if (isUnix()) {
                sh "$mvnHome/bin/mvn sonar:sonar -Dsonar.projectKey=${stackName} -Dsonar.host.url=${params.sonarqubeUrl}" +
                        " -Dsonar.login=${params.sonarqubeToken}"
            }
        }
    }

//    stage('Results') {
//        junit '**/target/surefire-reports/TEST-*.xml'
//        archiveArtifacts 'target/*.jar'
//    }

    stage("Build image : ${imageName}") {
        /* This builds the actual image; synonymous to
         * docker build on the command line */
        app = docker.build("${imageName}", "--build-arg SPRING_PROFILES_ACTIVE=${springProfilesActive} -f Dockerfile .")
    }

    stage('Deploy image') {
        sh "docker ps -f name=${containerName} -q | xargs --no-run-if-empty docker container stop"
        sh "docker container ls -a -fname=${containerName} -q | xargs -r docker container rm"

        if (params.logTo == 'Kibana'){
            sh "docker run --name ${containerName} --network ${networkName} -e SPRING_PROFILES_ACTIVE=${springProfilesActive} -d --log-driver gelf --log-opt gelf-address=udp://${logstashUrl} ${imageName}"
        } else{
            sh "docker run --name ${containerName} --network ${networkName} -e SPRING_PROFILES_ACTIVE=${springProfilesActive} -d ${imageName}"
        }

        sh "docker network connect ${proxyNetworkName} ${containerName}"
    }

}
