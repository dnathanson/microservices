# There can only be a single job definition per file.
# Create a job with ID and Name 'example'
job "netflix-poc" {
	# Run the job in the global region, which is the default.
	region = "us-east-1"

	# Specify the datacenters within the region this job can run in.
	datacenters = ["us-east-1a", "us-east-1c", "us-east-1d"]

	# Service type jobs optimize for long-lived services. This is
	# the default but we can change to batch for short-lived tasks.
	# type = "service"

	# Priority controls our access to resources and scheduling priority.
	# This can be 1 to 100, inclusively, and defaults to 50.
	# priority = 50

	# Restrict our job to only linux. We can specify multiple
	# constraints as needed.
	constraint {
		attribute = "$attr.kernel.name"
		value = "linux"
	}

	# Configure the job to do rolling updates
	update {
		# Stagger updates every 10 seconds
		stagger = "10s"

		# Update a single task at a time
		max_parallel = 1
	}

	# Create a 'cache' group. Each task in the group will be
	# scheduled onto the same machine.
	group "admin" {
		# Control the number of instances of this groups.
		# Defaults to 1
		count = 1

		# Restart Policy - This block defines the restart policy for TaskGroups,
		# the attempts value defines the number of restarts Nomad will do if Tasks
		# in this TaskGroup fails in a rolling window of interval duration
		# The delay value makes Nomad wait for that duration to restart after a Task
		# fails or crashes.
		restart {
			interval = "5m"
			attempts = 10
			delay = "25s"
		}

		# Define a task to run
		task "admin-server" {
			# Use Docker to run the task.
			driver = "docker"

			# Configure Docker driver with the image
			config {
				image = "docker.appdirectondemand.com/appdirect/admin-server:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			# We must specify the resources required for
			# this task to ensure it runs on a machine with
			# enough capacity.
			resources {
				cpu = 500 # 500 Mhz
				memory = 512 # 256MB
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}

	group "gateway" {
		count = 3

		restart {
			interval = "5m"
			attempts = 10
			delay = "25s"
		}

		task "api-gateway" {
			driver = "docker"

			config {
				image = "docker.appdirectondemand.com/appdirect/api-gateway:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			resources {
				cpu = 500
				memory = 512
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}

	group "config" {
		count = 2

		task "config-server" {
			driver = "docker"

			config {
				image = "docker.appdirectondemand.com/appdirect/config-server:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			resources {
				cpu = 500
				memory = 512
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}

	group "monitoring" {
		count = 1

		restart {
			interval = "5m"
			attempts = 10
			delay = "25s"
		}

		task "hystrix-dashboard" {
			driver = "docker"

			config {
				image = "docker.appdirectondemand.com/appdirect/hystrix-dashboard:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			resources {
				cpu = 500
				memory = 512
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}

	group "ping" {
		count = 3

		restart {
			interval = "5m"
			attempts = 10
			delay = "25s"
		}

		task "ping-service" {
			driver = "docker"

			config {
				image = "docker.appdirectondemand.com/appdirect/ping-service:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			resources {
				cpu = 500
				memory = 512
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}

	group "pong" {
		count = 3

		restart {
			interval = "5m"
			attempts = 10
			delay = "25s"
		}

		task "pong-service" {
			driver = "docker"

			config {
				image = "docker.appdirectondemand.com/appdirect/pong-service:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			resources {
				cpu = 500
				memory = 512
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}

	group "turbine" {
		count = 1

		restart {
			interval = "5m"
			attempts = 10
			delay = "25s"
		}

		task "turbine-server" {
			driver = "docker"

			config {
				image = "docker.appdirectondemand.com/appdirect/turbine-server:1.0-SNAPSHOT"
				network_mode = "host"
				auth {
					username = "docker-ro"
					password = "docker-ro"
					email = "plaftormdev@appdirect.com"
					server-address = "docker.appdirectondemand.com"
				}
			}

			env {
				SERVER_PORT = "${NOMAD_PORT_http}"
				EUREKA_CLIENT_SERVICE_URL_defaultZone = "http://eurekapoc.appdirectondemand.com:8761/eureka/"
			}

			resources {
				cpu = 500
				memory = 512
				network {
					mbits = 10
					port "http" {}
				}
			}
		}
	}
}
