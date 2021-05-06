# variables.tf
variable "profile" {
  description = "The AWS credentials profile to use"
  type        = string
}

variable "aws_account_number" {
  description = "The AWS account number to use"
  type        = string
  sensitive   = true
}

# variable "aws_region" {
#   description = "The AWS region things are created in"
#   default     = "us-east-1"
# }

variable "project" {
  description = "The project name to use as a prefix for resources created"
  type        = string
}

variable "environment" {
  description = "The name of the environment for this project. Should be like sandbox, dev, stag or prod"
  type        = string
}

# variable "domain_name" {
#   description = "The root URL of that will be used on the ACM cert"
#   type        = string
# }

variable "vpc_id" {
  description = "The AWS VPC ID to use"
  type        = string
}

variable "public_subnet1_id" {
  description = "The AWS public subnet 1 ID to use"
  type        = string
}

variable "public_subnet2_id" {
  description = "The AWS public subnet 2 ID to use"
  type        = string
}

variable "private_subnet1_id" {
  description = "The AWS private subnet 1 ID to use"
  type        = string
}

variable "private_subnet2_id" {
  description = "The AWS private subnet 2 ID to use"
  type        = string
}

# variable "ecs_task_execution_role_name" {
#   description = "ECS task execution role name"
#   type        = string
#   default     = "myEcsTaskExecutionRole"
# }

# variable "az_count" {
#   description = "Number of AZs to cover in a given region"
#   type        = number
#   default     = 2
# }

variable "service_vars" {
  description = "Set of variables that are unique to each service"
  type = map(object({
    service_name        = string
    image_name          = string
    image_tag           = string
    container_port      = optional(number)
    path                = string
    health_check_path   = optional(string)
    listener_priority   = number
    env_file_arn        = optional(string)
    env_file_bucket_arn = optional(string)
  }))
}

# variable "app_port" {
#   description = "Port exposed by the docker image to redirect traffic to"
#   type        = number
#   default     = 80
# }

variable "app_count" {
  description = "Number of docker containers to run"
  type        = number
  default     = 1
}

# variable "health_check_path" {
#   default = "/"
# }

variable "fargate_cpu" {
  description = "Fargate instance CPU units to provision (1 vCPU = 1024 CPU units)"
  type        = number
  default     = 256
}

variable "fargate_memory" {
  description = "Fargate instance memory to provision (in MiB)"
  type        = number
  default     = 512
}

variable "cidrs" {
  description = "Array of cidr blocks to allow traffic through load balancer"
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "lb_internal" {
  description = "Is the load balancer internal facing only or not"
  type        = bool
  default     = false
}

variable "tags" {
  description = "Set of default tags to apply to all resources that support tagging"
  type        = map(any)
  default = {
    "Cost Center" = "L854711"
  }
}
