package co.cubestudio;

public class Markers {
    private SoftwarePackageGraph graph;

    public Markers(SoftwarePackageGraph graph) {
	this.graph = graph;
    }
    
    void markProhibited()
    {
        graph.anotateNodesV3("bsh-gcj");
        graph.anotateNodesV3("cacao");
        graph.anotateNodesV3("ca-certificates-java");
        graph.anotateNodesV3("default-jre");
        graph.anotateNodesV3("default-jre-headless");
        graph.anotateNodesV3("gcj-4.4-base");
        graph.anotateNodesV3("gcj-4.4-jre");
        graph.anotateNodesV3("gcj-4.4-jre-headless");
        graph.anotateNodesV3("gcj-4.4-jre-lib");
        graph.anotateNodesV3("gcj-jre");
        graph.anotateNodesV3("gcj-jre-headless");
        graph.anotateNodesV3("gij-4.3");
        graph.anotateNodesV3("icedtea-6-jre-cacao");
        graph.anotateNodesV3("jamvm");
        graph.anotateNodesV3("java-common");
        graph.anotateNodesV3("libaccess-bridge-java");
        graph.anotateNodesV3("libaccess-bridge-java-jni");
        graph.anotateNodesV3("libgcj10");
        graph.anotateNodesV3("libgcj10-awt");
        graph.anotateNodesV3("libgcj-bc");
        graph.anotateNodesV3("libgcj-common");
        graph.anotateNodesV3("libhsqldb-java");
        graph.anotateNodesV3("libhsqldb-java-gcj");
        graph.anotateNodesV3("libjline-java");
        graph.anotateNodesV3("libservlet2.5-java");
        graph.anotateNodesV3("libswfdec-0.8-0");
        graph.anotateNodesV3("openjdk-6-jre");
        graph.anotateNodesV3("openjdk-6-jre-headless");
        graph.anotateNodesV3("openjdk-6-jre-lib");
        graph.anotateNodesV3("openoffice.org-gcj");
        graph.anotateNodesV3("openoffice.org-java-common");
        graph.anotateNodesV3("tzdata-java");
        graph.anotateNodesV3("tzdata-java");
        graph.anotateNodesV3("gnome-js-common");
        graph.anotateNodesV3("swfdec-gnome");
        graph.anotateNodesV3("swfdec-mozilla");
        graph.anotateNodesV3("lightsoff");
        graph.anotateNodesV3("gir1.0-gconf-2.0");
        graph.anotateNodesV3("gir1.0-atk-1.0");
        graph.anotateNodesV3("gir1.0-clutter-1.0");
        graph.anotateNodesV3("libgirepository1.0-0");
        graph.anotateNodesV3("gir1.0-glib-2.0");
        graph.anotateNodesV3("gir1.0-freedesktop");
        graph.anotateNodesV3("gir1.0-gstreamer-0.10");
        graph.anotateNodesV3("gir1.0-pango-1.0");
        graph.anotateNodesV3("gir1.0-gtk-2.0");
        graph.anotateNodesV3("libseed0");
        graph.anotateNodesV3("seed");
        graph.anotateNodesV3("gir1.0-clutter-gtk-0.10");
        graph.anotateNodesV3("swell-foop");
        graph.anotateNodesV3("nvidia-173-modaliases");
        graph.anotateNodesV3("nvidia-185-modaliases");
        graph.anotateNodesV3("nvidia-96-modaliases");
        graph.anotateNodesV3("nvidia-common");
        graph.anotateNodesV3("nvidia-current-modaliases");
    }

    void
    markBasic()
    {
        // Other commonly used development tools
        //graph.anotateNodesV2("xxdiff");
        graph.anotateNodesV2("xosview");
        graph.anotateNodesV2("gdb");
        graph.anotateNodesV2("valgrind");
        graph.anotateNodesV2("graphviz");

        // Vitral, JOGL
        graph.anotateNodesV2("libxrandr-dev");
        graph.anotateNodesV2("libxinerama-dev");
        graph.anotateNodesV2("git-core");
        graph.anotateNodesV2("libxxf86vm-dev");
        graph.anotateNodesV2("subversion");
        graph.anotateNodesV2("cvs");
        graph.anotateNodesV2("libc6-dev-i386");

        // Basic X11 server workstation capability: xdm + twm
        graph.anotateNodesV2("xinit");
        graph.anotateNodesV2("xdm");
        graph.anotateNodesV2("twm");
        graph.anotateNodesV2("motif-clients");
        graph.anotateNodesV2("xview-clients");
        graph.anotateNodesV2("olwm");
        graph.anotateNodesV2("olvwm");
        graph.anotateNodesV2("xfonts-100dpi");
        graph.anotateNodesV2("xfonts-75dpi");

        // AQUYNZA development support
        graph.anotateNodesV2("freeglut3-dev");
        graph.anotateNodesV2("xviewg-dev");
        graph.anotateNodesV2("xview-examples");
        graph.anotateNodesV2("libxaw7-dev");
        graph.anotateNodesV2("libmotif-dev");
        graph.anotateNodesV2("libglu1-mesa-dev");
        graph.anotateNodesV2("mesa-utils");
        graph.anotateNodesV2("libtiff4-dev");
        graph.anotateNodesV2("libpng12-dev");
        graph.anotateNodesV2("libgif-dev");
        graph.anotateNodesV2("libfreeimage-dev");
        graph.anotateNodesV2("libjpeg62-dev");
        graph.anotateNodesV2("libnetpbm10-dev");
        graph.anotateNodesV2("libreadline-dev");
        graph.anotateNodesV2("libncursesw5-dev");
        graph.anotateNodesV2("x11proto-print-dev");

        // Development tools
        graph.anotateNodesV2("g++");
        graph.anotateNodesV2("gcc");
        graph.anotateNodesV2("binutils");
        graph.anotateNodesV2("htop");
        graph.anotateNodesV2("ltrace");
        graph.anotateNodesV2("patch");
        graph.anotateNodesV2("strace");
        graph.anotateNodesV2("lsof");
        graph.anotateNodesV2("time");
        graph.anotateNodesV2("info");
        graph.anotateNodesV2("man-db");
        graph.anotateNodesV2("automake");
        graph.anotateNodesV2("autogen");
        graph.anotateNodesV2("flex");
        graph.anotateNodesV2("bison");
        graph.anotateNodesV2("manpages-posix");

        // System start & kernel
        graph.anotateNodesV2("grub2");
        graph.anotateNodesV2("upstart");
        graph.anotateNodesV2("initscripts");
        graph.anotateNodesV2("linux-server");
        graph.anotateNodesV2("linux-image-2.6.32-21-server");
        graph.anotateNodesV2("linux-image-2.6.35-22-server");
        graph.anotateNodesV2("linux-image-2.6.35-19-generic");
        graph.anotateNodesV2("linux-generic");
        graph.anotateNodesV2("linux-firmware");

        // Minimal openssh-based remote graphics
        graph.anotateNodesV2("xemacs21");
        graph.anotateNodesV2("xterm");
        graph.anotateNodesV2("x11-apps");
        graph.anotateNodesV2("xloadimage");
        graph.anotateNodesV2("openssh-server");
        graph.anotateNodesV2("dhcp3-client");
        graph.anotateNodesV2("xosview");

        // Text session
        graph.anotateNodesV2("login");
        graph.anotateNodesV2("dash");
        graph.anotateNodesV2("bash");
        graph.anotateNodesV2("vim-tiny");
        graph.anotateNodesV2("bzip2");
        graph.anotateNodesV2("less");
        graph.anotateNodesV2("grep");
        graph.anotateNodesV2("gzip");
        graph.anotateNodesV2("bzip2");
        graph.anotateNodesV2("tar");
        graph.anotateNodesV2("diffutils");
        graph.anotateNodesV2("openssh-client");
        graph.anotateNodesV2("language-pack-en-base");

        // System management
        graph.anotateNodesV2("apt");
        graph.anotateNodesV2("sudo");
        graph.anotateNodesV2("pciutils");
        graph.anotateNodesV2("usbutils");
        graph.anotateNodesV2("wget");
        //graph.anotateNodesV2("aptitude");

        // Marked as "essencial" by ubuntu, but not really really essencial
        graph.anotateNodesV2("ncurses-base");
        graph.anotateNodesV2("python");
        graph.anotateNodesV2("bsdutils");

        // Other
        graph.anotateNodesV2("mawk");
        graph.anotateNodesV2("iputils-ping");
        graph.anotateNodesV2("hostname");
        graph.anotateNodesV2("locales");
        graph.anotateNodesV2("libgd2-xpm");
        graph.anotateNodesV2("dialog");
    }

