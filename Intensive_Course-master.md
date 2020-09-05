# intensive_course

## Windows 10에 Linux 설치
### PowserShell을 관리자 권한으로 실행하여 WSL 활성화
```
Enable-WindowsOptionalFeature -Online -FeatureName Microsoft-Windows-Subsystem-Linux
```

### PC 재시작

### Windows 10에 Linux 설치
시작메뉴>Microsoft Stroe에서 ubuntu 검색 후, 18.04 version설치

### Ubuntu의 Archive Repository Server를 국내로 설정

```
sudo vi /etc/apt/sources.list
:%s/archive.ubuntu.com/ftp.daumkakao.com/g
:wq!
```

## Linux에 JDK 설치

### 설치 명령
```
sudo apt-get update
sudo apt install default-jdk
```

### JAVA_HOME 설정
 .bashrc에 export JAVA_HOME='usr/lib/jvm/java-11-openjdk-amd64' 추가
 
### 실행 path 추가
.bashrc에 export PATH=$PATH:$JAVA_HOME/bin:. 추가

### 수정사항 반영
```
source ~/.bashrc
```

### 설치 확인
```
echo $JAVA_HOME
java -version
```

## Docker Client 설치
실습시 이부분은 꼭 안해도 될듯
```
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
sudo apt update
sudo apt install docker-ce
sudo usermod -aG docker
```

## kubectl 설치
```
sudo apt-get update && sudo apt-get install -y apt-transport-https gnupg2
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y kubectl
```

## Azure-Cli 설치
```
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
az login
```

## Azure AKS connection
### Local에서 Azure AKS 연결
```
az aks get-credentials --resource-group MY_RESOURCE_GROUP --name MY_CLUSTER_NAME
```
### AKS 연결 확인
```
kubectl config current-context
kubectl get all
```

## Login Azure Container Registry
Docker 데몬이 설치되어 있지 않으면 이 부분 진행 안됨. 그러나 실기 테스트에서는 굳이 로컬에서 Docker 데몬을 구동할 필요 없음으로 패스해도 된다.
### Azure CLI
```
az acr login --name ACR_NAME
```
### 생성확인
```
cat ~/.docker/config.json
```

## Integrate AKS with ACR
```
az aks update -n CLUSTER_NAME -g RESOURCE_GROUP_NAME --attach-acr ACR_NAME
```

## Spring Boot 프로젝트 만들 때 추가할 라이브러리들
- JPA
- H2
- Rest Repository
- Spring Boot Actuator

## Local에 kafka 설치하기
Local에서 kafka는 윈도우 cmd에서 실행한다.
https://kafka.apache.org/downloads 에서 Binary downloads하여 압축 해제.
압축 해제 폴더에서 bin/windows 경로로 이동한다. 

### zookeeper 실행
kafka 실행전 zookeeper 실행 반드시 필요
```
zookeeper-server-start.bat ../../config/zookeeper.properties
```

### kafka 실행
```
kafka-server-start.bat ../../config/server.properties
```

### Topic 생성
```
kafka-topics.bat --zookeeper localhost:2181 --topic TOPIC_NAME --create --partitions 1 --replication-factor 1
```

### Topic 리스트 보기
```
kafka-topics.bat --zookeeper localhost:2181 --list
```

### 이벤트 발행하기
```
kafka-console-producer.bat --broker-list http://localhost:9092 --topic TOPIC_NAME
```

### 이벤트 수신하기
```
kafka-console-consumer.bat --bootstrap-server http://localhost:9092 --topic TOPIC_NAME --from-beginning
```
### config/server.properties
여기에 delete.topic.enable=true 로 설정

## helm 및 kafka 설치
### helm 설치
```
curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get | bash
kubectl --namespace kube-system create sa tiller      # helm 의 설치관리자를 위한 시스템 사용자 생성
kubectl create clusterrolebinding tiller --clusterrole cluster-admin --serviceaccount=kube-system:tiller
helm init --service-account tiller
helm repo update
```

### kafka 설치
```
kubectl patch deploy --namespace kube-system tiller-deploy -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
helm repo add incubator http://storage.googleapis.com/kubernetes-charts-incubator
helm repo update
helm install --name my-kafka --namespace kafka incubator/kafka
```

### kafka 토픽 생성
```
kubectl -n kafka exec my-kafka-0 -- /usr/bin/kafka-topics --zookeeper my-kafka-zookeeper:2181 --topic eventTopic --create --partitions 1 --replication-factor 1
```

### kafka 이벤트 발행
```
kubectl -n kafka exec -ti my-kafka-0 -- /usr/bin/kafka-console-producer --broker-list my-kafka:9092 --topic eventTopic

```

### kafka 이벤트 수신
```
kubectl -n kafka exec -ti my-kafka-0 -- /usr/bin/kafka-console-consumer --bootstrap-server my-kafka:9092 --topic eventTopic --from-beginning
```

### kafka 토픽 삭제
```
kubectl -n kafka exec my-kafka-0 -- /usr/bin/kafka-topics --delete --zookeeper my-kafka-zookeeper:2181 --topic TOPIC_NAME
```

## Spring Boot 프로젝트 설정파일
### application.yaml
src/main/resources/application.yaml

``` yaml
server:
  port: 8081

spring:
  profiles: default
  cloud:
    stream:
      kafka:
        binder:
          brokers: localhost:9092
      bindings:
        input:
          group: GROUP_NAME
          destination: TOPIC_NAME
          contentType: application/json
        output:
          destination: TOPIC_NAME
          contentType: application/json
---
server:
  port: 8080

spring:
  profiles: docker
  cloud:
    stream:
      kafka:
        binder:
          brokers: my-kafka.kafka.svc.cluster.local:9092
      bindings:
        input:
          group: GROUP_NAME
          destination: TOPIC_NAME
          contentType: application/json
        output:
          destination: shop
          contentType: application/json
```

