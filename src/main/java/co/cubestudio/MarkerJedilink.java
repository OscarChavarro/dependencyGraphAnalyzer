package co.cubestudio;

public class MarkerJedilink {
    private SoftwarePackageGraph graph;

    public MarkerJedilink(SoftwarePackageGraph graph) {
        this.graph = graph;
    }

    /**
    Criteria:
    - System can boot (not including boot manager, can be booted up from external env.)
    - System can start network connection (wired / eth0)
    - User can log on text console
    - System can install Debian/Ubuntu packages (apt / networked)
    */
    private void markLinuxMinimal() {
        // Kernel images
        graph.anotateNodesV2("linux-image-aws");
        graph.anotateNodesV2("linux-image-6.8.0-1029-aws");
        graph.anotateNodesV2("linux-image-6.2.16-custom");
        graph.anotateNodesV2("linux-image-6.2.0-36-generic");
        graph.anotateNodesV2("linux-image-6.2.0-32-generic");
        graph.anotateNodesV2("linux-image-generic");
        graph.anotateNodesV2("linux-image-5.15.131");
        graph.anotateNodesV2("linux-image-6.2.0-26-generic");
        graph.anotateNodesV2("linux-image-6.8.0-62-generic");
	graph.anotateNodesV2("linux-modules-6.8.0-62-generic");
        graph.anotateNodesV2("linux-image-6.8.0-63-generic");
	graph.anotateNodesV2("linux-modules-6.8.0-63-generic");
	graph.anotateNodesV2("linux-modules-extra-6.8.0-63-generic");
	graph.anotateNodesV2("linux-modules-extra-6.8.0-62-generic");
	graph.anotateNodesV2("linux-modules-6.8.0-1029-aws");
        graph.anotateNodesV2("linux-modules-6.2.0-26-generic");
        graph.anotateNodesV2("linux-modules-extra-5.15.0-79-generic");
        graph.anotateNodesV2("linux-modules-extra-5.15.0-82-generic");
        graph.anotateNodesV2("linux-modules-extra-5.15.0-139-generic");
        graph.anotateNodesV2("linux-modules-extra-6.2.0-32-generic");
        graph.anotateNodesV2("linux-modules-extra-6.2.0-31-generic");
        graph.anotateNodesV2("linux-modules-extra-6.2.0-26-generic");
        graph.anotateNodesV2("linux-image-generic-hwe-22.04");
        graph.anotateNodesV2("linux-image-5.15.0-78-generic");
        graph.anotateNodesV2("linux-image-6.2.0-31-generic");
        graph.anotateNodesV2("linux-image-5.4.0-99-generic");
        graph.anotateNodesV2("linux-image-5.4.0-custom");
        graph.anotateNodesV2("linux-image-5.4.74-custom");
        graph.anotateNodesV2("linux-modules-5.3.0-62-generic");
        graph.anotateNodesV2("linux-modules-extra-5.4.0-135-generic");
        graph.anotateNodesV2("linux-modules-extra-5.15.0-78-generic");
        graph.anotateNodesV2("linux-image-5.3.0-62-generic");
        graph.anotateNodesV2("linux-image-5.4.0-48-generic");
        graph.anotateNodesV2("linux-image-generic-hwe-18.04");
	graph.anotateNodesV2("linux-image-5.4.0-136-generic");
	graph.anotateNodesV2("linux-modules-extra-5.4.0-136-generic");
        graph.anotateNodesV2("linux-firmware"); // Check against kernel drivers used

	// Commands used by linux modules if WiFi is needed
        graph.anotateNodesV2("crda");

        // Libc
        graph.anotateNodesV2("libc-bin"); // ldd command
        graph.anotateNodesV2("libc6:i386");
	graph.anotateNodesV2("locales-all");

        // Encryption libraries
	graph.anotateNodesV2("libssl1.1"); // Old version used by awsvpnclient and mongo on old CPUs

        // Compression tools
        graph.anotateNodesV2("lib32z1");
        graph.anotateNodesV2("libx32z1");
        graph.anotateNodesV2("bzip2");
        graph.anotateNodesV2("pigz");
        graph.anotateNodesV2("lzma");
        graph.anotateNodesV2("xz-utils");
        graph.anotateNodesV2("rar");
        graph.anotateNodesV2("unrar");
        graph.anotateNodesV2("gzip");
        graph.anotateNodesV2("zstd");
        graph.anotateNodesV2("unzip");
        graph.anotateNodesV2("p7zip-full");
        graph.anotateNodesV2("zip");

        // Encryption tools
        graph.anotateNodesV2("ca-certificates"); // Used by Java, Python, Google Chrome, etc.
	
	// Boot
        graph.anotateNodesV2("bsdutils"); // renice, wall
        graph.anotateNodesV2("db-util"); // db_dump command to check Berkeley file databases
        graph.anotateNodesV2("dmsetup"); // RAID manager
        graph.anotateNodesV2("dosfstools");
        graph.anotateNodesV2("e2fsprogs");
        graph.anotateNodesV2("file");
        graph.anotateNodesV2("fuse");
        graph.anotateNodesV2("fuse3");
        graph.anotateNodesV2("ifupdown"); // Reads /etc/network/interfaces at boot up
        graph.anotateNodesV2("info");
        graph.anotateNodesV2("init");
        graph.anotateNodesV2("kbd"); // showconsolefont command
        graph.anotateNodesV2("kmod"); // lsmod / rmmod commands
        graph.anotateNodesV2("language-pack-en"); // avoid fail messages
        graph.anotateNodesV2("login");
        graph.anotateNodesV2("dbus"); // Used by some low level session operations as such reboot and send message to ptys
        graph.anotateNodesV2("net-tools"); // ifconfig
        graph.anotateNodesV2("module-init-tools");
        graph.anotateNodesV2("ntfs-3g"); 
        graph.anotateNodesV2("sed"); // To check why is this needed
        graph.anotateNodesV2("sysvinit-utils"); // To check why is this needed
        graph.anotateNodesV2("ucf"); // To check why is this needed
        graph.anotateNodesV2("udev"); // To check why is this needed
        graph.anotateNodesV2("util-linux"); // Commands as such fsck, mkfs*, 
	
        // Basic C/C++ system   
        graph.anotateNodesV2("apt-utils"); // Extra commands used by apt programs
        graph.anotateNodesV2("apt");
        graph.anotateNodesV2("bash"); // Shell
        graph.anotateNodesV2("bash-completion"); // For bash
        graph.anotateNodesV2("cpufrequtils"); // To change performance / powersafe CPU policy
        graph.anotateNodesV2("dialog"); // used on apt scripts
        graph.anotateNodesV2("diffutils");
        graph.anotateNodesV2("eject");
        graph.anotateNodesV2("find");
        graph.anotateNodesV2("findutils");
        graph.anotateNodesV2("grep");
        graph.anotateNodesV2("hostname");
        graph.anotateNodesV2("htop");
        graph.anotateNodesV2("neofetch"); // Text mode system resume
        graph.anotateNodesV2("less");
        graph.anotateNodesV2("mtools"); // Access to DOS FAT floppies
        graph.anotateNodesV2("nano"); // Simple text editor
        graph.anotateNodesV2("ncurses-base"); // Used by nano and other console apps
        graph.anotateNodesV2("read-edid");
        graph.anotateNodesV2("sudo");
        graph.anotateNodesV2("tcsh"); // Shell
        graph.anotateNodesV2("time");
        graph.anotateNodesV2("powermgmt-base"); // on_ac_power command
        graph.anotateNodesV2("tofrodos"); // Convert text files between DOS/Unix formats
        graph.anotateNodesV2("vim-tiny");
        graph.anotateNodesV2("zsh");
        graph.anotateNodesV2("psmisc"); // pstree command
        graph.anotateNodesV2("netbase"); // Network info files on /etc
        graph.anotateNodesV2("binfmt-support"); // Some magic to run for example, ARM machine instructions on X86 by using QEMU

        // Pending to verify why those should be essential
        graph.anotateNodesV2("dash"); // To check why is this needed (initramfs boot?)
        graph.anotateNodesV2("mawk"); // Used on /etc/* bash completion and other awk scripts
        graph.anotateNodesV2("base-passwd"); // To check why is this needed by apt

	// Temporary test
	graph.anotateNodesV2("openssh-client");
	graph.anotateNodesV2("initramfs-tools");
    }

