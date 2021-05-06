# terraform-cul-ecs-template
This is a template of Terraform configuration for creating ECS infrastructure and services. It calls the Terraform module [terraform-cul-ecs-base](https://github.com/cul-it/terraform-cul-ecs-base) which in turn calls the module [terraform-cul-ecs-service](https://github.com/cul-it/terraform-cul-ecs-service). These will create all the resources necessary to deploy services defined in [services.auto.tfvars](./services.auto.tfvars) to ECS.

To use this template you have to update a few things.
### [backend.tf](./backend.tf)
Change "docker-demo" in the following reference to an s3 key to something unique like the project name. The s3 backend stores the state of the Terraform deployment and each project should have a separate directory and key.
```
key            = "docker-demo/terraform.state"
```
### default.tfvars
##### *Don't commit this file to the repo!!!*
There can be multiple tfvars files that are used for different accounts. Having a separate sandbox.tfvars file can be useful. See the [example](./default.tfvars.example).
- profile            = This is the AWS profile in "$HOME/.aws/credentials" to use. This is useful when you want to be using the Sandbox account as well as the main account.
- aws_account_number = This is the AWS account number. Defining this here keeps the account number out of Github and is also useful for using the Sandbox.
- environment        = If this is set to "sandbox" the deployment will use "aws-ecs-sandbox" in the base url. See the top of [main.tf](./main.tf)
- vpc_id             = The id of the AWS VPC to use.
- public_subnet1_id  = The id of a public subnet to use.
- public_subnet2_id  = The id of another public subnet to use.
- private_subnet1_id = The id of a private subnet to use.
- private_subnet2_id = The id of another private subnet to use.

### [services.auto.tfvars](./services.auto.tfvars)
Terraform loads ".auto.tfvars" files and these aren't excluded from the repo in [.gitignore](./.gitignore). Here we define a project name that will be used as a prefix for almost every resource that is created. Then there is an array of services, each having the following variables.
- service_name        = The name of the service in ECS. Project name plus service name must be unique.
- image_name          = The name of the ECR repo.
- image_tag           = The tag of the image to deploy.
- container_port      = (optional) The port the container is listening on. Defaults to 80 if not provided.
- path                = The path attached to the domain name to reach the running container.
- health_check_path   = (optional) The path used by ECS to check if the container is healthy. Defaults to `path` if not provided.
- listener_priority   = Listener rules need a prioritization. Give each service a different one. They are evaluated from lowest to highest number.
- env_file_bucket_arn = (optional) The s3 bucket ARN of bootstrapping environment variables if needed.
- env_file_arn        = (optional) The s3 object ARN of bootstrapping environment variables if needed.

## Workspaces
Terraform allows for the creation of workspaces that can be used to deploy configurations for different "environments" or code branches. This can be useful to have a "default" workspace for production and a "dev" workspace for development, etc. For workspaces other than "default" the workspace name is appended to resources via the project_name variable and is added to the URL via the domain_name variable. Each workspace will have its own ECS cluster, services, load balancer, URL, etc. See [main.tf](./main.tf).

## Initial deployment
With library.cornell.edu URLs the initial deployment require some work in Cornell's DNSDB to do a DNS validation for the ACM certificate. This will cause the first application of a Terraform configuration with a new URL to fail. After doing the DNSDB work, and waiting for it to propagate, rerun the configuration application and all should be well.

After that add a CName of the desired URL to the ALB hostname in DNSDB. Talk to the LibSys folks for help with DNSDB work.
## Useful commands
To update the external modules code in this configuration. This is needed when the code in the external modules has changed.
```
terraform get -update
```
To create a new workspace
```
terraform workspace new dev
```
To list and switch to a workspace
```
terraform workspace list
terraform workspace select dev
```
To run a "dry-run" plan reference a tfvars file. See the [example](./default.tfvars.example).
```
terraform plan -var-file="default.tfvars"
```
To deploy the configuration
```
terraform apply -var-file="default.tfvars"
```
To show current state
```
terraform show
```
To remove the configuration
```
terraform destroy -var-file="default.tfvars"
```
Tired of typing "terraform"? Make it an alias in your `~/.bash_profile`
```
alias tf='terraform'
```
