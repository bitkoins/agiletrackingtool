#!/bin/bash
# Purpose: Installation and execution of the Agile Tracking Tool for demo's.
#
# The current version of the grails application can be found here:
#     http://code.google.com/p/agiletrackingtool/source/browse/trunk/application.properties
#
# The settings below can be adjusted to your preferences:
export INSTALL_DIR="$HOME/agilettdemo"
export TRACKING_TOOL_CODE_DIR="$INSTALL_DIR/agiletrackingtool-read-only"

export GRAILS_ZIP="grails-bin-1.2.1.zip"
export GRAILS_ZIP_LOCATION="http://dist.codehaus.org/grails"
export GRAILS_PLUGIN_URL="http://plugins.grails.org/grails-google-chart/tags/RELEASE_0_5_1/grails-google-chart-0.5.0.zip"

#-------------------------------------------------------------------------------
function test_if_java_environment_is_present {
	if [[ -de $JAVA_HOME ]]; then
		echo "Test for Java environment succesfull."
	else
		echo "No JAVA_HOME detected. Make sure a JAVA SDK is installed."
		exit
	fi
}

function install_grails_environment_if_needed {
	if [[ -de $GRAILS_HOME ]]; then
		echo "Test for Grails environment succesfull."
	else
		echo "Retrieving the Grails environment:"
		wget $GRAILS_ZIP_LOCATION/$GRAILS_ZIP
		unzip $GRAILS_ZIP exit
		export GRAILS_HOME="$INSTALL_DIR/grails-1.1.1"
	fi
}

function test_if_executable_is_present {
        which $1 > /dev/null
        if [ $? = 0 ]; then
                echo "Executable $1 found!"
        else
                echo "Executable $1 not found. Please install. For instance by using: sudo apt-get install $1"
                exit
        fi
}
#-------------------------------------------------------------------------------

test_if_java_environment_is_present
test_if_executable_is_present wget
test_if_executable_is_present unzip
test_if_executable_is_present svn

mkdir $INSTALL_DIR
cd $INSTALL_DIR

install_grails_environment_if_needed

echo "Retrieving the Agile Tracking Tool source code to $TRACKING_TOOL_DIR:"
svn checkout http://agiletrackingtool.googlecode.com/svn/trunk/ $TRACKING_TOOL_CODE_DIR

cd $TRACKING_TOOL_CODE_DIR

grails install-plugin $GRAILS_PLUGIN_URL
grails run-app 

