package com.nathanthomp.oddscraper;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Function;

public class OddscraperStack extends Stack {
    public OddscraperStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public OddscraperStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function hello = Function.Builder.create(this, "MyFunction")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.NODEJS_LATEST)
                .code(software.amazon.awscdk.services.lambda.Code.fromAsset("lib/lambda-handler"))
                .handler("index.handler")
                .build();

        LambdaRestApi api = LambdaRestApi.Builder.create(this, "ApiGwEndpoint")
                .restApiName("HelloApi")
                .handler(hello)
                .build();

    }
}