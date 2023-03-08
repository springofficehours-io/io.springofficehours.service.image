az extension add --name containerapp --upgrade
az provider register --namespace Microsoft.App
az provider register --namespace Microsoft.OperationalInsights
az containerapp env create --name "$MANAGED_ENVIRONMENT" --resource-group "$RESOURCE_GROUP" --location "$LOCATION" --no-wait
#Install spring extension specifically designed for StandardGen2 Azure Spring Apps
az extension remove -n spring && \
az extension add --source https://ascprivatecli.blob.core.windows.net/cli-extension/spring-1.1.11-py3-none-any.whl
#Register the Microsoft.AppPlatform provider for Azure Spring Apps
az provider register --namespace Microsoft.AppPlatform
az spring create \
        --resource-group "${RESOURCE_GROUP}" \
        --name "${SPRING_APPS_NAME}" \
        --managed-environment "${MANAGED_ENV_RESOURCE_ID}" \
        --sku standardGen2 \
        --location "${LOCATION}"
az spring app create --resource-group "${RESOURCE_GROUP}" --service "${SPRING_APPS_NAME}" --name image-service --cpu 1 --memory 2Gi --assign-endpoint true
mvn clean package -DskipTests
az spring app deploy --resource-group "${RESOURCE_GROUP}" --service "${SPRING_APPS_NAME}" --name image-service --artifact-path ./target/image-0.0.1-SNAPSHOT.jar --runtime-version Java_17 --jvm-options '-Xms1024 -Xmx2048m'
az spring app scale --resource-group "$RESOURCE_GROUP" --service "${SPRING_APPS_NAME}" --name image-service --min-instance-count 0 --max-instance-count 2