```shell
docker run -d --network ecommerce-network --name order-service -e "management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans" -e "eureka.client.serviceUrl.defaultZone=http://service-discovery:8761/eureka/" -e "spring.datasource.url=jdbc:mariadb://mariadb:3306/mydb" -e "logging.file=/api-logs/orders-ws.log" javython999/order-service:1.0
```