.DEFAULT_GOAL := help
DOCKER_COMPOSE ?= docker compose
# Les commandes Android reconstruisent l'image si son Dockerfile a changé (le cache la rend instantanée) ; la CI la précharge et passe ANDROID_BUILD=
ANDROID_BUILD ?= --build
ANDROID_RUN = $(DOCKER_COMPOSE) --profile android run --rm $(ANDROID_BUILD)
# Les conteneurs qui écrivent dans le dépôt tournent avec ton identifiant, pas root.
export HOST_UID := $(shell id -u)
export HOST_GID := $(shell id -g)

## —— Android 🤖 —————————————————————————————————————————————————————————————
.PHONY: android android-test android-screenshots android-release android-release-verify android-release-selftest check clean

android: ## Build the debug APK: dist/petitsjeux-debug.apk (first run downloads the SDK and Gradle: several minutes)
	@mkdir -p dist
	@$(ANDROID_RUN) android

android-test: ## Run the Kotlin unit tests (game engine, levels, saves)
	@$(ANDROID_RUN) android-test

android-screenshots: ## Render the screens to PNG with Robolectric (no phone needed): docs/screenshots/
	@mkdir -p docs/screenshots
	@$(ANDROID_RUN) android-screenshots

android-release: ## Build the signed APK dist/petitsjeux.apk (needs ANDROID_KEYSTORE_BASE64/_PASSWORD, ANDROID_KEY_ALIAS/_PASSWORD and ANDROID_VERSION_TAG=android-vX.Y.Z; refuses without a key)
	@mkdir -p dist
	@$(ANDROID_RUN) android ./build.sh release

android-release-verify: ## Check the signed APK right after android-release (needs ANDROID_VERSION_TAG and ANDROID_EXPECTED_CERT_SHA256, the public certificate fingerprint)
	@$(ANDROID_RUN) android ./build.sh verify-release

android-release-selftest: ## Prove the release refuses without a key and succeeds with a throwaway one (nothing is kept)
	@$(ANDROID_RUN) android ./build.sh release-selftest

check: android-test ## Run all the tests

clean: ## Remove the built APKs and the Gradle cache volume
	@rm -rf dist
	@$(DOCKER_COMPOSE) --profile android down --volumes --remove-orphans

## —— Design 🎨 ——————————————————————————————————————————————————————————————
.PHONY: design

design: ## Regenerate the mockups docs/design/*.svg (PNG: node tools/design/render.js, needs Playwright)
	@node tools/design/make-design.js

## —— Aide ———————————————————————————————————————————————————————————————————
.PHONY: help
help: ## Outputs this help screen
	@grep -E '(^[a-zA-Z0-9_-]+:.*?##.*$$)|(^##)' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}{printf "\033[32m%-24s\033[0m %s\n", $$1, $$2}' | sed -e 's/\[32m##/[33m/'
