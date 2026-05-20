package co.cubestudio;

public class Markers {
    private SoftwarePackageGraph graph;

    public Markers(SoftwarePackageGraph graph) {
	this.graph = graph;
    }
    
    void markProhibited()
    {
        graph.markPackageAndItsClients("bsh-gcj");
        graph.markPackageAndItsClients("cacao");
        graph.markPackageAndItsClients("ca-certificates-java");
        graph.markPackageAndItsClients("default-jre");
        graph.markPackageAndItsClients("default-jre-headless");
        graph.markPackageAndItsClients("gcj-4.4-base");
        graph.markPackageAndItsClients("gcj-4.4-jre");
        graph.markPackageAndItsClients("gcj-4.4-jre-headless");
        graph.markPackageAndItsClients("gcj-4.4-jre-lib");
        graph.markPackageAndItsClients("gcj-jre");
        graph.markPackageAndItsClients("gcj-jre-headless");
        graph.markPackageAndItsClients("gij-4.3");
        graph.markPackageAndItsClients("icedtea-6-jre-cacao");
        graph.markPackageAndItsClients("jamvm");
        graph.markPackageAndItsClients("java-common");
        graph.markPackageAndItsClients("libaccess-bridge-java");
        graph.markPackageAndItsClients("libaccess-bridge-java-jni");
        graph.markPackageAndItsClients("libgcj10");
        graph.markPackageAndItsClients("libgcj10-awt");
        graph.markPackageAndItsClients("libgcj-bc");
        graph.markPackageAndItsClients("libgcj-common");
        graph.markPackageAndItsClients("libhsqldb-java");
        graph.markPackageAndItsClients("libhsqldb-java-gcj");
        graph.markPackageAndItsClients("libjline-java");
        graph.markPackageAndItsClients("libservlet2.5-java");
        graph.markPackageAndItsClients("libswfdec-0.8-0");
        graph.markPackageAndItsClients("openjdk-6-jre");
        graph.markPackageAndItsClients("openjdk-6-jre-headless");
        graph.markPackageAndItsClients("openjdk-6-jre-lib");
        graph.markPackageAndItsClients("openoffice.org-gcj");
        graph.markPackageAndItsClients("openoffice.org-java-common");
        graph.markPackageAndItsClients("tzdata-java");
        graph.markPackageAndItsClients("tzdata-java");
        graph.markPackageAndItsClients("gnome-js-common");
        graph.markPackageAndItsClients("swfdec-gnome");
        graph.markPackageAndItsClients("swfdec-mozilla");
        graph.markPackageAndItsClients("lightsoff");
        graph.markPackageAndItsClients("gir1.0-gconf-2.0");
        graph.markPackageAndItsClients("gir1.0-atk-1.0");
        graph.markPackageAndItsClients("gir1.0-clutter-1.0");
        graph.markPackageAndItsClients("libgirepository1.0-0");
        graph.markPackageAndItsClients("gir1.0-glib-2.0");
        graph.markPackageAndItsClients("gir1.0-freedesktop");
        graph.markPackageAndItsClients("gir1.0-gstreamer-0.10");
        graph.markPackageAndItsClients("gir1.0-pango-1.0");
        graph.markPackageAndItsClients("gir1.0-gtk-2.0");
        graph.markPackageAndItsClients("libseed0");
        graph.markPackageAndItsClients("seed");
        graph.markPackageAndItsClients("gir1.0-clutter-gtk-0.10");
        graph.markPackageAndItsClients("swell-foop");
        graph.markPackageAndItsClients("nvidia-173-modaliases");
        graph.markPackageAndItsClients("nvidia-185-modaliases");
        graph.markPackageAndItsClients("nvidia-96-modaliases");
        graph.markPackageAndItsClients("nvidia-common");
        graph.markPackageAndItsClients("nvidia-current-modaliases");
    }

    void
    markBasic()
    {
        // Other commonly used development tools
        //graph.markPackageAndItsDependencies("xxdiff");
        graph.markPackageAndItsDependencies("xosview");
        graph.markPackageAndItsDependencies("gdb");
        graph.markPackageAndItsDependencies("valgrind");
        graph.markPackageAndItsDependencies("graphviz");

        // Vitral, JOGL
        graph.markPackageAndItsDependencies("libxrandr-dev");
        graph.markPackageAndItsDependencies("libxinerama-dev");
        graph.markPackageAndItsDependencies("git-core");
        graph.markPackageAndItsDependencies("libxxf86vm-dev");
        graph.markPackageAndItsDependencies("subversion");
        graph.markPackageAndItsDependencies("cvs");
        graph.markPackageAndItsDependencies("libc6-dev-i386");

        // Basic X11 server workstation capability: xdm + twm
        graph.markPackageAndItsDependencies("xinit");
        graph.markPackageAndItsDependencies("xdm");
        graph.markPackageAndItsDependencies("twm");
        graph.markPackageAndItsDependencies("motif-clients");
        graph.markPackageAndItsDependencies("xview-clients");
        graph.markPackageAndItsDependencies("olwm");
        graph.markPackageAndItsDependencies("olvwm");
        graph.markPackageAndItsDependencies("xfonts-100dpi");
        graph.markPackageAndItsDependencies("xfonts-75dpi");

        // AQUYNZA development support
        graph.markPackageAndItsDependencies("freeglut3-dev");
        graph.markPackageAndItsDependencies("xviewg-dev");
        graph.markPackageAndItsDependencies("xview-examples");
        graph.markPackageAndItsDependencies("libxaw7-dev");
        graph.markPackageAndItsDependencies("libmotif-dev");
        graph.markPackageAndItsDependencies("libglu1-mesa-dev");
        graph.markPackageAndItsDependencies("mesa-utils");
        graph.markPackageAndItsDependencies("libtiff4-dev");
        graph.markPackageAndItsDependencies("libpng12-dev");
        graph.markPackageAndItsDependencies("libgif-dev");
        graph.markPackageAndItsDependencies("libfreeimage-dev");
        graph.markPackageAndItsDependencies("libjpeg62-dev");
        graph.markPackageAndItsDependencies("libnetpbm10-dev");
        graph.markPackageAndItsDependencies("libreadline-dev");
        graph.markPackageAndItsDependencies("libncursesw5-dev");
        graph.markPackageAndItsDependencies("x11proto-print-dev");

        // Development tools
        graph.markPackageAndItsDependencies("g++");
        graph.markPackageAndItsDependencies("gcc");
        graph.markPackageAndItsDependencies("binutils");
        graph.markPackageAndItsDependencies("htop");
        graph.markPackageAndItsDependencies("ltrace");
        graph.markPackageAndItsDependencies("patch");
        graph.markPackageAndItsDependencies("strace");
        graph.markPackageAndItsDependencies("lsof");
        graph.markPackageAndItsDependencies("time");
        graph.markPackageAndItsDependencies("info");
        graph.markPackageAndItsDependencies("man-db");
        graph.markPackageAndItsDependencies("automake");
        graph.markPackageAndItsDependencies("autogen");
        graph.markPackageAndItsDependencies("flex");
        graph.markPackageAndItsDependencies("bison");
        graph.markPackageAndItsDependencies("manpages-posix");

        // System start & kernel
        graph.markPackageAndItsDependencies("grub2");
        graph.markPackageAndItsDependencies("upstart");
        graph.markPackageAndItsDependencies("initscripts");
        graph.markPackageAndItsDependencies("linux-server");
        graph.markPackageAndItsDependencies("linux-image-2.6.32-21-server");
        graph.markPackageAndItsDependencies("linux-image-2.6.35-22-server");
        graph.markPackageAndItsDependencies("linux-image-2.6.35-19-generic");
        graph.markPackageAndItsDependencies("linux-generic");
        graph.markPackageAndItsDependencies("linux-firmware");

        // Minimal openssh-based remote graphics
        graph.markPackageAndItsDependencies("xemacs21");
        graph.markPackageAndItsDependencies("xterm");
        graph.markPackageAndItsDependencies("x11-apps");
        graph.markPackageAndItsDependencies("xloadimage");
        graph.markPackageAndItsDependencies("openssh-server");
        graph.markPackageAndItsDependencies("dhcp3-client");
        graph.markPackageAndItsDependencies("xosview");

        // Text session
        graph.markPackageAndItsDependencies("login");
        graph.markPackageAndItsDependencies("dash");
        graph.markPackageAndItsDependencies("bash");
        graph.markPackageAndItsDependencies("vim-tiny");
        graph.markPackageAndItsDependencies("bzip2");
        graph.markPackageAndItsDependencies("less");
        graph.markPackageAndItsDependencies("grep");
        graph.markPackageAndItsDependencies("gzip");
        graph.markPackageAndItsDependencies("bzip2");
        graph.markPackageAndItsDependencies("tar");
        graph.markPackageAndItsDependencies("diffutils");
        graph.markPackageAndItsDependencies("openssh-client");
        graph.markPackageAndItsDependencies("language-pack-en-base");

        // System management
        graph.markPackageAndItsDependencies("apt");
        graph.markPackageAndItsDependencies("sudo");
        graph.markPackageAndItsDependencies("pciutils");
        graph.markPackageAndItsDependencies("usbutils");
        graph.markPackageAndItsDependencies("wget");
        //graph.markPackageAndItsDependencies("aptitude");

        // Marked as "essencial" by ubuntu, but not really really essencial
        graph.markPackageAndItsDependencies("ncurses-base");
        graph.markPackageAndItsDependencies("python");
        graph.markPackageAndItsDependencies("bsdutils");

        // Other
        graph.markPackageAndItsDependencies("mawk");
        graph.markPackageAndItsDependencies("iputils-ping");
        graph.markPackageAndItsDependencies("hostname");
        graph.markPackageAndItsDependencies("locales");
        graph.markPackageAndItsDependencies("libgd2-xpm");
        graph.markPackageAndItsDependencies("dialog");
    }

