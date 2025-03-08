const cdk = require('aws-cdk-lib');
const { Template } = require('aws-cdk-lib/assertions');
const Oddscraper = require('../lib/oddscraper-stack');

test('Api Gateway Created', () => {
  // const app = new cdk.App();
  // const stack = new Oddscraper.OddscraperStack(app, 'MyTestStack');
  // const template = Template.fromStack(stack);

  // template.hasResourceProperties('AWS::ApiGateway::RestApi');
});

test('Lambda Function Created', () => {
    //   const app = new cdk.App();
    //   // WHEN
    //   const stack = new Infrastructure.InfrastructureStack(app, 'MyTestStack');
    //   // THEN
    //   const template = Template.fromStack(stack);
    
    //   template.hasResourceProperties('AWS::SQS::Queue', {
    //     VisibilityTimeout: 300
    //   });
});

test('Secrets Manager Created', () => {
  //   const app = new cdk.App();
  //   // WHEN
  //   const stack = new Infrastructure.InfrastructureStack(app, 'MyTestStack');
  //   // THEN
  //   const template = Template.fromStack(stack);
  
  //   template.hasResourceProperties('AWS::SQS::Queue', {
  //     VisibilityTimeout: 300
  //   });
});
