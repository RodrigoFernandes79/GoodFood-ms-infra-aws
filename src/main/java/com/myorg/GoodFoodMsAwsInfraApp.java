package com.myorg;

import software.amazon.awscdk.App;

public class GoodFoodMsAwsInfraApp {
    public static void main(final String[] args) {
        App app = new App();

        VPCGoodFoodStack vpcStack = new VPCGoodFoodStack(app, "VPC");
        ClusterGoodFoodStack clusterStack = new ClusterGoodFoodStack(app, "CLUSTER", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);
        ServiceGoodFoodStack serviceStack = new ServiceGoodFoodStack(app, "SERVICE", clusterStack.getCluster());
        serviceStack.addDependency(clusterStack);
        app.synth();
    }
}