    void markMultimedia()
    {
        // Canberra
        graph.markPackageAndItsClients("libcanberra-gtk0:amd64");
        graph.markPackageAndItsClients("libcanberra-gtk3-0:amd64");
        graph.markPackageAndItsClients("libcanberra-gtk3-module:amd64");
        graph.markPackageAndItsClients("libcanberra-gtk-module:amd64");
        graph.markPackageAndItsClients("libcanberra-doc");
        graph.markPackageAndItsClients("libcanberra-pulse");

        // Gstreamer
        graph.markPackageAndItsClients("gstreamer0.10-pulseaudio:amd64");
        graph.markPackageAndItsClients("gstreamer0.10-x:amd64");
        graph.markPackageAndItsClients("gstreamer1.0-x:amd64");
        graph.markPackageAndItsClients("gstreamer0.10-x:i386");
        graph.markPackageAndItsClients("gstreamer0.10-x");
        graph.markPackageAndItsClients("gstreamer0.10-plugins-good:amd64");
        graph.markPackageAndItsClients("gstreamer0.10-plugins-base:amd64");
        graph.markPackageAndItsClients("gstreamer0.10-nice:amd64");
        graph.markPackageAndItsClients("gstreamer0.10-alsa");
        graph.markPackageAndItsClients("gstreamer0.10-tools");
        graph.markPackageAndItsClients("gstreamer1.0-plugins-base:amd64");
        graph.markPackageAndItsClients("gstreamer1.0-plugins-good:amd64");
        graph.markPackageAndItsClients("libgstreamer-plugins-bad0.10-0:amd64");
        graph.markPackageAndItsClients("libgstreamer-plugins-bad1.0-0:amd64");
        graph.markPackageAndItsClients("libgstreamer-plugins-base0.10-0:amd64");
        graph.markPackageAndItsClients("libgstreamer-plugins-base0.10-0:i386");
        graph.markPackageAndItsClients("libgstreamer-plugins-base1.0-0:amd64");
        graph.markPackageAndItsClients("libgstreamer0.10-0:amd64");

        // ALSA
        graph.markPackageAndItsClients("alsa-base");
        graph.markPackageAndItsClients("alsa-utils");
        graph.markPackageAndItsClients("libasound2:amd64");
        graph.markPackageAndItsClients("libasound2-plugins:amd64");
        graph.markPackageAndItsClients("libasound2");
        graph.markPackageAndItsClients("libasound2-doc");

        // Ffmpeg
        graph.markPackageAndItsClients("libavformat-extra-53:amd64");
        graph.markPackageAndItsClients("libavformat53:amd64");
        graph.markPackageAndItsClients("libavformat52");
        graph.markPackageAndItsClients("libavcodec52");
        graph.markPackageAndItsClients("libavcodec53:amd64");
        graph.markPackageAndItsClients("libavcodec-extra-53:amd64");
        graph.markPackageAndItsClients("libavutil49");
        graph.markPackageAndItsClients("libavutil-extra-49");
        graph.markPackageAndItsClients("libavutil-extra-50");
        graph.markPackageAndItsClients("libavutil-extra-51:amd64");
        graph.markPackageAndItsClients("libavutil-extra-51");
        graph.markPackageAndItsClients("libavutil-extra-52");
        graph.markPackageAndItsClients("libswscale0");

        // Video for linux
        graph.markPackageAndItsClients("libv4l-0");
        graph.markPackageAndItsClients("libv4lconvert0");
        
        // Codecs
        graph.markPackageAndItsClients("libogg0"); // OGG/Vorvis
        graph.markPackageAndItsClients("libspeex1"); // OGG/Vorvis
        graph.markPackageAndItsClients("libspeexdsp1"); // OGG/Vorvis
        graph.markPackageAndItsClients("libaom3"); // AV1
        graph.markPackageAndItsClients("libdav1d5"); // AV1	
        graph.markPackageAndItsClients("libvpx7"); // VP8 / VP9 codec
        graph.markPackageAndItsClients("libx264-163"); // X264
        graph.markPackageAndItsClients("libx265-199"); // H.265/HEVC

        // Other
        graph.markPackageAndItsClients("pocketsphinx-hmm-wsj1");
        graph.markPackageAndItsClients("libvpx-doc");
        graph.markPackageAndItsClients("libvpx1");
        graph.markPackageAndItsClients("flvmeta");
        graph.markPackageAndItsClients("libdvbcsa1");
        graph.markPackageAndItsClients("libopencore-amrwb0");
        graph.markPackageAndItsClients("libopencore-amrnb0");
        graph.markPackageAndItsClients("libmad0");
        graph.markPackageAndItsClients("libsonic0");
        graph.markPackageAndItsClients("dvsink");
        graph.markPackageAndItsClients("vdr-plugin-infosatepg");
        graph.markPackageAndItsClients("libmatroska5");
        graph.markPackageAndItsClients("dvdrip-utils");
        graph.markPackageAndItsClients("flvstreamer");
        graph.markPackageAndItsClients("dvblast");
        graph.markPackageAndItsClients("vdr-plugin-svdrposd");
        graph.markPackageAndItsClients("libev4");
        graph.markPackageAndItsClients("libsidplay2");
        graph.markPackageAndItsClients("libexempi3");
        graph.markPackageAndItsClients("libmtp-common");
        graph.markPackageAndItsClients("libtracker-sparql-0.14-0");
        graph.markPackageAndItsClients("libdirac-encoder0");
        graph.markPackageAndItsClients("libdirac-decoder0");
        graph.markPackageAndItsClients("libdvbpsi5");
        graph.markPackageAndItsClients("libdvbpsi6");
        graph.markPackageAndItsClients("libdvbpsi7");
        graph.markPackageAndItsClients("libopus0");
        graph.markPackageAndItsClients("libdca0");
        graph.markPackageAndItsClients("libaacs0");
        graph.markPackageAndItsClients("liblircclient0");
        graph.markPackageAndItsClients("libcrystalhd3");
        graph.markPackageAndItsClients("libbluray0");
        graph.markPackageAndItsClients("libbluray-doc");
        graph.markPackageAndItsClients("libquicktime-doc");
        graph.markPackageAndItsClients("libsamplerate0");
        graph.markPackageAndItsClients("libtag1-vanilla");
        graph.markPackageAndItsClients("libx264-123");
        graph.markPackageAndItsClients("flac");
        graph.markPackageAndItsClients("lame");
        graph.markPackageAndItsClients("libsoundtouch0:amd64");
        graph.markPackageAndItsClients("ubuntu-sounds");
        graph.markPackageAndItsClients("sound-icons");
        graph.markPackageAndItsClients("streamripper");
        graph.markPackageAndItsClients("libavc1394-0:i386");
        graph.markPackageAndItsClients("libdc1394-22:amd64");
        graph.markPackageAndItsClients("libzvbi-common");
        graph.markPackageAndItsClients("libaudiofile0");
        graph.markPackageAndItsClients("libaudiofile1");
        graph.markPackageAndItsClients("libaudiofile1:i386");
        graph.markPackageAndItsClients("libaudiofile1:amd64");
        graph.markPackageAndItsClients("sound-theme-freedesktop");
        graph.markPackageAndItsClients("freepats");
        graph.markPackageAndItsClients("libcdio13");
        graph.markPackageAndItsClients("xmms2-core");
        graph.markPackageAndItsClients("xmms2-icon");
        graph.markPackageAndItsClients("libav-doc");
        graph.markPackageAndItsClients("libxmmsclient6");
        graph.markPackageAndItsClients("esound-common");
        graph.markPackageAndItsClients("vobcopy");
        graph.markPackageAndItsClients("vorbis-tools");
        graph.markPackageAndItsClients("twolame");
        graph.markPackageAndItsClients("cdrdao");
        graph.markPackageAndItsClients("libffmpegthumbnailer4");
        graph.markPackageAndItsClients("libvo-amrwbenc0");
        graph.markPackageAndItsClients("libvo-aacenc0");
        graph.markPackageAndItsClients("linux-sound-base");
        graph.markPackageAndItsClients("libass4");
        graph.markPackageAndItsClients("libcdaudio1");
        graph.markPackageAndItsClients("libcelt0-0");
        graph.markPackageAndItsClients("libfaac0");
        graph.markPackageAndItsClients("libflite1");
        graph.markPackageAndItsClients("libgme0");
        graph.markPackageAndItsClients("libiptcdata0");
        graph.markPackageAndItsClients("libkate1");
        graph.markPackageAndItsClients("libmimic0");
        graph.markPackageAndItsClients("libesd0");
        graph.markPackageAndItsClients("libopenal1");
        graph.markPackageAndItsClients("libmpcdec3");
        graph.markPackageAndItsClients("libjack0");
        graph.markPackageAndItsClients("aumix-common");
        graph.markPackageAndItsClients("libgsm1");
        graph.markPackageAndItsClients("liboil0.3");
        graph.markPackageAndItsClients("libdvdnav4");
        graph.markPackageAndItsClients("libmp3lame0");
        graph.markPackageAndItsClients("liborc-0.4-0");
        graph.markPackageAndItsClients("libx264-85");
        graph.markPackageAndItsClients("libx264-106");
        graph.markPackageAndItsClients("libx264-116");
        graph.markPackageAndItsClients("libxvidcore4");
        graph.markPackageAndItsClients("libaudio2");
        graph.markPackageAndItsClients("libmpg123-0");
        graph.markPackageAndItsClients("libxine1-bin");
        graph.markPackageAndItsClients("liba52-0.7.4");
        graph.markPackageAndItsClients("faad");
        graph.markPackageAndItsClients("libmp4v2-0");
        graph.markPackageAndItsClients("mpg321");
        graph.markPackageAndItsClients("icedax");
        graph.markPackageAndItsClients("id3tool");
        graph.markPackageAndItsClients("mpegdemux");
        graph.markPackageAndItsClients("id3v2");
        graph.markPackageAndItsClients("libid3-3.8.3c2a");
        graph.markPackageAndItsClients("libx264-67");
        graph.markPackageAndItsClients("aumix");
        graph.markPackageAndItsClients("libportaudio2");
        graph.markPackageAndItsClients("libtwolame0");
        graph.markPackageAndItsClients("libscale0");
        graph.markPackageAndItsClients("libpostproc51");
        graph.markPackageAndItsClients("libsmpeg0");
        graph.markPackageAndItsClients("libcddb2");
        graph.markPackageAndItsClients("libebml0");
        graph.markPackageAndItsClients("vlc-data");
        graph.markPackageAndItsClients("libiso9660-7");
        graph.markPackageAndItsClients("libmikmod2");
        graph.markPackageAndItsClients("oss-compat");
        graph.markPackageAndItsClients("libva1");
        graph.markPackageAndItsClients("libvpx0");
        graph.markPackageAndItsClients("libx264-98");
        graph.markPackageAndItsClients("libjack-jackd2-0");
        graph.markPackageAndItsClients("libmpcdec6");
        graph.markPackageAndItsClients("libvdpau1");
        graph.markPackageAndItsClients("libdv4");
        graph.markPackageAndItsClients("libdc1394-22");
        graph.markPackageAndItsClients("libraw1394-11");
        graph.markPackageAndItsClients("libfaad0");
        graph.markPackageAndItsClients("libfaad2");
        graph.markPackageAndItsClients("libao-common");
        graph.markPackageAndItsClients("libwavpack1");
        graph.markPackageAndItsClients("libmpeg2-4");
        graph.markPackageAndItsClients("libsidplay1");
        graph.markPackageAndItsClients("libmatroska2");
        graph.markPackageAndItsClients("libsdl1.2debian");
        graph.markPackageAndItsClients("libmodplug1");
        graph.markPackageAndItsClients("libpostproc-dev");
        graph.markPackageAndItsClients("libavutil-dev");
        graph.markPackageAndItsClients("ffmpeg-doc");
        graph.markPackageAndItsClients("libsphinx2-dev");
        graph.markPackageAndItsClients("libmusicbrainz3-dev");
        graph.markPackageAndItsClients("libmusicbrainz4-dev");
        graph.markPackageAndItsClients("libmpeg4ip-dev");
        graph.markPackageAndItsClients("cmus-plugin-ffmpeg");
        graph.markPackageAndItsClients("librplay3");
        graph.markPackageAndItsClients("libportaudio0");
        graph.markPackageAndItsClients("libffado2");
        graph.markPackageAndItsClients("libiec61883-0");
        graph.markPackageAndItsClients("libavc1394-0");
        graph.markPackageAndItsClients("libzita-convolver2");
        graph.markPackageAndItsClients("vorbistools");
        graph.markPackageAndItsClients("libvamp-sdk2");
        graph.markPackageAndItsClients("libsdl-mixer1.2");
        graph.markPackageAndItsClients("timidity");
        graph.markPackageAndItsClients("mplayer-skins");
        graph.markPackageAndItsClients("timidity-daemon");
        graph.markPackageAndItsClients("libvisual-0.4-0");
        graph.markPackageAndItsClients("libvisual-0.4-plugins");
        graph.markPackageAndItsClients("libvisual-0.4-plugins:amd64");
        graph.markPackageAndItsClients("gpe-soundserver");
        graph.markPackageAndItsClients("pulseaudio-utils");
        graph.markPackageAndItsClients("libdmapsharing-3.0-2");
        graph.markPackageAndItsClients("libbdplus0"); // Blueray
        graph.markPackageAndItsClients("libudfread0"); // CDROM/ DVD Universal Disk Format (fs)
        graph.markPackageAndItsClients("libcodec2-1.0");
        graph.markPackageAndItsClients("libva2"); // Video Acceleration for linux
        graph.markPackageAndItsClients("libsoxr0"); // Sound eXchange 1d signal library
        graph.markPackageAndItsDependencies("libdvdread8");
        graph.markPackageAndItsDependencies("libv4l2rds0");
        graph.markPackageAndItsDependencies("libopenal-data");
        graph.markPackageAndItsDependencies("libfftw3-single3");
        graph.markPackageAndItsDependencies("libfftw3-double3");
        graph.markPackageAndItsDependencies("libde265-0");
        graph.markPackageAndItsDependencies("libasound2-data");
    }

