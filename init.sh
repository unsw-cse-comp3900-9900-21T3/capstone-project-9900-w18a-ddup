sudo apt update && sudo apt install -y curl openjdk-11-jdk-headless
sudo apt-get remove docker docker-engine docker.io containerd runc

curl -fsSL https://get.docker.com | sh

sudo apt install -y docker-compose

sudo groupadd docker
sudo usermod -aG docker "$USER"
newgrp docker