Data Rest를 특정 주소 밑으로 설정하려면
``` yaml
spring:
  data:
    rest:
      basePath: /api

```

### Dockerfile
Dockerfile은 모든 프로젝트 공통이다.
``` docker
FROM openjdk:8u212-jdk
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]
```

### azure-pipelines.yml
``` yaml
# Deploy to Azure Kubernetes Service
# Build and push image to Azure Container Registry; Deploy to Azure Kubernetes Service
# https://docs.microsoft.com/azure/devops/pipelines/languages/docker

trigger:
- master

resources:
- repo: self

variables:
- group: common-value
  # containerRegistry: 'event.azurecr.io'
  # containerRegistryDockerConnection: 'acr'
  # environment: 'aks.default'
- name: imageRepository
  value: 'IMAGE_NAME'
- name: dockerfilePath
  value: '**/Dockerfile'
- name: tag
  value: '$(Build.BuildId)'
  # Agent VM image name
- name: vmImageName
  value: 'ubuntu-latest'
- name: MAVEN_CACHE_FOLDER
  value: $(Pipeline.Workspace)/.m2/repository
- name: MAVEN_OPTS
  value: '-Dmaven.repo.local=$(MAVEN_CACHE_FOLDER)'


stages:
- stage: Build
  displayName: Build stage
  jobs:
  - job: Build
    displayName: Build
    pool:
      vmImage: $(vmImageName)
    steps:
    - task: CacheBeta@1
      inputs:
        key: 'maven | "$(Agent.OS)" | **/pom.xml'
        restoreKeys: |
           maven | "$(Agent.OS)"
           maven
        path: $(MAVEN_CACHE_FOLDER)
      displayName: Cache Maven local repo
    - task: Maven@3
      inputs:
        mavenPomFile: 'pom.xml'
        options: '-Dmaven.repo.local=$(MAVEN_CACHE_FOLDER)'
        javaHomeOption: 'JDKVersion'
        jdkVersionOption: '1.8'
        jdkArchitectureOption: 'x64'
        goals: 'package'
    - task: Docker@2
      inputs:
        containerRegistry: $(containerRegistryDockerConnection)
        repository: $(imageRepository)
        command: 'buildAndPush'
        Dockerfile: '**/Dockerfile'
        tags: |
          $(tag)

- stage: Deploy
  displayName: Deploy stage
  dependsOn: Build

  jobs:
  - deployment: Deploy
    displayName: Deploy
    pool:
      vmImage: $(vmImageName)
    environment: $(environment)
    strategy:
      runOnce:
        deploy:
          steps:
          - task: Kubernetes@1
            inputs:
              connectionType: 'Kubernetes Service Connection'
              namespace: 'default'
              command: 'apply'
              useConfigurationFile: true
              configurationType: 'inline'
              inline: |
                apiVersion: apps/v1
                kind: Deployment
                metadata:
                  name: $(imageRepository)
                  labels:
                    app: $(imageRepository)
                spec:
                  replicas: 1
                  selector:
                    matchLabels:
                      app: $(imageRepository)
                  template:
                    metadata:
                      labels:
                        app: $(imageRepository)
                    spec:
                      containers:
                        - name: $(imageRepository)
                          image: $(containerRegistry)/$(imageRepository):$(tag)
                          ports:
                            - containerPort: 8080
                          readinessProbe:
                            httpGet:
                              path: /actuator/health
                              port: 8080
                            initialDelaySeconds: 10
                            timeoutSeconds: 2
                            periodSeconds: 5
                            failureThreshold: 10
                          livenessProbe:
                            httpGet:
                              path: /actuator/health
                              port: 8080
                            initialDelaySeconds: 120
                            timeoutSeconds: 2
                            periodSeconds: 5
                            failureThreshold: 5
              secretType: 'dockerRegistry'
              containerRegistryType: 'Azure Container Registry'
          - task: Kubernetes@1
            inputs:
              connectionType: 'Kubernetes Service Connection'
              namespace: 'default'
              command: 'apply'
              useConfigurationFile: true
              configurationType: 'inline'
              inline: |
                apiVersion: v1
                kind: Service
                metadata:
                  name: $(imageRepository)
                  labels:
                    app: $(imageRepository)
                spec:
                  ports:
                    - port: 8080
                      targetPort: 8080
                  selector:
                    app: $(imageRepository)
              secretType: 'dockerRegistry'
              containerRegistryType: 'Azure Container Registry'
```

### pom.xml 수정 사항
``` xml
<properties>
	...
	<spring-cloud.version>Hoxton.SR3</spring-cloud.version>
</properties>
 
<dependencies>
	...
  	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-stream-kafka</artifactId>
	</dependency>
</dependencies>

<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

## Pipeline을 위한 dev.azure.com 셋팅
- 좌측 메뉴 하단에 Project settings로 이동
- Pipelines 메뉴 중에 Service connections 로 이동
- 우측 상단에 New service connection 로 이동
- Docker Registry 선택
- Registry type으로 Azure Container Registry 선택
- 자신의 Azure container registry 선택후 Service connection name에 "acr" 입력

### 파이프라인에서 사용할 Kubernetes 등록
- Pipelines > Environments에서 aks라는 이름으로 쿠버네티스 환경 추가

## Dashboard 구성
아래 사이트에 가이드 있음.
https://github.com/kubernetes/dashboard

## Autoscale 설정 (Horizontal Pod Autoscaler)
```
kubectl autoscale deployment DEPLOYMENT_NAME --cpu-percent=50 --min=1 --max=10
```