    void markJava()
    {
        graph.markPackageAndItsClients("java-common");
        graph.markPackageAndItsClients("ca-certificates-java");
        graph.markPackageAndItsClients("tzdata-java");
        graph.markPackageAndItsClients("openjdk-7-jre-lib");
        graph.markPackageAndItsClients("icedtea-7-jre-lib");
        graph.markPackageAndItsClients("icedtea-7-jre-jamvm");
        graph.markPackageAndItsClients("icedtea-7-jre-cacao");
        graph.markPackageAndItsClients("libatk-wrapper-java");
    }

    void markX11()
    {
        graph.markPackageAndItsClients("x11-common");   // X11
        graph.markPackageAndItsClients("libxau6");
        graph.markPackageAndItsClients("libxdmcp6");
        graph.markPackageAndItsClients("libx11-6");
        graph.markPackageAndItsClients("libx11-doc");
        graph.markPackageAndItsClients("libx11-data");
        graph.markPackageAndItsClients("libglitz1");
        graph.markPackageAndItsClients("libgl1-mesa-dri");
        graph.markPackageAndItsClients("xbitmaps");
        //graph.markPackageAndItsClients("xkb-data"); // Should be part of X? or just inherited console...
        graph.markPackageAndItsClients("xorg-docs-core");
        graph.markPackageAndItsClients("libx11-xcb1");
        graph.markPackageAndItsClients("xcursor-themes");
        graph.markPackageAndItsClients("xorg-sgml-doctools");

        graph.markPackageAndItsClients("latex-xft-fonts");
        graph.markPackageAndItsClients("libxfont1");
        graph.markPackageAndItsClients("libxfont2");
        graph.markPackageAndItsClients("xfonts-mona");
        graph.markPackageAndItsClients("x11proto-randr-dev");
        graph.markPackageAndItsClients("x11proto-record-dev");
        graph.markPackageAndItsClients("x11proto-video-dev");
        graph.markPackageAndItsClients("x11proto-xinerama-dev"); 
        graph.markPackageAndItsClients("x11proto-xf86misc-dev");
        graph.markPackageAndItsClients("x11proto-core-dev");
        graph.markPackageAndItsClients("libdrm-dev");

        graph.markPackageAndItsClients("libglapi-mesa");
        graph.markPackageAndItsClients("libglvnd0");
        graph.markPackageAndItsClients("libdrm-common");
        graph.markPackageAndItsClients("libxshmfence1");
    }

    void markOpenGL() {
        graph.markPackageAndItsClients("libgl1-mesa-dri"); // OpenGL
        graph.markPackageAndItsClients("libgl1-mesa-glx");
	graph.markPackageAndItsClients("libdrm-common");
	graph.markPackageAndItsClients("libxcb-glx0");
	graph.markPackageAndItsClients("libglapi-mesa");
	graph.markPackageAndItsClients("libglvnd0");
	graph.markPackageAndItsClients("libvdpau1"); // Digital video layer on GPU (codecs)
    }
    