    private void markX11Apps() {
        graph.anotateNodesV2("zulu17-jdk"); // Java SDK
        graph.anotateNodesV2("x11-apps");
        graph.anotateNodesV2("fig2dev");
        graph.anotateNodesV2("uil"); // Motif
	graph.anotateNodesV2("dclock");
        graph.anotateNodesV2("feh"); // minimalistic image viewer
        graph.anotateNodesV2("gmemusage");
        graph.anotateNodesV2("mesa-utils");
        graph.anotateNodesV2("mwm");
        graph.anotateNodesV2("oneko");
        graph.anotateNodesV2("twm");
        graph.anotateNodesV2("x11-utils");
        graph.anotateNodesV2("xauth"); // needed for tunneling X11 on SSH connections
        graph.anotateNodesV2("xfig");
        graph.anotateNodesV2("xfig-libs");
        graph.anotateNodesV2("xfonts-base"); // 10x20 and other basic X11 fonts
        graph.anotateNodesV2("xfishtank");
        graph.anotateNodesV2("xinput"); // used on X11 to list and manage HIDs
        graph.anotateNodesV2("xosview");
        graph.anotateNodesV2("xterm");
        graph.anotateNodesV2("xzoom");
        graph.anotateNodesV2("chocolate-doom");
        graph.anotateNodesV2("freedoom");
        graph.anotateNodesV2("gsfonts-x11"); // make GS fonts available to X (not used, to check)
    }
    
