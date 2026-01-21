# Create and enter xilinx_bitbake folder
mkdir -p xilinx_bitbake
cd xilinx_bitbake

# Download and install "repo" command into xilinx_bitbake
curl https://storage.googleapis.com/git-repo-downloads/repo > repo && chmod a+x repo
export PATH=$PATH:$PWD

# Use repo command to download all the different layers into sources
repo init -u https://github.com/Xilinx/yocto-manifests.git -b rel-v2024.2 && repo sync &&  repo start rel-2024.2 --all

# Clone the meta-l1calo layer repo into sources and add it as a layer to bitbake
cd sources
git clone git@github.com:UCATLAS/meta-l1calo.git
cd ../
source setupsdk && bitbake-layers add-layer ../sources/meta-l1calo 

# Manually add parallelizability to local.conf
printf "\nPARALLEL_MAKE = \"-j 32\"\n" >> conf/local.conf
printf "\nBB_NUMBER_THREADS = \"32\"\n" >> conf/local.conf

# Copy needed scripts to the xilinx_bitbake folder
cd ..
cp sources/meta-l1calo/scripts/generate_device_tree.sh ./generate_device_tree.sh
cp sources/meta-l1calo/scripts/copy_output.sh ./copy_output.sh
cp sources/meta-l1calo/scripts/write_output_to_sd.sh ./write_output_to_sd.sh