    void markDevel()
    {
        // Basic devel
        graph.markPackageAndItsClients("binutils");
        graph.markPackageAndItsClients("binutils-common");
        graph.markPackageAndItsClients("strace");
        graph.markPackageAndItsClients("ltrace");
        graph.markPackageAndItsClients("libopts25-dev");
        graph.markPackageAndItsClients("libibverbs-dev");
        graph.markPackageAndItsClients("libzzip-dev");
        graph.markPackageAndItsClients("libpthread-stubs0-dev");
        graph.markPackageAndItsClients("libc-dev-bin");
        graph.markPackageAndItsClients("libcppunit-dev");
        graph.markPackageAndItsClients("libcppunit-1.12-1");
        graph.markPackageAndItsClients("libc6-dbg");
        graph.markPackageAndItsClients("libc6-pic");
        graph.markPackageAndItsClients("libc6-prof");
        graph.markPackageAndItsClients("libstdc++6-4.4-doc");
        graph.markPackageAndItsClients("libgc-dev");
        graph.markPackageAndItsClients("libgcc-6-dev");
        graph.markPackageAndItsClients("libgcc-7-dev");
        graph.markPackageAndItsClients("libgcc-8-dev");
        graph.markPackageAndItsClients("lib32gcc-7-dev");
        graph.markPackageAndItsClients("libx32gcc-7-dev");
        graph.markPackageAndItsClients("libgcc1-dbg");
        graph.markPackageAndItsClients("autotools-dev");
        graph.markPackageAndItsClients("manpages");
        graph.markPackageAndItsClients("linux-libc-dev");
        graph.markPackageAndItsClients("m4");
        graph.markPackageAndItsClients("cvs");
        graph.markPackageAndItsClients("libopagent1");
        graph.markPackageAndItsClients("glibc-doc-reference");
        graph.markPackageAndItsClients("glibc-source");
        graph.markPackageAndItsClients("libbison-dev");
        graph.markPackageAndItsClients("make");
        graph.markPackageAndItsClients("gdb");
        graph.markPackageAndItsClients("git");
        graph.markPackageAndItsClients("git-man");
        graph.markPackageAndItsClients("patch");
        graph.markPackageAndItsClients("gdbserver");

        // mingw
        graph.markPackageAndItsClients("mingw-w64-dev");
        graph.markPackageAndItsClients("mingw-w64-common");
        graph.markPackageAndItsClients("mingw-w64-x86-64-dev");
        graph.markPackageAndItsClients("gcc-mingw-w64-base");

        // Compress
        graph.markPackageAndItsClients("libdeflate-dev");
        graph.markPackageAndItsClients("liblzma-dev");
        graph.markPackageAndItsClients("libzstd-dev");
        graph.markPackageAndItsClients("libminizip-dev");

        // Crypt
        graph.markPackageAndItsClients("libgpg-error-mingw-w64-dev");
        graph.markPackageAndItsClients("libgpg-error-dev");
        graph.markPackageAndItsClients("libcrack2-dev");
        graph.markPackageAndItsClients("libssl-dev");
        graph.markPackageAndItsClients("libcrypt-dev");

        // Multimedia
        graph.markPackageAndItsClients("libv4l-dev");
        graph.markPackageAndItsClients("libwebp-dev");
        graph.markPackageAndItsClients("libavutil-dev");
        graph.markPackageAndItsClients("libraw1394-dev");
        graph.markPackageAndItsClients("libaudiofile-dev:amd64");
        graph.markPackageAndItsClients("libjpeg-turbo8-dev");
        graph.markPackageAndItsClients("liblcms2-dev");
        graph.markPackageAndItsClients("libgif-dev");
        graph.markPackageAndItsClients("libjpeg62-dev");
        graph.markPackageAndItsClients("libjbig-dev");
        graph.markPackageAndItsClients("libpixman-1-dev");
        graph.markPackageAndItsClients("libchromaprint-dev");
        graph.markPackageAndItsClients("libasound2-dev");
        graph.markPackageAndItsClients("libopus-dev");
        graph.markPackageAndItsClients("libsndio-dev");
        graph.markPackageAndItsClients("libsoxr-dev");
        graph.markPackageAndItsClients("libspeex-dev");
        graph.markPackageAndItsClients("libx264-dev");
        graph.markPackageAndItsClients("libx265-dev");
        graph.markPackageAndItsClients("libmpg123-dev");
        graph.markPackageAndItsClients("libogg-dev");
        graph.markPackageAndItsClients("libavc1394-dev");
        graph.markPackageAndItsClients("libblosc-dev");	
        graph.markPackageAndItsClients("libcfitsio-dev");
        graph.markPackageAndItsClients("libaom-dev");
        graph.markPackageAndItsClients("libdav1d-dev");
        graph.markPackageAndItsClients("libde265-dev");
        graph.markPackageAndItsClients("unixodbc-dev");
        graph.markPackageAndItsClients("libtirpc-dev");
        graph.markPackageAndItsClients("liborc-0.4-dev-bin");

        // GDAL
        graph.markPackageAndItsClients("librttopo-dev");

        // Graphics and fonts
        graph.markPackageAndItsClients("libsqlite3-dev");
        graph.markPackageAndItsClients("libbsd-dev");
        graph.markPackageAndItsClients("libtinfo-dev");
        graph.markPackageAndItsClients("libart-2.0-dev");
        graph.markPackageAndItsClients("libfreeimage-dev");
        graph.markPackageAndItsClients("libgl2ps-dev");
        graph.markPackageAndItsClients("libnetpbm10-dev");
        graph.markPackageAndItsClients("libilmbase-dev");
        graph.markPackageAndItsClients("liblcms1-dev");
        graph.markPackageAndItsClients("libgraphite2-dev");

        // Languages
        graph.markPackageAndItsClients("libgcc-13-dev");
        graph.markPackageAndItsClients("libgcc-14-dev");
        graph.markPackageAndItsClients("yasm");
        graph.markPackageAndItsClients("nasm");
        graph.markPackageAndItsClients("libx32gcc-13-dev");
        graph.markPackageAndItsClients("lib32gcc-13-dev");
        graph.markPackageAndItsClients("cernlib-base-dev");
        graph.markPackageAndItsClients("tcl8.5-dev");
        graph.markPackageAndItsClients("libffi-dev");
        graph.markPackageAndItsClients("libgmp-dev");
        graph.markPackageAndItsClients("libfindlib-ocaml-dev");
        graph.markPackageAndItsClients("libboost1.46-dev");
        graph.markPackageAndItsClients("python3-all");
        graph.markPackageAndItsClients("python-all");
        graph.markPackageAndItsClients("golang-1.10-src");
        graph.markPackageAndItsClients("gem2deb-test-runner");
        graph.markPackageAndItsClients("openjdk-11-jdk");
        graph.markPackageAndItsClients("openjdk-11-jdk-headless");
        graph.markPackageAndItsClients("libomp-18-dev");

        // X11
        graph.markPackageAndItsClients("x11proto-core-dev");
        graph.markPackageAndItsClients("x11proto-print-dev");
        graph.markPackageAndItsClients("x11proto-record-dev");
        graph.markPackageAndItsClients("x11proto-video-dev");
        graph.markPackageAndItsClients("libjasper-dev");
        graph.markPackageAndItsClients("libdrm-dev");
        graph.markPackageAndItsClients("xtrans-dev");
        graph.markPackageAndItsClients("x11proto-kb-dev");
        graph.markPackageAndItsClients("cmake-data");
        graph.markPackageAndItsClients("x11proto-xinerama-dev");
        graph.markPackageAndItsClients("x11proto-dri2-dev");
        graph.markPackageAndItsClients("x11proto-gl-dev");
        graph.markPackageAndItsClients("x11proto-dmx-dev");
        graph.markPackageAndItsClients("x11proto-xf86vidmode-dev");
        graph.markPackageAndItsClients("x11proto-randr-dev");
        graph.markPackageAndItsClients("libdjvulibre-dev");
        graph.markPackageAndItsClients("libfftw3-dev");
        graph.markPackageAndItsClients("xutils-dev");
        graph.markPackageAndItsClients("libdbus-1-dev");
        graph.markPackageAndItsClients("x11proto-xf86misc-dev");
        graph.markPackageAndItsClients("x11proto-dev");
        graph.markPackageAndItsClients("libxkbcommon-dev");
        graph.markPackageAndItsClients("libxcb-composite0-dev");

        // X11 related
        graph.markPackageAndItsClients("libopengl-dev");
        graph.markPackageAndItsClients("libvulkan-dev");
        graph.markPackageAndItsClients("pvm-dev");
        graph.markPackageAndItsClients("libois-dev");
        graph.markPackageAndItsClients("libavahi-common-dev");
        graph.markPackageAndItsClients("libm17n-dev");
        graph.markPackageAndItsClients("libdevmapper-dev");
        graph.markPackageAndItsClients("libspectre-dev");
        graph.markPackageAndItsClients("libgii1-dev");
        graph.markPackageAndItsClients("libopenthreads-dev");
        graph.markPackageAndItsClients("libwayland-dev");
        graph.markPackageAndItsClients("libxshmfence-dev");
        graph.markPackageAndItsClients("libglvnd-core-dev");

        // Linux kernel
        graph.markPackageAndItsClients("linux-headers-5.4.74-custom");
        graph.markPackageAndItsClients("linux-headers-6.8.0-62");
	
	// Other
        graph.markPackageAndItsClients("libmkl-computational-dev");
        graph.markPackageAndItsClients("libmkl-interface-dev");
        graph.markPackageAndItsClients("libmkl-threading-dev");
        graph.markPackageAndItsClients("libevent-dev");
        graph.markPackageAndItsClients("libnl-3-dev");
        graph.markPackageAndItsClients("libsource-highlight-common");
        graph.markPackageAndItsClients("libdebuginfod1t64");
        graph.markPackageAndItsClients("libdebuginfod-common");
        graph.markPackageAndItsClients("libpcre2-dev");
        graph.markPackageAndItsClients("libsepol-dev");
        graph.markPackageAndItsClients("libglm-dev");
        graph.markPackageAndItsClients("libgbm-dev");
        graph.markPackageAndItsClients("libboost1.49-dev");
        graph.markPackageAndItsClients("libexttextcat-dev");
        graph.markPackageAndItsClients("libhunspell-dev:amd64");
        graph.markPackageAndItsClients("libhyphen-dev");
        graph.markPackageAndItsClients("libmdds-dev");
        graph.markPackageAndItsClients("libmysqlcppconn-dev");
        graph.markPackageAndItsClients("libpoppler-cpp-dev");
        graph.markPackageAndItsClients("libpoppler-private-dev");
        graph.markPackageAndItsClients("libwpd-dev");
        graph.markPackageAndItsClients("libqt4-dev-bin");
        graph.markPackageAndItsClients("libboost-dev");
        graph.markPackageAndItsClients("libboost1.65-dev");
        graph.markPackageAndItsClients("libisl-dev");
        graph.markPackageAndItsClients("libsepol1-dev");
        graph.markPackageAndItsClients("libjson-c-dev");
        graph.markPackageAndItsClients("libaec-dev");
        graph.markPackageAndItsClients("libaudit-dev");
        graph.markPackageAndItsClients("libudev-dev");
        graph.markPackageAndItsClients("libdb5.3-dev");
        graph.markPackageAndItsClients("libidb2-dev");
        graph.markPackageAndItsClients("libsystemd-dev");
        graph.markPackageAndItsClients("libcap-dev");
        graph.markPackageAndItsClients("libcap-ng-dev");
        graph.markPackageAndItsClients("libleptonica-dev");
        graph.markPackageAndItsClients("libevdev-dev");
        graph.markPackageAndItsClients("libmtdev-dev");
        graph.markPackageAndItsClients("libopenjp2-7-dev");
        graph.markPackageAndItsClients("libtasn1-6-dev");
        graph.markPackageAndItsClients("libidn2-0-dev");
        graph.markPackageAndItsClients("libidn2-dev");
        graph.markPackageAndItsClients("nettle-dev");
        graph.markPackageAndItsClients("libp11-kit-dev");
        graph.markPackageAndItsClients("man2html-base");
        graph.markPackageAndItsClients("libassuan-dev");
        graph.markPackageAndItsClients("libassuan-mingw-w64-dev");
        graph.markPackageAndItsClients("libcrystalhd-dev");
        graph.markPackageAndItsClients("libcunit1-dev");
        graph.markPackageAndItsClients("libfribidi-dev");
        graph.markPackageAndItsClients("libksba-dev");
        graph.markPackageAndItsClients("libksba-mingw-w64-dev");
        graph.markPackageAndItsClients("libnpth-mingw-w64-dev");
        graph.markPackageAndItsClients("libldap2-dev");
        graph.markPackageAndItsClients("libnpth0-dev");
        graph.markPackageAndItsClients("libnpth0-mingw-w64-dev");
        graph.markPackageAndItsClients("libomxil-bellagio-dev");
        graph.markPackageAndItsClients("libusb-1.0-0-dev");
        graph.markPackageAndItsClients("libz-mingw-w64-dev");
        graph.markPackageAndItsClients("libwrap0-dev");
        graph.markPackageAndItsClients("debootstrap");
        graph.markPackageAndItsClients("doxygen");
        graph.markPackageAndItsClients("libfakeroot");
        graph.markPackageAndItsClients("gperf");
        graph.markPackageAndItsClients("libblas-dev");
        graph.markPackageAndItsClients("libepoxy-dev");
        graph.markPackageAndItsClients("libltdl-dev");
        graph.markPackageAndItsClients("libqhull-dev");
        graph.markPackageAndItsClients("libpq-dev");
        graph.markPackageAndItsClients("libproj-dev");
        graph.markPackageAndItsClients("libcurl4-gnutls-dev");
        graph.markPackageAndItsClients("libgflags-dev");
        graph.markPackageAndItsClients("libgeos-dev");
        graph.markPackageAndItsClients("libepsilon-dev");
        graph.markPackageAndItsClients("libatlas-base-dev");
        graph.markPackageAndItsClients("libpoppler-dev");
        graph.markPackageAndItsClients("liburiparser-dev");
        graph.markPackageAndItsClients("libfreexl-dev");
        graph.markPackageAndItsClients("libfyba-dev");
        graph.markPackageAndItsClients("systemtap-sdt-dev");
        graph.markPackageAndItsClients("libsvn1");
        graph.markPackageAndItsClients("libpciaccess-dev");
        graph.markPackageAndItsClients("liblmdb-dev");
        graph.markPackageAndItsClients("");
    }

