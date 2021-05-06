# provider.tf

# Specify the provider and access details
provider "aws" {
  shared_credentials_file = "$HOME/.aws/credentials"
  profile                 = var.profile
  region                  = var.aws_region
}
