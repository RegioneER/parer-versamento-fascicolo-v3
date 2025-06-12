<img src="src/docs/quarkus.png" width="300">
<br/><br/>

# Versamento fascicolo v3 

Fonte template redazione documento:  https://www.makeareadme.com/.


# Descrizione

Web service REST per il versamento dei Pacchetti di Versamento (detti anche SIP o PdV) di Fascicoli nel sistema di conservazione SACER. Il SIP contiene i metadati descrittivi del fascicolo, inclusi quelli previsti dall'Allegato 5 delle Linee guida AgID, e gli identificativi delle Unità Documentarie (UD) contenute nel fascicolo stesso.

# Installazione

Di seguito verranno riportati sotto alcuni paragrafi, le modalità possibili con cui è possibile rendere operativo il microservizio. 

## Rilascio su RedHat Openshift

Vedere specifica guida per il rilascio [OKD.md](OKD.md).

### Openshift template

Per la creazione dell'applicazione con risorse necessarie correlate sotto Openshift (https://www.redhat.com/it/technologies/cloud-computing/openshift) viene fornito un apposito template (la solzuzione, modificabile, è basata su Oracle DB) [template](src/main/openshift/verifica-firma-crypto-template.yml).

## Installazione applicazione come servizio/demone

Vedere guida all'installazione [INSTALL.md](INSTALL.md).

# Utilizzo

