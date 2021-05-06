project = "folio-oimp"
service_vars = {
  order-import-poc = {
    service_name        = "oimp-poc"
    image_name          = "folio/order-import-poc"
    image_tag           = "latest"
    container_port      = 8080
    path                = "order-import-poc"
    health_check_path   = "order-import-poc/import"
    listener_priority   = 100
    env_file_bucket_arn = "arn:aws:s3:::ecs-env-files"
    env_file_arn        = "arn:aws:s3:::ecs-env-files/folio/order-import-poc/.env"
  }
}
cidrs       = ["10.17.58.0/24", "10.92.108.0/22"]
lb_internal = true
tags = {
  Application          = "Folio"
  "Cost Center"        = "L853737"
  "Technical Contact"  = "rld244"
  "Functional Contact" = "pr55"
}
