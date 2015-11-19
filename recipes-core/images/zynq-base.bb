inherit core-image

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_INSTALL += "python-lang python-core python-modules python-twisted python-pip"

