package com.myorg;

import software.amazon.awscdk.Fn;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.Map;

public class ServiceGoodFoodStack extends Stack {
    public ServiceGoodFoodStack(final Construct scope, final String id, final Cluster cluster) {
        this(scope, id, null, cluster);
    }

    public ServiceGoodFoodStack(final Construct scope, final String id, final StackProps props, final Cluster cluster) {
        super(scope, id, props);

        Map<String, String> dbAuthentication = new HashMap<>();
        dbAuthentication.put("SPRING_DATASOURCE_URL",
                "jdbc:mysql://" + Fn.importValue("orders-db-endpoint") + ":3306/order-ms-database?createDatabaseIfNotExist=true");
        dbAuthentication.put("SPRING_DATASOURCE_USERNAME", "admin");
        dbAuthentication.put("SPRING_DATASOURCE_PASSWORD", Fn.importValue("orders-db-senha"));

        IRepository repository = Repository.fromRepositoryName(this, "repository", "img-orders-ms");

        // Create a load-balanced Fargate service and make it public
        ApplicationLoadBalancedFargateService app = ApplicationLoadBalancedFargateService.Builder.create(this, "GoodFoodService")
                .serviceName("GoodFood-service-ola")
                .cluster(cluster)           // Required
                .cpu(512)                   // Default is 256
                .desiredCount(1)           // Default is 1
                .listenerPort(8080)
                .assignPublicIp(true)
                .taskImageOptions(
                        ApplicationLoadBalancedTaskImageOptions.builder()
                                .image(ContainerImage.fromEcrRepository(repository))
                                .containerPort(8080)
                                .containerName("orders-ms")
                                .environment(dbAuthentication)
                                .build())
                .memoryLimitMiB(1024)       // Default is 512
                .publicLoadBalancer(true)   // Default is true
                .build();
        app.getTargetGroup().configureHealthCheck(HealthCheck.builder().path("/orders").build());
    }
}