    /**
    Criteria:
    - No X11
    - No python
    - No multimedia other than bare minimal image formats
    - No development tools
    - No gpgp
    - Directly on top of minimal
    */
    private void markLinuxBasic() {
        // Intermediate C/C++ system
        graph.anotateNodesV2("base-files"); // Unix directories
        graph.anotateNodesV2("bluez"); // Bluetooth control (bluetoothctl)
        graph.anotateNodesV2("bluez-tools"); // bt-device, etc
        graph.anotateNodesV2("console-setup-linux"); // configure keyboard, psf bitmap font files for VGA text mode
        graph.anotateNodesV2("coreutils"); // cat cp date dd df rm, etc
        graph.anotateNodesV2("cpu-checker"); // kvm-ok command checks if CPU/BIOS has VT-X support
        graph.anotateNodesV2("cron");
        graph.anotateNodesV2("logrotate"); // Rotate and compress logs via crontab
        graph.anotateNodesV2("dcraw"); // Import Sony ARW raw files
        graph.anotateNodesV2("dmidecode"); // BIOS utils
        graph.anotateNodesV2("exif");
        graph.anotateNodesV2("file");
        graph.anotateNodesV2("gddrescue"); // low level backup tool
        graph.anotateNodesV2("gpgv1");
        graph.anotateNodesV2("gpgv2");
        graph.anotateNodesV2("hdparm");
        graph.anotateNodesV2("libcomerr2");
        graph.anotateNodesV2("libcap2-bin"); // Examine file capabilities (getcap)
        graph.anotateNodesV2("lshw");
        graph.anotateNodesV2("lsof");
        graph.anotateNodesV2("lsscsi"); // List installed SCSI devices / disks
        graph.anotateNodesV2("lrzsz"); // zmodem, xmodem - used by minicom
        graph.anotateNodesV2("lua5.2");
        graph.anotateNodesV2("lvm2"); // Tools for handling logical RAID devices, needed by crontab, needed to mount LVMs from initramfs if used
        graph.anotateNodesV2("man-db");
        graph.anotateNodesV2("nvme-cli"); // Tools for checking NVME (M2) storage devices
        graph.anotateNodesV2("ncurses-base");
        graph.anotateNodesV2("ncurses-bin");
        graph.anotateNodesV2("ncurses-term");
        graph.anotateNodesV2("jq"); // JSON pretty print
        graph.anotateNodesV2("xxd"); // HEX binary pretty print
        graph.anotateNodesV2("pciutils");
        graph.anotateNodesV2("ppp");
        graph.anotateNodesV2("iw"); // wireless / WiFi
        graph.anotateNodesV2("rsyslog"); // Copies the kernel ring buffer (dmesg) to /var/log/syslog
        graph.anotateNodesV2("smartmontools"); // Tools to check ATA devices
        graph.anotateNodesV2("tofrodos");
        graph.anotateNodesV2("tzdata"); // Timezones, needed to change system date / time location
        graph.anotateNodesV2("ufraw-batch");
        graph.anotateNodesV2("whiptail");
	graph.anotateNodesV2("wodim"); // CD/DVD burner

        // Internet command line tools and applications
        graph.anotateNodesV2("bridge-utils"); // eth. for machines with more than 1 interface
        graph.anotateNodesV2("curl");
        graph.anotateNodesV2("dnsutils"); // nslookup command
        graph.anotateNodesV2("bind9"); // DNS server
        graph.anotateNodesV2("bind9-dnsutils"); // nslookup command
        graph.anotateNodesV2("dnsmasq-base"); // DNS server? different to isc? pending to test
        graph.anotateNodesV2("ethtool");
        graph.anotateNodesV2("iptables");
        graph.anotateNodesV2("hostapd"); // Access Point (ap) for Wifi adapters :)
        graph.anotateNodesV2("iperf"); // Like speedtest but local
        graph.anotateNodesV2("iputils-ping"); // Level 3 (TCP/IP)
        graph.anotateNodesV2("iputils-arping"); // Level 2 (ARP) pint
        graph.anotateNodesV2("fping"); // Ping for ICMP
        graph.anotateNodesV2("isc-dhcp-common"); // Manual pages for DHCP?
        graph.anotateNodesV2("isc-dhcp-client"); // dhclient command for connecting to the net using DHCP
        graph.anotateNodesV2("minicom");
        graph.anotateNodesV2("mongodb-org");
        graph.anotateNodesV2("mongodb-mongosh");
        graph.anotateNodesV2("nfs-kernel-server"); // NFS to share with unix (used to mount local photos on cubestudio.co)
        graph.anotateNodesV2("nfs-common"); // NFS client to mount externally shared volumes
        graph.anotateNodesV2("lynx");
        graph.anotateNodesV2("ifuse"); // Mount iphone as USB storage to access camera images
        graph.anotateNodesV2("irqbalance"); // Interrupt optimizer for SMP systems
        graph.anotateNodesV2("mtr-tiny"); // ncurses traceroute
        graph.anotateNodesV2("numactl"); // Memory optimizer for SMP systems (i.e. mongodb systemctl script)
        graph.anotateNodesV2("openssh-client");
        graph.anotateNodesV2("ntp"); // Network time protocol
        graph.anotateNodesV2("systemd-timesyncd"); // Network time protocol on ubuntu 24.04
        graph.anotateNodesV2("systemd-sysv"); // reboot command
        graph.anotateNodesV2("sntp"); // Network time protocol
        graph.anotateNodesV2("openssh-server");
        graph.anotateNodesV2("tcpdump"); // Network traffic analyzer (sniffer)
        graph.anotateNodesV2("traceroute");
        graph.anotateNodesV2("w3m"); // Similar to Lynx
        graph.anotateNodesV2("w3m-img");
        graph.anotateNodesV2("wget");
        graph.anotateNodesV2("aria2"); // Parallel downloader
        graph.anotateNodesV2("wireless-tools"); // iwconfig command to turn on WiFi from command line
        graph.anotateNodesV2("wpasupplicant"); // to turn on WiFi from command line
        graph.anotateNodesV2("zerotier-one"); // Nice VPN service agent
        graph.anotateNodesV2("iftop"); // Monitor network interfaces
        graph.anotateNodesV2("isc-dhcp-server");

        // Use Iphone tethering via USB
        graph.anotateNodesV2("usbmuxd");
        graph.anotateNodesV2("libimobiledevice-utils");
        graph.anotateNodesV2("ipheth-utils");

        // Using boost
        graph.anotateNodesV2("aptitude");

        // Sometimes not needed
	graph.anotateNodesV2("grub2");
        graph.anotateNodesV2("grub-pc");
        graph.anotateNodesV2("efibootmgr"); // Access EFI options on BIOS, used by grub when installing
	graph.anotateNodesV2("shim-signed");
	graph.anotateNodesV2("secureboot-db");
	graph.anotateNodesV2("mokutil");
        graph.anotateNodesV2("initramfs-tools");
        graph.anotateNodesV2("os-prober");
        graph.anotateNodesV2("locales");
        graph.anotateNodesV2("syslinux-common");
        graph.anotateNodesV2("cpio");
        graph.anotateNodesV2("intel-microcode"); // Updates for bugs/security issues for CPU (intel) - fixes Meltdown/Spectrum bugs and such
        
        // Applications
        graph.anotateNodesV2("telnet");
        graph.anotateNodesV2("tftpd-hpa");
        graph.anotateNodesV2("tftp-hpa");
        graph.anotateNodesV2("tftp"); // Tftp client for testing server
        graph.anotateNodesV2("pxelinux");
        graph.anotateNodesV2("ftp");
        graph.anotateNodesV2("whois"); // Tool to get information about DNS domains
        graph.anotateNodesV2("gpm");
        graph.anotateNodesV2("mongodb");
        graph.anotateNodesV2("mongo-tools"); // mongodump, mongorestore, etc

        // For small systems not using opensshd
        graph.anotateNodesV2("telnetd");
        graph.anotateNodesV2("ftpd");

        // Others
        graph.anotateNodesV2("apparmor-utils"); // aa-status and other aa-* commands
        graph.anotateNodesV2("apparmor-profiles"); // /etc settings for controlling security profile permissions
	graph.anotateNodesV2("apparmor"); // Needed by snapd
	graph.anotateNodesV2("snapd"); // Universal linux distribution independent installer / appstore and app sandbox environment manager

	graph.anotateNodesV2("sysvbanner");
	graph.anotateNodesV2("tree");
	
        markLinuxMinimal();
    }