    void markMultimedia()
    {
        // Canberra
        graph.anotateNodesV3("libcanberra-gtk0:amd64");
        graph.anotateNodesV3("libcanberra-gtk3-0:amd64");
        graph.anotateNodesV3("libcanberra-gtk3-module:amd64");
        graph.anotateNodesV3("libcanberra-gtk-module:amd64");
        graph.anotateNodesV3("libcanberra-doc");
        graph.anotateNodesV3("libcanberra-pulse");

        // Gstreamer
        graph.anotateNodesV3("gstreamer0.10-pulseaudio:amd64");
        graph.anotateNodesV3("gstreamer0.10-x:amd64");
        graph.anotateNodesV3("gstreamer1.0-x:amd64");
        graph.anotateNodesV3("gstreamer0.10-x:i386");
        graph.anotateNodesV3("gstreamer0.10-x");
        graph.anotateNodesV3("gstreamer0.10-plugins-good:amd64");
        graph.anotateNodesV3("gstreamer0.10-plugins-base:amd64");
        graph.anotateNodesV3("gstreamer0.10-nice:amd64");
        graph.anotateNodesV3("gstreamer0.10-alsa");
        graph.anotateNodesV3("gstreamer0.10-tools");
        graph.anotateNodesV3("gstreamer1.0-plugins-base:amd64");
        graph.anotateNodesV3("gstreamer1.0-plugins-good:amd64");
        graph.anotateNodesV3("libgstreamer-plugins-bad0.10-0:amd64");
        graph.anotateNodesV3("libgstreamer-plugins-bad1.0-0:amd64");
        graph.anotateNodesV3("libgstreamer-plugins-base0.10-0:amd64");
        graph.anotateNodesV3("libgstreamer-plugins-base0.10-0:i386");
        graph.anotateNodesV3("libgstreamer-plugins-base1.0-0:amd64");
        graph.anotateNodesV3("libgstreamer0.10-0:amd64");

        // ALSA
        graph.anotateNodesV3("alsa-base");
        graph.anotateNodesV3("alsa-utils");
        graph.anotateNodesV3("libasound2:amd64");
        graph.anotateNodesV3("libasound2-plugins:amd64");
        graph.anotateNodesV3("libasound2");
        graph.anotateNodesV3("libasound2-doc");

        // Ffmpeg
        graph.anotateNodesV3("libavformat-extra-53:amd64");
        graph.anotateNodesV3("libavformat53:amd64");
        graph.anotateNodesV3("libavformat52");
        graph.anotateNodesV3("libavcodec52");
        graph.anotateNodesV3("libavcodec53:amd64");
        graph.anotateNodesV3("libavcodec-extra-53:amd64");
        graph.anotateNodesV3("libavutil49");
        graph.anotateNodesV3("libavutil-extra-49");
        graph.anotateNodesV3("libavutil-extra-50");
        graph.anotateNodesV3("libavutil-extra-51:amd64");
        graph.anotateNodesV3("libavutil-extra-51");
        graph.anotateNodesV3("libavutil-extra-52");
        graph.anotateNodesV3("libswscale0");

        // Video for linux
        graph.anotateNodesV3("libv4l-0");
        graph.anotateNodesV3("libv4lconvert0");
        
        // Codecs
        graph.anotateNodesV3("libogg0"); // OGG/Vorvis
        graph.anotateNodesV3("libspeex1"); // OGG/Vorvis
        graph.anotateNodesV3("libspeexdsp1"); // OGG/Vorvis
        graph.anotateNodesV3("libaom3"); // AV1
        graph.anotateNodesV3("libdav1d5"); // AV1	
        graph.anotateNodesV3("libvpx7"); // VP8 / VP9 codec
        graph.anotateNodesV3("libx264-163"); // X264
        graph.anotateNodesV3("libx265-199"); // H.265/HEVC

        // Other
        graph.anotateNodesV3("pocketsphinx-hmm-wsj1");
        graph.anotateNodesV3("libvpx-doc");
        graph.anotateNodesV3("libvpx1");
        graph.anotateNodesV3("flvmeta");
        graph.anotateNodesV3("libdvbcsa1");
        graph.anotateNodesV3("libopencore-amrwb0");
        graph.anotateNodesV3("libopencore-amrnb0");
        graph.anotateNodesV3("libmad0");
        graph.anotateNodesV3("libsonic0");
        graph.anotateNodesV3("dvsink");
        graph.anotateNodesV3("vdr-plugin-infosatepg");
        graph.anotateNodesV3("libmatroska5");
        graph.anotateNodesV3("dvdrip-utils");
        graph.anotateNodesV3("flvstreamer");
        graph.anotateNodesV3("dvblast");
        graph.anotateNodesV3("vdr-plugin-svdrposd");
        graph.anotateNodesV3("libev4");
        graph.anotateNodesV3("libsidplay2");
        graph.anotateNodesV3("libexempi3");
        graph.anotateNodesV3("libmtp-common");
        graph.anotateNodesV3("libtracker-sparql-0.14-0");
        graph.anotateNodesV3("libdirac-encoder0");
        graph.anotateNodesV3("libdirac-decoder0");
        graph.anotateNodesV3("libdvbpsi5");
        graph.anotateNodesV3("libdvbpsi6");
        graph.anotateNodesV3("libdvbpsi7");
        graph.anotateNodesV3("libopus0");
        graph.anotateNodesV3("libdca0");
        graph.anotateNodesV3("libaacs0");
        graph.anotateNodesV3("liblircclient0");
        graph.anotateNodesV3("libcrystalhd3");
        graph.anotateNodesV3("libbluray0");
        graph.anotateNodesV3("libbluray-doc");
        graph.anotateNodesV3("libquicktime-doc");
        graph.anotateNodesV3("libsamplerate0");
        graph.anotateNodesV3("libtag1-vanilla");
        graph.anotateNodesV3("libx264-123");
        graph.anotateNodesV3("flac");
        graph.anotateNodesV3("lame");
        graph.anotateNodesV3("libsoundtouch0:amd64");
        graph.anotateNodesV3("ubuntu-sounds");
        graph.anotateNodesV3("sound-icons");
        graph.anotateNodesV3("streamripper");
        graph.anotateNodesV3("libavc1394-0:i386");
        graph.anotateNodesV3("libdc1394-22:amd64");
        graph.anotateNodesV3("libzvbi-common");
        graph.anotateNodesV3("libaudiofile0");
        graph.anotateNodesV3("libaudiofile1");
        graph.anotateNodesV3("libaudiofile1:i386");
        graph.anotateNodesV3("libaudiofile1:amd64");
        graph.anotateNodesV3("sound-theme-freedesktop");
        graph.anotateNodesV3("freepats");
        graph.anotateNodesV3("libcdio13");
        graph.anotateNodesV3("xmms2-core");
        graph.anotateNodesV3("xmms2-icon");
        graph.anotateNodesV3("libav-doc");
        graph.anotateNodesV3("libxmmsclient6");
        graph.anotateNodesV3("esound-common");
        graph.anotateNodesV3("vobcopy");
        graph.anotateNodesV3("vorbis-tools");
        graph.anotateNodesV3("twolame");
        graph.anotateNodesV3("cdrdao");
        graph.anotateNodesV3("libffmpegthumbnailer4");
        graph.anotateNodesV3("libvo-amrwbenc0");
        graph.anotateNodesV3("libvo-aacenc0");
        graph.anotateNodesV3("linux-sound-base");
        graph.anotateNodesV3("libass4");
        graph.anotateNodesV3("libcdaudio1");
        graph.anotateNodesV3("libcelt0-0");
        graph.anotateNodesV3("libfaac0");
        graph.anotateNodesV3("libflite1");
        graph.anotateNodesV3("libgme0");
        graph.anotateNodesV3("libiptcdata0");
        graph.anotateNodesV3("libkate1");
        graph.anotateNodesV3("libmimic0");
        graph.anotateNodesV3("libesd0");
        graph.anotateNodesV3("libopenal1");
        graph.anotateNodesV3("libmpcdec3");
        graph.anotateNodesV3("libjack0");
        graph.anotateNodesV3("aumix-common");
        graph.anotateNodesV3("libgsm1");
        graph.anotateNodesV3("liboil0.3");
        graph.anotateNodesV3("libdvdnav4");
        graph.anotateNodesV3("libmp3lame0");
        graph.anotateNodesV3("liborc-0.4-0");
        graph.anotateNodesV3("libx264-85");
        graph.anotateNodesV3("libx264-106");
        graph.anotateNodesV3("libx264-116");
        graph.anotateNodesV3("libxvidcore4");
        graph.anotateNodesV3("libaudio2");
        graph.anotateNodesV3("libmpg123-0");
        graph.anotateNodesV3("libxine1-bin");
        graph.anotateNodesV3("liba52-0.7.4");
        graph.anotateNodesV3("faad");
        graph.anotateNodesV3("libmp4v2-0");
        graph.anotateNodesV3("mpg321");
        graph.anotateNodesV3("icedax");
        graph.anotateNodesV3("id3tool");
        graph.anotateNodesV3("mpegdemux");
        graph.anotateNodesV3("id3v2");
        graph.anotateNodesV3("libid3-3.8.3c2a");
        graph.anotateNodesV3("libx264-67");
        graph.anotateNodesV3("aumix");
        graph.anotateNodesV3("libportaudio2");
        graph.anotateNodesV3("libtwolame0");
        graph.anotateNodesV3("libscale0");
        graph.anotateNodesV3("libpostproc51");
        graph.anotateNodesV3("libsmpeg0");
        graph.anotateNodesV3("libcddb2");
        graph.anotateNodesV3("libebml0");
        graph.anotateNodesV3("vlc-data");
        graph.anotateNodesV3("libiso9660-7");
        graph.anotateNodesV3("libmikmod2");
        graph.anotateNodesV3("oss-compat");
        graph.anotateNodesV3("libva1");
        graph.anotateNodesV3("libvpx0");
        graph.anotateNodesV3("libx264-98");
        graph.anotateNodesV3("libjack-jackd2-0");
        graph.anotateNodesV3("libmpcdec6");
        graph.anotateNodesV3("libvdpau1");
        graph.anotateNodesV3("libdv4");
        graph.anotateNodesV3("libdc1394-22");
        graph.anotateNodesV3("libraw1394-11");
        graph.anotateNodesV3("libfaad0");
        graph.anotateNodesV3("libfaad2");
        graph.anotateNodesV3("libao-common");
        graph.anotateNodesV3("libwavpack1");
        graph.anotateNodesV3("libmpeg2-4");
        graph.anotateNodesV3("libsidplay1");
        graph.anotateNodesV3("libmatroska2");
        graph.anotateNodesV3("libsdl1.2debian");
        graph.anotateNodesV3("libmodplug1");
        graph.anotateNodesV3("libpostproc-dev");
        graph.anotateNodesV3("libavutil-dev");
        graph.anotateNodesV3("ffmpeg-doc");
        graph.anotateNodesV3("libsphinx2-dev");
        graph.anotateNodesV3("libmusicbrainz3-dev");
        graph.anotateNodesV3("libmusicbrainz4-dev");
        graph.anotateNodesV3("libmpeg4ip-dev");
        graph.anotateNodesV3("cmus-plugin-ffmpeg");
        graph.anotateNodesV3("librplay3");
        graph.anotateNodesV3("libportaudio0");
        graph.anotateNodesV3("libffado2");
        graph.anotateNodesV3("libiec61883-0");
        graph.anotateNodesV3("libavc1394-0");
        graph.anotateNodesV3("libzita-convolver2");
        graph.anotateNodesV3("vorbistools");
        graph.anotateNodesV3("libvamp-sdk2");
        graph.anotateNodesV3("libsdl-mixer1.2");
        graph.anotateNodesV3("timidity");
        graph.anotateNodesV3("mplayer-skins");
        graph.anotateNodesV3("timidity-daemon");
        graph.anotateNodesV3("libvisual-0.4-0");
        graph.anotateNodesV3("libvisual-0.4-plugins");
        graph.anotateNodesV3("libvisual-0.4-plugins:amd64");
        graph.anotateNodesV3("gpe-soundserver");
        graph.anotateNodesV3("pulseaudio-utils");
        graph.anotateNodesV3("libdmapsharing-3.0-2");
        graph.anotateNodesV3("libbdplus0"); // Blueray
        graph.anotateNodesV3("libudfread0"); // CDROM/ DVD Universal Disk Format (fs)
        graph.anotateNodesV3("libcodec2-1.0");
        graph.anotateNodesV3("libva2"); // Video Acceleration for linux
        graph.anotateNodesV3("libsoxr0"); // Sound eXchange 1d signal library
        graph.anotateNodesV2("libdvdread8");
        graph.anotateNodesV2("libv4l2rds0");
        graph.anotateNodesV2("libopenal-data");
        graph.anotateNodesV2("libfftw3-single3");
        graph.anotateNodesV2("libfftw3-double3");
        graph.anotateNodesV2("libde265-0");
        graph.anotateNodesV2("libasound2-data");
    }

    void markJava()
    {
        graph.anotateNodesV3("java-common");
        graph.anotateNodesV3("ca-certificates-java");
        graph.anotateNodesV3("tzdata-java");
        graph.anotateNodesV3("openjdk-7-jre-lib");
        graph.anotateNodesV3("icedtea-7-jre-lib");
        graph.anotateNodesV3("icedtea-7-jre-jamvm");
        graph.anotateNodesV3("icedtea-7-jre-cacao");
        graph.anotateNodesV3("libatk-wrapper-java");
    }

    void markX11()
    {
        graph.anotateNodesV3("x11-common");   // X11
        graph.anotateNodesV3("libxau6");
        graph.anotateNodesV3("libxdmcp6");
        graph.anotateNodesV3("libx11-6");
        graph.anotateNodesV3("libx11-doc");
        graph.anotateNodesV3("libx11-data");
        graph.anotateNodesV3("libglitz1");
        graph.anotateNodesV3("libgl1-mesa-dri");
        graph.anotateNodesV3("xbitmaps");
        //graph.anotateNodesV3("xkb-data"); // Should be part of X? or just inherited console...
        graph.anotateNodesV3("xorg-docs-core");
        graph.anotateNodesV3("libx11-xcb1");
        graph.anotateNodesV3("xcursor-themes");
        graph.anotateNodesV3("xorg-sgml-doctools");

        graph.anotateNodesV3("latex-xft-fonts");
        graph.anotateNodesV3("libxfont1");
        graph.anotateNodesV3("libxfont2");
        graph.anotateNodesV3("xfonts-mona");
        graph.anotateNodesV3("x11proto-randr-dev");
        graph.anotateNodesV3("x11proto-record-dev");
        graph.anotateNodesV3("x11proto-video-dev");
        graph.anotateNodesV3("x11proto-xinerama-dev"); 
        graph.anotateNodesV3("x11proto-xf86misc-dev");
        graph.anotateNodesV3("x11proto-core-dev");
        graph.anotateNodesV3("libdrm-dev");

        graph.anotateNodesV3("libglapi-mesa");
        graph.anotateNodesV3("libglvnd0");
        graph.anotateNodesV3("libdrm-common");
        graph.anotateNodesV3("libxshmfence1");
    }

    void markOpenGL() {
        graph.anotateNodesV3("libgl1-mesa-dri"); // OpenGL
        graph.anotateNodesV3("libgl1-mesa-glx");
	graph.anotateNodesV3("libdrm-common");
	graph.anotateNodesV3("libxcb-glx0");
	graph.anotateNodesV3("libglapi-mesa");
	graph.anotateNodesV3("libglvnd0");
	graph.anotateNodesV3("libvdpau1"); // Digital video layer on GPU (codecs)
    }
    