    void markKDE()
    {
        graph.markPackageAndItsClients("libkdecore5"); // KDE

        graph.markPackageAndItsClients("k3b-data");
        graph.markPackageAndItsClients("kalzium-data");
        graph.markPackageAndItsClients("kate-data");
        graph.markPackageAndItsClients("katepart");
        graph.markPackageAndItsClients("kde-icons-crystal");
        graph.markPackageAndItsClients("kde-icons-kneu");
        graph.markPackageAndItsClients("kde-icons-mono");
        graph.markPackageAndItsClients("kde-icons-noia");
        graph.markPackageAndItsClients("kde-icons-nuvola");
        graph.markPackageAndItsClients("kde-runtime");
        graph.markPackageAndItsClients("kde-runtime-data");
        graph.markPackageAndItsClients("kde-wallpapers-default");
        graph.markPackageAndItsClients("kde-workspace-kgreet-plugins");
        graph.markPackageAndItsClients("kdeartwork-emoticons");
        graph.markPackageAndItsClients("kdeartwork-theme-icon");
        graph.markPackageAndItsClients("kdebase-data");
        graph.markPackageAndItsClients("kdebase-runtime");
        graph.markPackageAndItsClients("kdebase-runtime-data");
        graph.markPackageAndItsClients("kdebase-workspace-data");
        graph.markPackageAndItsClients("kdebase-workspace-wallpapers");
        graph.markPackageAndItsClients("kdeedu-kvtml-data");
        graph.markPackageAndItsClients("kdegames-card-data");
        graph.markPackageAndItsClients("kdegames-mahjongg-data");
        graph.markPackageAndItsClients("kdegraphics-libs-data");
        graph.markPackageAndItsClients("kdegraphics-strigi-analyzer");
        graph.markPackageAndItsClients("kdelibs-bin");
        graph.markPackageAndItsClients("kdelibs5-data");
        graph.markPackageAndItsClients("kdelibs5-plugins");
        graph.markPackageAndItsClients("kdewallpapers");
        graph.markPackageAndItsClients("kdm");
        graph.markPackageAndItsClients("kdoctools");
        graph.markPackageAndItsClients("kgamma");
        graph.markPackageAndItsClients("kgeography-data");
        graph.markPackageAndItsClients("klettres-data");
        graph.markPackageAndItsClients("kstars-data");
        graph.markPackageAndItsClients("ksysguardd");
        graph.markPackageAndItsClients("kubuntu-debug-installer");
        graph.markPackageAndItsClients("libkatepartinterfaces4");
        graph.markPackageAndItsClients("libkcmutils4");
        graph.markPackageAndItsClients("libkdcraw-data");
        graph.markPackageAndItsClients("libkde3support4");
        graph.markPackageAndItsClients("libkdeedu-data");
        graph.markPackageAndItsClients("libkdesu5");
        graph.markPackageAndItsClients("libkdesu5q");
        graph.markPackageAndItsClients("libkdeui5");
        graph.markPackageAndItsClients("libkdewebkit5");
        graph.markPackageAndItsClients("libkdnssd4");
        graph.markPackageAndItsClients("libkemoticons4");
        graph.markPackageAndItsClients("libkexiv2-data");
        graph.markPackageAndItsClients("libkfile4");
        graph.markPackageAndItsClients("libkhtml5");
        graph.markPackageAndItsClients("libkidletime4");
        graph.markPackageAndItsClients("libkio5");
        graph.markPackageAndItsClients("libkjsapi4");
        graph.markPackageAndItsClients("libkjsembed4");
        graph.markPackageAndItsClients("libkmediaplayer4");
        graph.markPackageAndItsClients("libknewstuff3-4");
        graph.markPackageAndItsClients("libknotifyconfig4");
        graph.markPackageAndItsClients("libkntlm4");
        graph.markPackageAndItsClients("libkonq5-templates");
        graph.markPackageAndItsClients("libkparts4");
        graph.markPackageAndItsClients("libkpty4");
        graph.markPackageAndItsClients("libkrosscore4");
        graph.markPackageAndItsClients("libktexteditor4");
        graph.markPackageAndItsClients("libkworkspace4");
        graph.markPackageAndItsClients("libnepomuk4");
        graph.markPackageAndItsClients("libnepomukquery4a");
        graph.markPackageAndItsClients("libnepomukutils4");
        graph.markPackageAndItsClients("libplasma3");
        graph.markPackageAndItsClients("marble-data");
        graph.markPackageAndItsClients("parley-data");
        graph.markPackageAndItsClients("pkg-kde-tools");
        graph.markPackageAndItsClients("plasma-scriptengine-javascript");
        graph.markPackageAndItsClients("polkit-kde-1");
        graph.markPackageAndItsClients("qapt-batch");

        graph.markPackageAndItsClients("tagua-data");
        graph.markPackageAndItsClients("plasma-widget-kimpanel-backend-ibus");
        graph.markPackageAndItsClients("kubuntu-docs");
        graph.markPackageAndItsClients("kubuntu-netbook-default-settings");
        graph.markPackageAndItsClients("kubuntu-web-shortcuts");
        graph.markPackageAndItsClients("kubuntu-default-settings");
        graph.markPackageAndItsClients("kde-baseapps-data");
        graph.markPackageAndItsClients("kdeartwork-dbg");
        graph.markPackageAndItsClients("kdepim-doc");
        graph.markPackageAndItsClients("kdepim-runtime-dbg");
        graph.markPackageAndItsClients("python-kde4-doc");
        graph.markPackageAndItsClients("willowng-config-kde");
        graph.markPackageAndItsClients("xsettings-kde");
        graph.markPackageAndItsClients("quassel-data");

        graph.markPackageAndItsClients("language-pack-kde-aa");
        graph.markPackageAndItsClients("language-pack-kde-aa-base");
        graph.markPackageAndItsClients("language-pack-kde-af-base");
        graph.markPackageAndItsClients("language-pack-kde-am");
        graph.markPackageAndItsClients("language-pack-kde-am-base");
        graph.markPackageAndItsClients("language-pack-kde-ar-base");
        graph.markPackageAndItsClients("language-pack-kde-as");
        graph.markPackageAndItsClients("language-pack-kde-as-base");
        graph.markPackageAndItsClients("language-pack-kde-ast");
        graph.markPackageAndItsClients("language-pack-kde-ast-base");
        graph.markPackageAndItsClients("language-pack-kde-az-base");
        graph.markPackageAndItsClients("language-pack-kde-be-base");
        graph.markPackageAndItsClients("language-pack-kde-bg-base");
        graph.markPackageAndItsClients("language-pack-kde-bn-base");
        graph.markPackageAndItsClients("language-pack-kde-bo");
        graph.markPackageAndItsClients("language-pack-kde-bo-base");
        graph.markPackageAndItsClients("language-pack-kde-br-base");
        graph.markPackageAndItsClients("language-pack-kde-bs-base");
        graph.markPackageAndItsClients("language-pack-kde-ca-base");
        graph.markPackageAndItsClients("language-pack-kde-crh");
        graph.markPackageAndItsClients("language-pack-kde-crh-base");
        graph.markPackageAndItsClients("language-pack-kde-cs-base");
        graph.markPackageAndItsClients("language-pack-kde-csb-base");
        graph.markPackageAndItsClients("language-pack-kde-cy-base");
        graph.markPackageAndItsClients("language-pack-kde-da-base");
        graph.markPackageAndItsClients("language-pack-kde-de-base");
        graph.markPackageAndItsClients("language-pack-kde-dv");
        graph.markPackageAndItsClients("language-pack-kde-dv-base");
        graph.markPackageAndItsClients("language-pack-kde-el-base");
        graph.markPackageAndItsClients("language-pack-kde-en-base");
        graph.markPackageAndItsClients("language-pack-kde-eo-base");
        graph.markPackageAndItsClients("language-pack-kde-es-base");
        graph.markPackageAndItsClients("language-pack-kde-et-base");
        graph.markPackageAndItsClients("language-pack-kde-eu-base");
        graph.markPackageAndItsClients("language-pack-kde-fa-base");
        graph.markPackageAndItsClients("language-pack-kde-fi-base");
        graph.markPackageAndItsClients("language-pack-kde-fil");
        graph.markPackageAndItsClients("language-pack-kde-fil-base");
        graph.markPackageAndItsClients("language-pack-kde-fo");
        graph.markPackageAndItsClients("language-pack-kde-fo-base");
        graph.markPackageAndItsClients("language-pack-kde-fr-base");
        graph.markPackageAndItsClients("language-pack-kde-fur");
        graph.markPackageAndItsClients("language-pack-kde-fur-base");
        graph.markPackageAndItsClients("language-pack-kde-fy-base");
        graph.markPackageAndItsClients("language-pack-kde-ga-base");
        graph.markPackageAndItsClients("language-pack-kde-gd");
        graph.markPackageAndItsClients("language-pack-kde-gd-base");
        graph.markPackageAndItsClients("language-pack-kde-gl-base");
        graph.markPackageAndItsClients("language-pack-kde-gu-base");
        graph.markPackageAndItsClients("language-pack-kde-gv");
        graph.markPackageAndItsClients("language-pack-kde-gv-base");
        graph.markPackageAndItsClients("language-pack-kde-ha");
        graph.markPackageAndItsClients("language-pack-kde-ha-base");
        graph.markPackageAndItsClients("language-pack-kde-he-base");
        graph.markPackageAndItsClients("language-pack-kde-hi-base");
        graph.markPackageAndItsClients("language-pack-kde-hne");
        graph.markPackageAndItsClients("language-pack-kde-hne-base");
        graph.markPackageAndItsClients("language-pack-kde-hr-base");
        graph.markPackageAndItsClients("language-pack-kde-hsb");
        graph.markPackageAndItsClients("language-pack-kde-hsb-base");
        graph.markPackageAndItsClients("language-pack-kde-hu-base");
        graph.markPackageAndItsClients("language-pack-kde-hy");
        graph.markPackageAndItsClients("language-pack-kde-hy-base");
        graph.markPackageAndItsClients("language-pack-kde-ia");
        graph.markPackageAndItsClients("language-pack-kde-ia-base");
        graph.markPackageAndItsClients("language-pack-kde-id-base");
        graph.markPackageAndItsClients("language-pack-kde-is-base");
        graph.markPackageAndItsClients("language-pack-kde-it-base");
        graph.markPackageAndItsClients("language-pack-kde-ja-base");
        graph.markPackageAndItsClients("language-pack-kde-ka");
        graph.markPackageAndItsClients("language-pack-kde-ka-base");
        graph.markPackageAndItsClients("language-pack-kde-kk-base");
        graph.markPackageAndItsClients("language-pack-kde-kl");
        graph.markPackageAndItsClients("language-pack-kde-kl-base");
        graph.markPackageAndItsClients("language-pack-kde-km-base");
        graph.markPackageAndItsClients("language-pack-kde-kn-base");
        graph.markPackageAndItsClients("language-pack-kde-ko-base");
        graph.markPackageAndItsClients("language-pack-kde-ku");
        graph.markPackageAndItsClients("language-pack-kde-ku-base");
        graph.markPackageAndItsClients("language-pack-kde-kw");
        graph.markPackageAndItsClients("language-pack-kde-kw-base");
        graph.markPackageAndItsClients("language-pack-kde-la");
        graph.markPackageAndItsClients("language-pack-kde-la-base");
        graph.markPackageAndItsClients("language-pack-kde-lb");
        graph.markPackageAndItsClients("language-pack-kde-lb-base");
        graph.markPackageAndItsClients("language-pack-kde-lo");
        graph.markPackageAndItsClients("language-pack-kde-lo-base");
        graph.markPackageAndItsClients("language-pack-kde-lt-base");
        graph.markPackageAndItsClients("language-pack-kde-lv-base");
        graph.markPackageAndItsClients("language-pack-kde-mai-base");
        graph.markPackageAndItsClients("language-pack-kde-mg");
        graph.markPackageAndItsClients("language-pack-kde-mg-base");
        graph.markPackageAndItsClients("language-pack-kde-mk-base");
        graph.markPackageAndItsClients("language-pack-kde-ml-base");
        graph.markPackageAndItsClients("language-pack-kde-mn-base");
        graph.markPackageAndItsClients("language-pack-kde-mr");
        graph.markPackageAndItsClients("language-pack-kde-mr-base");
        graph.markPackageAndItsClients("language-pack-kde-ms-base");
        graph.markPackageAndItsClients("language-pack-kde-mt");
        graph.markPackageAndItsClients("language-pack-kde-mt-base");
        graph.markPackageAndItsClients("language-pack-kde-nb-base");
        graph.markPackageAndItsClients("language-pack-kde-nds-base");
        graph.markPackageAndItsClients("language-pack-kde-ne");
        graph.markPackageAndItsClients("language-pack-kde-ne-base");
        graph.markPackageAndItsClients("language-pack-kde-nl-base");
        graph.markPackageAndItsClients("language-pack-kde-nn-base");
        graph.markPackageAndItsClients("language-pack-kde-oc");
        graph.markPackageAndItsClients("language-pack-kde-oc-base");
        graph.markPackageAndItsClients("language-pack-kde-om");
        graph.markPackageAndItsClients("language-pack-kde-om-base");
        graph.markPackageAndItsClients("language-pack-kde-or");
        graph.markPackageAndItsClients("language-pack-kde-or-base");
        graph.markPackageAndItsClients("language-pack-kde-pa-base");
        graph.markPackageAndItsClients("language-pack-kde-pl-base");
        graph.markPackageAndItsClients("language-pack-kde-ps");
        graph.markPackageAndItsClients("language-pack-kde-ps-base");
        graph.markPackageAndItsClients("language-pack-kde-pt-base");
        graph.markPackageAndItsClients("language-pack-kde-ro-base");
        graph.markPackageAndItsClients("language-pack-kde-ru-base");
        graph.markPackageAndItsClients("language-pack-kde-rw-base");
        graph.markPackageAndItsClients("language-pack-kde-sd");
        graph.markPackageAndItsClients("language-pack-kde-sd-base");
        graph.markPackageAndItsClients("language-pack-kde-se-base");
        graph.markPackageAndItsClients("language-pack-kde-si-base");
        graph.markPackageAndItsClients("language-pack-kde-sk-base");
        graph.markPackageAndItsClients("language-pack-kde-sl-base");
        graph.markPackageAndItsClients("language-pack-kde-sq");
        graph.markPackageAndItsClients("language-pack-kde-sq-base");
        graph.markPackageAndItsClients("language-pack-kde-sr-base");
        graph.markPackageAndItsClients("language-pack-kde-ss-base");
        graph.markPackageAndItsClients("language-pack-kde-st");
        graph.markPackageAndItsClients("language-pack-kde-st-base");
        graph.markPackageAndItsClients("language-pack-kde-sv-base");
        graph.markPackageAndItsClients("language-pack-kde-sw");
        graph.markPackageAndItsClients("language-pack-kde-sw-base");
        graph.markPackageAndItsClients("language-pack-kde-ta-base");
        graph.markPackageAndItsClients("language-pack-kde-te-base");
        graph.markPackageAndItsClients("language-pack-kde-tg-base");
        graph.markPackageAndItsClients("language-pack-kde-th-base");
        graph.markPackageAndItsClients("language-pack-kde-tl");
        graph.markPackageAndItsClients("language-pack-kde-tl-base");
        graph.markPackageAndItsClients("language-pack-kde-tlh");
        graph.markPackageAndItsClients("language-pack-kde-tlh-base");
        graph.markPackageAndItsClients("language-pack-kde-tr-base");
        graph.markPackageAndItsClients("language-pack-kde-tt");
        graph.markPackageAndItsClients("language-pack-kde-tt-base");
        graph.markPackageAndItsClients("language-pack-kde-ug");
        graph.markPackageAndItsClients("language-pack-kde-ug-base");
        graph.markPackageAndItsClients("language-pack-kde-uk-base");
        graph.markPackageAndItsClients("language-pack-kde-ur");
        graph.markPackageAndItsClients("language-pack-kde-ur-base");
        graph.markPackageAndItsClients("language-pack-kde-uz-base");
        graph.markPackageAndItsClients("language-pack-kde-vi");
        graph.markPackageAndItsClients("language-pack-kde-vi-base");
        graph.markPackageAndItsClients("language-pack-kde-wa-base");
        graph.markPackageAndItsClients("language-pack-kde-xh");
        graph.markPackageAndItsClients("language-pack-kde-xh-base");
        graph.markPackageAndItsClients("language-pack-kde-zh-base");
        graph.markPackageAndItsClients("language-pack-kde-zh-hans-base");
        graph.markPackageAndItsClients("language-pack-kde-zh-hant-base");
    }