    private void markMultimedia() {
        // Unknown!
        graph.anotateNodesV2("chromium-browser");
        graph.anotateNodesV2("chromium-chromedriver");
        graph.anotateNodesV2("chromium-browser-l10n");

        // Multimedia
        graph.anotateNodesV2("bb"); // ASCII art demo :)
        graph.anotateNodesV2("jp2a"); // image to ASCII art conversor
        graph.anotateNodesV2("toilet"); // Text console banners with special ASCII art fonts
        graph.anotateNodesV2("caca-utils"); // ASCII art demos, in color :)
        graph.anotateNodesV2("toilet-fonts"); // Fonts used by caca-utils
        graph.anotateNodesV2("bolt"); // Control thunderbolt port / devices
        graph.anotateNodesV2("pulseaudio"); // System sound server/daemon
        graph.anotateNodesV2("vdpauinfo");
        graph.anotateNodesV2("vdpau-driver-all");
        graph.anotateNodesV2("espeak");
        graph.anotateNodesV2("lm-sensors");
        graph.anotateNodesV2("usbutils");
        graph.anotateNodesV2("usb.ids");
        graph.anotateNodesV2("alsa-utils");
        graph.anotateNodesV2("flac");
        graph.anotateNodesV2("ffmpeg");
        graph.anotateNodesV2("lame");
        graph.anotateNodesV2("libavfilter-extra6"); // extended version ffmpeg
        graph.anotateNodesV2("libavcodec-extra57"); // extended version ffmpeg
        graph.anotateNodesV2("libsixel-bin"); // DIGITAL text/image ASCII art format
	graph.anotateNodesV2("mlterm"); // Emulador de terminal para X11 compatible con formato sixel de VT330
        graph.anotateNodesV2("libjpeg-progs");
        graph.anotateNodesV2("libpng-tools");
        graph.anotateNodesV2("libsox-fmt-ao");
        graph.anotateNodesV2("libsox-fmt-oss");
        graph.anotateNodesV2("libsox-fmt-pulse");
        graph.anotateNodesV2("libwmf-bin");
        graph.anotateNodesV2("libfftw3-single3");
        graph.anotateNodesV2("libde265-0");
        graph.anotateNodesV2("libasound2-data");
        graph.anotateNodesV2("mpg123");
        graph.anotateNodesV2("netpbm");
        graph.anotateNodesV2("opus-tools");
        graph.anotateNodesV2("sox");
        graph.anotateNodesV2("v4l-utils");
        graph.anotateNodesV2("vorbis-tools");
        graph.anotateNodesV2("mplayer"); // Python2.7 based
	graph.anotateNodesV2("clinfo"); // Get info from CUDA context for OpenCL
        graph.anotateNodesV2("libavfilter6"); // fmpeg libs
        graph.anotateNodesV2("lsdvd");	
        graph.anotateNodesV2("hwloc");
        graph.anotateNodesV2("libhwloc-plugins");
        graph.anotateNodesV2("libheif-examples"); // Converts .HEIC IOS image files
        graph.anotateNodesV2("pipewire-pulse"); // Jack-like routing audio library with spacialitation
        graph.anotateNodesV2("pulsemixer"); // Console based volume control for pulseaudio
	
        // Pending to test utilities
        graph.anotateNodesV2("alsa-base");
        graph.anotateNodesV2("libsoxr-lsr0");
        graph.anotateNodesV2("libmtp-runtime");
        graph.anotateNodesV2("cdrdao");
        graph.anotateNodesV2("vcdimager");
        graph.anotateNodesV2("libraw1394-tools");
        graph.anotateNodesV2("twolame");
        graph.anotateNodesV2("cdparanoia");
        graph.anotateNodesV2("libbdplus0"); // Blue ray disc reader
        graph.anotateNodesV2("libaacs0"); // AACS cyptographic content protection
        graph.anotateNodesV2("freepats"); // MIDI audio synthesis

        // Multimedia applications
        graph.anotateNodesV2("gphoto2"); // External camera control (Sony)
        graph.anotateNodesV2("xawtv"); // light v4l client
        graph.anotateNodesV2("fontforge"); // fonts editor
        graph.anotateNodesV2("blender"); // 3d modeller
        graph.anotateNodesV2("mupen64plus-ui-console");
        graph.anotateNodesV2("libmupen64plus2");
        graph.anotateNodesV2("mupen64plus-video-arachnoid");
        graph.anotateNodesV2("mupen64plus-video-glide64");
        graph.anotateNodesV2("mupen64plus-video-z64");
        graph.anotateNodesV2("mupen64plus-video-all");
        graph.anotateNodesV2("transcode");
        graph.anotateNodesV2("transcode-doc");

        graph.anotateNodesV2("testdisk");
    }
    
    private void markX11Server() {
        // X11 server
        graph.anotateNodesV2("xdm");
        graph.anotateNodesV2("xinit");
        graph.anotateNodesV2("xserver-xorg-core"); // This contains /usr/bin/X
        graph.anotateNodesV2("xserver-xorg-input-all"); // X11 server could react to mouse and keyboard
        graph.anotateNodesV2("xnest");
        graph.anotateNodesV2("xbase-clients");
        graph.anotateNodesV2("xserver-xephyr");
        graph.anotateNodesV2("xserver-xorg-video-intel"); // Needed on taciturna
        graph.anotateNodesV2("xserver-xorg-video-fbdev"); // Needed by intel + xdm
        graph.anotateNodesV2("xserver-xorg-video-vesa"); // Needed by intel + xdm
	graph.anotateNodesV2("x11-session-utils");
        graph.anotateNodesV2("xvfb");
        graph.anotateNodesV2("x11vnc");
    }
    