    void markDevel()
    {
        // Basic devel
        graph.anotateNodesV3("binutils");
        graph.anotateNodesV3("binutils-common");
        graph.anotateNodesV3("strace");
        graph.anotateNodesV3("ltrace");
        graph.anotateNodesV3("libopts25-dev");
        graph.anotateNodesV3("libibverbs-dev");
        graph.anotateNodesV3("libzzip-dev");
        graph.anotateNodesV3("libpthread-stubs0-dev");
        graph.anotateNodesV3("libc-dev-bin");
        graph.anotateNodesV3("libcppunit-dev");
        graph.anotateNodesV3("libcppunit-1.12-1");
        graph.anotateNodesV3("libc6-dbg");
        graph.anotateNodesV3("libc6-pic");
        graph.anotateNodesV3("libc6-prof");
        graph.anotateNodesV3("libstdc++6-4.4-doc");
        graph.anotateNodesV3("libgc-dev");
        graph.anotateNodesV3("libgcc-6-dev");
        graph.anotateNodesV3("libgcc-7-dev");
        graph.anotateNodesV3("libgcc-8-dev");
        graph.anotateNodesV3("lib32gcc-7-dev");
        graph.anotateNodesV3("libx32gcc-7-dev");
        graph.anotateNodesV3("libgcc1-dbg");
        graph.anotateNodesV3("autotools-dev");
        graph.anotateNodesV3("manpages");
        graph.anotateNodesV3("linux-libc-dev");
        graph.anotateNodesV3("m4");
        graph.anotateNodesV3("cvs");
        graph.anotateNodesV3("libopagent1");
        graph.anotateNodesV3("glibc-doc-reference");
        graph.anotateNodesV3("glibc-source");
        graph.anotateNodesV3("libbison-dev");
        graph.anotateNodesV3("make");
        graph.anotateNodesV3("gdb");
        graph.anotateNodesV3("git");
        graph.anotateNodesV3("git-man");
        graph.anotateNodesV3("patch");
        graph.anotateNodesV3("gdbserver");

        // mingw
        graph.anotateNodesV3("mingw-w64-dev");
        graph.anotateNodesV3("mingw-w64-common");
        graph.anotateNodesV3("mingw-w64-x86-64-dev");
        graph.anotateNodesV3("gcc-mingw-w64-base");

        // Compress
        graph.anotateNodesV3("libdeflate-dev");
        graph.anotateNodesV3("liblzma-dev");
        graph.anotateNodesV3("libzstd-dev");
        graph.anotateNodesV3("libminizip-dev");

        // Crypt
        graph.anotateNodesV3("libgpg-error-mingw-w64-dev");
        graph.anotateNodesV3("libgpg-error-dev");
        graph.anotateNodesV3("libcrack2-dev");
        graph.anotateNodesV3("libssl-dev");
        graph.anotateNodesV3("libcrypt-dev");

        // Multimedia
        graph.anotateNodesV3("libv4l-dev");
        graph.anotateNodesV3("libwebp-dev");
        graph.anotateNodesV3("libavutil-dev");
        graph.anotateNodesV3("libraw1394-dev");
        graph.anotateNodesV3("libaudiofile-dev:amd64");
        graph.anotateNodesV3("libjpeg-turbo8-dev");
        graph.anotateNodesV3("liblcms2-dev");
        graph.anotateNodesV3("libgif-dev");
        graph.anotateNodesV3("libjpeg62-dev");
        graph.anotateNodesV3("libjbig-dev");
        graph.anotateNodesV3("libpixman-1-dev");
        graph.anotateNodesV3("libchromaprint-dev");
        graph.anotateNodesV3("libasound2-dev");
        graph.anotateNodesV3("libopus-dev");
        graph.anotateNodesV3("libsndio-dev");
        graph.anotateNodesV3("libsoxr-dev");
        graph.anotateNodesV3("libspeex-dev");
        graph.anotateNodesV3("libx264-dev");
        graph.anotateNodesV3("libx265-dev");
        graph.anotateNodesV3("libmpg123-dev");
        graph.anotateNodesV3("libogg-dev");
        graph.anotateNodesV3("libavc1394-dev");
        graph.anotateNodesV3("libblosc-dev");	
        graph.anotateNodesV3("libcfitsio-dev");
        graph.anotateNodesV3("libaom-dev");
        graph.anotateNodesV3("libdav1d-dev");
        graph.anotateNodesV3("libde265-dev");
        graph.anotateNodesV3("unixodbc-dev");
        graph.anotateNodesV3("libtirpc-dev");
        graph.anotateNodesV3("liborc-0.4-dev-bin");

        // GDAL
        graph.anotateNodesV3("librttopo-dev");

        // Graphics and fonts
        graph.anotateNodesV3("libsqlite3-dev");
        graph.anotateNodesV3("libbsd-dev");
        graph.anotateNodesV3("libtinfo-dev");
        graph.anotateNodesV3("libart-2.0-dev");
        graph.anotateNodesV3("libfreeimage-dev");
        graph.anotateNodesV3("libgl2ps-dev");
        graph.anotateNodesV3("libnetpbm10-dev");
        graph.anotateNodesV3("libilmbase-dev");
        graph.anotateNodesV3("liblcms1-dev");
        graph.anotateNodesV3("libgraphite2-dev");

        // Languages
        graph.anotateNodesV3("libgcc-13-dev");
        graph.anotateNodesV3("libgcc-14-dev");
        graph.anotateNodesV3("yasm");
        graph.anotateNodesV3("nasm");
        graph.anotateNodesV3("libx32gcc-13-dev");
        graph.anotateNodesV3("lib32gcc-13-dev");
        graph.anotateNodesV3("cernlib-base-dev");
        graph.anotateNodesV3("tcl8.5-dev");
        graph.anotateNodesV3("libffi-dev");
        graph.anotateNodesV3("libgmp-dev");
        graph.anotateNodesV3("libfindlib-ocaml-dev");
        graph.anotateNodesV3("libboost1.46-dev");
        graph.anotateNodesV3("python3-all");
        graph.anotateNodesV3("python-all");
        graph.anotateNodesV3("golang-1.10-src");
        graph.anotateNodesV3("gem2deb-test-runner");
        graph.anotateNodesV3("openjdk-11-jdk");
        graph.anotateNodesV3("openjdk-11-jdk-headless");
        graph.anotateNodesV3("libomp-18-dev");

        // X11
        graph.anotateNodesV3("x11proto-core-dev");
        graph.anotateNodesV3("x11proto-print-dev");
        graph.anotateNodesV3("x11proto-record-dev");
        graph.anotateNodesV3("x11proto-video-dev");
        graph.anotateNodesV3("libjasper-dev");
        graph.anotateNodesV3("libdrm-dev");
        graph.anotateNodesV3("xtrans-dev");
        graph.anotateNodesV3("x11proto-kb-dev");
        graph.anotateNodesV3("cmake-data");
        graph.anotateNodesV3("x11proto-xinerama-dev");
        graph.anotateNodesV3("x11proto-dri2-dev");
        graph.anotateNodesV3("x11proto-gl-dev");
        graph.anotateNodesV3("x11proto-dmx-dev");
        graph.anotateNodesV3("x11proto-xf86vidmode-dev");
        graph.anotateNodesV3("x11proto-randr-dev");
        graph.anotateNodesV3("libdjvulibre-dev");
        graph.anotateNodesV3("libfftw3-dev");
        graph.anotateNodesV3("xutils-dev");
        graph.anotateNodesV3("libdbus-1-dev");
        graph.anotateNodesV3("x11proto-xf86misc-dev");
        graph.anotateNodesV3("x11proto-dev");
        graph.anotateNodesV3("libxkbcommon-dev");
        graph.anotateNodesV3("libxcb-composite0-dev");

        // X11 related
        graph.anotateNodesV3("libopengl-dev");
        graph.anotateNodesV3("libvulkan-dev");
        graph.anotateNodesV3("pvm-dev");
        graph.anotateNodesV3("libois-dev");
        graph.anotateNodesV3("libavahi-common-dev");
        graph.anotateNodesV3("libm17n-dev");
        graph.anotateNodesV3("libdevmapper-dev");
        graph.anotateNodesV3("libspectre-dev");
        graph.anotateNodesV3("libgii1-dev");
        graph.anotateNodesV3("libopenthreads-dev");
        graph.anotateNodesV3("libwayland-dev");
        graph.anotateNodesV3("libxshmfence-dev");
        graph.anotateNodesV3("libglvnd-core-dev");

        // Linux kernel
        graph.anotateNodesV3("linux-headers-5.4.74-custom");
        graph.anotateNodesV3("linux-headers-6.8.0-62");
	
	// Other
        graph.anotateNodesV3("libmkl-computational-dev");
        graph.anotateNodesV3("libmkl-interface-dev");
        graph.anotateNodesV3("libmkl-threading-dev");
        graph.anotateNodesV3("libevent-dev");
        graph.anotateNodesV3("libnl-3-dev");
        graph.anotateNodesV3("libsource-highlight-common");
        graph.anotateNodesV3("libdebuginfod1t64");
        graph.anotateNodesV3("libdebuginfod-common");
        graph.anotateNodesV3("libpcre2-dev");
        graph.anotateNodesV3("libsepol-dev");
        graph.anotateNodesV3("libglm-dev");
        graph.anotateNodesV3("libgbm-dev");
        graph.anotateNodesV3("libboost1.49-dev");
        graph.anotateNodesV3("libexttextcat-dev");
        graph.anotateNodesV3("libhunspell-dev:amd64");
        graph.anotateNodesV3("libhyphen-dev");
        graph.anotateNodesV3("libmdds-dev");
        graph.anotateNodesV3("libmysqlcppconn-dev");
        graph.anotateNodesV3("libpoppler-cpp-dev");
        graph.anotateNodesV3("libpoppler-private-dev");
        graph.anotateNodesV3("libwpd-dev");
        graph.anotateNodesV3("libqt4-dev-bin");
        graph.anotateNodesV3("libboost-dev");
        graph.anotateNodesV3("libboost1.65-dev");
        graph.anotateNodesV3("libisl-dev");
        graph.anotateNodesV3("libsepol1-dev");
        graph.anotateNodesV3("libjson-c-dev");
        graph.anotateNodesV3("libaec-dev");
        graph.anotateNodesV3("libaudit-dev");
        graph.anotateNodesV3("libudev-dev");
        graph.anotateNodesV3("libdb5.3-dev");
        graph.anotateNodesV3("libidb2-dev");
        graph.anotateNodesV3("libsystemd-dev");
        graph.anotateNodesV3("libcap-dev");
        graph.anotateNodesV3("libcap-ng-dev");
        graph.anotateNodesV3("libleptonica-dev");
        graph.anotateNodesV3("libevdev-dev");
        graph.anotateNodesV3("libmtdev-dev");
        graph.anotateNodesV3("libopenjp2-7-dev");
        graph.anotateNodesV3("libtasn1-6-dev");
        graph.anotateNodesV3("libidn2-0-dev");
        graph.anotateNodesV3("libidn2-dev");
        graph.anotateNodesV3("nettle-dev");
        graph.anotateNodesV3("libp11-kit-dev");
        graph.anotateNodesV3("man2html-base");
        graph.anotateNodesV3("libassuan-dev");
        graph.anotateNodesV3("libassuan-mingw-w64-dev");
        graph.anotateNodesV3("libcrystalhd-dev");
        graph.anotateNodesV3("libcunit1-dev");
        graph.anotateNodesV3("libfribidi-dev");
        graph.anotateNodesV3("libksba-dev");
        graph.anotateNodesV3("libksba-mingw-w64-dev");
        graph.anotateNodesV3("libnpth-mingw-w64-dev");
        graph.anotateNodesV3("libldap2-dev");
        graph.anotateNodesV3("libnpth0-dev");
        graph.anotateNodesV3("libnpth0-mingw-w64-dev");
        graph.anotateNodesV3("libomxil-bellagio-dev");
        graph.anotateNodesV3("libusb-1.0-0-dev");
        graph.anotateNodesV3("libz-mingw-w64-dev");
        graph.anotateNodesV3("libwrap0-dev");
        graph.anotateNodesV3("debootstrap");
        graph.anotateNodesV3("doxygen");
        graph.anotateNodesV3("libfakeroot");
        graph.anotateNodesV3("gperf");
        graph.anotateNodesV3("libblas-dev");
        graph.anotateNodesV3("libepoxy-dev");
        graph.anotateNodesV3("libltdl-dev");
        graph.anotateNodesV3("libqhull-dev");
        graph.anotateNodesV3("libpq-dev");
        graph.anotateNodesV3("libproj-dev");
        graph.anotateNodesV3("libcurl4-gnutls-dev");
        graph.anotateNodesV3("libgflags-dev");
        graph.anotateNodesV3("libgeos-dev");
        graph.anotateNodesV3("libepsilon-dev");
        graph.anotateNodesV3("libatlas-base-dev");
        graph.anotateNodesV3("libpoppler-dev");
        graph.anotateNodesV3("liburiparser-dev");
        graph.anotateNodesV3("libfreexl-dev");
        graph.anotateNodesV3("libfyba-dev");
        graph.anotateNodesV3("systemtap-sdt-dev");
        graph.anotateNodesV3("libsvn1");
        graph.anotateNodesV3("libpciaccess-dev");
        graph.anotateNodesV3("liblmdb-dev");
        graph.anotateNodesV3("");
    }

    void markKDE()
    {
        graph.anotateNodesV3("libkdecore5"); // KDE

        graph.anotateNodesV3("k3b-data");
        graph.anotateNodesV3("kalzium-data");
        graph.anotateNodesV3("kate-data");
        graph.anotateNodesV3("katepart");
        graph.anotateNodesV3("kde-icons-crystal");
        graph.anotateNodesV3("kde-icons-kneu");
        graph.anotateNodesV3("kde-icons-mono");
        graph.anotateNodesV3("kde-icons-noia");
        graph.anotateNodesV3("kde-icons-nuvola");
        graph.anotateNodesV3("kde-runtime");
        graph.anotateNodesV3("kde-runtime-data");
        graph.anotateNodesV3("kde-wallpapers-default");
        graph.anotateNodesV3("kde-workspace-kgreet-plugins");
        graph.anotateNodesV3("kdeartwork-emoticons");
        graph.anotateNodesV3("kdeartwork-theme-icon");
        graph.anotateNodesV3("kdebase-data");
        graph.anotateNodesV3("kdebase-runtime");
        graph.anotateNodesV3("kdebase-runtime-data");
        graph.anotateNodesV3("kdebase-workspace-data");
        graph.anotateNodesV3("kdebase-workspace-wallpapers");
        graph.anotateNodesV3("kdeedu-kvtml-data");
        graph.anotateNodesV3("kdegames-card-data");
        graph.anotateNodesV3("kdegames-mahjongg-data");
        graph.anotateNodesV3("kdegraphics-libs-data");
        graph.anotateNodesV3("kdegraphics-strigi-analyzer");
        graph.anotateNodesV3("kdelibs-bin");
        graph.anotateNodesV3("kdelibs5-data");
        graph.anotateNodesV3("kdelibs5-plugins");
        graph.anotateNodesV3("kdewallpapers");
        graph.anotateNodesV3("kdm");
        graph.anotateNodesV3("kdoctools");
        graph.anotateNodesV3("kgamma");
        graph.anotateNodesV3("kgeography-data");
        graph.anotateNodesV3("klettres-data");
        graph.anotateNodesV3("kstars-data");
        graph.anotateNodesV3("ksysguardd");
        graph.anotateNodesV3("kubuntu-debug-installer");
        graph.anotateNodesV3("libkatepartinterfaces4");
        graph.anotateNodesV3("libkcmutils4");
        graph.anotateNodesV3("libkdcraw-data");
        graph.anotateNodesV3("libkde3support4");
        graph.anotateNodesV3("libkdeedu-data");
        graph.anotateNodesV3("libkdesu5");
        graph.anotateNodesV3("libkdesu5q");
        graph.anotateNodesV3("libkdeui5");
        graph.anotateNodesV3("libkdewebkit5");
        graph.anotateNodesV3("libkdnssd4");
        graph.anotateNodesV3("libkemoticons4");
        graph.anotateNodesV3("libkexiv2-data");
        graph.anotateNodesV3("libkfile4");
        graph.anotateNodesV3("libkhtml5");
        graph.anotateNodesV3("libkidletime4");
        graph.anotateNodesV3("libkio5");
        graph.anotateNodesV3("libkjsapi4");
        graph.anotateNodesV3("libkjsembed4");
        graph.anotateNodesV3("libkmediaplayer4");
        graph.anotateNodesV3("libknewstuff3-4");
        graph.anotateNodesV3("libknotifyconfig4");
        graph.anotateNodesV3("libkntlm4");
        graph.anotateNodesV3("libkonq5-templates");
        graph.anotateNodesV3("libkparts4");
        graph.anotateNodesV3("libkpty4");
        graph.anotateNodesV3("libkrosscore4");
        graph.anotateNodesV3("libktexteditor4");
        graph.anotateNodesV3("libkworkspace4");
        graph.anotateNodesV3("libnepomuk4");
        graph.anotateNodesV3("libnepomukquery4a");
        graph.anotateNodesV3("libnepomukutils4");
        graph.anotateNodesV3("libplasma3");
        graph.anotateNodesV3("marble-data");
        graph.anotateNodesV3("parley-data");
        graph.anotateNodesV3("pkg-kde-tools");
        graph.anotateNodesV3("plasma-scriptengine-javascript");
        graph.anotateNodesV3("polkit-kde-1");
        graph.anotateNodesV3("qapt-batch");

        graph.anotateNodesV3("tagua-data");
        graph.anotateNodesV3("plasma-widget-kimpanel-backend-ibus");
        graph.anotateNodesV3("kubuntu-docs");
        graph.anotateNodesV3("kubuntu-netbook-default-settings");
        graph.anotateNodesV3("kubuntu-web-shortcuts");
        graph.anotateNodesV3("kubuntu-default-settings");
        graph.anotateNodesV3("kde-baseapps-data");
        graph.anotateNodesV3("kdeartwork-dbg");
        graph.anotateNodesV3("kdepim-doc");
        graph.anotateNodesV3("kdepim-runtime-dbg");
        graph.anotateNodesV3("python-kde4-doc");
        graph.anotateNodesV3("willowng-config-kde");
        graph.anotateNodesV3("xsettings-kde");
        graph.anotateNodesV3("quassel-data");

        graph.anotateNodesV3("language-pack-kde-aa");
        graph.anotateNodesV3("language-pack-kde-aa-base");
        graph.anotateNodesV3("language-pack-kde-af-base");
        graph.anotateNodesV3("language-pack-kde-am");
        graph.anotateNodesV3("language-pack-kde-am-base");
        graph.anotateNodesV3("language-pack-kde-ar-base");
        graph.anotateNodesV3("language-pack-kde-as");
        graph.anotateNodesV3("language-pack-kde-as-base");
        graph.anotateNodesV3("language-pack-kde-ast");
        graph.anotateNodesV3("language-pack-kde-ast-base");
        graph.anotateNodesV3("language-pack-kde-az-base");
        graph.anotateNodesV3("language-pack-kde-be-base");
        graph.anotateNodesV3("language-pack-kde-bg-base");
        graph.anotateNodesV3("language-pack-kde-bn-base");
        graph.anotateNodesV3("language-pack-kde-bo");
        graph.anotateNodesV3("language-pack-kde-bo-base");
        graph.anotateNodesV3("language-pack-kde-br-base");
        graph.anotateNodesV3("language-pack-kde-bs-base");
        graph.anotateNodesV3("language-pack-kde-ca-base");
        graph.anotateNodesV3("language-pack-kde-crh");
        graph.anotateNodesV3("language-pack-kde-crh-base");
        graph.anotateNodesV3("language-pack-kde-cs-base");
        graph.anotateNodesV3("language-pack-kde-csb-base");
        graph.anotateNodesV3("language-pack-kde-cy-base");
        graph.anotateNodesV3("language-pack-kde-da-base");
        graph.anotateNodesV3("language-pack-kde-de-base");
        graph.anotateNodesV3("language-pack-kde-dv");
        graph.anotateNodesV3("language-pack-kde-dv-base");
        graph.anotateNodesV3("language-pack-kde-el-base");
        graph.anotateNodesV3("language-pack-kde-en-base");
        graph.anotateNodesV3("language-pack-kde-eo-base");
        graph.anotateNodesV3("language-pack-kde-es-base");
        graph.anotateNodesV3("language-pack-kde-et-base");
        graph.anotateNodesV3("language-pack-kde-eu-base");
        graph.anotateNodesV3("language-pack-kde-fa-base");
        graph.anotateNodesV3("language-pack-kde-fi-base");
        graph.anotateNodesV3("language-pack-kde-fil");
        graph.anotateNodesV3("language-pack-kde-fil-base");
        graph.anotateNodesV3("language-pack-kde-fo");
        graph.anotateNodesV3("language-pack-kde-fo-base");
        graph.anotateNodesV3("language-pack-kde-fr-base");
        graph.anotateNodesV3("language-pack-kde-fur");
        graph.anotateNodesV3("language-pack-kde-fur-base");
        graph.anotateNodesV3("language-pack-kde-fy-base");
        graph.anotateNodesV3("language-pack-kde-ga-base");
        graph.anotateNodesV3("language-pack-kde-gd");
        graph.anotateNodesV3("language-pack-kde-gd-base");
        graph.anotateNodesV3("language-pack-kde-gl-base");
        graph.anotateNodesV3("language-pack-kde-gu-base");
        graph.anotateNodesV3("language-pack-kde-gv");
        graph.anotateNodesV3("language-pack-kde-gv-base");
        graph.anotateNodesV3("language-pack-kde-ha");
        graph.anotateNodesV3("language-pack-kde-ha-base");
        graph.anotateNodesV3("language-pack-kde-he-base");
        graph.anotateNodesV3("language-pack-kde-hi-base");
        graph.anotateNodesV3("language-pack-kde-hne");
        graph.anotateNodesV3("language-pack-kde-hne-base");
        graph.anotateNodesV3("language-pack-kde-hr-base");
        graph.anotateNodesV3("language-pack-kde-hsb");
        graph.anotateNodesV3("language-pack-kde-hsb-base");
        graph.anotateNodesV3("language-pack-kde-hu-base");
        graph.anotateNodesV3("language-pack-kde-hy");
        graph.anotateNodesV3("language-pack-kde-hy-base");
        graph.anotateNodesV3("language-pack-kde-ia");
        graph.anotateNodesV3("language-pack-kde-ia-base");
        graph.anotateNodesV3("language-pack-kde-id-base");
        graph.anotateNodesV3("language-pack-kde-is-base");
        graph.anotateNodesV3("language-pack-kde-it-base");
        graph.anotateNodesV3("language-pack-kde-ja-base");
        graph.anotateNodesV3("language-pack-kde-ka");
        graph.anotateNodesV3("language-pack-kde-ka-base");
        graph.anotateNodesV3("language-pack-kde-kk-base");
        graph.anotateNodesV3("language-pack-kde-kl");
        graph.anotateNodesV3("language-pack-kde-kl-base");
        graph.anotateNodesV3("language-pack-kde-km-base");
        graph.anotateNodesV3("language-pack-kde-kn-base");
        graph.anotateNodesV3("language-pack-kde-ko-base");
        graph.anotateNodesV3("language-pack-kde-ku");
        graph.anotateNodesV3("language-pack-kde-ku-base");
        graph.anotateNodesV3("language-pack-kde-kw");
        graph.anotateNodesV3("language-pack-kde-kw-base");
        graph.anotateNodesV3("language-pack-kde-la");
        graph.anotateNodesV3("language-pack-kde-la-base");
        graph.anotateNodesV3("language-pack-kde-lb");
        graph.anotateNodesV3("language-pack-kde-lb-base");
        graph.anotateNodesV3("language-pack-kde-lo");
        graph.anotateNodesV3("language-pack-kde-lo-base");
        graph.anotateNodesV3("language-pack-kde-lt-base");
        graph.anotateNodesV3("language-pack-kde-lv-base");
        graph.anotateNodesV3("language-pack-kde-mai-base");
        graph.anotateNodesV3("language-pack-kde-mg");
        graph.anotateNodesV3("language-pack-kde-mg-base");
        graph.anotateNodesV3("language-pack-kde-mk-base");
        graph.anotateNodesV3("language-pack-kde-ml-base");
        graph.anotateNodesV3("language-pack-kde-mn-base");
        graph.anotateNodesV3("language-pack-kde-mr");
        graph.anotateNodesV3("language-pack-kde-mr-base");
        graph.anotateNodesV3("language-pack-kde-ms-base");
        graph.anotateNodesV3("language-pack-kde-mt");
        graph.anotateNodesV3("language-pack-kde-mt-base");
        graph.anotateNodesV3("language-pack-kde-nb-base");
        graph.anotateNodesV3("language-pack-kde-nds-base");
        graph.anotateNodesV3("language-pack-kde-ne");
        graph.anotateNodesV3("language-pack-kde-ne-base");
        graph.anotateNodesV3("language-pack-kde-nl-base");
        graph.anotateNodesV3("language-pack-kde-nn-base");
        graph.anotateNodesV3("language-pack-kde-oc");
        graph.anotateNodesV3("language-pack-kde-oc-base");
        graph.anotateNodesV3("language-pack-kde-om");
        graph.anotateNodesV3("language-pack-kde-om-base");
        graph.anotateNodesV3("language-pack-kde-or");
        graph.anotateNodesV3("language-pack-kde-or-base");
        graph.anotateNodesV3("language-pack-kde-pa-base");
        graph.anotateNodesV3("language-pack-kde-pl-base");
        graph.anotateNodesV3("language-pack-kde-ps");
        graph.anotateNodesV3("language-pack-kde-ps-base");
        graph.anotateNodesV3("language-pack-kde-pt-base");
        graph.anotateNodesV3("language-pack-kde-ro-base");
        graph.anotateNodesV3("language-pack-kde-ru-base");
        graph.anotateNodesV3("language-pack-kde-rw-base");
        graph.anotateNodesV3("language-pack-kde-sd");
        graph.anotateNodesV3("language-pack-kde-sd-base");
        graph.anotateNodesV3("language-pack-kde-se-base");
        graph.anotateNodesV3("language-pack-kde-si-base");
        graph.anotateNodesV3("language-pack-kde-sk-base");
        graph.anotateNodesV3("language-pack-kde-sl-base");
        graph.anotateNodesV3("language-pack-kde-sq");
        graph.anotateNodesV3("language-pack-kde-sq-base");
        graph.anotateNodesV3("language-pack-kde-sr-base");
        graph.anotateNodesV3("language-pack-kde-ss-base");
        graph.anotateNodesV3("language-pack-kde-st");
        graph.anotateNodesV3("language-pack-kde-st-base");
        graph.anotateNodesV3("language-pack-kde-sv-base");
        graph.anotateNodesV3("language-pack-kde-sw");
        graph.anotateNodesV3("language-pack-kde-sw-base");
        graph.anotateNodesV3("language-pack-kde-ta-base");
        graph.anotateNodesV3("language-pack-kde-te-base");
        graph.anotateNodesV3("language-pack-kde-tg-base");
        graph.anotateNodesV3("language-pack-kde-th-base");
        graph.anotateNodesV3("language-pack-kde-tl");
        graph.anotateNodesV3("language-pack-kde-tl-base");
        graph.anotateNodesV3("language-pack-kde-tlh");
        graph.anotateNodesV3("language-pack-kde-tlh-base");
        graph.anotateNodesV3("language-pack-kde-tr-base");
        graph.anotateNodesV3("language-pack-kde-tt");
        graph.anotateNodesV3("language-pack-kde-tt-base");
        graph.anotateNodesV3("language-pack-kde-ug");
        graph.anotateNodesV3("language-pack-kde-ug-base");
        graph.anotateNodesV3("language-pack-kde-uk-base");
        graph.anotateNodesV3("language-pack-kde-ur");
        graph.anotateNodesV3("language-pack-kde-ur-base");
        graph.anotateNodesV3("language-pack-kde-uz-base");
        graph.anotateNodesV3("language-pack-kde-vi");
        graph.anotateNodesV3("language-pack-kde-vi-base");
        graph.anotateNodesV3("language-pack-kde-wa-base");
        graph.anotateNodesV3("language-pack-kde-xh");
        graph.anotateNodesV3("language-pack-kde-xh-base");
        graph.anotateNodesV3("language-pack-kde-zh-base");
        graph.anotateNodesV3("language-pack-kde-zh-hans-base");
        graph.anotateNodesV3("language-pack-kde-zh-hant-base");
    }

