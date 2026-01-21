# Add tmp_sdt folder and copy the sdt.tcl script into it
mkdir tmp_sdt
cp sources/meta-l1calo/scripts/sdt.tcl tmp_sdt/sdt.tcl

# Enter the tmp_sdt folder and run the sdt.tcl script, generating the intermediate device trees
cd tmp_sdt
xsct sdt.tcl ../sources/meta-l1calo/recipes-bsp/external-hdf/files/zfpga_top.xsa std_outdir

# Enter the build folder and ask the user to choose their target machines
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

# Use gen-machineconf to generate cortexa53-linux.dts, <MACHINE>-microblaze-pmu.dts, and
# <MACHINE>-cortexa53-fsbl.dts in sources/meta-l1calo/conf/dts/<MACHINE>
../sources/meta-xilinx/meta-xilinx-core/gen-machine-conf/gen-machineconf parse-sdt --hw-description ../tmp_sdt/std_outdir/ -c ../sources/meta-l1calo/conf/ -l ./conf/local.conf --machine-name "$TARGET_MACHINE_NAME"

cd ..

# Workaround for axi clock bug, see https://github.com/UCATLAS/meta-l1calo/commit/bb59df72a56955e2e860b9d26f190abee5dfbc0a
cat tmp_sdt/std_outdir/pl.dtsi >> sources/meta-l1calo/conf/dts/"$TARGET_MACHINE_NAME"/cortexa53-linux.dts