    private void markBasicDevelopment() {
        // General software development tools
        graph.anotateNodesV2("build-essential"); // Recommended kit for nvidia driver installation
        graph.anotateNodesV2("cloc"); // Source code line counter
        graph.anotateNodesV2("automake");
        graph.anotateNodesV2("autoconf2.64");
        graph.anotateNodesV2("autopoint"); // GNU gettext? why is this included?
        graph.anotateNodesV2("scons"); // Similar to make or cmake
        graph.anotateNodesV2("cmake");
        graph.anotateNodesV2("cmake-curses-gui");
        graph.anotateNodesV2("debhelper");
        graph.anotateNodesV2("debootstrap");
        graph.anotateNodesV2("doxygen");
        graph.anotateNodesV2("dpkg-dev"); // Can build debian packages from source
        graph.anotateNodesV2("devscripts"); // Can build debian packages from source
	graph.anotateNodesV2("perl-openssl-defaults"); // Needed by devscripts
        graph.anotateNodesV2("fakeroot");
        graph.anotateNodesV2("fdisk");
        graph.anotateNodesV2("lsb-release"); // lsb_release -a command to identify Ubuntu version
        graph.anotateNodesV2("gdisk"); // fdisk for GPT partitions
        graph.anotateNodesV2("parted"); // Filesystem operations
        graph.anotateNodesV2("xfsprogs"); // Used on XFS (i.e. mongod)
        graph.anotateNodesV2("g++");
        graph.anotateNodesV2("gawk");
        graph.anotateNodesV2("gperf"); // Why is this included? Hash functions?
        graph.anotateNodesV2("linux-generic");
        graph.anotateNodesV2("linux-generic-hwe-22.04");
        graph.anotateNodesV2("linux-aws-tools-6.8.0-1029");
        graph.anotateNodesV2("linux-tools-6.8.0-1029-aws");
        graph.anotateNodesV2("linux-headers-generic");
        graph.anotateNodesV2("linux-headers-6.8.0-62-generic");
        graph.anotateNodesV2("linux-headers-6.8.0-63-generic");
        graph.anotateNodesV2("linux-headers-aws");
        graph.anotateNodesV2("linux-headers-6.8.0-1029-aws");
        graph.anotateNodesV2("linux-aws-headers-6.8.0-1029");
        graph.anotateNodesV2("linux-headers-5.15.131");
        graph.anotateNodesV2("linux-headers-5.15.0-139-generic");
        graph.anotateNodesV2("linux-headers-6.2.16-custom");
        graph.anotateNodesV2("linux-headers-6.2.0-32-generic");
        graph.anotateNodesV2("linux-headers-6.2.0-36-generic");
        graph.anotateNodesV2("linux-headers-6.2.0-31-generic");
        graph.anotateNodesV2("linux-headers-5.15.0-82-generic");
        graph.anotateNodesV2("linux-headers-5.15.0-79-generic");
        graph.anotateNodesV2("linux-headers-5.15.0-78-generic");
        graph.anotateNodesV2("linux-headers-5.4.0-136-generic");
        graph.anotateNodesV2("linux-headers-5.4.0-99-generic");
        graph.anotateNodesV2("linux-headers-5.3.0-62-generic");
        graph.anotateNodesV2("linux-headers-5.4.0-custom");
        graph.anotateNodesV2("linux-headers-5.4.74-custom");
        graph.anotateNodesV2("linux-headers-5.4.0-48-generic");
        graph.anotateNodesV2("linux-headers-5.4.0-135-generic");
        graph.anotateNodesV2("linux-headers-generic-hwe-18.04");
        graph.anotateNodesV2("ltrace");
        graph.anotateNodesV2("libtool");
        graph.anotateNodesV2("liblapack-dev");
        graph.anotateNodesV2("liblapacke-dev");
        graph.anotateNodesV2("libmotif-dev");
        graph.anotateNodesV2("libosmesa6-dev");
        graph.anotateNodesV2("mdadm"); // Needed on systems with hardware/BIOS RAID volumes
        graph.anotateNodesV2("make");
        graph.anotateNodesV2("manpages");
        graph.anotateNodesV2("manpages-dev");
        graph.anotateNodesV2("nasm");
        graph.anotateNodesV2("yasm");
        graph.anotateNodesV2("git");
        graph.anotateNodesV2("gdb");
        graph.anotateNodesV2("libboost-regex1.74.0"); // Needed by gdb
        graph.anotateNodesV2("gdbserver");
        graph.anotateNodesV2("gource");
        graph.anotateNodesV2("python3-pip");
        graph.anotateNodesV2("strace");
        graph.anotateNodesV2("subversion");
        graph.anotateNodesV2("valgrind");
        graph.anotateNodesV2("libcfitsio-doc");

	// X11 / GL dev
        graph.anotateNodesV2("freeglut3-dev");
        graph.anotateNodesV2("x11proto-input-dev");
        graph.anotateNodesV2("x11proto-kb-dev");
        graph.anotateNodesV2("x11proto-render-dev");
        graph.anotateNodesV2("webp");
        graph.anotateNodesV2("libxpm-dev"); // Used by rpk
        graph.anotateNodesV2("x11proto-print-dev"); // Used by rpk
        graph.anotateNodesV2("libglm-dev"); // Window kit replacing glut and being used by newer OpenGL and Vulkan apps
	graph.anotateNodesV2("libglfw3-dev");

        // 32-bit development environment (test with rpk/Renderpark project)
        graph.anotateNodesV2("g++-multilib");
        graph.anotateNodesV2("freeglut3:i386");
        graph.anotateNodesV2("libstdc++-7-dev:i386"); // review this (dev?)
        graph.anotateNodesV2("libosmesa6:i386");
        graph.anotateNodesV2("libxm4:i386");
        graph.anotateNodesV2("libxft2:i386");
        graph.anotateNodesV2("libtiff5:i386");

        // Media dev
        graph.anotateNodesV2("libexif-doc");
	
        // Profilers & debuggers
	graph.anotateNodesV2("linux-tools-common"); // Contains perf
	
        // Python3 development (can build tensorflow)
	graph.anotateNodesV2("gitsome"); // For gh command
        graph.anotateNodesV2("python3-pip");
        graph.anotateNodesV2("python3-dev");
        graph.anotateNodesV2("python3-jinja2");
        graph.anotateNodesV2("git");

        // Can build dcraw
	graph.anotateNodesV2("liblcms2-dev");

        // Other commonly used
        graph.anotateNodesV2("libraw-dev"); // DCraw derived library to import raw images from digital cameras
        graph.anotateNodesV2("arduino");
        graph.anotateNodesV2("docker-ce");
        graph.anotateNodesV2("docker-ce-rootless-extras");
        graph.anotateNodesV2("docker-compose-plugin");
        graph.anotateNodesV2("docker-scan-plugin");
        graph.anotateNodesV2("docker-compose");
        graph.anotateNodesV2("clang-tidy");
    }

    private void markKernelDevelopment() {
        // Can compile kernel
        //graph.anotateNodesV2("linux-generic"); // Not true after kernel v4
        graph.anotateNodesV2("kernel-package");
        graph.anotateNodesV2("libncurses5-dev");
        graph.anotateNodesV2("flex");
        graph.anotateNodesV2("bison");
        graph.anotateNodesV2("libssl-dev");
        graph.anotateNodesV2("libelf-dev");
        graph.anotateNodesV2("rsync");
        graph.anotateNodesV2("debhelper");
        graph.anotateNodesV2("pkg-config");
        graph.anotateNodesV2("liborc-0.4-dev-bin");
        graph.anotateNodesV2("gtk+-2.0"); // make gconfig
        graph.anotateNodesV2("gmodule-2.0");
        graph.anotateNodesV2("libglade2-0");
        graph.anotateNodesV2("libglade2-dev");

        //graph.anotateNodesV2("libqt4-dev"); // For X11 menu, should include qt devel
        //graph.anotateNodesV2("qt4-dev-tools"); // make xconfig
    }
    