    void markGnome()
    {
        // Gnome core
        graph.anotateNodesV3("libgnome-keyring0");
        graph.anotateNodesV3("policykit-1-gnome");
        graph.anotateNodesV3("libgnome-autoar-0-0");
        graph.anotateNodesV3("libunity-protocol-private0");
        graph.anotateNodesV3("gnome-keyring");
        graph.anotateNodesV3("libindicator3-7");
        graph.anotateNodesV3("libunity-scopes-json-def-desktop");
        graph.anotateNodesV3("pinentry-gnome3");
        graph.anotateNodesV3("gcr");
        graph.anotateNodesV3("libgnome-games-support-common");
        graph.anotateNodesV3("libgnome-panel0");
        graph.anotateNodesV3("gnome-desktop3-data");
        graph.anotateNodesV3("libgnome-desktop-2-17");
        graph.anotateNodesV3("libgnome-desktop-3-2");
        graph.anotateNodesV3("libgnome-desktop-3-19");
        //graph.anotateNodesV3("gnome-icon-theme"); // This is GTK

        // Gnome applications (games)
	graph.anotateNodesV3("gnome-mahjongg");
	graph.anotateNodesV3("gnome-mines");
	graph.anotateNodesV3("gnome-sudoku");

        // Gnome applications (basic)
        graph.anotateNodesV3("gnome-terminal-data");
        graph.anotateNodesV3("gedit-common");
        graph.anotateNodesV3("gnome-system-log");
        graph.anotateNodesV3("gnome-system-monitor");
	graph.anotateNodesV3("transmission-common"); // Torrent downloader
        graph.anotateNodesV3("usb-creator-gtk");
	
        // Gnome applications (multimedia)
        graph.anotateNodesV3("brasero-common");
        graph.anotateNodesV3("celestia-common");
        graph.anotateNodesV3("cheese-common");
        graph.anotateNodesV3("evince-common");
        graph.anotateNodesV3("gnome-disk-utility");
        graph.anotateNodesV3("libnautilus-extension1a");
        graph.anotateNodesV3("libtotem-plparser-common");
        graph.anotateNodesV3("nautilus-data");
        graph.anotateNodesV3("pitivi");
        graph.anotateNodesV3("remmina-common");
        graph.anotateNodesV3("rhythmbox-data");
        graph.anotateNodesV3("totem-common");

        // Gnome applications
	graph.anotateNodesV3("alacarte");
        graph.anotateNodesV3("atomix-data");
        graph.anotateNodesV3("etoys-doc");
        graph.anotateNodesV3("evolution-indicator");
        graph.anotateNodesV3("gnome-accessibility-themes");
        graph.anotateNodesV3("gnome-backgrounds");
        graph.anotateNodesV3("gnome-desktop-data");
        graph.anotateNodesV3("gnome-desktop3-data");
        graph.anotateNodesV3("gnome-doc-utils");
        graph.anotateNodesV3("gnome-games-extra-data");
        graph.anotateNodesV3("gnome-menus");
        graph.anotateNodesV3("gnome-mime-data");
        graph.anotateNodesV3("gnome-nettool");
        graph.anotateNodesV3("gnome-orca");
        graph.anotateNodesV3("gnome-panel-control");
        graph.anotateNodesV3("gnome-power-manager");
        graph.anotateNodesV3("gnome-session-bin");
        graph.anotateNodesV3("gnome-session-canberra");
        graph.anotateNodesV3("gnome-session-common");
        graph.anotateNodesV3("gnome-settings-daemon-schemas");
        graph.anotateNodesV3("gnome-shell-common");
        graph.anotateNodesV3("gnome-themes-more");
        graph.anotateNodesV3("gnome-themes-selected");
        graph.anotateNodesV3("gnome-user-guide");
        graph.anotateNodesV3("gnome-utils-common");
        graph.anotateNodesV3("gnome-video-effects");
        graph.anotateNodesV3("ibus-pinyin-db-android");
        graph.anotateNodesV3("ibus-pinyin-db-open-phrase");
        graph.anotateNodesV3("libatspi1.0-0");
        graph.anotateNodesV3("libbonoboui2-common");
        graph.anotateNodesV3("libgnome-bluetooth11");
        graph.anotateNodesV3("libgnome-control-center1");
        graph.anotateNodesV3("libgnome-keyring-common");
        graph.anotateNodesV3("libgnome-mag2");
        graph.anotateNodesV3("libgnome-menu-3-0");
        graph.anotateNodesV3("libgnome-menu2");
        graph.anotateNodesV3("libgnome-speech7");
        graph.anotateNodesV3("libgnomecanvas2-common");
        graph.anotateNodesV3("libgnomecups1.0-1");
        graph.anotateNodesV3("libgnomekbd-common");
        graph.anotateNodesV3("libgnomeprint2.2-data");
        graph.anotateNodesV3("libgnomeui-common");
        graph.anotateNodesV3("libgsf-gnome-1-114");
        graph.anotateNodesV3("libgtksourceview-common");
        graph.anotateNodesV3("libgucharmap-2-90-7");
        graph.anotateNodesV3("libmimedir-gnome0.4");
        graph.anotateNodesV3("libnautilus-extension1a");
        graph.anotateNodesV3("libopenrawgnome1");
        graph.anotateNodesV3("libpam-gnome-keyring");
        graph.anotateNodesV3("libyelp0");
        graph.anotateNodesV3("sawfish-data");
        graph.anotateNodesV3("ssh-askpass-gnome");
        graph.anotateNodesV3("yelp-xsl");
        graph.anotateNodesV3("libgoa-backend-1.0-1");
        graph.anotateNodesV3("gnome-control-center-data");
        graph.anotateNodesV3("language-selector-gnome");
        graph.anotateNodesV3("gnome-settings-daemon-common");
        graph.anotateNodesV3("gnome-panel-data");	
        graph.anotateNodesV3("gnome-applets-data");	
    }

    void markPerl()
    {
        //graph.anotateNodesV3("perl-base");
        graph.anotateNodesV3("perl");
        graph.anotateNodesV3("perl-modules-5.26");
        graph.anotateNodesV3("perl-modules-5.24");
        graph.anotateNodesV3("libperl5.26");
        graph.anotateNodesV3("libperl5.24");
        graph.anotateNodesV3("libperl5.14");
        graph.anotateNodesV3("libperl5.12");
        graph.anotateNodesV3("libperl5.10");
        graph.anotateNodesV3("libperl5.8");
        graph.anotateNodesV3("libcompress-zlib-perl");
        graph.anotateNodesV3("libio-compress-base-perl");
        graph.anotateNodesV3("libio-compress-zlib-perl");
        graph.anotateNodesV3("libuuid-perl");
        graph.anotateNodesV3("libapt-pkg-perl");
        graph.anotateNodesV3("libconfig-inifiles-perl");
        graph.anotateNodesV3("libyaml-tiny-perl");
        graph.anotateNodesV3("libclass-isa-perl");
        graph.anotateNodesV3("liburi-perl");
        graph.anotateNodesV3("libio-socket-ip-perl");
        graph.anotateNodesV3("libunicode-collate-perl");
        graph.anotateNodesV3("perl-openssl-defaults");
        graph.anotateNodesV3("libdpkg-perl");
        graph.anotateNodesV3("libdynaloader-functions-perl");
        graph.anotateNodesV3("liberror-perl");
        graph.anotateNodesV3("libencode-locale-perl");
        graph.anotateNodesV3("libarchive-zip-perl");
        graph.anotateNodesV3("libtext-iconv-perl");
        graph.anotateNodesV3("libintl-perl");
        graph.anotateNodesV3("libmodule-scandeps-perl");
        graph.anotateNodesV3("perl-modules-5.34");
        graph.anotateNodesV3("libyaml-pp-perl");
        graph.anotateNodesV3("libipc-system-simple-perl");
    }