    void markGnome()
    {
        // Gnome core
        graph.markPackageAndItsClients("libgnome-keyring0");
        graph.markPackageAndItsClients("policykit-1-gnome");
        graph.markPackageAndItsClients("libgnome-autoar-0-0");
        graph.markPackageAndItsClients("libunity-protocol-private0");
        graph.markPackageAndItsClients("gnome-keyring");
        graph.markPackageAndItsClients("libindicator3-7");
        graph.markPackageAndItsClients("libunity-scopes-json-def-desktop");
        graph.markPackageAndItsClients("pinentry-gnome3");
        graph.markPackageAndItsClients("gcr");
        graph.markPackageAndItsClients("libgnome-games-support-common");
        graph.markPackageAndItsClients("libgnome-panel0");
        graph.markPackageAndItsClients("gnome-desktop3-data");
        graph.markPackageAndItsClients("libgnome-desktop-2-17");
        graph.markPackageAndItsClients("libgnome-desktop-3-2");
        graph.markPackageAndItsClients("libgnome-desktop-3-19");
        //graph.markPackageAndItsClients("gnome-icon-theme"); // This is GTK

        // Gnome applications (games)
	graph.markPackageAndItsClients("gnome-mahjongg");
	graph.markPackageAndItsClients("gnome-mines");
	graph.markPackageAndItsClients("gnome-sudoku");

        // Gnome applications (basic)
        graph.markPackageAndItsClients("gnome-terminal-data");
        graph.markPackageAndItsClients("gedit-common");
        graph.markPackageAndItsClients("gnome-system-log");
        graph.markPackageAndItsClients("gnome-system-monitor");
	graph.markPackageAndItsClients("transmission-common"); // Torrent downloader
        graph.markPackageAndItsClients("usb-creator-gtk");
	
        // Gnome applications (multimedia)
        graph.markPackageAndItsClients("brasero-common");
        graph.markPackageAndItsClients("celestia-common");
        graph.markPackageAndItsClients("cheese-common");
        graph.markPackageAndItsClients("evince-common");
        graph.markPackageAndItsClients("gnome-disk-utility");
        graph.markPackageAndItsClients("libnautilus-extension1a");
        graph.markPackageAndItsClients("libtotem-plparser-common");
        graph.markPackageAndItsClients("nautilus-data");
        graph.markPackageAndItsClients("pitivi");
        graph.markPackageAndItsClients("remmina-common");
        graph.markPackageAndItsClients("rhythmbox-data");
        graph.markPackageAndItsClients("totem-common");

        // Gnome applications
	graph.markPackageAndItsClients("alacarte");
        graph.markPackageAndItsClients("atomix-data");
        graph.markPackageAndItsClients("etoys-doc");
        graph.markPackageAndItsClients("evolution-indicator");
        graph.markPackageAndItsClients("gnome-accessibility-themes");
        graph.markPackageAndItsClients("gnome-backgrounds");
        graph.markPackageAndItsClients("gnome-desktop-data");
        graph.markPackageAndItsClients("gnome-desktop3-data");
        graph.markPackageAndItsClients("gnome-doc-utils");
        graph.markPackageAndItsClients("gnome-games-extra-data");
        graph.markPackageAndItsClients("gnome-menus");
        graph.markPackageAndItsClients("gnome-mime-data");
        graph.markPackageAndItsClients("gnome-nettool");
        graph.markPackageAndItsClients("gnome-orca");
        graph.markPackageAndItsClients("gnome-panel-control");
        graph.markPackageAndItsClients("gnome-power-manager");
        graph.markPackageAndItsClients("gnome-session-bin");
        graph.markPackageAndItsClients("gnome-session-canberra");
        graph.markPackageAndItsClients("gnome-session-common");
        graph.markPackageAndItsClients("gnome-settings-daemon-schemas");
        graph.markPackageAndItsClients("gnome-shell-common");
        graph.markPackageAndItsClients("gnome-themes-more");
        graph.markPackageAndItsClients("gnome-themes-selected");
        graph.markPackageAndItsClients("gnome-user-guide");
        graph.markPackageAndItsClients("gnome-utils-common");
        graph.markPackageAndItsClients("gnome-video-effects");
        graph.markPackageAndItsClients("ibus-pinyin-db-android");
        graph.markPackageAndItsClients("ibus-pinyin-db-open-phrase");
        graph.markPackageAndItsClients("libatspi1.0-0");
        graph.markPackageAndItsClients("libbonoboui2-common");
        graph.markPackageAndItsClients("libgnome-bluetooth11");
        graph.markPackageAndItsClients("libgnome-control-center1");
        graph.markPackageAndItsClients("libgnome-keyring-common");
        graph.markPackageAndItsClients("libgnome-mag2");
        graph.markPackageAndItsClients("libgnome-menu-3-0");
        graph.markPackageAndItsClients("libgnome-menu2");
        graph.markPackageAndItsClients("libgnome-speech7");
        graph.markPackageAndItsClients("libgnomecanvas2-common");
        graph.markPackageAndItsClients("libgnomecups1.0-1");
        graph.markPackageAndItsClients("libgnomekbd-common");
        graph.markPackageAndItsClients("libgnomeprint2.2-data");
        graph.markPackageAndItsClients("libgnomeui-common");
        graph.markPackageAndItsClients("libgsf-gnome-1-114");
        graph.markPackageAndItsClients("libgtksourceview-common");
        graph.markPackageAndItsClients("libgucharmap-2-90-7");
        graph.markPackageAndItsClients("libmimedir-gnome0.4");
        graph.markPackageAndItsClients("libnautilus-extension1a");
        graph.markPackageAndItsClients("libopenrawgnome1");
        graph.markPackageAndItsClients("libpam-gnome-keyring");
        graph.markPackageAndItsClients("libyelp0");
        graph.markPackageAndItsClients("sawfish-data");
        graph.markPackageAndItsClients("ssh-askpass-gnome");
        graph.markPackageAndItsClients("yelp-xsl");
        graph.markPackageAndItsClients("libgoa-backend-1.0-1");
        graph.markPackageAndItsClients("gnome-control-center-data");
        graph.markPackageAndItsClients("language-selector-gnome");
        graph.markPackageAndItsClients("gnome-settings-daemon-common");
        graph.markPackageAndItsClients("gnome-panel-data");	
        graph.markPackageAndItsClients("gnome-applets-data");	
    }

