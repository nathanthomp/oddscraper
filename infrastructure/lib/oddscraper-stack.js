const { Stack } = require('aws-cdk-lib');
const lambda = require('aws-cdk-lib/aws-lambda');
const apigateway = require('aws-cdk-lib/aws-apigateway');
const secretsmanager = require('aws-cdk-lib/aws-secretsmanager')
const iam = require('aws-cdk-lib/aws-iam')

class OddscraperStack extends Stack {

  constructor(scope, id, props) {
    super(scope, id, props);

    const secret = new secretsmanager.Secret(this, 'OddscraperSecretsManager');

    const fnRole = new iam.Role(this, 'OddscraperLambdaRole', {
      assumedBy: new iam.ServicePrincipal('lambda.amazonaws.com')
    });

    secret.grantRead(fnRole);
    
    const fn = new lambda.Function(this, 'OddscraperLambda', {
      code: lambda.Code.fromAsset('../target/oddscraper-1.0.0-SNAPSHOT.jar'),
      runtime: lambda.Runtime.JAVA_21,
      handler: 'com.nathanthomp.oddscraper.OddscraperRequestHandler::handleRequest',
      role: fnRole
    });

    const endpoint = new apigateway.LambdaRestApi(this, 'OddscraperApi', {
      handler: fn,
    });

  }
}

module.exports = { OddscraperStack }