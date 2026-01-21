set outdir [lindex $argv 1]
set xsa [lindex $argv 0]
exec rm -rf $outdir
sdtgen set_dt_param -xsa $xsa -dir $outdir -include_dts ../sources/meta-l1calo/conf/machine/gfex-production.dtsi
sdtgen generate_sdt