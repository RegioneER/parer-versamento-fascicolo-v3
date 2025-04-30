<img src="src/docs/quarkus.png" width="300">
<br/><br/>

# Versamento fascicolo v3 (microservice)


## Pre-requisito

Installazione wrapper maven, attraverso il seguente comando:

```shell script
mvn wrapper:wrapper
```

## Esposizione applicazione in DEV mode 

Data la seguente configurazione: 

```bash
quarkus.http.root-path = /versamento-fascicolo-v3
```

l'applicazione (in modalità dev, vedi [paragrafo](#running-the-application-in-dev-mode)) viene esposta al seguente indirizzo (locale): ``http://localhost:10011/versamento-fascicolo-v3``.

## Requisiti per lo start dell'applicazione

Per effettuare lo start dell'applicazione occorrono i seguenti parametri (chiave=valore) i quali potranno essere configurati con le modalità descritti nella seguente guida https://quarkus.io/guides/config-reference.

Gestione dell'autenticazione con token (keycloak - OpenIdConnect):

- QUARKUS_OIDC_AUTH_SERVER_URL=${valore}
- QUARKUS_OIDC_INTROSPECTION_PATH=${valore}
- QUARKUS_OIDC_CLIENT_ID=${valore}
- QUARKUS_OIDC_CREDENTIALS_CLIENT_SECRET_VALUE=${valore}

Gestione datasource: 

- QUARKUS_DATASOURCE_JDBC_URL=${valore}
- QUARKUS_DATASOURCE_USERNAME=${valore}
- QUARKUS_DATASOURCE_PASSWORD=${valore}

Gestione object storage:

- Modalità non statica (dipendente dal DB):
    - FASCICOLI_W_ACCESSKEYID=${valore}
    - FASCICOLI_W_SECRETKEY=${valore}
    - SESSIONI_FASC_ERR_KO_W_ACCESSKEYID=${valore}
    - SESSIONI_FASC_ERR_KO_W_SECRETKEY=${valore}

- Modalità statica (indipendente dal DB):
    - QUARKUS_S3_AWS_CREDENTIALS_TYPE=static
    - QUARKUS_S3_ENDPOINT_OVERRIDE=${valore}
    - QUARKUS_S3_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID=${valore}
    - QUARKUS_S3_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY=${valore}
    - BUCKET_NAME_METADATA_FASC=${valore}
    - BUCKET_NAME_SES_ERR_KO_FASC=${valore}

In particolare, si consiglia di creare un file <strong>.env</strong> nella directory root di progetto, di seguito un esempio:

```
QUARKUS_OIDC_AUTH_SERVER_URL=https://keycloak-latest.apps.test-parerocp.ente.regione.emr.it/auth/realms/Parer
QUARKUS_OIDC_INTROSPECTION_PATH=/protocol/openid-connect/token/introspect
QUARKUS_OIDC_CLIENT_ID=<id>
QUARKUS_OIDC_CREDENTIALS_CLIENT_SECRET_VALUE=<secret>
_DEV_QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@url
_DEV_QUARKUS_DATASOURCE_USERNAME=<user>
_DEV_QUARKUS_DATASOURCE_PASSWORD=<password>
_TEST_QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@url
_TEST_QUARKUS_DATASOURCE_USERNAME=<user>
_TEST_QUARKUS_DATASOURCE_PASSWORD=<password>
_DEV_FASCICOLI_W_ACCESSKEYID=<accessKey>
_DEV_FASCICOLI_W_SECRETKEY=<secret>
_DEV_SESSIONI_FASC_ERR_KO_W_ACCESSKEYID=<accessKey>
_DEV_SESSIONI_FASC_ERR_KO_W_SECRETKEY=<secret>
_TEST_FASCICOLI_W_ACCESSKEYID=<accessKey>
_TEST_FASCICOLI_W_SECRETKEY=<secret>
_TEST_SESSIONI_FASC_ERR_KO_W_ACCESSKEYID=<accessKey>
_TEST_SESSIONI_FASC_ERR_KO_W_SECRETKEY=<secret>
_H2_QUARKUS_DATASOURCE_JDBC_URL=jdbc:h2:tcp://localhost/mem:test
```

da notare che la parte legata ai datasource viene profilata attraverso i due profili standard dev e test (vedi <strong>_DEV</strong>_config e <strong>_TEST</strong>_config).

## JUnit test con Quarkus

L'applicazione prevede l'esecuzione di test di unità (vedi stage "Test") in cui attraverso il profilo "%test" il plugin quarkus si occupa dell'esecuzione runtime dei test definiti.
Al fine di effettuare l'esecuzione correttamente il runtime di quarkus viene predisposto con un environment specifico, in cui sono necessarie le seguenti variabili d'ambiente che sono: 

- QUARKUS_DATASOURCE_JDBC_URL
- QUARKUS_DATASOURCE_USERNAME
- QUARKUS_DATASOURCE_PASSWORD
  
le quali sono definite come variabili "masked" nei settings di progetto della CI/CD di Gitlab (https://gitlab.ente.regione.emr.it/parer/okd/versamento-fascicolo-ws20/-/settings/ci_cd), il plugin quarkus lavora attraverso varie modalità di accesso a tali variabili, tra cui appunto, quelle di environment messe a disposizione in questo caso, al runner Gitlab attraverso l'impostazione citata in precedenza.

## Docker build

Per effettuare una build del progetto via Docker è stato predisposto lo standard [Dockerfile](src/main/docker/Dockerfile.jvm) e una directory [docker_build](docker_build) con all'interno i file da integrare all'immagine base <strong>registry.access.redhat.com/ubi8/openjdk-11</strong>.
La directory [docker_build](docker_build) è strutturata come segue: 
```bash
|____README.md
|____certs
| |____README.md

```
al fine di integrare certificati non presenti di default nell'immagine principale è stata introdotta la sotto-directory [docker_build/certs](docker_build/certs) in cui dovranno essere inseriti gli appositi certificati che verranno "trustati" in fase di build dell'immagine.
La compilazione dell'immagine può essere eseguita con il comando: 
```bash
docker build -t <registry> -f ./src/main/docker/Dockerfile.jvm --build-arg EXTRA_CA_CERTS_DIR=docker_build/certs .
``` 
```bash
docker build -t <registry> -f ./src/main/docker/Dockerfile.legacy-jar --build-arg EXTRA_CA_CERTS_DIR=docker_build/certs .

```

## Openshift template

Presente anche il [template](src/main/openshift/versamento-fascicolo-v3-config-template.yml) che permette la creazione dell'applicazione su soluzione Openshift (sia licensed che open).

## Documentazione da README originale (lingua inglese)

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-test-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and JPA
- YAML Configuration ([guide](https://quarkus.io/guides/config#yaml)): Use YAML to configure your Quarkus application
- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
