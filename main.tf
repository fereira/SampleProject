# main.tf
locals {
  base_url = var.environment == "sandbox" ? "aws-ecs-sandbox.library.cornell.edu" : "aws-ecs.library.cornell.edu"
}
module "cul-ecs-base" {
  source = "git@github.com:cul-it/terraform-cul-ecs-base.git"
  # source             = "../terraform-cul-ecs-base"
  profile            = var.profile
  aws_account_number = var.aws_account_number
  project_name       = terraform.workspace == "default" ? "${var.project}-prod" : "${var.project}-${terraform.workspace}"
  domain_name        = local.base_url
  vpc_id             = var.vpc_id
  public_subnet1_id  = var.public_subnet1_id
  public_subnet2_id  = var.public_subnet2_id
  private_subnet1_id = var.private_subnet1_id
  private_subnet2_id = var.private_subnet2_id
  service_vars       = var.service_vars
  cidrs              = var.cidrs
  lb_internal        = var.lb_internal
  tags = merge({
    Environment = terraform.workspace == "default" ? "prod" : terraform.workspace
  }, var.tags)
}
