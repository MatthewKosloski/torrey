# Update the local APT package index and then upgrade any 
# upgradeable packages.
apt-get update -y && apt-get upgrade -y

# Install Java JDK and gcc, g++, and make
apt-get install -y default-jdk build-essential

# Install Maven (used to build the compiler for tests)
apt-get install maven -y