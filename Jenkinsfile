pipeline {
    agent any

    parameters {
        choice(name: 'PLATFORM', choices: ['ANDROID', 'IOS'], description: 'Target Platform')
        choice(name: 'ENVIRONMENT', choices: ['QA', 'DEV', 'STAGE', 'PROD'], description: 'Target Environment')
        choice(name: 'EXECUTION_MODE', choices: ['LOCAL', 'BROWSERSTACK'], description: 'Execution Mode')
        choice(name: 'TEST_SUITE', choices: ['smoke', 'regression', 'sanity'], description: 'Test Suite')
        string(name: 'THREAD_COUNT', defaultValue: '3', description: 'Parallel Thread Count')
        booleanParam(name: 'VIDEO_RECORDING', defaultValue: true, description: 'Enable Video Recording')
    }

    environment {
        BROWSERSTACK_USERNAME = credentials('browserstack-username')
        BROWSERSTACK_ACCESS_KEY = credentials('browserstack-access-key')
        SLACK_WEBHOOK_URL = credentials('slack-webhook-url')
        JAVA_HOME = tool('JDK-17')
        MAVEN_HOME = tool('Maven-3.9')
    }

    options {
        timeout(time: 60, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '30'))
        timestamps()
        ansiColor('xterm')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Branch: ${env.BRANCH_NAME}"
                echo "Build: ${env.BUILD_NUMBER}"
            }
        }

        stage('Setup') {
            steps {
                sh '''
                    echo "Java Version:"
                    java -version
                    echo "Maven Version:"
                    mvn --version
                '''
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Execute Tests') {
            steps {
                script {
                    def suiteFile = "testng-${params.TEST_SUITE}.xml"
                    if (params.EXECUTION_MODE == 'BROWSERSTACK') {
                        suiteFile = "testng-browserstack.xml"
                    }

                    sh """
                        mvn test \
                            -Dsuite.xml=${suiteFile} \
                            -Dfw.platform=${params.PLATFORM} \
                            -Dfw.environment=${params.ENVIRONMENT} \
                            -Dfw.execution.mode=${params.EXECUTION_MODE} \
                            -Dfw.video.recording=${params.VIDEO_RECORDING} \
                            -Dthread.count=${params.THREAD_COUNT} \
                            -DBROWSERSTACK_USERNAME=${BROWSERSTACK_USERNAME} \
                            -DBROWSERSTACK_ACCESS_KEY=${BROWSERSTACK_ACCESS_KEY}
                    """
                }
            }
        }

        stage('Generate Reports') {
            steps {
                // Allure Report
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])

                // Publish TestNG Results
                testNG(reportFilenamePattern: '**/testng-results.xml')

                // Archive Extent Report
                archiveArtifacts artifacts: 'test-output/reports/**/*.html', allowEmptyArchive: true
                archiveArtifacts artifacts: 'test-output/screenshots/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'test-output/videos/**', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }

        success {
            script {
                slackNotification('SUCCESS')
            }
        }

        failure {
            script {
                slackNotification('FAILURE')
            }
        }

        unstable {
            script {
                slackNotification('UNSTABLE')
            }
        }
    }
}

def slackNotification(String buildStatus) {
    def color = buildStatus == 'SUCCESS' ? '#36a64f' : buildStatus == 'FAILURE' ? '#ff0000' : '#ffaa00'
    def emoji = buildStatus == 'SUCCESS' ? ':white_check_mark:' : buildStatus == 'FAILURE' ? ':x:' : ':warning:'

    def payload = """
    {
        "attachments": [
            {
                "color": "${color}",
                "blocks": [
                    {
                        "type": "header",
                        "text": {
                            "type": "plain_text",
                            "text": "${emoji} Mobile Automation - ${buildStatus}"
                        }
                    },
                    {
                        "type": "section",
                        "fields": [
                            {"type": "mrkdwn", "text": "*Platform:* ${params.PLATFORM}"},
                            {"type": "mrkdwn", "text": "*Environment:* ${params.ENVIRONMENT}"},
                            {"type": "mrkdwn", "text": "*Suite:* ${params.TEST_SUITE}"},
                            {"type": "mrkdwn", "text": "*Build:* #${env.BUILD_NUMBER}"},
                            {"type": "mrkdwn", "text": "*Branch:* ${env.BRANCH_NAME}"},
                            {"type": "mrkdwn", "text": "*Mode:* ${params.EXECUTION_MODE}"}
                        ]
                    },
                    {
                        "type": "actions",
                        "elements": [
                            {
                                "type": "button",
                                "text": {"type": "plain_text", "text": "View Report"},
                                "url": "${env.BUILD_URL}allure"
                            }
                        ]
                    }
                ]
            }
        ]
    }
    """

    sh "curl -X POST -H 'Content-type: application/json' --data '${payload}' ${SLACK_WEBHOOK_URL}"
}