Basato su [Quarkus](https://quarkus.io/) e Java 21, è consigliato fare riferimento alle [linee guida](https://quarkus.io/guides/) ufficiali per avere ulteriori dettagli nonché le best practice da utilizzare in caso di modifiche ed interventi al progetto stesso.
Le configurazioni sono legate ai file yaml che sono gestiti come previsto dai meccanismi di overrinding messi a disposizione, vedi apposita [guida](https://quarkus.io/guides/config).

# Immagine Docker

Per effettuare una build del progetto via Docker è stato predisposto lo standard [Dockerfile](src/main/docker/Dockerfile.jvm) e una directory [docker_build](docker_build) con all'interno i file da integrare all'immagine base.
La directory [docker_build](docker_build) è strutturata come segue: 
```bash
|____README.md
|____certs
| |____README.md

```
al fine di integrare certificati non presenti di default nell'immagine principale è stata introdotta la sotto-directory [docker_build/certs](docker_build/certs) in cui dovranno essere inseriti gli appositi certificati che verranno "trustati" in fase di build dell'immagine.
La compilazione dell'immagine può essere eseguita con il comando: 
```bash
docker build -t <registry> -f ./Dockerfile --build-arg EXTRA_CA_CERTS_DIR=docker_build/certs .
```

# Requisiti e librerie utilizzate

Requisiti minimi per installazione: 

- Sistema operativo : consigliato Linux server (in alternativa compatibilità con Windows server)
- Java versione 21 (OpenJDK / Oracle)
- Kubernetes / Docker : se rilasciato attraverso container oppure si esegue una build del progetto attraverso il profilo maven **uber-jar** per ottenere il JAR eseguibile (vedi paragrafi precendeti)

# Librerie utilizzate


|  GroupId | ArtifactId  | Version |
|:---:|:---:|:---:|
|The|||
|aopalliance|aopalliance|1.0|
|com.aayushatharva.brotli4j|brotli4j|1.16.0|
|com.aayushatharva.brotli4j|native-linux-x86_64|1.16.0|
|com.aayushatharva.brotli4j|service|1.16.0|
|com.fasterxml.jackson.core|jackson-annotations|2.18.2|
|com.fasterxml.jackson.core|jackson-core|2.18.2|
|com.fasterxml.jackson.core|jackson-databind|2.18.2|
|com.fasterxml.jackson.dataformat|jackson-dataformat-yaml|2.18.2|
|com.fasterxml.jackson.datatype|jackson-datatype-jdk8|2.18.2|
|com.fasterxml.jackson.datatype|jackson-datatype-jsr310|2.18.2|
|com.fasterxml.jackson.module|jackson-module-parameter-names|2.18.2|
|com.fasterxml.woodstox|woodstox-core|6.6.0|
|com.fasterxml|classmate|1.7.0|
|com.github.ben-manes.caffeine|caffeine|3.1.8|
|com.google.errorprone|error_prone_annotations|2.36.0|
|com.google.guava|failureaccess|1.0.1|
|com.google.guava|guava|33.4.0-jre|
|com.google.inject|guice|5.1.0|
|com.h2database|h2|2.3.230|
|com.oracle.database.jdbc|ojdbc11|23.5.0.24.07|
|com.oracle.database.nls|orai18n|23.5.0.24.07|
|com.sun.istack|istack-commons-runtime|4.1.2|
|commons-cli|commons-cli|1.8.0|
|commons-codec|commons-codec|1.17.1|
|commons-io|commons-io|2.16.1|
|commons-net|commons-net|3.11.1|
|io.agroal|agroal-api|2.5|
|io.agroal|agroal-narayana|2.5|
|io.agroal|agroal-pool|2.5|
|io.github.crac|org-crac|0.1.3|
|io.micrometer|micrometer-commons|1.14.4|
|io.micrometer|micrometer-core|1.14.4|
|io.micrometer|micrometer-observation|1.14.4|
|io.micrometer|micrometer-registry-prometheus-simpleclient|1.14.4|
|io.netty|netty-buffer|4.1.118.Final|
|io.netty|netty-codec-dns|4.1.118.Final|
|io.netty|netty-codec-haproxy|4.1.118.Final|
|io.netty|netty-codec-http2|4.1.118.Final|
|io.netty|netty-codec-http|4.1.118.Final|
|io.netty|netty-codec-socks|4.1.118.Final|
|io.netty|netty-codec|4.1.118.Final|
|io.netty|netty-common|4.1.118.Final|
|io.netty|netty-handler-proxy|4.1.118.Final|
|io.netty|netty-handler|4.1.118.Final|
|io.netty|netty-resolver-dns|4.1.118.Final|
|io.netty|netty-resolver|4.1.118.Final|
|io.netty|netty-transport-native-unix-common|4.1.118.Final|
|io.netty|netty-transport|4.1.118.Final|
|io.prometheus|simpleclient|0.16.0|
|io.prometheus|simpleclient_common|0.16.0|
|io.prometheus|simpleclient_tracer_common|0.16.0|
|io.prometheus|simpleclient_tracer_otel|0.16.0|
|io.prometheus|simpleclient_tracer_otel_agent|0.16.0|
|io.quarkiverse.amazonservices|quarkus-amazon-common|2.20.0|
|io.quarkiverse.amazonservices|quarkus-amazon-s3|2.20.0|
|io.quarkus.arc|arc-processor|3.18.3|
|io.quarkus.arc|arc|3.18.3|
|io.quarkus.gizmo|gizmo|1.8.0|
|io.quarkus.qute|qute-core|3.18.3|
|io.quarkus.resteasy.reactive|resteasy-reactive-common-types|3.18.3|
|io.quarkus.resteasy.reactive|resteasy-reactive-common|3.18.3|
|io.quarkus.resteasy.reactive|resteasy-reactive-jackson|3.18.3|
|io.quarkus.resteasy.reactive|resteasy-reactive-vertx|3.18.3|
|io.quarkus.resteasy.reactive|resteasy-reactive|3.18.3|
|io.quarkus.security|quarkus-security|2.2.0|
|io.quarkus.vertx.utils|quarkus-vertx-utils|3.18.3|
|io.quarkus|quarkus-agroal|3.18.3|
|io.quarkus|quarkus-apache-httpclient|3.18.3|
|io.quarkus|quarkus-arc-deployment|3.18.3|
|io.quarkus|quarkus-arc-test-supplement|3.18.3|
|io.quarkus|quarkus-arc|3.18.3|
|io.quarkus|quarkus-bootstrap-app-model|3.18.3|
|io.quarkus|quarkus-bootstrap-core|3.18.3|
|io.quarkus|quarkus-bootstrap-gradle-resolver|3.18.3|
|io.quarkus|quarkus-bootstrap-maven-resolver|3.18.3|
|io.quarkus|quarkus-bootstrap-runner|3.18.3|
|io.quarkus|quarkus-builder|3.18.3|
|io.quarkus|quarkus-caffeine|3.18.3|
|io.quarkus|quarkus-class-change-agent|3.18.3|
|io.quarkus|quarkus-classloader-commons|3.18.3|
|io.quarkus|quarkus-config-yaml|3.18.3|
|io.quarkus|quarkus-core-deployment|3.18.3|
|io.quarkus|quarkus-core|3.18.3|
|io.quarkus|quarkus-credentials|3.18.3|
|io.quarkus|quarkus-datasource-common|3.18.3|
|io.quarkus|quarkus-datasource|3.18.3|
|io.quarkus|quarkus-development-mode-spi|3.18.3|
|io.quarkus|quarkus-devtools-utilities|3.18.3|
|io.quarkus|quarkus-elytron-security-common|3.18.3|
|io.quarkus|quarkus-elytron-security-properties-file|3.18.3|
|io.quarkus|quarkus-elytron-security|3.18.3|
|io.quarkus|quarkus-fs-util|0.0.10|
|io.quarkus|quarkus-hibernate-orm|3.18.3|
|io.quarkus|quarkus-hibernate-validator-spi|3.18.3|
|io.quarkus|quarkus-hibernate-validator|3.18.3|
|io.quarkus|quarkus-ide-launcher|3.18.3|
|io.quarkus|quarkus-jackson|3.18.3|
|io.quarkus|quarkus-jacoco|3.18.3|
|io.quarkus|quarkus-jaxb|3.18.3|
|io.quarkus|quarkus-jaxp|3.18.3|
|io.quarkus|quarkus-jdbc-oracle|3.18.3|
|io.quarkus|quarkus-jsonp|3.18.3|
|io.quarkus|quarkus-junit5-config|3.18.3|
|io.quarkus|quarkus-junit5-mockito-config|3.18.3|
|io.quarkus|quarkus-junit5-mockito|3.18.3|
|io.quarkus|quarkus-junit5|3.18.3|
|io.quarkus|quarkus-logging-json|3.18.3|
|io.quarkus|quarkus-micrometer-registry-prometheus|3.18.3|
|io.quarkus|quarkus-micrometer|3.18.3|
|io.quarkus|quarkus-mutiny|3.18.3|
|io.quarkus|quarkus-narayana-jta|3.18.3|
|io.quarkus|quarkus-netty|3.18.3|
|io.quarkus|quarkus-oidc-common|3.18.3|
|io.quarkus|quarkus-oidc|3.18.3|
|io.quarkus|quarkus-qute|3.18.3|
|io.quarkus|quarkus-rest-common|3.18.3|
|io.quarkus|quarkus-rest-jackson-common|3.18.3|
|io.quarkus|quarkus-rest-jackson|3.18.3|
|io.quarkus|quarkus-rest-jaxb|3.18.3|
|io.quarkus|quarkus-rest-qute|3.18.3|
|io.quarkus|quarkus-rest|3.18.3|
|io.quarkus|quarkus-security-jpa-common|3.18.3|
|io.quarkus|quarkus-security-jpa|3.18.3|
|io.quarkus|quarkus-security-runtime-spi|3.18.3|
|io.quarkus|quarkus-security|3.18.3|
|io.quarkus|quarkus-smallrye-context-propagation-spi|3.18.3|
|io.quarkus|quarkus-smallrye-context-propagation|3.18.3|
|io.quarkus|quarkus-smallrye-health|3.18.3|
|io.quarkus|quarkus-smallrye-jwt-build|3.18.3|
|io.quarkus|quarkus-smallrye-openapi|3.18.3|
|io.quarkus|quarkus-swagger-ui|3.18.3|
|io.quarkus|quarkus-test-common|3.18.3|
|io.quarkus|quarkus-test-h2|3.18.3|
|io.quarkus|quarkus-test-security|3.18.3|
|io.quarkus|quarkus-tls-registry|3.18.3|
|io.quarkus|quarkus-transaction-annotations|3.18.3|
|io.quarkus|quarkus-vertx-http-dev-ui-spi|3.18.3|
|io.quarkus|quarkus-vertx-http|3.18.3|
|io.quarkus|quarkus-vertx-latebound-mdc-provider|3.18.3|
|io.quarkus|quarkus-vertx|3.18.3|
|io.quarkus|quarkus-virtual-threads|3.18.3|
|io.rest-assured|json-path|5.5.0|
|io.rest-assured|rest-assured-common|5.5.0|
|io.rest-assured|rest-assured|5.5.0|
|io.rest-assured|xml-path|5.5.0|
|io.smallrye.beanbag|smallrye-beanbag-maven|1.5.2|
|io.smallrye.beanbag|smallrye-beanbag-sisu|1.5.2|
|io.smallrye.beanbag|smallrye-beanbag|1.5.2|
|io.smallrye.certs|smallrye-private-key-pem-parser|0.9.2|
|io.smallrye.common|smallrye-common-annotation|2.9.0|
|io.smallrye.common|smallrye-common-classloader|2.9.0|
|io.smallrye.common|smallrye-common-constraint|2.9.0|
|io.smallrye.common|smallrye-common-cpu|2.9.0|
|io.smallrye.common|smallrye-common-expression|2.9.0|
|io.smallrye.common|smallrye-common-function|2.9.0|
|io.smallrye.common|smallrye-common-io|2.9.0|
|io.smallrye.common|smallrye-common-net|2.9.0|
|io.smallrye.common|smallrye-common-os|2.9.0|
|io.smallrye.common|smallrye-common-ref|2.9.0|
|io.smallrye.common|smallrye-common-vertx-context|2.9.0|
|io.smallrye.config|smallrye-config-common|3.11.2|
|io.smallrye.config|smallrye-config-core|3.11.2|
|io.smallrye.config|smallrye-config-source-yaml|3.11.2|
|io.smallrye.config|smallrye-config-validator|3.11.2|
|io.smallrye.config|smallrye-config|3.11.2|
|io.smallrye.reactive|mutiny-smallrye-context-propagation|2.8.0|
|io.smallrye.reactive|mutiny-zero-flow-adapters|1.1.0|
|io.smallrye.reactive|mutiny|2.8.0|
|io.smallrye.reactive|smallrye-mutiny-vertx-auth-common|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-bridge-common|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-core|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-runtime|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-uri-template|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-web-client|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-web-common|3.18.1|
|io.smallrye.reactive|smallrye-mutiny-vertx-web|3.18.1|
|io.smallrye.reactive|smallrye-reactive-converter-api|3.0.1|
|io.smallrye.reactive|smallrye-reactive-converter-mutiny|3.0.1|
|io.smallrye.reactive|vertx-mutiny-generator|3.18.1|
|io.smallrye|jandex|3.2.6|
|io.smallrye|smallrye-context-propagation-api|2.2.0|
|io.smallrye|smallrye-context-propagation-jta|2.2.0|
|io.smallrye|smallrye-context-propagation-storage|2.2.0|
|io.smallrye|smallrye-context-propagation|2.2.0|
|io.smallrye|smallrye-fault-tolerance-vertx|6.7.3|
|io.smallrye|smallrye-health-api|4.1.1|
|io.smallrye|smallrye-health-provided-checks|4.1.1|
|io.smallrye|smallrye-health|4.1.1|
|io.smallrye|smallrye-jwt-build|4.6.1|
|io.smallrye|smallrye-jwt-common|4.6.1|
|io.smallrye|smallrye-jwt|4.6.1|
|io.smallrye|smallrye-open-api-core|4.0.8|
|io.smallrye|smallrye-open-api-model|4.0.8|
|io.vertx|vertx-auth-common|4.5.13|
|io.vertx|vertx-bridge-common|4.5.13|
|io.vertx|vertx-codegen|4.5.13|
|io.vertx|vertx-core|4.5.13|
|io.vertx|vertx-uri-template|4.5.13|
|io.vertx|vertx-web-client|4.5.13|
|io.vertx|vertx-web-common|4.5.13|
|io.vertx|vertx-web|4.5.13|
|it.eng.parer|idp-jaas-rdbms|0.0.9|
|it.eng.parer|quarkus-custom-log-handlers|1.2.1|
|jakarta.activation|jakarta.activation-api|2.1.3|
|jakarta.annotation|jakarta.annotation-api|3.0.0|
|jakarta.authentication|jakarta.authentication-api|3.0.0|
|jakarta.authorization|jakarta.authorization-api|2.1.0|
|jakarta.el|jakarta.el-api|5.0.1|
|jakarta.enterprise|jakarta.enterprise.cdi-api|4.1.0|
|jakarta.enterprise|jakarta.enterprise.lang-model|4.1.0|
|jakarta.inject|jakarta.inject-api|2.0.1|
|jakarta.interceptor|jakarta.interceptor-api|2.2.0|
|jakarta.json|jakarta.json-api|2.1.3|
|jakarta.persistence|jakarta.persistence-api|3.1.0|
|jakarta.resource|jakarta.resource-api|2.1.0|
|jakarta.servlet|jakarta.servlet-api|6.0.0|
|jakarta.transaction|jakarta.transaction-api|2.0.1|
|jakarta.validation|jakarta.validation-api|3.0.2|
|jakarta.ws.rs|jakarta.ws.rs-api|3.1.0|
|jakarta.xml.bind|jakarta.xml.bind-api|4.0.2|
|javax.annotation|javax.annotation-api|1.3.2|
|javax.inject|javax.inject|1|
|net.bytebuddy|byte-buddy-agent|1.15.11|
|net.bytebuddy|byte-buddy|1.15.11|
|org.aesh|aesh|2.8.2|
|org.aesh|readline|2.6|
|org.antlr|antlr4-runtime|4.13.0|
|org.apache.commons|commons-collections4|4.5.0-M2|
|org.apache.commons|commons-compress|1.26.2|
|org.apache.commons|commons-lang3|3.15.0|
|org.apache.commons|commons-text|1.12.0|
|org.apache.groovy|groovy-json|4.0.22|
|org.apache.groovy|groovy-xml|4.0.22|
|org.apache.groovy|groovy|4.0.22|
|org.apache.httpcomponents|httpclient|4.5.14|
|org.apache.httpcomponents|httpcore|4.4.16|
|org.apache.httpcomponents|httpmime|4.5.14|
|org.apache.maven.resolver|maven-resolver-api|1.9.22|
|org.apache.maven.resolver|maven-resolver-connector-basic|1.9.22|
|org.apache.maven.resolver|maven-resolver-impl|1.9.22|
|org.apache.maven.resolver|maven-resolver-named-locks|1.9.22|
|org.apache.maven.resolver|maven-resolver-spi|1.9.22|
|org.apache.maven.resolver|maven-resolver-transport-http|1.9.20|
|org.apache.maven.resolver|maven-resolver-transport-wagon|1.9.22|
|org.apache.maven.resolver|maven-resolver-util|1.9.22|
|org.apache.maven.shared|maven-shared-utils|3.4.2|
|org.apache.maven.wagon|wagon-file|3.5.3|
|org.apache.maven.wagon|wagon-http-shared|3.5.3|
|org.apache.maven.wagon|wagon-http|3.5.3|
|org.apache.maven.wagon|wagon-provider-api|3.5.3|
|org.apache.maven|maven-api-meta|4.0.0-alpha-5|
|org.apache.maven|maven-api-xml|4.0.0-alpha-5|
|org.apache.maven|maven-artifact|3.9.9|
|org.apache.maven|maven-builder-support|3.9.9|
|org.apache.maven|maven-core|3.9.9|
|org.apache.maven|maven-embedder|3.9.9|
|org.apache.maven|maven-model-builder|3.9.9|
|org.apache.maven|maven-model|3.9.9|
|org.apache.maven|maven-plugin-api|3.9.9|
|org.apache.maven|maven-repository-metadata|3.9.9|
|org.apache.maven|maven-resolver-provider|3.9.9|
|org.apache.maven|maven-settings-builder|3.9.9|
|org.apache.maven|maven-settings|3.9.9|
|org.apache.maven|maven-xml-impl|4.0.0-alpha-5|
|org.apache.santuario|xmlsec|4.0.2|
|org.apiguardian|apiguardian-api|1.1.2|
|org.bitbucket.b_c|jose4j|0.9.6|
|org.ccil.cowan.tagsoup|tagsoup|1.2.1|
|org.codehaus.plexus|plexus-cipher|2.0|
|org.codehaus.plexus|plexus-classworlds|2.6.0|
|org.codehaus.plexus|plexus-component-annotations|2.1.0|
|org.codehaus.plexus|plexus-interpolation|1.26|
|org.codehaus.plexus|plexus-sec-dispatcher|2.0|
|org.codehaus.plexus|plexus-utils|3.5.1|
|org.codehaus.plexus|plexus-xml|4.0.1|
|org.codehaus.woodstox|stax2-api|4.2.2|
|org.dom4j|dom4j|2.1.3|
|org.eclipse.angus|angus-activation|2.0.2|
|org.eclipse.microprofile.config|microprofile-config-api|3.1|
|org.eclipse.microprofile.context-propagation|microprofile-context-propagation-api|1.3|
|org.eclipse.microprofile.health|microprofile-health-api|4.0.1|
|org.eclipse.microprofile.jwt|microprofile-jwt-auth-api|2.1|
|org.eclipse.microprofile.openapi|microprofile-openapi-api|4.0.2|
|org.eclipse.microprofile.reactive-streams-operators|microprofile-reactive-streams-operators-api|3.0.1|
|org.eclipse.parsson|parsson|1.1.7|
|org.eclipse.sisu|org.eclipse.sisu.inject|0.9.0.M3|
|org.eclipse.sisu|org.eclipse.sisu.plexus|0.9.0.M3|
|org.fusesource.jansi|jansi|2.4.0|
|org.glassfish.expressly|expressly|5.0.0|
|org.glassfish.jaxb|jaxb-core|4.0.5|
|org.glassfish.jaxb|jaxb-runtime|4.0.5|
|org.glassfish.jaxb|txw2|4.0.5|
|org.graalvm.sdk|nativeimage|23.1.2|
|org.graalvm.sdk|word|23.1.2|
|org.hamcrest|hamcrest|2.2|
|org.hdrhistogram|HdrHistogram|2.2.2|
|org.hibernate.common|hibernate-commons-annotations|7.0.3.Final|
|org.hibernate.orm|hibernate-core|6.6.7.Final|
|org.hibernate.orm|hibernate-graalvm|6.6.7.Final|
|org.hibernate.validator|hibernate-validator|8.0.2.Final|
|org.hibernate|quarkus-local-cache|0.3.0|
|org.jacoco|org.jacoco.agent|0.8.12|
|org.jacoco|org.jacoco.agent|runtime|
|org.jacoco|org.jacoco.core|0.8.12|
|org.jacoco|org.jacoco.report|0.8.12|
|org.jboss.invocation|jboss-invocation|2.0.0.Final|
|org.jboss.logging|commons-logging-jboss-logging|1.0.0.Final|
|org.jboss.logging|jboss-logging-annotations|3.0.3.Final|
|org.jboss.logging|jboss-logging|3.6.1.Final|
|org.jboss.logmanager|jboss-logmanager|3.1.1.Final|
|org.jboss.marshalling|jboss-marshalling|2.2.2.Final|
|org.jboss.narayana.jta|narayana-jta|7.1.0.Final|
|org.jboss.narayana.jts|narayana-jts-integration|7.1.0.Final|
|org.jboss.slf4j|slf4j-jboss-logmanager|2.0.0.Final|
|org.jboss.threads|jboss-threads|3.8.0.Final|
|org.jboss|jboss-transaction-spi|8.0.0.Final|
|org.jctools|jctools-core|4.0.5|
|org.junit.jupiter|junit-jupiter-api|5.10.5|
|org.junit.jupiter|junit-jupiter-engine|5.10.5|
|org.junit.jupiter|junit-jupiter-params|5.10.5|
|org.junit.jupiter|junit-jupiter|5.10.5|
|org.junit.platform|junit-platform-commons|1.10.5|
|org.junit.platform|junit-platform-engine|1.10.5|
|org.junit.platform|junit-platform-launcher|1.10.5|
|org.latencyutils|LatencyUtils|2.0.3|
|org.mockito|mockito-core|5.15.2|
|org.mockito|mockito-junit-jupiter|5.15.2|
|org.mockito|mockito-subclass|5.15.2|
|org.objenesis|objenesis|3.3|
|org.opentest4j|opentest4j|1.3.0|
|org.osgi|org.osgi.annotation.bundle|2.0.0|
|org.osgi|org.osgi.annotation.versioning|1.1.2|
|org.ow2.asm|asm-analysis|9.7.1|
|org.ow2.asm|asm-commons|9.7.1|
|org.ow2.asm|asm-tree|9.7.1|
|org.ow2.asm|asm-util|9.7.1|
|org.ow2.asm|asm|9.7.1|
|org.reactivestreams|reactive-streams|1.0.4|
|org.slf4j|slf4j-api|2.0.6|
|org.wildfly.common|wildfly-common|1.7.0.Final|
|org.wildfly.security|wildfly-elytron-asn1|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-auth-server|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-auth|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-base|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-credential|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-encryption|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-keystore|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-password-impl|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-permission|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-provider-util|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-realm|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-util|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-x500-cert-util|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-x500-cert|2.6.0.Final|
|org.wildfly.security|wildfly-elytron-x500|2.6.0.Final|
|org.yaml|snakeyaml|2.3|
|software.amazon.awssdk|annotations|2.29.14|
|software.amazon.awssdk|apache-client|2.29.14|
|software.amazon.awssdk|arns|2.29.14|
|software.amazon.awssdk|auth|2.29.14|
|software.amazon.awssdk|aws-core|2.29.14|
|software.amazon.awssdk|aws-query-protocol|2.29.14|
|software.amazon.awssdk|aws-xml-protocol|2.29.14|
|software.amazon.awssdk|checksums-spi|2.29.14|
|software.amazon.awssdk|checksums|2.29.14|
|software.amazon.awssdk|crt-core|2.29.14|
|software.amazon.awssdk|endpoints-spi|2.29.14|
|software.amazon.awssdk|http-auth-aws-eventstream|2.29.14|
|software.amazon.awssdk|http-auth-aws|2.29.14|
|software.amazon.awssdk|http-auth-spi|2.29.14|
|software.amazon.awssdk|http-auth|2.29.14|
|software.amazon.awssdk|http-client-spi|2.29.14|
|software.amazon.awssdk|identity-spi|2.29.14|
|software.amazon.awssdk|json-utils|2.29.14|
|software.amazon.awssdk|metrics-spi|2.29.14|
|software.amazon.awssdk|profiles|2.29.14|
|software.amazon.awssdk|protocol-core|2.29.14|
|software.amazon.awssdk|regions|2.29.14|
|software.amazon.awssdk|retries-spi|2.29.14|
|software.amazon.awssdk|retries|2.29.14|
|software.amazon.awssdk|s3|2.29.14|
|software.amazon.awssdk|sdk-core|2.29.14|
|software.amazon.awssdk|third-party-jackson-core|2.29.14|
|software.amazon.awssdk|url-connection-client|2.29.14|
|software.amazon.awssdk|utils|2.29.14|
|software.amazon.eventstream|eventstream|1.0.1|


## Lista licenze in uso

 * apache_v2   : Apache License version 2.0
 * bsd_2       : BSD 2-Clause License
 * bsd_3       : BSD 3-Clause License
 * cddl_v1     : COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0
 * epl_only_v1 : Eclipse Public License - v 1.0
 * epl_only_v2 : Eclipse Public License - v 2.0
 * epl_v1      : Eclipse Public + Distribution License - v 1.0
 * epl_v2      : Eclipse Public License - v 2.0 with Secondary License
 * eupl_v1_1   : European Union Public License v1.1
 * fdl_v1_3    : GNU Free Documentation License (FDL) version 1.3
 * gpl_v1      : GNU General Public License (GPL) version 1.0
 * gpl_v2      : GNU General Public License (GPL) version 2.0
 * gpl_v3      : GNU General Public License (GPL) version 3.0
 * lgpl_v2_1   : GNU General Lesser Public License (LGPL) version 2.1
 * lgpl_v3     : GNU General Lesser Public License (LGPL) version 3.0
 * mit         : MIT-License

# Supporto

Mantainer del progetto è [Engineering Ingegneria Informatica S.p.A.](https://www.eng.it/).

# Contributi

Se interessati a contribuire alla crescita del progetto potete scrivere all'indirizzo email <a href="mailto:areasviluppoparer@regione.emilia-romagna.it">areasviluppoparer@regione.emilia-romagna.it</a>.

# Credits

Progetto di proprietà di [Regione Emilia-Romagna](https://www.regione.emilia-romagna.it/) sviluppato a cura di [Engineering Ingegneria Informatica S.p.A.](https://www.eng.it/).

# Licenza

Questo progetto è rilasciato sotto licenza GNU Affero General Public License v3.0 or later ([LICENSE.txt](LICENSE.txt)).

# Appendice

## Spring Boot 3.x

Alcuni riferimenti:

- Migrazione Spring boot versione 3 https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide

