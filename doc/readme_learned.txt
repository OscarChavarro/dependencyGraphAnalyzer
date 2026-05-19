Some interesting things from Ubuntu linux learned from the visual analysis of
package dependencies excercise:
  - "perl-base" is not really "essencial", but Ubuntu refuses to uninstall, and
    utilities as such aptitude insists on reinstalling once forcelly removed.

Packages of a bare minimal Ubuntu distribution:
  - Basic Unix commands / parts
    adduser, bash, gzip, tar, hostname, grep, findutils, eject, makedev, login,
    bzip2, diff, file, linux-server, e2fslibs
  - Basic Debian commands
    apt, dpkg, aptitude, dash
  - Networking
    dhcp3-client, iputils-ping
  - Other
    ncurses-base, locales, mktemp, mawk, util-linux, bsdutils
  - And the corresponding dependencies of such packages. This acccounts for
    about 111 packages.

Some packages are needed for the correct working on other packages, but are not
listed as a dependency on dpkg-query -l (but ... aptitude is able to detect
those dependencies!). Some of those are:
  - apt depends on "gpgv" (command needed to verify sources). gpgv should be
    include on LINUX_MINIMAL group. Note that this is not a missing from
    ubuntu, the problem is that apt dependeds on ubuntu-keyring which in turn
    depends on gpgv. Note that this comment is deprecated, and "ubuntu-keyring"
    and its dependencies must be part of LINUX_MINIMAL.

As a result, there is a recomended sequence for minimal install for a
"perfectionist Ubuntu":
[LINUX_MINIMAL]
  - Trim down a new system to updated & upgraded set of LINUX_MINIMAL group
[LINUX_X11_CLIENTS_AND_SSH]
  - Add the basic X11 client (X11 + Xm + Xaw apps) EOE (Execution Only Environment)
    . xterm
    . x11-apps
    . twm
    . motif-clients
  - Add the basic openssh client / server
[LINUX_EMACS_AND_GLIB_GTK]
  - Add the basic glib / gtk EOE
    . emacs

---------------------------------------------------------------------------
Interesting software:
  - advene: video annotation software
  - aegisub: dvd subtitle editor
  - transmageddon: video converter
  - photofilmstrip: convert photos to video
  - *osm* - openstreetmap related tools

---------------------------------------------------------------------------
Packages with interactive configuration and/or downloading of extra files:
postfix
hddtemp
lirc
xdm
gdm
kdm
wdm
vdr
tvtime
jackd
flashplugin-installer
mathematica-fonts
rocksndiamonds
rott
ttf-mscorefonts-installer
quake3-data

Others to check:
  - krb* ?
  - exult

---------------------------------------------------------------------------
Packages with some impact on the visual presentation:
xubuntu-gdm-theme xubuntu-artwork xubuntu-default-settings
edubuntu-artwork gnome-icon-theme-gartoon human-theme
plymouth-theme-xubuntu-logo xubuntu-plymouth-theme

---------------------------------------------------------------------------
Linux UBUNTU server 9.04 (jaunty) base installation have some packages
not considered "basic" for X11 instalation with development (steps 00 to 07)
on "_x11_path". Following packages can be removed (groups indicate that
packages must be removed in groups):

dmidecode
laptop-detect

iproute
ubuntu-minimal

klogd
sysklogd

libcap2
ntpdate

liblockfile1
lockfile-progs

libsqlite3-0
lsb-release
python
python2.6

tasksel
tasksel-data

vim-common
vim-tiny

less
libatm1
mime-support
netcat
netcat-traditional
sudo

python2.6-minimal
python-minimal

