# meta-l1calo

## Introduction

This layer is intended to be the home of l1calo modules for OpenEmbedded.
Modules in this layer need to be independent of openembedded-core and
meta-openembedded/meta-oe.

## Dependencies

The meta-l1calo layer depends on:

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: master
	revision: HEAD

	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: master
	revision: HEAD

	URI: git://git.yoctoproject.org/meta-xilinx
	layers: meta-xilinx
	branch: master
	revision: HEAD

	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-python
	branch: master
	revision: HEAD

Please follow the recommended setup procedures of your OE distribution.

## Contributing

The meta-openembedded mailinglist
(openembedded-devel@lists.openembedded.org) is used for questions,
comments and patch review. It is subscriber only, so please register
before posting.

Send pull requests to openembedded-devel@lists.openembedded.org with
'[meta-l1calo]' in the subject.

When sending single patches, please use something like:
'git send-email -M -1 --to=openembedded-devel@lists.openembedded.org --subject-prefix=meta-l1calo][PATCH'

## Maintenance

Maintainers:
        Giordon "kratsg" Stark <gstark@cern.ch>
        Emily Smith <emily.ann.smith@cern.ch>
