SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

mkdir tmp_sdt
cp sources/meta-l1calo/scripts/sdt.tcl tmp_sdt/sdt.tcl

cd tmp_sdt
/local/code/Xilinx_2024.2/Vitis/2024.2/bin/xsct sdt.tcl ../sources/meta-l1calo/recipes-bsp/external-hdf/files/zfpga_top.xsa std_outdir

cd ../build
echo "$SCRIPT_DIR"/source/meta-xilinx/meta-xilinx-core/gen-machine-conf/gen-machineconf
export PATH=$PATH:"$SCRIPT_DIR"/source/meta-xilinx/meta-xilinx-core/gen-machine-conf/gen-machineconf
gen-machineconf parse-sdt --hw-description ../tmp_sdt/std_outdir/ -c ../sources/meta-l1calo/conf/ -l ./conf/local.conf --machine-name gfex-production-stf