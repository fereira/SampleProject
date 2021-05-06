# outputs.tf

output "aws_profile" {
  value = var.profile
}

output "alb_hostname" {
  value = module.cul-ecs-base.alb_hostname
}

output "terrraform_workspace" {
  value = terraform.workspace
}
