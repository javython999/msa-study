```shell
docker run -d --network ecommerce-network  --name catalog-service -e "eureka.client.serviceUrl.defaultZone=http://service-discovery:8761/eureka/" -e "logging.file=/api-logs/catalogs-ws.log" javython999/catalog-service:1.0
```