    void markPerl()
    {
        //graph.markPackageAndItsClients("perl-base");
        graph.markPackageAndItsClients("perl");
        graph.markPackageAndItsClients("perl-modules-5.26");
        graph.markPackageAndItsClients("perl-modules-5.24");
        graph.markPackageAndItsClients("libperl5.26");
        graph.markPackageAndItsClients("libperl5.24");
        graph.markPackageAndItsClients("libperl5.14");
        graph.markPackageAndItsClients("libperl5.12");
        graph.markPackageAndItsClients("libperl5.10");
        graph.markPackageAndItsClients("libperl5.8");
        graph.markPackageAndItsClients("libcompress-zlib-perl");
        graph.markPackageAndItsClients("libio-compress-base-perl");
        graph.markPackageAndItsClients("libio-compress-zlib-perl");
        graph.markPackageAndItsClients("libuuid-perl");
        graph.markPackageAndItsClients("libapt-pkg-perl");
        graph.markPackageAndItsClients("libconfig-inifiles-perl");
        graph.markPackageAndItsClients("libyaml-tiny-perl");
        graph.markPackageAndItsClients("libclass-isa-perl");
        graph.markPackageAndItsClients("liburi-perl");
        graph.markPackageAndItsClients("libio-socket-ip-perl");
        graph.markPackageAndItsClients("libunicode-collate-perl");
        graph.markPackageAndItsClients("perl-openssl-defaults");
        graph.markPackageAndItsClients("libdpkg-perl");
        graph.markPackageAndItsClients("libdynaloader-functions-perl");
        graph.markPackageAndItsClients("liberror-perl");
        graph.markPackageAndItsClients("libencode-locale-perl");
        graph.markPackageAndItsClients("libarchive-zip-perl");
        graph.markPackageAndItsClients("libtext-iconv-perl");
        graph.markPackageAndItsClients("libintl-perl");
        graph.markPackageAndItsClients("libmodule-scandeps-perl");
        graph.markPackageAndItsClients("perl-modules-5.34");
        graph.markPackageAndItsClients("libyaml-pp-perl");
        graph.markPackageAndItsClients("libipc-system-simple-perl");
    }

    void markPythonDev() {
        graph.markPackageAndItsClients("python2.6-dev");
        graph.markPackageAndItsClients("libpython2.7-dev");
        graph.markPackageAndItsClients("python3.1-dev");
        graph.markPackageAndItsClients("libpython3.12-dev");
    }

    void markPython()
    {
        // Python 2
        graph.markPackageAndItsClients("libpython2.6");
        graph.markPackageAndItsClients("dh-python");
        graph.markPackageAndItsClients("python2.6");
        graph.markPackageAndItsClients("python2.7");
        graph.markPackageAndItsClients("libpython-stdlib");
        graph.markPackageAndItsClients("libpython2.7-minimal");
        graph.markPackageAndItsClients("python-minimal");
        graph.markPackageAndItsClients("python-simpy-doc");
        graph.markPackageAndItsClients("python-pip");
        graph.markPackageAndItsClients("python-pkg-resources");
        graph.markPackageAndItsClients("python-wheel");
        graph.markPackageAndItsClients("python-dnspython");
        graph.markPackageAndItsClients("python-six");
        graph.markPackageAndItsClients("python-enum34");
        graph.markPackageAndItsClients("python-idna");
        graph.markPackageAndItsClients("python-asn1crypto");
        graph.markPackageAndItsClients("python-ipaddress");

        // Python 3
        graph.markPackageAndItsClients("python3.2-minimal");
        graph.markPackageAndItsClients("libpython3.4-minimal");
        graph.markPackageAndItsClients("libpython3.5-minimal");
        graph.markPackageAndItsClients("libpython3.12-minimal");
        graph.markPackageAndItsClients("libpython3.6-minimal");
        graph.markPackageAndItsClients("libpython3.10-minimal");
        graph.markPackageAndItsClients("python3-colorama");
        graph.markPackageAndItsClients("python3-more-itertools");
        graph.markPackageAndItsClients("python3-six");
        graph.markPackageAndItsClients("python3-pkg-resources");
        graph.markPackageAndItsClients("python3-idna");
        graph.markPackageAndItsClients("python3-certifi");
        graph.markPackageAndItsClients("python-talloc");
        graph.markPackageAndItsClients("python-dnspython");
        graph.markPackageAndItsClients("python-sphinx-rtd-theme");
        graph.markPackageAndItsClients("python-apt-common");
        graph.markPackageAndItsClients("python-pip-whl");
        graph.markPackageAndItsClients("python3-xkit");
        graph.markPackageAndItsClients("python3-roman");
        graph.markPackageAndItsClients("python3-alabaster");
        graph.markPackageAndItsClients("python-babel-localedata");
        graph.markPackageAndItsClients("python3-tz");
        graph.markPackageAndItsClients("python3-imagesize");
        graph.markPackageAndItsClients("python3-pygments");
        graph.markPackageAndItsClients("python3-lib2to3");
        graph.markPackageAndItsClients("python3-attr");
        graph.markPackageAndItsClients("python3-debconf");
        graph.markPackageAndItsClients("python3-serial");
        graph.markPackageAndItsClients("python3-jwt");
        graph.markPackageAndItsClients("python3-blinker");
        graph.markPackageAndItsClients("python3-distro");
        graph.markPackageAndItsClients("python3-pyparsing");
        graph.markPackageAndItsClients("python3-problem-report");
        graph.markPackageAndItsClients("python3-distro-info");
        graph.markPackageAndItsClients("python3-jeepney");
        graph.markPackageAndItsClients("python3-json-pointer");
        graph.markPackageAndItsClients("python3-gast");
        graph.markPackageAndItsClients("python3-decorator");
        graph.markPackageAndItsClients("python3-ply");
        graph.markPackageAndItsClients("python3-cycler");
        graph.markPackageAndItsClients("python3-mpmath");
        graph.markPackageAndItsClients("python3-xdg");
        graph.markPackageAndItsClients("python3-dnspython");
        graph.markPackageAndItsClients("python3-soupsieve");
        graph.markPackageAndItsClients("python3-appdirs");
        graph.markPackageAndItsClients("python3-defer");
        graph.markPackageAndItsClients("python3-docopt");
        graph.markPackageAndItsClients("python-matplotlib-data");
        graph.markPackageAndItsClients("python3-snowballstemmer");
        graph.markPackageAndItsClients("python3-sigmavirus24-urltemplate");
        graph.markPackageAndItsClients("python3-sgmllib3k");
        graph.markPackageAndItsClients("python3-packaging");
        graph.markPackageAndItsClients("python3-urllib3");
        graph.markPackageAndItsClients("python3-wheel");
        graph.markPackageAndItsClients("python3-tabulate");
        graph.markPackageAndItsClients("python3-wcwidth");

	markPythonDev();
    }