    void markPythonDev() {
        graph.anotateNodesV3("python2.6-dev");
        graph.anotateNodesV3("libpython2.7-dev");
        graph.anotateNodesV3("python3.1-dev");
        graph.anotateNodesV3("libpython3.12-dev");
    }

    void markPython()
    {
        // Python 2
        graph.anotateNodesV3("libpython2.6");
        graph.anotateNodesV3("dh-python");
        graph.anotateNodesV3("python2.6");
        graph.anotateNodesV3("python2.7");
        graph.anotateNodesV3("libpython-stdlib");
        graph.anotateNodesV3("libpython2.7-minimal");
        graph.anotateNodesV3("python-minimal");
        graph.anotateNodesV3("python-simpy-doc");
        graph.anotateNodesV3("python-pip");
        graph.anotateNodesV3("python-pkg-resources");
        graph.anotateNodesV3("python-wheel");
        graph.anotateNodesV3("python-dnspython");
        graph.anotateNodesV3("python-six");
        graph.anotateNodesV3("python-enum34");
        graph.anotateNodesV3("python-idna");
        graph.anotateNodesV3("python-asn1crypto");
        graph.anotateNodesV3("python-ipaddress");

        // Python 3
        graph.anotateNodesV3("python3.2-minimal");
        graph.anotateNodesV3("libpython3.4-minimal");
        graph.anotateNodesV3("libpython3.5-minimal");
        graph.anotateNodesV3("libpython3.12-minimal");
        graph.anotateNodesV3("libpython3.6-minimal");
        graph.anotateNodesV3("libpython3.10-minimal");
        graph.anotateNodesV3("python3-colorama");
        graph.anotateNodesV3("python3-more-itertools");
        graph.anotateNodesV3("python3-six");
        graph.anotateNodesV3("python3-pkg-resources");
        graph.anotateNodesV3("python3-idna");
        graph.anotateNodesV3("python3-certifi");
        graph.anotateNodesV3("python-talloc");
        graph.anotateNodesV3("python-dnspython");
        graph.anotateNodesV3("python-sphinx-rtd-theme");
        graph.anotateNodesV3("python-apt-common");
        graph.anotateNodesV3("python-pip-whl");
        graph.anotateNodesV3("python3-xkit");
        graph.anotateNodesV3("python3-roman");
        graph.anotateNodesV3("python3-alabaster");
        graph.anotateNodesV3("python-babel-localedata");
        graph.anotateNodesV3("python3-tz");
        graph.anotateNodesV3("python3-imagesize");
        graph.anotateNodesV3("python3-pygments");
        graph.anotateNodesV3("python3-lib2to3");
        graph.anotateNodesV3("python3-attr");
        graph.anotateNodesV3("python3-debconf");
        graph.anotateNodesV3("python3-serial");
        graph.anotateNodesV3("python3-jwt");
        graph.anotateNodesV3("python3-blinker");
        graph.anotateNodesV3("python3-distro");
        graph.anotateNodesV3("python3-pyparsing");
        graph.anotateNodesV3("python3-problem-report");
        graph.anotateNodesV3("python3-distro-info");
        graph.anotateNodesV3("python3-jeepney");
        graph.anotateNodesV3("python3-json-pointer");
        graph.anotateNodesV3("python3-gast");
        graph.anotateNodesV3("python3-decorator");
        graph.anotateNodesV3("python3-ply");
        graph.anotateNodesV3("python3-cycler");
        graph.anotateNodesV3("python3-mpmath");
        graph.anotateNodesV3("python3-xdg");
        graph.anotateNodesV3("python3-dnspython");
        graph.anotateNodesV3("python3-soupsieve");
        graph.anotateNodesV3("python3-appdirs");
        graph.anotateNodesV3("python3-defer");
        graph.anotateNodesV3("python3-docopt");
        graph.anotateNodesV3("python-matplotlib-data");
        graph.anotateNodesV3("python3-snowballstemmer");
        graph.anotateNodesV3("python3-sigmavirus24-urltemplate");
        graph.anotateNodesV3("python3-sgmllib3k");
        graph.anotateNodesV3("python3-packaging");
        graph.anotateNodesV3("python3-urllib3");
        graph.anotateNodesV3("python3-wheel");
        graph.anotateNodesV3("python3-tabulate");
        graph.anotateNodesV3("python3-wcwidth");

	markPythonDev();
    }

    void markQT()
    {
        graph.anotateNodesV3("libqtcore4");
        graph.anotateNodesV3("qt3-doc");
        graph.anotateNodesV3("libqt3-mt");
        graph.anotateNodesV3("libqtcore4:i386");
        graph.anotateNodesV3("libntrack-qt4-1");
        graph.anotateNodesV3("libpolkit-qt-1-1");
        graph.anotateNodesV3("libqca2");
        graph.anotateNodesV3("libsoprano4");
        graph.anotateNodesV3("soprano-daemon");
        graph.anotateNodesV3("qtcreator-doc");
        graph.anotateNodesV3("python-qt4-dev");
        graph.anotateNodesV3("qt4-doc");
        graph.anotateNodesV3("qt4-qmake");
        graph.anotateNodesV3("phonon");
        graph.anotateNodesV3("libqt5core5a");
        graph.anotateNodesV3("qtchooser");
        graph.anotateNodesV3("qtcore4-l10n");
        graph.anotateNodesV3("qt5-qmake-bin");
    }


    void markGstreamer() {
        graph.anotateNodesV3("libgstreamer1.0-0");
    }

    void markGtk()
    {
        graph.anotateNodesV3("libgtk2.0-0");
        graph.anotateNodesV3("libgtk2.0-common");
        graph.anotateNodesV3("libgtk-3-0");
        graph.anotateNodesV3("libgtk-3-doc");
        graph.anotateNodesV3("libgtk-3-common");
        graph.anotateNodesV3("gnome-icon-theme");
        graph.anotateNodesV3("humanity-icon-theme");
        graph.anotateNodesV3("hicolor-icon-theme");
        graph.anotateNodesV3("adawita-icon-theme-full");
        graph.anotateNodesV3("gtk-update-icon-cache");
        graph.anotateNodesV3("libdbusmenu-gtk4");
        graph.anotateNodesV3("libdbusmenu-gtk3-4");
        graph.anotateNodesV3("libgtksourceview-3.0-common");
        graph.anotateNodesV3("ibus-gtk");
        graph.anotateNodesV3("libgweather-common");
        graph.anotateNodesV3("libvte-2.91-common");
        graph.anotateNodesV3("libwmf0.2-7-gtk");

        /*
        graph.anotateNodesV3("libindicate-gtk3");
        graph.anotateNodesV3("xdg-user-dirs-gtk");
        graph.anotateNodesV3("libjavascriptcoregtk-1.0-0");
        graph.anotateNodesV3("gtk2-engines-oxygen");
        graph.anotateNodesV3("libwebkitgtk-3.0-common");
        graph.anotateNodesV3("gir1.2-javascriptcoregtk-4.0");
	*/
    }

    void markGnustep()
    {
        graph.anotateNodesV3("gnustep-common"); // GnuStep
        graph.anotateNodesV3("renaissance-doc");
        graph.anotateNodesV3("gnustep-make-doc");
        graph.anotateNodesV3("gnustep-make-doc");
        graph.anotateNodesV3("gnustep-gui-doc");
        graph.anotateNodesV3("gnustep-icons");
        graph.anotateNodesV3("wmaker");
    }

    void markMultiplatform(String n)
    {
        graph.anotateSingleNode(n);
        graph.anotateSingleNode(n + ":amd64");
        graph.anotateSingleNode(n + ":i386");
    }

    void
    markMediaMinimal() {
        graph.anotateNodesV3("libjpeg-turbo8");
        graph.anotateNodesV3("libjpeg9");
        graph.anotateNodesV3("libwebp7");
        graph.anotateNodesV3("libgif7");
        graph.anotateNodesV3("libjbig2dec0");
        graph.anotateNodesV3("libnetpbm10");
        graph.anotateNodesV3("libjbig0");
        graph.anotateNodesV3("libilmbase25");
        graph.anotateNodesV3("libpixman-1-0");
        graph.anotateNodesV3("liblcms2-2");
        graph.anotateNodesV3("libmjpegutils-2.1-0");
        graph.anotateNodesV3("libexif12");
        graph.anotateNodesV3("libpng16-16");
        graph.anotateNodesV3("libopenjp2-7");
    }
    
    void
    markMinimal() {
        // System can boot (Ubuntu 07.04)
        //graph.anotateNodesV2("linux-image-2.6.20-15-generic");
        //graph.anotateNodesV2("linux-image-2.6.20.3-ubuntu1-custom");
        //graph.anotateNodesV2("lilo");

        // System can boot (Ubuntu 18.04)
        graph.anotateNodesV2("grub-pc");
        graph.anotateNodesV2("init");
        graph.anotateNodesV2("sysvinit-utils");
        graph.anotateNodesV2("console-setup");
        graph.anotateNodesV2("linux-image-5.3.0-62-generic");
        graph.anotateNodesV2("linux-modules-5.3.0-62-generic");
        graph.anotateNodesV2("linux-modules-extra-5.3.0-62-generic");

	// System can boot (Common)
        graph.anotateNodesV2("linux-image-generic");
        //graph.anotateNodesV2("linux-image");
        //graph.anotateNodesV2("linux-generic");
        //graph.anotateNodesV2("grub2");

        // System can mount storage devices
        graph.anotateNodesV2("fdisk");
        graph.anotateNodesV2("e2fsprogs");

        // User can log in (ncurses-base contains terminal definitions,
        // and at least a single language pack is needed to avoid
        // annoying console errors)
        graph.anotateNodesV2("login");
        graph.anotateNodesV2("bash");
        graph.anotateNodesV2("language-pack-en-base");
        graph.anotateNodesV2("ncurses-base");
        graph.anotateNodesV2("libc-bin"); // ldd command
        graph.anotateNodesV2("ncurses-bin"); // clear command
        graph.anotateNodesV2("coreutils"); // dd, wc, du, md5sum, nice, nohup and other unix commands
        graph.anotateNodesV2("system-services"); // local ttys

        // User can edit files
        graph.anotateNodesV2("nano");

        // Can connect to the network and install software
        //graph.anotateNodesV2("isc-dhcp-client");
        graph.anotateNodesV2("ifupdown");
        graph.anotateNodesV2("apt");

        // Can install locally downloaded Debian packages
        graph.anotateNodesV2("dpkg");

        // A minimal useful system include some utilites to check the hardware
        // (user should do ping, traceroute, nslookup, lspci, lsusb, file,
	// time)
        graph.anotateNodesV2("iputils-ping");
        graph.anotateNodesV2("traceroute");
        graph.anotateNodesV2("dnsutils");
        graph.anotateNodesV2("pciutils");
        graph.anotateNodesV2("usbutils");
        graph.anotateNodesV2("file");
        graph.anotateNodesV2("time");
        graph.anotateNodesV2("htop");
        graph.anotateNodesV2("aptitude");
        graph.anotateNodesV2("aptitude-common");
	
        // Admin task as dmesg, file system formatting, IPC control and getty
        graph.anotateNodesV2("util-linux");

        // Is documentation minimal?
        graph.anotateNodesV2("man-db");

        // Other candidates for analysis
        //graph.anotateNodesV2("openssh-client");
        //graph.anotateNodesV2("ftpd");
        //graph.anotateNodesV2("telnetd");
    }

