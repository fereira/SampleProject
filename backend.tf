# backend.tf
terraform {
  backend "s3" {
    profile        = "default"
    bucket         = "cul-admin-terraform-state"
    key            = "order-import-poc/terraform.state"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "cul-admin-terraform-state-lock"
  }
}

# terraform {
#   backend "s3" {
#     profile        = "rld244-sandbox"
#     bucket         = "cul-admin-terraform-state-sandbox"
#     key            = "docker-demo-dev/terraform.state"
#     region         = "us-east-1"
#     encrypt        = true
#     dynamodb_table = "cul-admin-terraform-state-lock"
#   }
# }