    private void markGtkApps() {
        // Pending to locate
        graph.anotateNodesV2("fonts-inconsolata"); // unlinked dependency!
        graph.anotateNodesV2("fonts-cantarell"); // unlinked dependency!

        // GTK apps
        graph.anotateNodesV2("lshw-gtk"); // List hardware elements
        graph.anotateNodesV2("soundmodem"); // TCP/IP stack over audio :)
        graph.anotateNodesV2("baobab"); // disk usage!
        graph.anotateNodesV2("aisleriot"); // /usr/games/sol
        graph.anotateNodesV2("pavucontrol");
        graph.anotateNodesV2("ghex");
        graph.anotateNodesV2("gparted");
        graph.anotateNodesV2("gtkterm"); // Serial, minicom like program with GUI
        graph.anotateNodesV2("inkscape");
        graph.anotateNodesV2("view3dscene");
        //graph.anotateNodesV2("libmagic-dev"); // needed by inkscape (export files)
        //graph.anotateNodesV2("python-pip"); // Python2, needed by inkscape
        // For inkscape: pip install lxml scour unicode libmagic file
        graph.anotateNodesV2("emacs");
        graph.anotateNodesV2("emacs25-el");
        graph.anotateNodesV2("firefox");
        graph.anotateNodesV2("firefox-locale-en");
        graph.anotateNodesV2("imagemagick");
        graph.anotateNodesV2("chafa"); // Image processing - downscaler, dithering, ASCII art
        graph.anotateNodesV2("wireshark-gtk"); // Packet analizer / sniffer
        graph.anotateNodesV2("wireshark"); 
        graph.anotateNodesV2("tshark"); 
        //graph.anotateNodesV2("wireshark-qt"); // can not be used along wx version
        graph.anotateNodesV2("xsane"); // Scanner software, not compatible with Canon Pixma TS9551C
	graph.anotateNodesV2("xpra"); // Efficient remote X11 connections
        graph.anotateNodesV2("xdg-utils"); // Used by google chrome
	graph.anotateNodesV2("baobab"); // Similar to WinDirStat (visualization of disk space)
	graph.anotateNodesV2("celestia"); // Extra repo
	graph.anotateNodesV2("code"); // Visual Studio code editor
	
        // GTK themes
        graph.anotateNodesV2("adwaita-icon-theme-full");
        graph.anotateNodesV2("yaru-theme-gnome-shell"); // Default Look and Feel for Gnome
        graph.anotateNodesV2("yaru-theme-icon");
        graph.anotateNodesV2("fonts-ubuntu"); // Contains fonts used by yaru theme
        graph.anotateNodesV2("gnome-icon-theme");
	
	// GTK + Python2.7 based apps
        graph.anotateNodesV2("gimp");
        graph.anotateNodesV2("gimp-ufraw");
        graph.anotateNodesV2("dia");
        graph.anotateNodesV2("dia-shapes");
        graph.anotateNodesV2("zenmap"); // nmapfe - packet prober
        graph.anotateNodesV2("nmap");

        // Custom repositories
        graph.anotateNodesV2("google-earth-pro-stable");
        graph.anotateNodesV2("awsvpnclient");
        graph.anotateNodesV2("code");
    }
    
    private void markOtherDependencies() {
        // Povray 3.7 dependencies
        graph.anotateNodesV2("libz-dev");
        graph.anotateNodesV2("libpng-dev");
        graph.anotateNodesV2("libjpeg-dev");
        graph.anotateNodesV2("libtiff-dev");
        graph.anotateNodesV2("libboost-dev");
        graph.anotateNodesV2("libopenexr-dev");	
        graph.anotateNodesV2("libsdl1.2-dev");
        //graph.anotateNodesV2("libtiff5-dev");
        //graph.anotateNodesV2("libsdl-dev");
        //graph.anotateNodesV2("libboost-date-time-dev");

        // JED development dependencies
        graph.anotateNodesV2("libmotif-dev");
}

    private void markOpenCvDependencies() {
        // Requires to have install custom:
	// - Java
	// - Ant
	// - Cuda + Nvidia drivers + cudnn
        graph.anotateNodesV2("gdb");
        graph.anotateNodesV2("g++-7");
        graph.anotateNodesV2("python3-bs4");
        graph.anotateNodesV2("libblas-dev");
        graph.anotateNodesV2("doxygen");
        graph.anotateNodesV2("libfreetype6-dev");
        graph.anotateNodesV2("libharfbuzz-dev");
        graph.anotateNodesV2("libhdf5-dev");
        graph.anotateNodesV2("libgflags-dev");
        graph.anotateNodesV2("libjpeg-dev");
        graph.anotateNodesV2("libjpeg8-dev");
        graph.anotateNodesV2("libopenjp2-7-dev");
        graph.anotateNodesV2("libjpeg-turbo8-dev");
        graph.anotateNodesV2("libavcodec-dev");
        graph.anotateNodesV2("libavformat-dev");
        graph.anotateNodesV2("libavresample-dev");
        graph.anotateNodesV2("libavutil-dev");
        graph.anotateNodesV2("libdc1394-22-dev");
        graph.anotateNodesV2("libgphoto2-dev");
        graph.anotateNodesV2("libswscale-dev");
        graph.anotateNodesV2("libopenexr-dev");
        graph.anotateNodesV2("libpng-dev");
        graph.anotateNodesV2("libprotobuf-dev");
        graph.anotateNodesV2("libtesseract-dev");
        graph.anotateNodesV2("libtiff-dev");
        graph.anotateNodesV2("libwebp-dev");
        graph.anotateNodesV2("libpixman-1-dev");
        graph.anotateNodesV2("zlib1g-dev");
        graph.anotateNodesV2("libopenjp2-7-dev");
        graph.anotateNodesV2("libxine2-dev");
        graph.anotateNodesV2("libgdal-dev");
        graph.anotateNodesV2("libv4l-dev");
        graph.anotateNodesV2("libpthread-stubs0-dev");
        graph.anotateNodesV2("libeigen3-dev");
	graph.anotateNodesV2("libatlas-base-dev");
        graph.anotateNodesV2("libgtk-3-dev");
	graph.anotateNodesV2("libgtkglext1-dev");
        graph.anotateNodesV2("libqt4-opengl-dev");
        graph.anotateNodesV2("libgstreamer-plugins-base1.0-dev");
        graph.anotateNodesV2("libhdf5-mpich-dev");

        //graph.anotateNodesV2("libz-dev");
        //graph.anotateNodesV2("libgtk2.0-dev");

        // Custom repositories
	graph.anotateNodesV2("cuda");
	graph.anotateNodesV2("cuda-repo-ubuntu2204-12-2-local");
	graph.anotateNodesV2("cudnn-local-repo-ubuntu2204-8.9.3.28");
        graph.anotateNodesV2("libcudnn7-dev");
        graph.anotateNodesV2("cuda-cudart-dev-10-0");
	graph.anotateNodesV2("gnupg1");
	graph.anotateNodesV2("gnupg2");

    	// NVIDIA
        graph.anotateNodesV2("nvidia-prime");
        graph.anotateNodesV2("nvidia-modprobe");
        graph.anotateNodesV2("libcudnn7-doc");
        graph.anotateNodesV2("libnvinfer-dev");
        graph.anotateNodesV2("cuda-tools-10-0");
        graph.anotateNodesV2("cuda-documentation-10-0");
        graph.anotateNodesV2("nvidia-driver-410");
        graph.anotateNodesV2("cuda-libraries-dev-10-0");
        graph.anotateNodesV2("cuda-libraries-10-0");
        graph.anotateNodesV2("cuda-compiler-10-0");
        graph.anotateNodesV2("cuda-nvprune-10-0");
        graph.anotateNodesV2("cuda-demo-suite-10-0");

        // Vulkan
        graph.anotateNodesV2("libvulkan-dev");
        graph.anotateNodesV2("vulkan-utils");
        graph.anotateNodesV2("vulkan-sdk");
        graph.anotateNodesV2("dxc-dev");
    }

    private void markLinuxIntermediate() {
        // Perl applications
	graph.anotateNodesV2("arp-scan"); // Network tool, used by nmap
	graph.anotateNodesV2("wakeonlan");
	graph.anotateNodesV2("irssi");
        graph.anotateNodesV2("findimagedupes");
        graph.anotateNodesV2("needrestart"); // Helper to restart services after library updates

	// Python3x applications
        graph.anotateNodesV2("youtube-dl");
        graph.anotateNodesV2("command-not-found");

        // Intermediate dependencies (gs, pango, cairo, gtk, cups, samba)
        graph.anotateNodesV2("graphviz");
        graph.anotateNodesV2("cups-client"); // lp command
        graph.anotateNodesV2("tesseract-ocr"); // Not still tested!
        graph.anotateNodesV2("tesseract-ocr-spa");
	graph.anotateNodesV2("nginx");
	graph.anotateNodesV2("certbot"); // Creates SSL certificates for Nginx! 
	graph.anotateNodesV2("python3-certbot-nginx");
        graph.anotateNodesV2("samba");
        graph.anotateNodesV2("samba-vfs-modules");
        graph.anotateNodesV2("smbclient");
        graph.anotateNodesV2("samba-client"); // deprecated?
        graph.anotateNodesV2("cups"); // Pending to research how to use without Gnome
        graph.anotateNodesV2("pstoedit");

        // TK based X11 apps
        graph.anotateNodesV2("tkcvs"); // tkdiff
        graph.anotateNodesV2("stopwatch");

	// Custom repositories
        graph.anotateNodesV2("google-chrome-stable");
        graph.anotateNodesV2("cnijfilter2"); // Driver Canon Pixma TS9551C
        graph.anotateNodesV2("scangearmp2"); // Canon Pixma TS9551C scanner util
        graph.anotateNodesV2("libpango1.0-0"); // Needed by scangearmp2
    }

    private void markQtApps() {
	// QT apps
        graph.anotateNodesV2("qgis-plugin-grass");
	graph.anotateNodesV2("qt5-image-formats-plugin-pdf");
        graph.anotateNodesV2("cmake-qt-gui");
        graph.anotateNodesV2("virtualbox-qt");
        graph.anotateNodesV2("vlc");
        graph.anotateNodesV2("vlc-plugin-skins2");
        graph.anotateNodesV2("vlc-plugin-visualization");
        graph.anotateNodesV2("vlc-plugin-video-splitter");
        graph.anotateNodesV2("vlc-plugin-notify");
        graph.anotateNodesV2("vlc-plugin-samba");
        graph.anotateNodesV2("vlc-plugin-access-extra");
        graph.anotateNodesV2("vlc-l10n");
        graph.anotateNodesV2("teamviewer");
        graph.anotateNodesV2("sqlitebrowser"); // GUI for sqlite, useful to browse iphone's image database

	// Custom repositories
        graph.anotateNodesV2("obs-studio");
        graph.anotateNodesV2("obs-plugins");

        // QT development
        graph.anotateNodesV2("libqt4-dev");
        graph.anotateNodesV2("qt4-dev-tools"); // make xconfig
    }

    private void markWxApps() {
        // WX apps
        graph.anotateNodesV2("audacity");
    }

    private void markKde() {
        graph.anotateNodesV2("k3b");
        graph.anotateNodesV2("kwin");
    }

