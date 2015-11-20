DESCRIPTION = "Twisted is an event-driven networking framework written in Python and licensed under the LGPL. \
Twisted supports TCP, UDP, SSL/TLS, multicast, Unix sockets, a large number of protocols                   \
(including HTTP, NNTP, IMAP, SSH, IRC, FTP, and others), and much more."
HOMEPAGE = "http://www.twistedmatrix.com"
SECTION = "console/network"

#twisted/topfiles/NEWS:655: - Relicensed: Now under the MIT license, rather than LGPL.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1f3b211e44b7dae1674fb1e252fc556a"

SRC_URI = "https://pypi.python.org/packages/source/T/Twisted/Twisted-${PV}.tar.bz2"
SRC_URI[md5sum] = "5337ffb6aeeff3790981a2cd56db9655"
SRC_URI[sha256sum] = "78862662fa9ae29654bc2b9d349c3f1d887e6b2ed978512c4442d53ea861f05c"

S = "${WORKDIR}/Twisted-${PV}"

inherit setuptools

do_install_append() {
    # remove some useless files before packaging
    find ${D} \( -name "*.bat" -o -name "*.c" -o -name "*.h" \) -exec rm -f {} \;
}

# see http://twistedmatrix.com/pipermail/twisted-python/2015-April/029392.html
#       as of 15.3.0 -- no subpackages are strictly supported
PACKAGES += "\
    ${PN}-docs \
    ${PN}-scripts \
    ${PN}-test \
    ${PN}-topfiles \
    ${PN}-ui \
    ${PN}-core \
"

PACKAGES += "\
    ${PN}-bin \
    ${PN}-src \
"

# use python-twisted for the bare minimum that people want
RDEPENDS_${PN} = "\
    ${PN}-bin \
    ${PN}-docs \
    ${PN}-scripts \
    ${PN}-topfiles \
    ${PN}-ui \
"

RDEPENDS_${PN}-bin += "${PN}-core"
RDEPENDS_${PN}-dbg += "${PN}"
RDEPENDS_${PN}-docs += "${PN}-core"
RDEPENDS_${PN}-scripts += "${PN}-core"
RDEPENDS_${PN}-test += "${PN}"
RDEPENDS_${PN}-topfiles += "${PN}-core"
RDEPENDS_${PN}-ui += "${PN}-core"

RDEPENDS_${PN}-core = "python-core python-zopeinterface python-contextlib"

ALLOW_EMPTY_${PN} = "1"
FILES_${PN} = ""

FILES_${PN}-bin = " \
    ${bindir}/cftp \
    ${bindir}/ckeygen \
    ${bindir}/conch \
    ${bindir}/mailmail \
    ${bindir}/manhole \
    ${bindir}/pyhtmlizer \
    ${bindir}/tap2deb \
    ${bindir}/tap2rpm \
    ${bindir}/tkconch \
    ${bindir}/trial \
    ${bindir}/twistd \
    ${bindir}/admin/ \
"

# no actual debug files, but one can only hope
FILES_${PN}-dbg += " \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/.debug \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*/.debug \
"

FILES_${PN}-docs = " \
    ${PYTHON_SITEPACKAGES_DIR}/docs/ \
"

FILES_${PN}-scripts = " \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/scripts/ \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/scripts/ \
"

FILES_${PN}-test = " \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/test/ \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/test/ \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*/test/ \
"

FILES_${PN}-topfiles = " \
    ${PYTHON_SITEPACKAGES_DIR}/CONTRIBUTING \
    ${PYTHON_SITEPACKAGES_DIR}/INSTALL \
    ${PYTHON_SITEPACKAGES_DIR}/LICENSE \
    ${PYTHON_SITEPACKAGES_DIR}/NEWS \
    ${PYTHON_SITEPACKAGES_DIR}/README \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/topfiles \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/topfiles \
"

FILES_${PN}-ui = " \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/ui/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/ui/*.glade \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/ui/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/ui/*.glade \
"

# note, all *.c files are compiled to *.so files which we copy over
FILES_${PN}-core = " \
    ${PYTHON_SITEPACKAGES_DIR}/Twisted-${PV}-*.egg-info \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/__init__.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/_version.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/copyright.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/plugin.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/_threads/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/application/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/conch/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/conch/client/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/conch/insults/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/conch/openshh_compat/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/conch/ssh/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/cred/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/enterprise/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/internet/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/internet/iocpreactor/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/internet/iocpreactor/*.txt \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/internet/iocpreactor/iocpsupport/ \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/logger/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/mail/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/manhole/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/manhole/*.glade \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/names/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/news/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/pair/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/persisted/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/plugins/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/positioning/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/protocols/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/protocols/gps/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/protocols/mice/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/python/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/python/*.so \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/python/*.zsh \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/runner/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/runner/*.so \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/spread/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/tap/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/trial/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/trial/_dist/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/web/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/web/*.xhtml \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/web/_auth/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/im/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/im/*.glade \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/protocols/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/protocols/jabber/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/xish/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/words/xish/*.g \
"

RDEPENDS_${PN}-src += "${PN}"
FILES_${PN}-src = " \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*/*.py* \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*.c \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*.c \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*/*.c \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*.h \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*.h \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*/*.h \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*.pxi \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*.pxi \
    ${PYTHON_SITEPACKAGES_DIR}/twisted/*/*/*.pxi \
"
