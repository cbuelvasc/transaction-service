# Transaction Service

## REST API

* Business Domain

| Endpoint	                  | Method   | Req. body    | Status | Resp. body       | Description    		   	                      |
|:---------------------------:|:--------:|:------------:|:------:|:----------------:|:-----------------------------------------------:|
| `/api/transactions`         | `GET`    |              | 200    | Transactions[]   | Get all the transaction.                        |
| `/api/transactions`         | `POST`   | Transactions | 201    | Transactions     | Add a new transaction.                          |
|                             |          |              | 422    |                  | A transaction with the same id already exists.  |
| `/api/transactions/{id}`    | `GET`    |              | 200    | Transactions     | Get the transaction with the given id.          |
|                             |          |              | 404    |                  | No transaction with the given id exists.        |
| `/api/transactions/{id}`    | `PUT`    | Transactions | 200    | Transactions     | Update the transaction with the given id.       |
|                             |          |              | 201    | Transactions     | Update a transaction with the given id.         |
|                             |          |              | 404    |                  | No transaction with the given id exists.        |
| `/api/transactions/{id}`    | `DELETE` |              | 204    |                  | Delete the transaction with the given id.       |

* Monitoring        

| Endpoint	                  | Method   | Status | Description    		   	                        |
|:---------------------------:|:--------:|:------:|:-----------------------------------------------:|
| `/actuator`                 | `GET`    | 200    | Get info of actuator.                           |
| `/actuator/refresh`         | `POST`   | 200    | Update properties.                              |
| `/actuator/info`            | `GET`    | 200    | Get info of service.                            |
| `/actuator/health`          | `GET`    | 200    | Get health of service.                          |
| `/actuator/health/readiness`| `GET`    | 200    | Get readiness state of service.                 |
| `/actuator/health/liveness` | `GET`    | 200    | Get liveness state of service.                  |
| `/actuator/flyway`          | `GET`    | 200    | Get config of flyway.                           |
| `/actuator/prometheus`      | `GET`    | 200    | Get Prometheus metrics of service.              |
| `/actuator/metrics`         | `GET`    | 200    | Get all metrics of service.                     |


## Useful Commands

| Gradle Command	                 | Description                                                  |
|:-----------------------------------|:-------------------------------------------------------------|
| `./gradlew bootRun`                | Run the application.                                         |
| `./gradlew build`                  | Build the application.                                       |
| `./gradlew build -x test`          | Build the application and omit test.                         |
| `./gradlew test`                   | Run tests.                                                   |
| `./gradlew bootJar`                | Package the application as a JAR.                            |
| `./gradlew bootBuildImage`         | Package the application as a container image.                |
| `./gradlew bootBuildImage -x test` | Package the application as a container image and omit test.  |

After building the application, you can also run it from the Java CLI:

```bash
java -jar build/libs/transaction-service-0.0.1-SNAPSHOT.jar
```

## Docker Registry
```bash
docker tag transaction-service:0.0.1-SNAPSHOT cbuelvasc/transaction-service:0.0.1-SNAPSHOT
```

```bash
docker push cbuelvasc/transaction-service:0.0.1-SNAPSHOT
```