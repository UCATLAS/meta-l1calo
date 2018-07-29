In this wiki, I describe the general steps I used in order to create a patch for `u-boot-xlnx` to disable the SD card as needed for gFEX prototype 3. One should be able to follow/understand the procedure here and repeat it for future recipes. The magic functionality here is the `devtool` command.

# Load the recipe we need to modify

Let's start by running

```
devtool modify u-boot-xlnx
```

and the output looks like below

```
Loading cache: 100% |########################################################################################################################################################################| Time: 0:00:00
Loaded 2524 entries from dependency cache.
Parsing recipes: 100% |######################################################################################################################################################################| Time: 0:00:01
Parsing of 1748 .bb files complete (1747 cached, 1 parsed). 2525 targets, 176 skipped, 0 masked, 0 errors.
NOTE: Executing RunQueue Tasks
NOTE: Executing do_fetch...
NOTE: Executing do_unpack...
NOTE: Tasks Summary: Attempted 2 tasks of which 0 didn't need to be rerun and all succeeded.
NOTE: Patching...
NOTE: Executing RunQueue Tasks
NOTE: Executing do_patch...
NOTE: Tasks Summary: Attempted 3 tasks of which 2 didn't need to be rerun and all succeeded.
NOTE: Source tree extracted to /local/d4/gstark/poky/build/workspace/sources/u-boot-xlnx
NOTE: Recipe u-boot-xlnx now set up to build from /local/d4/gstark/poky/build/workspace/sources/u-boot-xlnx
```

Notice that the last line here indicates that it has extracted the source for `u-boot-xlnx` to `/local/d4/gstark/poky/build/workspace/sources/u-boot-xlnx`. If you go into this directory, in this case, you'll notice that it's a `git` repository!

# Modifying the source code

Make the necessary changes you need to make to the source code. E.G.

```
cd workspace/sources/u-boot-xlnx
vim include/configs/xilinx_zynqmp_zcu102.h
# make changes
```

## Testing the modifications

You can run `bitbake u-boot-xlnx` to recompile and use the `devtool deploy-target` to copy files over to the target machine. [I did not try this]

## Commit your local changes

Once you've made the changes, you will need to create a commit containing those changes.

```
git add include/configs/xilinx_zynqmp_zcu102.h
git commit -m "a helpful description of what I just did and why"
```

# Generating a bbappends and patches

The last thing is to generate the necessary `.bbappends` and `.patch` files and store them in the `meta-l1calo` layer I work with. The way to do this is to let `devtool` handle this for you by comparing differences between the local source repo and the remote source repo.

First I need to go back to my actual workspace

```
cd ../../../
```

and then let `devtool` generate the necessary files

```
devtool update-recipe -m patch -a /path/to/meta-l1calo/ u-boot-xlnx
```

and the output looks like

```
Loading cache: 100% |########################################################################################################################################################################| Time: 0:00:00
Loaded 2524 entries from dependency cache.
Parsing recipes: 100% |######################################################################################################################################################################| Time: 0:00:01
Parsing of 1748 .bb files complete (1746 cached, 2 parsed). 2525 targets, 176 skipped, 0 masked, 0 errors.
NOTE: Writing append file /local/d4/gstark/meta-l1calo/recipes-bsp/u-boot/u-boot-xlnx_2017.1.bbappend
NOTE: Writing append file /local/d4/gstark/meta-l1calo/recipes-bsp/u-boot/u-boot-xlnx_2017.1.bbappend
NOTE: Copying 0001-no-SDCard-available-for-gFEX-prototype-v3.patch to /local/d4/gstark/meta-l1calo/recipes-bsp/u-boot/u-boot-xlnx/0001-no-SDCard-available-for-gFEX-prototype-v3.patch
NOTE: Copying 0001-no-SDCard-available-for-gFEX-prototype-v3.patch to /local/d4/gstark/meta-l1calo/recipes-bsp/u-boot/u-boot-xlnx/0001-no-SDCard-available-for-gFEX-prototype-v3.patch
```

Notice that the patch files will contain the short commit message in the name to help you identify which patch is which. The patch files are part of the `SRC_URI` for bitbake/yocto while the `.bbappends` are what defines recipes for including those source files!

# Cleaning up

Now we get to clean up our local changes. First run

```
devtool reset u-boot-xlnx
```

which outputs

```
NOTE: Cleaning sysroot for recipe u-boot-xlnx...
NOTE: Leaving source tree /local/d4/gstark/poky/build/workspace/sources/u-boot-xlnx as-is; if you no longer need it then please delete it manually
```

Notice that it informs us that it leaves the workspace as it. We can (and should) delete the `u-boot-xlnx` sources:

```
rm -rf workspace/sources/u-boot-xlnx
```

# Making sure changes only applied for a specific board

The last thing we need to do is make sure that the `.bbappends` only applies the patch for the specific board `gfex-prototype3` that needs it. In this case, we go to the `meta-l1calo` layer where `devtool` made all the changes, and then edit `recipes-bsp/u-boot/u-boot-xlnx_2017.1.bbappend`:

```
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-no-SDCard-available-for-gFEX-prototype-v3.patch"
```

and change `SRC_URI` to add an append operation to a specific machine

```
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_gfex-prototype3 += "file://0001-no-SDCard-available-for-gFEX-prototype-v3.patch"
```

and this will only patch the ZynqMP ZCU102 header file for u-boot if the target machine is `gfex-prototype3`.

# Helpful References
- [1] https://wiki.yoctoproject.org/wiki/TipsAndTricks/Patching_the_source_for_a_recipe
- [2] http://www.yoctoproject.org/docs/1.5/dev-manual/dev-manual.html#structure-your-layers