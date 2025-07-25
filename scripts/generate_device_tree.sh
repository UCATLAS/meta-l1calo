mkdir tmp_sdt
cp sources/meta-l1calo/scripts/sdt.tcl tmp_sdt/sdt.tcl

cd tmp_sdt
/local/code/Xilinx_2024.2/Vitis/2024.2/bin/xsct sdt.tcl ../sources/meta-l1calo/recipes-bsp/external-hdf/files/zfpga_top.xsa std_outdir

cd ../build

echo ""
echo ""
echo "Choose From the following target machines:"
echo "	[] gfex-prototype1b"
echo "	[] gfex-prototype2"
echo "	[] gfex-prototype3a"
echo "	[] gfex-prototype3b"
echo "	[] gfex-prototype4"
echo "	[] gfex-production-stf"
echo "	[] gfex-production-p1"
read -p "Please enter the name of your target machine (e.g. gfex-production-stf): " TARGET_MACHINE_NAME
echo ""

../sources/meta-xilinx/meta-xilinx-core/gen-machine-conf/gen-machineconf parse-sdt --hw-description ../tmp_sdt/std_outdir/ -c ../sources/meta-l1calo/conf/ -l ./conf/local.conf --machine-name "$TARGET_MACHINE_NAME"