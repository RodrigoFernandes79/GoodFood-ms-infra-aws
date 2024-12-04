package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;


public class VPCGoodFoodStack extends Stack {
    public VPCGoodFoodStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public VPCGoodFoodStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Vpc vpc = Vpc.Builder.create(this, "GoodFoodVpc")
                .maxAzs(3)  // Default is all AZs in region
                .build();
    }
}
