#!/bin/bash

STACK_NAME="MySpringBootAppStack"
TEMPLATE_FILE="cloudformation.yaml"
CAPABILITIES="CAPABILITY_NAMED_IAM"

# Deploy the CloudFormation stack
aws cloudformation deploy \
  --template-file $TEMPLATE_FILE \
  --stack-name $STACK_NAME \
  --capabilities $CAPABILITIES \
  --parameter-overrides \
    ParameterKey=ExampleParameter,ParameterValue=ExampleValue \
  --tags \
    project=MySpringBootApp