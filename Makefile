

# Config to generate code using buf.
gen-code-java:
	rm -rf shared/proto-gen-java/src
	buf generate --template shared/proto-gen-java/buf.gen.yaml

# Configurations related to test workflows.
act_cmd=act -P ubuntu-latest=catthehacker/ubuntu:act-latest --bind --env GITHUB_REF_NAME=dev --container-architecture linux/arm64 --secret-file .secrets -W

users-workflow:
	 $(act_cmd) .github/workflows/users.yml

workspace-workflow:
	 $(act_cmd) .github/workflows/workspaces.yml

gateway-workflow:
	 $(act_cmd) .github/workflows/gateway.yml

access-manager-workflow:
	 $(act_cmd) .github/workflows/access-manager.yml

web-workflow:
	 $(act_cmd) .github/workflows/web.yml

# Docker hub username
APP_NAME=biswasakash/nexussphere

# Command
DOCKER_BUILD_CMD=docker build --platform linux/amd64,linux/arm64 -t $(APP_NAME)
DOCKER_TAG_CMD=docker tag $(APP_NAME)

users-build:
	$(DOCKER_BUILD_CMD)-users:local -f services/users/Dockerfile .

workspace-build:
	$(DOCKER_BUILD_CMD)-workspaces:local -f services/workspaces/Dockerfile .

gateway-build:
	$(DOCKER_BUILD_CMD)-gateway:local -f services/gateway/Dockerfile .

access-manager-build:
	$(DOCKER_BUILD_CMD)-access_manager:local -f services/access-manager/Dockerfile .

web-build:
	$(DOCKER_BUILD_CMD)-web:local -f web/Dockerfile .


build-all: users-build gateway-build workspace-build access-manager-build web-build 


DOCKER_PUSH_CMD=docker push $(APP_NAME)

# Container registry push config
users-push:
	$(DOCKER_PUSH_CMD)-users:local

gateway-push:
	$(DOCKER_PUSH_CMD)-gateway:local

access-manager-push:
	$(DOCKER_PUSH_CMD)-access-manager:local

workspace-push:
	$(DOCKER_PUSH_CMD)-workspaces:local

web-push:
	$(DOCKER_PUSH_CMD)-web:local

push-all: users-push gateway-push workspace-push access-manager-push web-push

# Delete containers config.
users-delete:
	docker rmi -f $(APP_NAME)-users:local

gateway-delete:
	docker rmi -f $(APP_NAME)-gateway:local

workspace-delete:
	docker rmi -f $(APP_NAME)-workspace:local

access-manager-delete:
	docker rmi -f $(APP_NAME)-access-manager:local

web-delete:
	docker rmi -f $(APP_NAME)-web:local

delete-all: users-delete gateway-delete access-manager-delete workspace-delete web-delete