    // See also: markBasic
    void markIntermediate()
    {
	// Level 3 components: gtk based
        graph.anotateNodesV2("pavucontrol");
        graph.anotateNodesV2("emacs");

        // Level 2 components: X11 (server)
        graph.anotateNodesV2("xinit");
        graph.anotateNodesV2("xdm");

        // Level 2 components: X11 (multimedia)
        graph.anotateNodesV2("mplayer");
        graph.anotateNodesV2("pulseaudio");

        // Level 2 components: X11 (client)
        graph.anotateNodesV2("gmemusage");
        graph.anotateNodesV2("htop");
        graph.anotateNodesV2("mesa-utils");
        graph.anotateNodesV2("motif-clients");
        graph.anotateNodesV2("openssh-server");
        graph.anotateNodesV2("twm");
        graph.anotateNodesV2("x11-apps");
        graph.anotateNodesV2("xosview");
        graph.anotateNodesV2("xterm");

        // Level 1 components: console (multimedia)
        graph.anotateNodesV2("alsa-utils");

        //markMinimal();
    }

    public void
    markLinuxFromScratch() {
	String glibc[] = {
	    "glibc-doc",
	    "glibc-source",
	    "libc6",
	    "libc6-dbg",
	    "libc6-dev",
	    "libc6-dev-i386",
	    "libc6-dev-x32",
	    "libc6-i386",
	    "libc6-pic",
	    "libc6-x32",
	    "libc-bin",
	    "libc-dev-bin",
	    "locales",
	    "locales-all",
	    "multiarch-support",
	    "nscd"};
	
	String gcc[] = {
            "cpp-8",
            "cpp-8-doc",
            "fixincludes",
            "g++-8",
            "g++-8-multilib",
            "gcc-8",
            "gcc-8-base",
            "gcc-8-doc",
            "gcc-8-hppa64-linux-gnu",
            "gcc-8-locales",
            "gcc-8-multilib",
            "gcc-8-offload-nvptx",
            "gcc-8-plugin-dev",
            "gcc-8-source",
            "gcc-8-test-results",
            "gccbrig-8",
            "gccgo-8",
            "gccgo-8-doc",
            "gccgo-8-multilib",
            "gdc-8",
            "gdc-8-multilib",
            "gfortran-8",
            "gfortran-8-doc",
            "gfortran-8-multilib",
            "gnat-8",
            "gnat-8-doc",
            "gnat-8-sjlj",
            "gobjc-8",
            "gobjc++-8",
            "gobjc-8-multilib",
            "gobjc++-8-multilib",
            "lib32asan5",
            "lib32asan5-dbg",
            "lib32atomic1",
            "lib32atomic1-dbg",
            "lib32gcc1",
            "lib32gcc1-dbg",
            "lib32gcc-8-dev",
            "lib32gfortran5",
            "lib32gfortran5-dbg",
            "lib32gfortran-8-dev",
            "lib32go13",
            "lib32go13-dbg",
            "lib32gomp1",
            "lib32gomp1-dbg",
            "lib32gphobos76",
            "lib32gphobos76-dbg",
            "lib32gphobos-8-dev",
            "lib32itm1",
            "lib32itm1-dbg",
            "lib32lsan0",
            "lib32lsan0-dbg",
            "lib32mpx2",
            "lib32mpx2-dbg",
            "lib32objc4",
            "lib32objc4-dbg",
            "lib32objc-8-dev",
            "lib32quadmath0",
            "lib32quadmath0-dbg",
            "lib32stdc++6",
            "lib32stdc++6-8-dbg",
            "lib32stdc++-8-dev",
            "lib32ubsan1",
            "lib32ubsan1-dbg",
            "libasan5",
            "libasan5-dbg",
            "libatomic1",
            "libatomic1-dbg",
            "libcc1-0",
            "libgcc1",
            "libgcc1-dbg",
            "libgcc-8-dev",
            "libgccjit0",
            "libgccjit0-dbg",
            "libgccjit-8-dev",
            "libgccjit-8-doc",
            "libgfortran5",
            "libgfortran5-dbg",
            "libgfortran-8-dev",
            "libgnat-8",
            "libgnat-8-dbg",
            "libgnatvsn8",
            "libgnatvsn8-dbg",
            "libgnatvsn8-dev",
            "libgo13",
            "libgo13-dbg",
            "libgomp1",
            "libgomp1-dbg",
            "libgomp-plugin-nvptx1",
            "libgphobos76",
            "libgphobos76-dbg",
            "libgphobos-8-dev",
            "libhsail-rt0",
            "libhsail-rt0-dbg",
            "libhsail-rt-8-dev",
            "libitm1",
            "libitm1-dbg",
            "liblsan0",
            "liblsan0-dbg",
            "libmpx2",
            "libmpx2-dbg",
            "libobjc4",
            "libobjc4-dbg",
            "libobjc-8-dev",
            "libquadmath0",
            "libquadmath0-dbg",
            "libstdc++6",
            "libstdc++6-8-dbg",
            "libstdc++-8-dev",
            "libstdc++-8-doc",
            "libstdc++-8-pic",
            "libtsan0",
            "libtsan0-dbg",
            "libubsan1",
            "libubsan1-dbg",
            "libx32asan5",
            "libx32asan5-dbg",
            "libx32atomic1",
            "libx32atomic1-dbg",
            "libx32gcc1",
            "libx32gcc1-dbg",
            "libx32gcc-8-dev",
            "libx32gfortran5",
            "libx32gfortran5-dbg",
            "libx32gfortran-8-dev",
            "libx32go13",
            "libx32go13-dbg",
            "libx32gomp1",
            "libx32gomp1-dbg",
            "libx32gphobos76",
            "libx32gphobos76-dbg",
            "libx32gphobos-8-dev",
            "libx32itm1",
            "libx32itm1-dbg",
            "libx32lsan0",
            "libx32lsan0-dbg",
            "libx32objc4",
            "libx32objc4-dbg",
            "libx32objc-8-dev",
            "libx32quadmath0",
            "libx32quadmath0-dbg",
            "libx32stdc++6",
            "libx32stdc++6-8-dbg",
            "libx32stdc++-8-dev",
            "libx32ubsan1",
            "libx32ubsan1-dbg"
	};

	String ncurses[] = {
            "lib32ncurses5",
            "lib32ncurses5-dev",
            "lib32ncursesw5",
            "lib32ncursesw5-dev",
            "lib32tinfo5",
            "lib32tinfo-dev",
            "libncurses5",
            "libncurses5-dbg",
            "libncurses5-dev",
            "libncursesw5",
            "libncursesw5-dbg",
            "libncursesw5-dev",
            "libtinfo5",
            "libtinfo5-dbg",
            "libtinfo-dev",
            "libx32ncurses5",
            "libx32ncurses5-dev",
            "libx32ncursesw5",
            "libx32ncursesw5-dev",
            "libx32tinfo5",
            "libx32tinfo-dev",
            "ncurses-base",
            "ncurses-bin",
            "ncurses-doc",
            "ncurses-examples",
            "ncurses-term"
	};

        String gpm[] = {
	    "gpm",
            "libgpm2",
            "libgpm-dev"
	};

	String pcre3[] = {
	    "libpcre16-3",
            "libpcre32-3",
            "libpcre3",
            "libpcre3-dbg",
            "libpcre3-dev",
            "libpcrecpp0v5",
            "pcregrep"
	};

	String selinux[] = {
	    "libselinux1",
            "libselinux1-dev",
            "python3-selinux",
            "python-selinux",
            "ruby-selinux",
            "selinux-utils"
	};

	String libsepol1[] = {
	    "libsepol1", "libsepol1-dev", "sepol-utils"
	};

	String gmp[] = {
	    "libgmp10",
            "libgmp10-doc",
            "libgmp3-dev",
            "libgmp-dev",
            "libgmpxx4ldbl"
	};

	String apparmor[] = {
	    "apparmor",
            "apparmor-easyprof",
            "apparmor-notify",
            "apparmor-profiles",
            "apparmor-utils",
            "dh-apparmor",
            "libapache2-mod-apparmor",
            "libapparmor1",
            "libapparmor-dev",
            "libapparmor-perl",
            "libpam-apparmor",
            "python3-apparmor",
            "python3-libapparmor",
            "python-apparmor",
            "python-libapparmor"
	};

	String bzip2[] = {
	    "bzip2",
            "bzip2-doc",
            "libbz2-1.0",
            "libbz2-dev"
	};

	String python36[] = {
	    "idle-python3.6",
            "libpython3.6",
            "libpython3.6-dbg",
            "libpython3.6-dev",
            "libpython3.6-minimal",
            "libpython3.6-stdlib",
            "libpython3.6-testsuite",
            "python3.6",
            "python3.6-dbg",
            "python3.6-dev",
            "python3.6-doc",
            "python3.6-examples",
            "python3.6-minimal",
            "python3.6-venv"
	};

	String libdb53[] = {
            "db5.3-doc",
            "db5.3-sql-util",
            "db5.3-util",
            "libdb5.3",
            "libdb5.3++",
            "libdb5.3-dbg",
            "libdb5.3-dev",
            "libdb5.3++-dev",
            "libdb5.3-java",
            "libdb5.3-java-dev",
            "libdb5.3-java-jni",
            "libdb5.3-sql",
            "libdb5.3-sql-dev",
            "libdb5.3-stl",
            "libdb5.3-stl-dev",
            "libdb5.3-tcl"
	};

        String attr[] = {
	    "attr",
            "libattr1",
            "libattr1-dev"
	};

	String acl[] = {
	    "acl",
            "libacl1",
            "libacl1-dev"
	};

	String tar[] = {
	    "tar",
            "tar-scripts"
	};

        String libzstd[] = {
	    "libzstd1",
            "libzstd1-dev",
            "libzstd-dev",
            "zstd"
	};

	String xzutils[] = {
	    "liblzma5",
            "liblzma-dev",
            "liblzma-doc",
            "xzdec",
            "xz-utils"
	};

	String zlib[] = {
	    "lib32z1",
            "libx32z1",
            "zlib1g",
            "zlib1g-dbg",
            "lib32z1-dev",
            "libx32z1-dev",
            "zlib1g-dev"
	};

        String dpkg[] = {
	    "dpkg",
            "dpkg-dev",
            "dselect",
            "libdpkg-dev",
            "libdpkg-perl"
	};

	String libgpgerror[] = {
	    "libgpg-error0",
            "libgpg-error-dev",
            "libgpg-error-mingw-w64-dev"
	};

	String libgcrypt20[] = {
	    "libgcrypt11-dev",
            "libgcrypt20",
            "libgcrypt20-dev",
            "libgcrypt20-doc",
            "libgcrypt-mingw-w64-dev"
	};

	String lz4[] = {
	    "liblz4-1",
            "liblz4-1-dbg",
            "liblz4-dev",
            "liblz4-tool"
	};

	String systemd[] = {
	    "libnss-myhostname",
            "libnss-mymachines",
            "libnss-resolve",
            "libnss-systemd",
            "libpam-systemd",
            "libsystemd0",
            "libsystemd-dev",
            "libudev1",
            "libudev-dev",
            "systemd",
            "systemd-container",
            "systemd-coredump",
            "systemd-journal-remote",
            "systemd-sysv",
            "systemd-tests",
            "udev"
	};

        String coreutils[] = {
	    "coreutils"
	};

	String perl[] = {
	    "libperl5.26",
            "libperl-dev",
            "perl",
            "perl-base",
            "perl-debug",
            "perl-doc",
            "perl-modules-5.26"
	};

        String debconf[] = {
	    "debconf",
            "debconf-doc",
            "debconf-i18n",
            "debconf-utils",
            "python3-debconf",
            "python-debconf"
	};

	String liblocalegettextperl[] = {
	    "liblocale-gettext-perl"
	};

	String libtextcharwidthperl[] = {
            "libtext-charwidth-perl"
	};

	String libtexticonvperl[] = {
            "libtext-iconv-perl"
	};

	String libtextwrapi18nperl[] = {
            "libtext-wrapi18n-perl"
	};

	String libcapng[] = {
	    "libcap-ng0",
            "libcap-ng-dev",
            "libcap-ng-utils",
            "python3-cap-ng",
            "python-cap-ng"
	};

        String audit[] = {
	    "audispd-plugins",
            "auditd",
            "golang-redhat-audit-dev",
            "libaudit1",
            "libaudit-common",
            "libaudit-dev",
            "libauparse0",
            "libauparse-dev",
            "python3-audit",
            "python-audit"
	};

	String sensibleutils[] = {
	    "sensible-utils"
	};


	String ucf[] = {
	    "ucf"
	};

	String distroinfodata[] = {
	    "distro-info-data"
	};

        String lsb[] = {
	    "lsb",
            "lsb-base",
            "lsb-core",
            "lsb-invalid-mta",
            "lsb-printing",
            "lsb-release",
            "lsb-security"
	};

	String gnupg2[] = {
	    "dirmngr",
            "gnupg2",
            "gnupg",
            "gnupg-agent",
            "gnupg-l10n",
            "gnupg-utils",
            "gpg",
            "gpg-agent",
            "gpgconf",
            "gpgsm",
            "gpgv2",
            "gpgv",
            "gpgv-static",
            "gpgv-win32",
            "gpg-wks-client",
            "gpg-wks-server",
            "scdaemon"
	};

	String pam[] = {
	    "libpam0g",
            "libpam0g-dev",
            "libpam-cracklib",
            "libpam-doc",
            "libpam-modules",
            "libpam-modules-bin",
            "libpam-runtime"
	};

        String libsemanage[] = {
	    "libsemanage1",
            "libsemanage1-dev",
            "libsemanage-common",
            "python3-semanage",
            "python-semanage",
            "ruby-semanage",
            "semanage-utils"
	};

	String gnupg1[] = {
	    "gnupg1",
            "gnupg1-l10n",
            "gpgv1"
	};

	String texinfo[] = {
	    "info",
            "install-info",
            "texinfo"
	};

	String shadow[] = {
	    "login",
            "passwd",
            "uidmap"
	};

        String adduser[] = {
	    "adduser"
	};

	String libffi[] = {
	    "libffi6",
            "libffi6-dbg",
            "libffi-dev"
	};

        String p11kit0[] = {
	    "libp11-kit0",
            "libp11-kit-dev",
            "p11-kit",
            "p11-kit-modules"
	};
	
        String libtasn1[] = {
	    "libtasn1-6",
            "libtasn1-6-dev",
            "libtasn1-bin",
            "libtasn1-doc"
	};

	String libunistring[] = {
	    "libunistring2",
            "libunistring-dev"
	};

	String nettle[] = {
	    "libhogweed4",
            "libnettle6",
            "nettle-bin",
            "nettle-dev"
	};

	String libidn2[] = {
	    "idn2",
            "libidn2-0",
            "libidn2-0-dev",
            "libidn2-dev",
            "libidn2-doc"
	};

	String gnutls28[] = {
	    "gnutls-bin",
            "gnutls-doc",
            "libgnutls28-dev",
            "libgnutls30",
            "libgnutls-dane0",
            "libgnutls-openssl27",
            "libgnutlsxx28"
	};

	String libseccomp[] = {
	    "libseccomp2",
            "libseccomp-dev",
            "seccomp"
	};

	String ubuntukeyring[] = {
	    "ubuntu-cloudimage-keyring",
            "ubuntu-cloud-keyring",
            "ubuntu-dbgsym-keyring",
            "ubuntu-keyring"
	};

	String apt[] = {
	    "apt",
            "apt-doc",
            "apt-transport-https",
            "apt-utils",
            "libapt-inst2.0",
            "libapt-pkg5.0",
            "libapt-pkg-dev",
            "libapt-pkg-doc"
	};

        String utillinux[] = {
	    "bsdutils",
            "fdisk",
            "libblkid1",
            "libblkid-dev",
            "libfdisk1",
            "libfdisk-dev",
            "libmount1",
            "libmount-dev",
            "libsmartcols1",
            "libsmartcols-dev",
            "libuuid1",
            "mount",
            "rfkill",
            "setpriv",
            "util-linux",
            "util-linux-locales",
            "uuid-dev",
            "uuid-runtime"
	};

        String fuse[] = {
	    "fuse",
            "fuse-dbg",
            "libfuse2",
            "libfuse-dev"
	};

	String sed[] = {
	    "sed"
	};

	String e2fsprogs[] = {
	    "comerr-dev",
            "e2fsck-static",
            "e2fslibs",
            "e2fslibs-dev",
            "e2fsprogs",
            "e2fsprogs-l10n",
            "fuse2fs",
            "libcom-err2",
            "libcomerr2",
            "libext2fs2",
            "libext2fs-dev",
            "libss2",
            "ss-dev"
	};

        String file[] = {
	    "file",
            "libmagic1",
            "libmagic-dev",
            "libmagic-mgc"
	};

	String ntfs3g[] = {
	    "libntfs-3g88",
            "ntfs-3g",
            "ntfs-3g-dbg",
            "ntfs-3g-dev"
	};

	String slang2[] = {
	    "libslang2",
            "libslang2-dev",
            "libslang2-modules",
            "libslang2-pic",
            "slsh"
	};

	String newt[] = {
            "whiptail",
	    "libnewt0.52",
            "libnewt-dev",
            "libnewt-pic",
            "newt-tcl",
            "python3-newt",
            "python-newt"
	};

	String libtextwrap[] = {
	    "libtextwrap1",
	    "libtextwrap-dev"
	};

        String libdebianinstaller[] = {
	    "libdebian-installer4",
            "libdebian-installer4-dev",
            "libdebian-installer-extra4"
	};

        String cdebconf[] = {
	    "cdebconf",
            "cdebconf-gtk",
            "libdebconfclient0",
            "libdebconfclient0-dev"
	};

        String kmod[] = {
	    "kmod",
            "libkmod2",
            "libkmod-dev",
            "module-init-tools"
	};

	String libcap2[] = {
	    "libcap2",
            "libcap2-bin",
            "libcap-dev",
            "libpam-cap"
	};

        String lvm2[] = {
	    "clvm",
            "dmeventd",
            "dmsetup",
            "libdevmapper1.02.1",
            "libdevmapper-dev",
            "libdevmapper-event1.02.1",
            "liblvm2app2.2",
            "liblvm2cmd2.02",
            "liblvm2-dev",
            "lvm2",
            "lvm2-dbusd",
            "lvm2-lockd"
	};

	String argon2[] = {
	    "argon2",
            "libargon2-0",
            "libargon2-0-dev"
	};

	String popt[] = {
	    "libpopt0",
            "libpopt-dev"
	};

	String jsonc[] = {
	    "libjson-c3",
            "libjson-c-dev",
            "libjson-c-doc"
	};

	String cryptsetup[] = {
	    "cryptsetup",
            "cryptsetup-bin",
            "libcryptsetup12",
            "libcryptsetup-dev"
	};

	String libidn[] = {
	    "idn",
            "libidn11",
            "libidn11-dev",
            "libidn11-java"
	};

	String iptables[] = {
	    "iptables",
            "iptables-dev",
            "iptables-nftables-compat",
            "libip4tc0",
            "libip4tc-dev",
            "libip6tc0",
            "libip6tc-dev",
            "libiptc0",
            "libiptc-dev",
            "libxtables12",
            "libxtables-dev"
	};

	String procps[] = {
	    "libprocps6",
            "libprocps-dev",
            "procps"
	};

	String initsystemhelpers[] = {
	    "init",
            "init-system-helpers"
	};

	String libbsd[] = {
	    "libbsd0",
            "libbsd-dev"
	};

	String debianutils[] = {
	    "debianutils"
	};

	String bsdmainutils[] = {
	    "bsdmainutils"
	};

	String dialog[] = {
	    "dialog"
	};

	String basefiles[] = {
	    "base-files"
	};

	String consolesetup[] = {
	    "bdf2psf",
            "console-setup",
            "console-setup-linux",
            "console-setup-mini",
            "keyboard-configuration"
	};

        String xkeyboardconfig[] = {
	    "xkb-data",
            "xkb-data-i18n"
	};

	String gettext[] = {
	    "autopoint",
            "gettext",
            "gettext-base",
            "gettext-doc",
            "gettext-el",
            "libasprintf0v5",
            "libasprintf-dev",
            "libgettextpo0",
            "libgettextpo-dev"
	};

	String kbd[] = {
	    "kbd"
	};

	String sysvinit[] = {
	    "sysvinit-utils"
	};

	String nettools[] = {
	    "net-tools"
	};

	String languagepacken[] = {
	    "language-pack-en"
	};

	String languagepackenbase[] = {
	    "language-pack-en-base"
	};

        String libnl3[] = {
	    "libnl-3-200",
            "libnl-3-200-dbg",
            "libnl-3-dev",
            "libnl-cli-3-200",
            "libnl-cli-3-dev",
            "libnl-genl-3-200",
            "libnl-genl-3-dev",
            "libnl-idiag-3-200",
            "libnl-idiag-3-dev",
            "libnl-nf-3-200",
            "libnl-nf-3-dev",
            "libnl-route-3-200",
            "libnl-route-3-dev",
            "libnl-utils",
            "libnl-xfrm-3-200",
            "libnl-xfrm-3-dev"
	};

        String iw[] = {
	    "iw"
	};

	String wirelessregdb[] = {
            "wireless-regdb"	    
	};

	String crda[] = {
	    "crda"
	};

	String wirelesscrda[] = {
	    "wireless-crda"
	};

	String openssl[] = {
	    "libssl1.1",
            "libssl-dev",
            "libssl-doc",
            "openssl"
	};

	String linuxbase[] = {
	    "linux-base",
	    "linux-base-sgx"
	};

        String linuxfirmware[] = {
	    "linux-firmware"
	};

        String iucodetool[] = {
	    "iucode-tool"
	};

        String intelmicrocode[] = {
	    "intel-microcode"
	};

        String amd64microcode[] = {
	    "amd64-microcode"
	};

        String cacertificates[] = {
	    "ca-certificates"
	};

	String set[][] = {
            // level 00_
	    glibc, gcc, gpm, ncurses, pcre3, selinux, libsepol1, gmp,
            // level 01_
	    attr, acl, tar, libzstd, xzutils, zlib, bzip2, dpkg, libgpgerror,
	    libgcrypt20, lz4, systemd, coreutils, perl, liblocalegettextperl,
	    libtextcharwidthperl, libtexticonvperl, libtextwrapi18nperl,
	    debconf, libcapng, audit, sensibleutils, ucf, distroinfodata, lsb,
	    gnupg2, libdb53, pam, libsemanage, gnupg1, texinfo, shadow, adduser,
	    libffi, p11kit0, libtasn1,libunistring, nettle, libidn2, gnutls28,
	    libseccomp, ubuntukeyring, apt,
            // level 02_
	    utillinux, sed, fuse, e2fsprogs, file, ntfs3g,
	    // level 03_
	    slang2, newt, libtextwrap, libdebianinstaller, cdebconf, kmod,
	    libcap2, lvm2, argon2, popt, jsonc, cryptsetup, libidn, iptables,
	    procps, apparmor, libbsd, debianutils, bsdmainutils, dialog,
	    basefiles, xkeyboardconfig, consolesetup, initsystemhelpers,
	    gettext, kbd, sysvinit, nettools, languagepacken,
	    languagepackenbase,
            // level 03_
	    libnl3, iw, wirelessregdb, crda, wirelesscrda, openssl,
	    linuxbase, linuxfirmware, iucodetool, intelmicrocode,
	    amd64microcode, cacertificates,
            // level 04_
	    python36
	};

	for (String source[]: set) {
	    for (String name: source) {
	        graph.anotateSingleNode(name);
	        graph.anotateSingleNode(name + ":i386");
	        graph.anotateSingleNode(name + ":amd64");
	    }
	}
    }
}