===========================================================================
# Following packages depends on prohibited java packages for building
# (apt-get build-dep):
brltty-x11
openoffice.org-calc
openoffice.org-gnome
openoffice.org-impress
openoffice.org-l10n-en-gb
openoffice.org-l10n-en-za
openoffice.org-style-crystal
openoffice.org-style-galaxy
openoffice.org-style-hicontrast
openoffice.org-style-industrial
openoffice.org-style-oxygen
openoffice.org-style-tango
openoffice.org-writer
kdesdk
poxml
python-brlapi
brltty
libprotobuf-dev
cdbs
gobjc
gettext
python-protobuf
cdrdao
docbook-defguide
ure
protobuf-compiler
ttf-opensymbol
libdb4.7
gettext-base
gettext-doc
libprotobuf6
libprotobuf-lite6
liblink-grammar4
# Following packages conflicts with software versions for building
# (apt-get build-dep):
# uses tcl8.3, wants to downgrade tcl8.4:
graphviz-dev
graphviz
libsqlite3-dev
python2.6-dbg
sqlite3 
libsqlite3-0 
libgvpr1
libxdot4
libgraph4
libpathplan4
# wants to downgrade libreadline6-dev to libreadline5.dev:
liblualib50-dev
liblua50-dev
liblualib50
# wants to downgrade libelfg0-dev to libelf-dev, conflict with lua?:
xulrunner-1.9.2-gnome-support
libcegui-mk2-dev
libcegui-mk2-1
ltrace
virtuoso-nepomuk
virtuoso-opensource-6.1-bin
virtuoso-opensource-6.1-common
librpm1
# wants to downgrade automake1.4 / autoconf2.13:
flite
sensors-applet
dia-gnome
libflite1
xpdf
erlang-xmerl
erlang-inets
libtool
system-config-audit
tao-ftrtevent
tao-idl
libelf1 
apt
radeontool
apt-transport-https
curl
auditd
# wants to downgrade libboost:
libslp1
libldap2-dev
# wants to replace libcurl4-gnutls-dev by libcurl4-openssl-dev, or
# libcurl4-gnutls-dev by libcurl4-openssl-dev:
firefox-gnome-support
couchdb-bin
libxmlrpc-core-c3
libofa0
cvs
# wants to downgrade libreadline-dev:
libgda3-common
librplay3
# wants to remove libreadline-dev and pvm-dev:
lua50
ntp
ntpdate
# want to remove itself... :
freebsd-manpages
# want to remove jackd jackd2 jackd2-firewire libjack-jackd2-0,
# and/or change g++ platform
audacity-data
wine-gecko
libgnustep-gui0.16-dbg
vlc-plugin-notify
gstreamer0.10-plugins-bad
vlc
snes9x-gtk
guvcview
wireshark-common
xmms2
mencoder
lirc
mplayer
mpg123-nas
timidity
vlc-plugin-pulse
pulseaudio-module-bluetooth
pulseaudio-module-gconf
libxmmsclient-glib1
gstreamer0.10-sdl
libpulse-mainloop-glib0
pulseaudio-utils
snes9x-x
mpg123-esd
mpg123-alsa
espeak-data
moc
vlc-data
libxine1-ffmpeg
liblircclient0
ttf-symbol-replacement
# want to remove all flavours of itself:
libgdchart-gd2-xpm
libgdchart-gd2-noxpm
dvipng
openvas-client
gnuplot-doc
# wants to remove imagemagick
libpsiconv6
# Conflicts with octave3.2 and texinfo
check
# Conflicts with libiodbc2-dev
libqt4-assistant
libqt4-gui
libqt4-sql-sqlite
libqt4-core
qt4-qtconfig
libqt4-webkit
libpt2.6.7
libgdal1-1.6.0
libpoco-dev
libpocomysql9
libpoconetssl9
libpocosqlite9
libpocozip9
libopenthreads13
libpocoodbc9
# Conflicts with python-setuptools
python-numeric
python-twisted
# Conflicts with several python components
python-lxml
# Depends on bad Nvidia drivers
update-manager-core
# Depends on web browser?
libapparmor-perl
subversion
apparmor-utils
# In conflict with bonobo and several other componentes
libgnutls-dev
# In conflict with fontforge
ttf-tuffy
# In conflict with flex/flex-old
xaw3dg
# In conflict with liblockdev10-dev
libgphoto2-2
# In conflict with motif/lesstif
ddd
# In conflict with openjade
gtk-doc-tools
# Conflicting with haskell
gnustep-base-examples
libgnustep-base1.19-dbg
# Conflicting with some kde/qt
kprof
# Conflicting with dblatex
gimp-help-es
gimp-help-fr
gimp-help-it
# Conflicting with mpi
ekiga

===========================================================================
Recommended games! :)

0ad (like Age Of Mythology)
armagetronad
blockout2
chromium-bsu
cuyo (tetris like balls)
dangen (vector game)
freegish (balls)
fretsonfire
gweled
glmark2
glmemperf
gltron
kali (simetry editor)
enemylines3
enemylines7
frozen-bubble
kiki-the-nano-bot
lincity-ng
mokomaze
neverball
neverputt
numptyphysics
supertuxkart (supermariokart clone)
tdfsb (File system browser in 3D)
torcs (need for speed clone)
tumiki-fighters (very playable plane game with simple elements)
billard-gl
openttd
micropolis
openarena
opencity
pingus (lemmings clone)
raincat (cute kitten puzzle, lemmings like)
simutrans
stormbaancoureur
viruskiller
sopwith (classic 80's plane game!)
tictactoe
xjig (nice game by its own simplicity)
xpenguins (drawing on gnome root window)
warzone2100 (war simulator)
zaz (luxor clone)

===========================================================================
Plan:
  - Install desktop ubuntu, server ubuntu, edubuntu, kubuntu and xubuntu
    and merge all of its base packages
  - Include all ttf and xfonts packages
  - Include all games. Use "goplay" tool to list uninstalled games
  - Include all OpenGL related packages
===========================================================================
