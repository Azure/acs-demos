CONFIG_FILE=~/.acs/blc_demo.cfg
	
check_prerequisites () {
	if hash az 2>/dev/null; then
		echo "Azure CLI 2.0 is installed"
	else
		echo "Azure CLI 2.0 is not installed. Please install it and run
		 `az login`. You may also want to ensure you are using the correct subscription. Then run this script again (see https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)"
	fi

	if hash kubectl 2>/dev/null; then
		echo "Kubernetes CLI is installed"
	else
		sudo az acs kubernetes install-cli
	fi 

    if hash helm 2>/dev/null; then
		echo "Helm CLI is installed"
	else
		curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get | bash
	fi 
}

write_config () {
    echo "UUID=$UUID" > "$CONFIG_FILE"
    echo "DNS_PREFIX=$DNS_PREFIX" >> "$CONFIG_FILE"
    echo "CLUSTER_NAME=$CLUSTER_NAME" >> "$CONFIG_FILE"
    echo "RESOURCE_GROUP=$RESOURCE_GROUP" >> "$CONFIG_FILE"
    echo "LOCATION=$LOCATION" >> "$CONFIG_FILE"
    echo "Config file written to $CONFIG_FILE"
}

read_config () {
	if [ ! -f "$CONFIG_FILE" ]; then
      echo "Configuration file not found"
	  UUID=$(cat /dev/urandom | tr -dc 'A-Z0-9' | fold -w 8 | head -n 1)
	  DNS_PREFIX=blc-k8s-$UUID
	  CLUSTER_NAME=$DNS_PREFIX
	  RESOURCE_GROUP=$DNS_PREFIX
	  LOCATION=eastus	
	  write_config
	else
		source $CONFIG_FILE
		echo "Config read from $CONFIG_FILE"
	fi

	echo "Working with a Kubernetes cluster on ACS with DNS Prefix of $DNS_PREFIX in resource group $RESOURCE_GROUP ($LOCATION)"

}

create_resource_group () {
	if [[ $(az group show --name=$RESOURCE_GROUP --query=properties.provisioningState) ]]; then
        echo "Looks like the resource group named $RESOURCE_GROUP already exists"
    else
    	echo "Looks like the resource group named $RESOURCE_GROUP does not exist. Creating it."
        az group create --location=$LOCATION --name=$RESOURCE_GROUP
    fi
}

create_kubernetes_cluster () {
	if [[ $(az acs list --resource-group=blc-k8s-HJ1GE12K --query="[?name == '$CLUSTER_NAME']") ]]; then
        echo "Looks like the ACS cluster named $CLUSTER_NAME already exists."
    else
    	echo "Looks like the ACS cluster named $CLUSTER_NAME does not exist. Creating it."
        az acs create --orchestrator-type=kubernetes --resource-group=$RESOURCE_GROUP --name=$CLUSTER_NAME --dns-prefix=$DNS_PREFIX --generate-ssh-keys
    fi
	
	echo "Getting credentials for ACS cluster named $CLUSTER_NAME"
    az acs kubernetes get-credentials --resource-group=$RESOURCE_GROUP --name=$CLUSTER_NAME
    
    echo "Nodes in the cluster:"
    kubectl get nodes
}

check_prerequisites
read_config
create_resource_group
create_kubernetes_cluster