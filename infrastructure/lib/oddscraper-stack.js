const { Stack } = require('aws-cdk-lib');
const lambda = require('aws-cdk-lib/aws-lambda');
const apigw = require('aws-cdk-lib/aws-apigateway');

class OddscraperStack extends Stack {

  constructor(scope, id, props) {
    super(scope, id, props);
    const fn = new lambda.Function(this, 'OddscraperLambda', {
      code: lambda.Code.fromAsset('../target/oddscraper-1.0.0-SNAPSHOT.jar'),
      runtime: lambda.Runtime.JAVA_21,
      handler: 'com.nathanthomp.oddscraper.OddscraperRequestHandler::handleRequest'
    });

    const endpoint = new apigw.LambdaRestApi(this, 'OddscraperApi', {
      handler: fn,
    });

  }
}

module.exports = { OddscraperStack }