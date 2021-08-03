mkdir -p xilinx_bitbake
cd xilinx_bitbake

curl https://storage.googleapis.com/git-repo-downloads/repo > repo && chmod a+x repo
export PATH=$PATH:$PWD

repo init -u git://github.com/Xilinx/yocto-manifests.git -b rel-v2020.2 && repo sync &&  repo start rel-2020.2 --all

cd sources
git clone git@github.com:kratsg/meta-l1calo.git
source setupsdk && bitbake-layers add-layer ../sources/meta-l1calo 
#manually set machine in local.conf and add parallelizability 