    private void markGnome() {
        // Gnome applications
        graph.anotateNodesV2("gnome-keyring"); // seahorse app, similar to MacOS keyring
        graph.anotateNodesV2("activity-log-manager");
        graph.anotateNodesV2("bluetooth"); // Bluetooth audio
        graph.anotateNodesV2("cheese"); // Camera access
        graph.anotateNodesV2("gnome-calculator");
        graph.anotateNodesV2("dbus-user-session"); // Needed by gnome-terminal
        graph.anotateNodesV2("eog"); // Photo viewer
        graph.anotateNodesV2("evince"); // PDF viewer
        graph.anotateNodesV2("file-roller"); // Gnome compressed file manager
        graph.anotateNodesV2("gedit"); // Text editor
        graph.anotateNodesV2("gir1.2-gsound-1.0"); // used by pitivi
        graph.anotateNodesV2("gnome-calendar");
        graph.anotateNodesV2("gnome-disk-utility");
        graph.anotateNodesV2("gnome-font-viewer");
        graph.anotateNodesV2("gnome-tweak-tool"); // Desktop configuration
        graph.anotateNodesV2("gnome-tweaks"); // Desktop configuration
        graph.anotateNodesV2("gnome-mahjongg");
        graph.anotateNodesV2("gnome-mines");
        graph.anotateNodesV2("gnome-screenshot"); // Screenshot!
        graph.anotateNodesV2("gnome-sudoku");
        graph.anotateNodesV2("gnome-system-monitor");
        graph.anotateNodesV2("gnome-terminal");
        graph.anotateNodesV2("gthumb"); // Image viewer, supports camera raw
        graph.anotateNodesV2("nautilus"); // File explorer
        graph.anotateNodesV2("pavucontrol"); // Nice sound hardware util
        graph.anotateNodesV2("pitivi"); // video editor
        graph.anotateNodesV2("pulseaudio-module-bluetooth"); // Pair BT audio
        graph.anotateNodesV2("remmina"); // Remote desktop
        graph.anotateNodesV2("remmina-plugin-rdp");
        graph.anotateNodesV2("remmina-plugin-secret");
        graph.anotateNodesV2("remmina-plugin-vnc");
        graph.anotateNodesV2("rhythmbox-plugins");
        graph.anotateNodesV2("seahorse"); // Gnome keyring manager
        graph.anotateNodesV2("shotwell"); // Photo viewer
        graph.anotateNodesV2("system-config-printer"); // Printer installer
        graph.anotateNodesV2("totem-plugins");
        graph.anotateNodesV2("transmageddon"); // media transcoder
        graph.anotateNodesV2("transmission-gtk"); // torrent client
        graph.anotateNodesV2("ufraw"); // Raw image viewer for Sony camera
        graph.anotateNodesV2("usb-creator-gtk"); // Linux installer for usb boot pendrive
        graph.anotateNodesV2("woeusb-frontend-wxgtk"); // Create UEFI compatible Windows 10 installer pendrive
        graph.anotateNodesV2("yelp"); // Gnome desktop help system
        graph.anotateNodesV2("zeitgeist"); // Needed by activity-log-manager

        // Libreoffice
        //graph.anotateNodesV2("libreoffice-gnome");
        //graph.anotateNodesV2("libreoffice-help-en-us");
        //graph.anotateNodesV2("libreoffice-math");
        //graph.anotateNodesV2("libreoffice-ogltrans");
        //graph.anotateNodesV2("libreoffice-pdfimport");
        //graph.anotateNodesV2("libreoffice-style-breeze");
        //graph.anotateNodesV2("libreoffice-calc");
        //graph.anotateNodesV2("libreoffice-avmedia-backend-gstreamer");
        //graph.anotateNodesV2("python3-uno");

        // Gnome session (level 1: gnome panel = applets + indicators)
        graph.anotateNodesV2("indicator-session"); // Logout and settings
        graph.anotateNodesV2("indicator-datetime"); // Hour and date
        graph.anotateNodesV2("indicator-bluetooth"); // Bluetooth sync / audio
        graph.anotateNodesV2("indicator-keyboard"); // Change input language
        graph.anotateNodesV2("indicator-sound"); // Volume controls
        graph.anotateNodesV2("indicator-printers"); // UNTESTED
        graph.anotateNodesV2("indicator-application"); // UNTESTED
        graph.anotateNodesV2("indicator-applet-complete"); // UNTESTED
	graph.anotateNodesV2("indicator-appmenu"); // UNTESTED
	graph.anotateNodesV2("indicator-applet"); // UNTESTED
	graph.anotateNodesV2("indicator-messages"); // UNTESTED
	graph.anotateNodesV2("gir1.2-appindicator3-0.1"); // Used by xpra?
        graph.anotateNodesV2("unity-control-center"); // check if it is needed/wanted
        graph.anotateNodesV2("gnome-applets"); // To review
        graph.anotateNodesV2("gnome-control-center"); // System settings app
	graph.anotateNodesV2("cups-pk-helper"); // Needed by gnome-control-center to enable unlock button on printer settings tab
	graph.anotateNodesV2("python3-cffi-backend"); // needed by unity-control-center
        graph.anotateNodesV2("gnome-panel"); // If this fails, try: gnome-panel --replace &

        // Gnome session (level 2: gdm + window manager + theme + menus)
        graph.anotateNodesV2("gdm3"); // Gnome login manager (replaces lightdm, xdm, etc.)
        graph.anotateNodesV2("alacarte"); // Gnome menu editor
        graph.anotateNodesV2("gnome-session-flashback"); // Metacity based session start for gdm3 menu
        graph.anotateNodesV2("gnome-themes-extra");
        graph.anotateNodesV2("gnome-screensaver");
        graph.anotateNodesV2("xscreensaver");
        graph.anotateNodesV2("xscreensaver-gl");
        graph.anotateNodesV2("metacity"); // Gnome window manager, replaces mwm, twm, etc.
        graph.anotateNodesV2("ubuntu-artwork"); // Make session use Ubuntu colors and background
        graph.anotateNodesV2("ubuntu-settings");
        graph.anotateNodesV2("gnome-startup-applications"); // gnome-session-properties command

        graph.anotateNodesV2("ubuntu-wallpapers-jammy");

        // Third party apps
        graph.anotateNodesV2("github-desktop");
        graph.anotateNodesV2("gitkraken");

        // Other (unused) Gnome packages
        //graph.anotateNodesV2("gnome-user-guide");
        //graph.anotateNodesV2("thunderbird-gnome-support");
        //graph.anotateNodesV2("thunderbird"); // Email reader (like outlook)
        //graph.anotateNodesV2("thunderbird-locale-en-us");
    }

    void markAwsEC2() {
	graph.anotateNodesV2("linux-aws");
	graph.anotateNodesV2("linux-headers-5.19.0-1025-aws");
	graph.anotateNodesV2("linux-modules-5.19.0-1025-aws");
	graph.anotateNodesV2("ec2-hibinit-agent");
	graph.anotateNodesV2("ec2-instance-connect");
	graph.anotateNodesV2("cloud-init");
	graph.anotateNodesV2("chrony");
    }

    void markJedilinkCustom() {
	graph.anotateNodesV2("*renderpark");
	graph.anotateNodesV2("*sdkman");
	graph.anotateNodesV2("*nvidia_driver");
	graph.anotateNodesV2("*povray");
	graph.anotateNodesV2("*eureka");
	graph.anotateNodesV2("*stalker");
	graph.anotateNodesV2("*vk_raytracing_tutorial_KHR");
	graph.anotateNodesV2("*snap-postman");
	graph.anotateNodesV2("*snap-code");
	graph.anotateNodesV2("*snap-clion");
	graph.anotateNodesV2("*snap-intellij-idea-community");
	graph.anotateNodesV2("*davinci-resolve");
	graph.anotateNodesV2("*opencv-4.x");
	graph.anotateNodesV2("*darknet");
	graph.anotateNodesV2("*studio3t");
	graph.anotateNodesV2("*chromium-test-kit");
	graph.anotateNodesV2("*cde"); // Original CDE from git clone https://git.code.sf.net/p/cdesktopenv/code cde
	graph.anotateNodesV2("*nscde"); // CDE like desktop :)
	graph.anotateNodesV2("*lsix"); // Thumbnail image generator for console DEC sixel format https://github.com/hackerb9/lsix
	graph.anotateNodesV2("*eureka");
	graph.anotateNodesV2("*vulkan-tutorial");
	graph.anotateNodesV2("*apitrace");
	graph.anotateNodesV2("awsvpnclient");
	graph.anotateNodesV2("zerotier-one"); // Free VPN :)
	graph.anotateNodesV2("sendanywhere"); // Multi platform file transfer
        graph.anotateNodesV2("resilio-sync"); // Sync files with mobile device :) better than "SendAnywhere"
	graph.anotateNodesV2("darktable"); // Adobe Lightroom replacement :)
    }

    void markRiskVCrossEnvironment() {
	graph.anotateNodesV2("qemu");
	graph.anotateNodesV2("qemu-user-static");
	graph.anotateNodesV2("dpkg-cross");
    }
    
    void markCustomJedilink() {
        // Minimal
        markLinuxMinimal();
        markLinuxBasic();
	markX11Apps();

        // Mid-level
	markLinuxIntermediate();
	markMultimedia();
        markX11Server();	
	markBasicDevelopment();
        markKernelDevelopment();
        markOpenCvDependencies(); // Includes Qt devel
	markOtherDependencies();

	// GUI toolkits - based
        markGtkApps();
        markQtApps();
	markWxApps();
        markKde();
        markGnome();
        markRiskVCrossEnvironment();

	markAwsEC2();
        markJedilinkCustom();
    }
}
