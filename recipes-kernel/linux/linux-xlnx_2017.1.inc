FILESEXTRAPATHS_prepend := "${THISDIR}/files/${PV}:"

SRC_URI_append_gfex-prototype4 = "                                         \
    file://0001-net-macb-Add-MDIO-driver-for-accessing-multiple-PHY-.patch \
    file://Documentation-devictree-Add-macb-mdio-bindings.patch            \
"
