Vagrant.configure("2") do |config|
	# Ubuntu 18.04 LTS
	config.vm.box = "ubuntu/bionic64"

	config.vm.provision "shell", path: "bootstrap.sh"
	config.vm.provision "shell", path: "bootstrap_not_privileged.sh", privileged: false
end