    void markQT()
    {
        graph.markPackageAndItsClients("libqtcore4");
        graph.markPackageAndItsClients("qt3-doc");
        graph.markPackageAndItsClients("libqt3-mt");
        graph.markPackageAndItsClients("libqtcore4:i386");
        graph.markPackageAndItsClients("libntrack-qt4-1");
        graph.markPackageAndItsClients("libpolkit-qt-1-1");
        graph.markPackageAndItsClients("libqca2");
        graph.markPackageAndItsClients("libsoprano4");
        graph.markPackageAndItsClients("soprano-daemon");
        graph.markPackageAndItsClients("qtcreator-doc");
        graph.markPackageAndItsClients("python-qt4-dev");
        graph.markPackageAndItsClients("qt4-doc");
        graph.markPackageAndItsClients("qt4-qmake");
        graph.markPackageAndItsClients("phonon");
        graph.markPackageAndItsClients("libqt5core5a");
        graph.markPackageAndItsClients("qtchooser");
        graph.markPackageAndItsClients("qtcore4-l10n");
        graph.markPackageAndItsClients("qt5-qmake-bin");
    }


    void markGstreamer() {
        graph.markPackageAndItsClients("libgstreamer1.0-0");
    }

    void markGtk()
    {
        graph.markPackageAndItsClients("libgtk2.0-0");
        graph.markPackageAndItsClients("libgtk2.0-common");
        graph.markPackageAndItsClients("libgtk-3-0");
        graph.markPackageAndItsClients("libgtk-3-doc");
        graph.markPackageAndItsClients("libgtk-3-common");
        graph.markPackageAndItsClients("gnome-icon-theme");
        graph.markPackageAndItsClients("humanity-icon-theme");
        graph.markPackageAndItsClients("hicolor-icon-theme");
        graph.markPackageAndItsClients("adawita-icon-theme-full");
        graph.markPackageAndItsClients("gtk-update-icon-cache");
        graph.markPackageAndItsClients("libdbusmenu-gtk4");
        graph.markPackageAndItsClients("libdbusmenu-gtk3-4");
        graph.markPackageAndItsClients("libgtksourceview-3.0-common");
        graph.markPackageAndItsClients("ibus-gtk");
        graph.markPackageAndItsClients("libgweather-common");
        graph.markPackageAndItsClients("libvte-2.91-common");
        graph.markPackageAndItsClients("libwmf0.2-7-gtk");

        /*
        graph.markPackageAndItsClients("libindicate-gtk3");
        graph.markPackageAndItsClients("xdg-user-dirs-gtk");
        graph.markPackageAndItsClients("libjavascriptcoregtk-1.0-0");
        graph.markPackageAndItsClients("gtk2-engines-oxygen");
        graph.markPackageAndItsClients("libwebkitgtk-3.0-common");
        graph.markPackageAndItsClients("gir1.2-javascriptcoregtk-4.0");
	*/
    }

    void markGnustep()
    {
        graph.markPackageAndItsClients("gnustep-common"); // GnuStep
        graph.markPackageAndItsClients("renaissance-doc");
        graph.markPackageAndItsClients("gnustep-make-doc");
        graph.markPackageAndItsClients("gnustep-make-doc");
        graph.markPackageAndItsClients("gnustep-gui-doc");
        graph.markPackageAndItsClients("gnustep-icons");
        graph.markPackageAndItsClients("wmaker");
    }

    void markMultiplatform(String n)
    {
        graph.anotateSingleNode(n);
        graph.anotateSingleNode(n + ":amd64");
        graph.anotateSingleNode(n + ":i386");
    }

    void
    markMediaMinimal() {
        graph.markPackageAndItsClients("libjpeg-turbo8");
        graph.markPackageAndItsClients("libjpeg9");
        graph.markPackageAndItsClients("libwebp7");
        graph.markPackageAndItsClients("libgif7");
        graph.markPackageAndItsClients("libjbig2dec0");
        graph.markPackageAndItsClients("libnetpbm10");
        graph.markPackageAndItsClients("libjbig0");
        graph.markPackageAndItsClients("libilmbase25");
        graph.markPackageAndItsClients("libpixman-1-0");
        graph.markPackageAndItsClients("liblcms2-2");
        graph.markPackageAndItsClients("libmjpegutils-2.1-0");
        graph.markPackageAndItsClients("libexif12");
        graph.markPackageAndItsClients("libpng16-16");
        graph.markPackageAndItsClients("libopenjp2-7");
    }
    
    void
    markMinimal() {
        // System can boot (Ubuntu 07.04)
        //graph.markPackageAndItsDependencies("linux-image-2.6.20-15-generic");
        //graph.markPackageAndItsDependencies("linux-image-2.6.20.3-ubuntu1-custom");
        //graph.markPackageAndItsDependencies("lilo");

        // System can boot (Ubuntu 18.04)
        graph.markPackageAndItsDependencies("grub-pc");
        graph.markPackageAndItsDependencies("init");
        graph.markPackageAndItsDependencies("sysvinit-utils");
        graph.markPackageAndItsDependencies("console-setup");
        graph.markPackageAndItsDependencies("linux-image-5.3.0-62-generic");
        graph.markPackageAndItsDependencies("linux-modules-5.3.0-62-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-5.3.0-62-generic");

	// System can boot (Common)
        graph.markPackageAndItsDependencies("linux-image-generic");
        //graph.markPackageAndItsDependencies("linux-image");
        //graph.markPackageAndItsDependencies("linux-generic");
        //graph.markPackageAndItsDependencies("grub2");

        // System can mount storage devices
        graph.markPackageAndItsDependencies("fdisk");
        graph.markPackageAndItsDependencies("e2fsprogs");

        // User can log in (ncurses-base contains terminal definitions,
        // and at least a single language pack is needed to avoid
        // annoying console errors)
        graph.markPackageAndItsDependencies("login");
        graph.markPackageAndItsDependencies("bash");
        graph.markPackageAndItsDependencies("language-pack-en-base");
        graph.markPackageAndItsDependencies("ncurses-base");
        graph.markPackageAndItsDependencies("libc-bin"); // ldd command
        graph.markPackageAndItsDependencies("ncurses-bin"); // clear command
        graph.markPackageAndItsDependencies("coreutils"); // dd, wc, du, md5sum, nice, nohup and other unix commands
        graph.markPackageAndItsDependencies("system-services"); // local ttys

        // User can edit files
        graph.markPackageAndItsDependencies("nano");

        // Can connect to the network and install software
        //graph.markPackageAndItsDependencies("isc-dhcp-client");
        graph.markPackageAndItsDependencies("ifupdown");
        graph.markPackageAndItsDependencies("apt");

        // Can install locally downloaded Debian packages
        graph.markPackageAndItsDependencies("dpkg");

        // A minimal useful system include some utilites to check the hardware
        // (user should do ping, traceroute, nslookup, lspci, lsusb, file,
	// time)
        graph.markPackageAndItsDependencies("iputils-ping");
        graph.markPackageAndItsDependencies("traceroute");
        graph.markPackageAndItsDependencies("dnsutils");
        graph.markPackageAndItsDependencies("pciutils");
        graph.markPackageAndItsDependencies("usbutils");
        graph.markPackageAndItsDependencies("file");
        graph.markPackageAndItsDependencies("time");
        graph.markPackageAndItsDependencies("htop");
        graph.markPackageAndItsDependencies("aptitude");
        graph.markPackageAndItsDependencies("aptitude-common");
	
        // Admin task as dmesg, file system formatting, IPC control and getty
        graph.markPackageAndItsDependencies("util-linux");

        // Is documentation minimal?
        graph.markPackageAndItsDependencies("man-db");

        // Other candidates for analysis
        //graph.markPackageAndItsDependencies("openssh-client");
        //graph.markPackageAndItsDependencies("ftpd");
        //graph.markPackageAndItsDependencies("telnetd");
    }

    // See also: markBasic
    void markIntermediate()
    {
	// Level 3 components: gtk based
        graph.markPackageAndItsDependencies("pavucontrol");
        graph.markPackageAndItsDependencies("emacs");

        // Level 2 components: X11 (server)
        graph.markPackageAndItsDependencies("xinit");
        graph.markPackageAndItsDependencies("xdm");

        // Level 2 components: X11 (multimedia)
        graph.markPackageAndItsDependencies("mplayer");
        graph.markPackageAndItsDependencies("pulseaudio");

        // Level 2 components: X11 (client)
        graph.markPackageAndItsDependencies("gmemusage");
        graph.markPackageAndItsDependencies("htop");
        graph.markPackageAndItsDependencies("mesa-utils");
        graph.markPackageAndItsDependencies("motif-clients");
        graph.markPackageAndItsDependencies("openssh-server");
        graph.markPackageAndItsDependencies("twm");
        graph.markPackageAndItsDependencies("x11-apps");
        graph.markPackageAndItsDependencies("xosview");
        graph.markPackageAndItsDependencies("xterm");

        // Level 1 components: console (multimedia)
        graph.markPackageAndItsDependencies("alsa-utils");

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
