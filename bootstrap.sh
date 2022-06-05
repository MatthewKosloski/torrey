# Update the local APT package index and then upgrade any
# upgradeable packages.
apt-get update -y && apt-get upgrade -y

# Install Java JDK and gcc, g++, and make
apt-get install -y default-jdk build-essential

# Install Maven
apt-get install maven -y

# Install Clojure
curl -O https://download.clojure.org/install/linux-install-1.11.0.1100.sh
chmod +x linux-install-1.11.0.1100.sh
sudo ./linux-install-1.11.0.1100.sh
