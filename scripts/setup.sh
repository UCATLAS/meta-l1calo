mkdir -p xilinx_bitbake
cd xilinx_bitbake

curl https://storage.googleapis.com/git-repo-downloads/repo > repo && chmod a+x repo
export PATH=$PATH:$PWD

repo init -u https://github.com/Xilinx/yocto-manifests.git -b rel-v2024.2 && repo sync &&  repo start rel-2024.2 --all

cd sources
git clone --branch kmd-update-2024 git@github.com:UCATLAS/meta-l1calo.git
cd ../
source setupsdk && bitbake-layers add-layer ../sources/meta-l1calo 

printf "\nPARALLEL_MAKE = \"-j 32\"\n" >> conf/local.conf
printf "\nBB_NUMBER_THREADS = \"32\"\n" >> conf/local.conf
#manually set machine in local.conf and add parallelizability

cd ..
cp sources/meta-l1calo/scripts/*.sh ./
rm setup.sh