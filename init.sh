sudo apt update && sudo apt install -y curl openjdk-11-jdk-headless docker-compose

echo "Install Docker and docker-compose"
curl -fsSL https://get.docker.com | sh

sudo groupadd docker
sudo usermod -aG docker "$USER"
newgrp docker
