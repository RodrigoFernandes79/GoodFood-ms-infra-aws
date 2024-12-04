package com.myorg;

import software.amazon.awscdk.App;

public class GoodFoodMsAwsInfraApp {
    public static void main(final String[] args) {
        App app = new App();

        new VPCGoodFoodStack(app, "VPC");
        app.synth();
    }
}

