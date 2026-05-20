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
        graph.markPackageAndItsDependencies("linux-image-aws");
        graph.markPackageAndItsDependencies("linux-image-6.8.0-1029-aws");
        graph.markPackageAndItsDependencies("linux-image-6.2.16-custom");
        graph.markPackageAndItsDependencies("linux-image-6.2.0-36-generic");
        graph.markPackageAndItsDependencies("linux-image-6.2.0-32-generic");
        graph.markPackageAndItsDependencies("linux-image-generic");
        graph.markPackageAndItsDependencies("linux-image-5.15.131");
        graph.markPackageAndItsDependencies("linux-image-6.2.0-26-generic");
        graph.markPackageAndItsDependencies("linux-image-6.8.0-62-generic");
	graph.markPackageAndItsDependencies("linux-modules-6.8.0-62-generic");
        graph.markPackageAndItsDependencies("linux-image-6.8.0-63-generic");
	graph.markPackageAndItsDependencies("linux-modules-6.8.0-63-generic");
	graph.markPackageAndItsDependencies("linux-modules-extra-6.8.0-63-generic");
	graph.markPackageAndItsDependencies("linux-modules-extra-6.8.0-62-generic");
	graph.markPackageAndItsDependencies("linux-modules-6.8.0-1029-aws");
        graph.markPackageAndItsDependencies("linux-modules-6.2.0-26-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-5.15.0-79-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-5.15.0-82-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-5.15.0-139-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-6.2.0-32-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-6.2.0-31-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-6.2.0-26-generic");
        graph.markPackageAndItsDependencies("linux-image-generic-hwe-22.04");
        graph.markPackageAndItsDependencies("linux-image-5.15.0-78-generic");
        graph.markPackageAndItsDependencies("linux-image-6.2.0-31-generic");
        graph.markPackageAndItsDependencies("linux-image-5.4.0-99-generic");
        graph.markPackageAndItsDependencies("linux-image-5.4.0-custom");
        graph.markPackageAndItsDependencies("linux-image-5.4.74-custom");
        graph.markPackageAndItsDependencies("linux-modules-5.3.0-62-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-5.4.0-135-generic");
        graph.markPackageAndItsDependencies("linux-modules-extra-5.15.0-78-generic");
        graph.markPackageAndItsDependencies("linux-image-5.3.0-62-generic");
        graph.markPackageAndItsDependencies("linux-image-5.4.0-48-generic");
        graph.markPackageAndItsDependencies("linux-image-generic-hwe-18.04");
	graph.markPackageAndItsDependencies("linux-image-5.4.0-136-generic");
	graph.markPackageAndItsDependencies("linux-modules-extra-5.4.0-136-generic");
        graph.markPackageAndItsDependencies("linux-firmware"); // Check against kernel drivers used

	// Commands used by linux modules if WiFi is needed
        graph.markPackageAndItsDependencies("crda");

        // Libc
        graph.markPackageAndItsDependencies("libc-bin"); // ldd command
        graph.markPackageAndItsDependencies("libc6:i386");
	graph.markPackageAndItsDependencies("locales-all");

        // Encryption libraries
	graph.markPackageAndItsDependencies("libssl1.1"); // Old version used by awsvpnclient and mongo on old CPUs

        // Compression tools
        graph.markPackageAndItsDependencies("lib32z1");
        graph.markPackageAndItsDependencies("libx32z1");
        graph.markPackageAndItsDependencies("bzip2");
        graph.markPackageAndItsDependencies("pigz");
        graph.markPackageAndItsDependencies("lzma");
        graph.markPackageAndItsDependencies("xz-utils");
        graph.markPackageAndItsDependencies("rar");
        graph.markPackageAndItsDependencies("unrar");
        graph.markPackageAndItsDependencies("gzip");
        graph.markPackageAndItsDependencies("zstd");
        graph.markPackageAndItsDependencies("unzip");
        graph.markPackageAndItsDependencies("p7zip-full");
        graph.markPackageAndItsDependencies("zip");

        // Encryption tools
        graph.markPackageAndItsDependencies("ca-certificates"); // Used by Java, Python, Google Chrome, etc.
	
	// Boot
        graph.markPackageAndItsDependencies("bsdutils"); // renice, wall
        graph.markPackageAndItsDependencies("db-util"); // db_dump command to check Berkeley file databases
        graph.markPackageAndItsDependencies("dmsetup"); // RAID manager
        graph.markPackageAndItsDependencies("dosfstools");
        graph.markPackageAndItsDependencies("e2fsprogs");
        graph.markPackageAndItsDependencies("file");
        graph.markPackageAndItsDependencies("fuse");
        graph.markPackageAndItsDependencies("fuse3");
        graph.markPackageAndItsDependencies("ifupdown"); // Reads /etc/network/interfaces at boot up
        graph.markPackageAndItsDependencies("info");
        graph.markPackageAndItsDependencies("init");
        graph.markPackageAndItsDependencies("kbd"); // showconsolefont command
        graph.markPackageAndItsDependencies("kmod"); // lsmod / rmmod commands
        graph.markPackageAndItsDependencies("language-pack-en"); // avoid fail messages
        graph.markPackageAndItsDependencies("login");
        graph.markPackageAndItsDependencies("dbus"); // Used by some low level session operations as such reboot and send message to ptys
        graph.markPackageAndItsDependencies("net-tools"); // ifconfig
        graph.markPackageAndItsDependencies("module-init-tools");
        graph.markPackageAndItsDependencies("ntfs-3g"); 
        graph.markPackageAndItsDependencies("sed"); // To check why is this needed
        graph.markPackageAndItsDependencies("sysvinit-utils"); // To check why is this needed
        graph.markPackageAndItsDependencies("ucf"); // To check why is this needed
        graph.markPackageAndItsDependencies("udev"); // To check why is this needed
        graph.markPackageAndItsDependencies("util-linux"); // Commands as such fsck, mkfs*, 
	
        // Basic C/C++ system   
        graph.markPackageAndItsDependencies("apt-utils"); // Extra commands used by apt programs
        graph.markPackageAndItsDependencies("apt");
        graph.markPackageAndItsDependencies("bash"); // Shell
        graph.markPackageAndItsDependencies("bash-completion"); // For bash
        graph.markPackageAndItsDependencies("cpufrequtils"); // To change performance / powersafe CPU policy
        graph.markPackageAndItsDependencies("dialog"); // used on apt scripts
        graph.markPackageAndItsDependencies("diffutils");
        graph.markPackageAndItsDependencies("eject");
        graph.markPackageAndItsDependencies("find");
        graph.markPackageAndItsDependencies("findutils");
        graph.markPackageAndItsDependencies("grep");
        graph.markPackageAndItsDependencies("hostname");
        graph.markPackageAndItsDependencies("htop");
        graph.markPackageAndItsDependencies("neofetch"); // Text mode system resume
        graph.markPackageAndItsDependencies("less");
        graph.markPackageAndItsDependencies("mtools"); // Access to DOS FAT floppies
        graph.markPackageAndItsDependencies("nano"); // Simple text editor
        graph.markPackageAndItsDependencies("ncurses-base"); // Used by nano and other console apps
        graph.markPackageAndItsDependencies("read-edid");
        graph.markPackageAndItsDependencies("sudo");
        graph.markPackageAndItsDependencies("tcsh"); // Shell
        graph.markPackageAndItsDependencies("time");
        graph.markPackageAndItsDependencies("powermgmt-base"); // on_ac_power command
        graph.markPackageAndItsDependencies("tofrodos"); // Convert text files between DOS/Unix formats
        graph.markPackageAndItsDependencies("vim-tiny");
        graph.markPackageAndItsDependencies("zsh");
        graph.markPackageAndItsDependencies("psmisc"); // pstree command
        graph.markPackageAndItsDependencies("netbase"); // Network info files on /etc
        graph.markPackageAndItsDependencies("binfmt-support"); // Some magic to run for example, ARM machine instructions on X86 by using QEMU

        // Pending to verify why those should be essential
        graph.markPackageAndItsDependencies("dash"); // To check why is this needed (initramfs boot?)
        graph.markPackageAndItsDependencies("mawk"); // Used on /etc/* bash completion and other awk scripts
        graph.markPackageAndItsDependencies("base-passwd"); // To check why is this needed by apt

	// Temporary test
	graph.markPackageAndItsDependencies("openssh-client");
	graph.markPackageAndItsDependencies("initramfs-tools");
    }

    private void markX11Apps() {
        graph.markPackageAndItsDependencies("zulu17-jdk"); // Java SDK
        graph.markPackageAndItsDependencies("x11-apps");
        graph.markPackageAndItsDependencies("fig2dev");
        graph.markPackageAndItsDependencies("uil"); // Motif
	graph.markPackageAndItsDependencies("dclock");
        graph.markPackageAndItsDependencies("feh"); // minimalistic image viewer
        graph.markPackageAndItsDependencies("gmemusage");
        graph.markPackageAndItsDependencies("mesa-utils");
        graph.markPackageAndItsDependencies("mwm");
        graph.markPackageAndItsDependencies("oneko");
        graph.markPackageAndItsDependencies("twm");
        graph.markPackageAndItsDependencies("x11-utils");
        graph.markPackageAndItsDependencies("xauth"); // needed for tunneling X11 on SSH connections
        graph.markPackageAndItsDependencies("xfig");
        graph.markPackageAndItsDependencies("xfig-libs");
        graph.markPackageAndItsDependencies("xfonts-base"); // 10x20 and other basic X11 fonts
        graph.markPackageAndItsDependencies("xfishtank");
        graph.markPackageAndItsDependencies("xinput"); // used on X11 to list and manage HIDs
        graph.markPackageAndItsDependencies("xosview");
        graph.markPackageAndItsDependencies("xterm");
        graph.markPackageAndItsDependencies("xzoom");
        graph.markPackageAndItsDependencies("chocolate-doom");
        graph.markPackageAndItsDependencies("freedoom");
        graph.markPackageAndItsDependencies("gsfonts-x11"); // make GS fonts available to X (not used, to check)
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
        graph.markPackageAndItsDependencies("base-files"); // Unix directories
        graph.markPackageAndItsDependencies("bluez"); // Bluetooth control (bluetoothctl)
        graph.markPackageAndItsDependencies("bluez-tools"); // bt-device, etc
        graph.markPackageAndItsDependencies("console-setup-linux"); // configure keyboard, psf bitmap font files for VGA text mode
        graph.markPackageAndItsDependencies("coreutils"); // cat cp date dd df rm, etc
        graph.markPackageAndItsDependencies("cpu-checker"); // kvm-ok command checks if CPU/BIOS has VT-X support
        graph.markPackageAndItsDependencies("cron");
        graph.markPackageAndItsDependencies("logrotate"); // Rotate and compress logs via crontab
        graph.markPackageAndItsDependencies("dcraw"); // Import Sony ARW raw files
        graph.markPackageAndItsDependencies("dmidecode"); // BIOS utils
        graph.markPackageAndItsDependencies("exif");
        graph.markPackageAndItsDependencies("file");
        graph.markPackageAndItsDependencies("gddrescue"); // low level backup tool
        graph.markPackageAndItsDependencies("gpgv1");
        graph.markPackageAndItsDependencies("gpgv2");
        graph.markPackageAndItsDependencies("hdparm");
        graph.markPackageAndItsDependencies("libcomerr2");
        graph.markPackageAndItsDependencies("libcap2-bin"); // Examine file capabilities (getcap)
        graph.markPackageAndItsDependencies("lshw");
        graph.markPackageAndItsDependencies("lsof");
        graph.markPackageAndItsDependencies("lsscsi"); // List installed SCSI devices / disks
        graph.markPackageAndItsDependencies("lrzsz"); // zmodem, xmodem - used by minicom
        graph.markPackageAndItsDependencies("lua5.2");
        graph.markPackageAndItsDependencies("lvm2"); // Tools for handling logical RAID devices, needed by crontab, needed to mount LVMs from initramfs if used
        graph.markPackageAndItsDependencies("man-db");
        graph.markPackageAndItsDependencies("nvme-cli"); // Tools for checking NVME (M2) storage devices
        graph.markPackageAndItsDependencies("ncurses-base");
        graph.markPackageAndItsDependencies("ncurses-bin");
        graph.markPackageAndItsDependencies("ncurses-term");
        graph.markPackageAndItsDependencies("jq"); // JSON pretty print
        graph.markPackageAndItsDependencies("xxd"); // HEX binary pretty print
        graph.markPackageAndItsDependencies("pciutils");
        graph.markPackageAndItsDependencies("ppp");
        graph.markPackageAndItsDependencies("iw"); // wireless / WiFi
        graph.markPackageAndItsDependencies("rsyslog"); // Copies the kernel ring buffer (dmesg) to /var/log/syslog
        graph.markPackageAndItsDependencies("smartmontools"); // Tools to check ATA devices
        graph.markPackageAndItsDependencies("tofrodos");
        graph.markPackageAndItsDependencies("tzdata"); // Timezones, needed to change system date / time location
        graph.markPackageAndItsDependencies("ufraw-batch");
        graph.markPackageAndItsDependencies("whiptail");
	graph.markPackageAndItsDependencies("wodim"); // CD/DVD burner

        // Internet command line tools and applications
        graph.markPackageAndItsDependencies("bridge-utils"); // eth. for machines with more than 1 interface
        graph.markPackageAndItsDependencies("curl");
        graph.markPackageAndItsDependencies("dnsutils"); // nslookup command
        graph.markPackageAndItsDependencies("bind9"); // DNS server
        graph.markPackageAndItsDependencies("bind9-dnsutils"); // nslookup command
        graph.markPackageAndItsDependencies("dnsmasq-base"); // DNS server? different to isc? pending to test
        graph.markPackageAndItsDependencies("ethtool");
        graph.markPackageAndItsDependencies("iptables");
        graph.markPackageAndItsDependencies("hostapd"); // Access Point (ap) for Wifi adapters :)
        graph.markPackageAndItsDependencies("iperf"); // Like speedtest but local
        graph.markPackageAndItsDependencies("iputils-ping"); // Level 3 (TCP/IP)
        graph.markPackageAndItsDependencies("iputils-arping"); // Level 2 (ARP) pint
        graph.markPackageAndItsDependencies("fping"); // Ping for ICMP
        graph.markPackageAndItsDependencies("isc-dhcp-common"); // Manual pages for DHCP?
        graph.markPackageAndItsDependencies("isc-dhcp-client"); // dhclient command for connecting to the net using DHCP
        graph.markPackageAndItsDependencies("minicom");
        graph.markPackageAndItsDependencies("mongodb-org");
        graph.markPackageAndItsDependencies("mongodb-mongosh");
        graph.markPackageAndItsDependencies("nfs-kernel-server"); // NFS to share with unix (used to mount local photos on cubestudio.co)
        graph.markPackageAndItsDependencies("nfs-common"); // NFS client to mount externally shared volumes
        graph.markPackageAndItsDependencies("lynx");
        graph.markPackageAndItsDependencies("ifuse"); // Mount iphone as USB storage to access camera images
        graph.markPackageAndItsDependencies("irqbalance"); // Interrupt optimizer for SMP systems
        graph.markPackageAndItsDependencies("mtr-tiny"); // ncurses traceroute
        graph.markPackageAndItsDependencies("numactl"); // Memory optimizer for SMP systems (i.e. mongodb systemctl script)
        graph.markPackageAndItsDependencies("openssh-client");
        graph.markPackageAndItsDependencies("ntp"); // Network time protocol
        graph.markPackageAndItsDependencies("systemd-timesyncd"); // Network time protocol on ubuntu 24.04
        graph.markPackageAndItsDependencies("systemd-sysv"); // reboot command
        graph.markPackageAndItsDependencies("sntp"); // Network time protocol
        graph.markPackageAndItsDependencies("openssh-server");
        graph.markPackageAndItsDependencies("tcpdump"); // Network traffic analyzer (sniffer)
        graph.markPackageAndItsDependencies("traceroute");
        graph.markPackageAndItsDependencies("w3m"); // Similar to Lynx
        graph.markPackageAndItsDependencies("w3m-img");
        graph.markPackageAndItsDependencies("wget");
        graph.markPackageAndItsDependencies("aria2"); // Parallel downloader
        graph.markPackageAndItsDependencies("wireless-tools"); // iwconfig command to turn on WiFi from command line
        graph.markPackageAndItsDependencies("wpasupplicant"); // to turn on WiFi from command line
        graph.markPackageAndItsDependencies("zerotier-one"); // Nice VPN service agent
        graph.markPackageAndItsDependencies("iftop"); // Monitor network interfaces
        graph.markPackageAndItsDependencies("isc-dhcp-server");

        // Use Iphone tethering via USB
        graph.markPackageAndItsDependencies("usbmuxd");
        graph.markPackageAndItsDependencies("libimobiledevice-utils");
        graph.markPackageAndItsDependencies("ipheth-utils");

        // Using boost
        graph.markPackageAndItsDependencies("aptitude");

        // Sometimes not needed
	graph.markPackageAndItsDependencies("grub2");
        graph.markPackageAndItsDependencies("grub-pc");
        graph.markPackageAndItsDependencies("efibootmgr"); // Access EFI options on BIOS, used by grub when installing
	graph.markPackageAndItsDependencies("shim-signed");
	graph.markPackageAndItsDependencies("secureboot-db");
	graph.markPackageAndItsDependencies("mokutil");
        graph.markPackageAndItsDependencies("initramfs-tools");
        graph.markPackageAndItsDependencies("os-prober");
        graph.markPackageAndItsDependencies("locales");
        graph.markPackageAndItsDependencies("syslinux-common");
        graph.markPackageAndItsDependencies("cpio");
        graph.markPackageAndItsDependencies("intel-microcode"); // Updates for bugs/security issues for CPU (intel) - fixes Meltdown/Spectrum bugs and such
        
        // Applications
        graph.markPackageAndItsDependencies("telnet");
        graph.markPackageAndItsDependencies("tftpd-hpa");
        graph.markPackageAndItsDependencies("tftp-hpa");
        graph.markPackageAndItsDependencies("tftp"); // Tftp client for testing server
        graph.markPackageAndItsDependencies("pxelinux");
        graph.markPackageAndItsDependencies("ftp");
        graph.markPackageAndItsDependencies("whois"); // Tool to get information about DNS domains
        graph.markPackageAndItsDependencies("gpm");
        graph.markPackageAndItsDependencies("mongodb");
        graph.markPackageAndItsDependencies("mongo-tools"); // mongodump, mongorestore, etc

        // For small systems not using opensshd
        graph.markPackageAndItsDependencies("telnetd");
        graph.markPackageAndItsDependencies("ftpd");

        // Others
        graph.markPackageAndItsDependencies("apparmor-utils"); // aa-status and other aa-* commands
        graph.markPackageAndItsDependencies("apparmor-profiles"); // /etc settings for controlling security profile permissions
	graph.markPackageAndItsDependencies("apparmor"); // Needed by snapd
	graph.markPackageAndItsDependencies("snapd"); // Universal linux distribution independent installer / appstore and app sandbox environment manager

	graph.markPackageAndItsDependencies("sysvbanner");
	graph.markPackageAndItsDependencies("tree");
	
        markLinuxMinimal();
    }

    private void markMultimedia() {
        // Unknown!
        graph.markPackageAndItsDependencies("chromium-browser");
        graph.markPackageAndItsDependencies("chromium-chromedriver");
        graph.markPackageAndItsDependencies("chromium-browser-l10n");

        // Multimedia
        graph.markPackageAndItsDependencies("bb"); // ASCII art demo :)
        graph.markPackageAndItsDependencies("jp2a"); // image to ASCII art conversor
        graph.markPackageAndItsDependencies("toilet"); // Text console banners with special ASCII art fonts
        graph.markPackageAndItsDependencies("caca-utils"); // ASCII art demos, in color :)
        graph.markPackageAndItsDependencies("toilet-fonts"); // Fonts used by caca-utils
        graph.markPackageAndItsDependencies("bolt"); // Control thunderbolt port / devices
        graph.markPackageAndItsDependencies("pulseaudio"); // System sound server/daemon
        graph.markPackageAndItsDependencies("vdpauinfo");
        graph.markPackageAndItsDependencies("vdpau-driver-all");
        graph.markPackageAndItsDependencies("espeak");
        graph.markPackageAndItsDependencies("lm-sensors");
        graph.markPackageAndItsDependencies("usbutils");
        graph.markPackageAndItsDependencies("usb.ids");
        graph.markPackageAndItsDependencies("alsa-utils");
        graph.markPackageAndItsDependencies("flac");
        graph.markPackageAndItsDependencies("ffmpeg");
        graph.markPackageAndItsDependencies("lame");
        graph.markPackageAndItsDependencies("libavfilter-extra6"); // extended version ffmpeg
        graph.markPackageAndItsDependencies("libavcodec-extra57"); // extended version ffmpeg
        graph.markPackageAndItsDependencies("libsixel-bin"); // DIGITAL text/image ASCII art format
	graph.markPackageAndItsDependencies("mlterm"); // Emulador de terminal para X11 compatible con formato sixel de VT330
        graph.markPackageAndItsDependencies("libjpeg-progs");
        graph.markPackageAndItsDependencies("libpng-tools");
        graph.markPackageAndItsDependencies("libsox-fmt-ao");
        graph.markPackageAndItsDependencies("libsox-fmt-oss");
        graph.markPackageAndItsDependencies("libsox-fmt-pulse");
        graph.markPackageAndItsDependencies("libwmf-bin");
        graph.markPackageAndItsDependencies("libfftw3-single3");
        graph.markPackageAndItsDependencies("libde265-0");
        graph.markPackageAndItsDependencies("libasound2-data");
        graph.markPackageAndItsDependencies("mpg123");
        graph.markPackageAndItsDependencies("netpbm");
        graph.markPackageAndItsDependencies("opus-tools");
        graph.markPackageAndItsDependencies("sox");
        graph.markPackageAndItsDependencies("v4l-utils");
        graph.markPackageAndItsDependencies("vorbis-tools");
        graph.markPackageAndItsDependencies("mplayer"); // Python2.7 based
	graph.markPackageAndItsDependencies("clinfo"); // Get info from CUDA context for OpenCL
        graph.markPackageAndItsDependencies("libavfilter6"); // fmpeg libs
        graph.markPackageAndItsDependencies("lsdvd");	
        graph.markPackageAndItsDependencies("hwloc");
        graph.markPackageAndItsDependencies("libhwloc-plugins");
        graph.markPackageAndItsDependencies("libheif-examples"); // Converts .HEIC IOS image files
        graph.markPackageAndItsDependencies("pipewire-pulse"); // Jack-like routing audio library with spacialitation
        graph.markPackageAndItsDependencies("pulsemixer"); // Console based volume control for pulseaudio
	
        // Pending to test utilities
        graph.markPackageAndItsDependencies("alsa-base");
        graph.markPackageAndItsDependencies("libsoxr-lsr0");
        graph.markPackageAndItsDependencies("libmtp-runtime");
        graph.markPackageAndItsDependencies("cdrdao");
        graph.markPackageAndItsDependencies("vcdimager");
        graph.markPackageAndItsDependencies("libraw1394-tools");
        graph.markPackageAndItsDependencies("twolame");
        graph.markPackageAndItsDependencies("cdparanoia");
        graph.markPackageAndItsDependencies("libbdplus0"); // Blue ray disc reader
        graph.markPackageAndItsDependencies("libaacs0"); // AACS cyptographic content protection
        graph.markPackageAndItsDependencies("freepats"); // MIDI audio synthesis

        // Multimedia applications
        graph.markPackageAndItsDependencies("gphoto2"); // External camera control (Sony)
        graph.markPackageAndItsDependencies("xawtv"); // light v4l client
        graph.markPackageAndItsDependencies("fontforge"); // fonts editor
        graph.markPackageAndItsDependencies("blender"); // 3d modeller
        graph.markPackageAndItsDependencies("mupen64plus-ui-console");
        graph.markPackageAndItsDependencies("libmupen64plus2");
        graph.markPackageAndItsDependencies("mupen64plus-video-arachnoid");
        graph.markPackageAndItsDependencies("mupen64plus-video-glide64");
        graph.markPackageAndItsDependencies("mupen64plus-video-z64");
        graph.markPackageAndItsDependencies("mupen64plus-video-all");
        graph.markPackageAndItsDependencies("transcode");
        graph.markPackageAndItsDependencies("transcode-doc");

        graph.markPackageAndItsDependencies("testdisk");
    }
    
    private void markX11Server() {
        // X11 server
        graph.markPackageAndItsDependencies("xdm");
        graph.markPackageAndItsDependencies("xinit");
        graph.markPackageAndItsDependencies("xserver-xorg-core"); // This contains /usr/bin/X
        graph.markPackageAndItsDependencies("xserver-xorg-input-all"); // X11 server could react to mouse and keyboard
        graph.markPackageAndItsDependencies("xnest");
        graph.markPackageAndItsDependencies("xbase-clients");
        graph.markPackageAndItsDependencies("xserver-xephyr");
        graph.markPackageAndItsDependencies("xserver-xorg-video-intel"); // Needed on taciturna
        graph.markPackageAndItsDependencies("xserver-xorg-video-fbdev"); // Needed by intel + xdm
        graph.markPackageAndItsDependencies("xserver-xorg-video-vesa"); // Needed by intel + xdm
	graph.markPackageAndItsDependencies("x11-session-utils");
        graph.markPackageAndItsDependencies("xvfb");
        graph.markPackageAndItsDependencies("x11vnc");
    }
    
    private void markBasicDevelopment() {
        // General software development tools
        graph.markPackageAndItsDependencies("build-essential"); // Recommended kit for nvidia driver installation
        graph.markPackageAndItsDependencies("cloc"); // Source code line counter
        graph.markPackageAndItsDependencies("automake");
        graph.markPackageAndItsDependencies("autoconf2.64");
        graph.markPackageAndItsDependencies("autopoint"); // GNU gettext? why is this included?
        graph.markPackageAndItsDependencies("scons"); // Similar to make or cmake
        graph.markPackageAndItsDependencies("cmake");
        graph.markPackageAndItsDependencies("cmake-curses-gui");
        graph.markPackageAndItsDependencies("debhelper");
        graph.markPackageAndItsDependencies("debootstrap");
        graph.markPackageAndItsDependencies("doxygen");
        graph.markPackageAndItsDependencies("dpkg-dev"); // Can build debian packages from source
        graph.markPackageAndItsDependencies("devscripts"); // Can build debian packages from source
	graph.markPackageAndItsDependencies("perl-openssl-defaults"); // Needed by devscripts
        graph.markPackageAndItsDependencies("fakeroot");
        graph.markPackageAndItsDependencies("fdisk");
        graph.markPackageAndItsDependencies("lsb-release"); // lsb_release -a command to identify Ubuntu version
        graph.markPackageAndItsDependencies("gdisk"); // fdisk for GPT partitions
        graph.markPackageAndItsDependencies("parted"); // Filesystem operations
        graph.markPackageAndItsDependencies("xfsprogs"); // Used on XFS (i.e. mongod)
        graph.markPackageAndItsDependencies("g++");
        graph.markPackageAndItsDependencies("gawk");
        graph.markPackageAndItsDependencies("gperf"); // Why is this included? Hash functions?
        graph.markPackageAndItsDependencies("linux-generic");
        graph.markPackageAndItsDependencies("linux-generic-hwe-22.04");
        graph.markPackageAndItsDependencies("linux-aws-tools-6.8.0-1029");
        graph.markPackageAndItsDependencies("linux-tools-6.8.0-1029-aws");
        graph.markPackageAndItsDependencies("linux-headers-generic");
        graph.markPackageAndItsDependencies("linux-headers-6.8.0-62-generic");
        graph.markPackageAndItsDependencies("linux-headers-6.8.0-63-generic");
        graph.markPackageAndItsDependencies("linux-headers-aws");
        graph.markPackageAndItsDependencies("linux-headers-6.8.0-1029-aws");
        graph.markPackageAndItsDependencies("linux-aws-headers-6.8.0-1029");
        graph.markPackageAndItsDependencies("linux-headers-5.15.131");
        graph.markPackageAndItsDependencies("linux-headers-5.15.0-139-generic");
        graph.markPackageAndItsDependencies("linux-headers-6.2.16-custom");
        graph.markPackageAndItsDependencies("linux-headers-6.2.0-32-generic");
        graph.markPackageAndItsDependencies("linux-headers-6.2.0-36-generic");
        graph.markPackageAndItsDependencies("linux-headers-6.2.0-31-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.15.0-82-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.15.0-79-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.15.0-78-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.4.0-136-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.4.0-99-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.3.0-62-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.4.0-custom");
        graph.markPackageAndItsDependencies("linux-headers-5.4.74-custom");
        graph.markPackageAndItsDependencies("linux-headers-5.4.0-48-generic");
        graph.markPackageAndItsDependencies("linux-headers-5.4.0-135-generic");
        graph.markPackageAndItsDependencies("linux-headers-generic-hwe-18.04");
        graph.markPackageAndItsDependencies("ltrace");
        graph.markPackageAndItsDependencies("libtool");
        graph.markPackageAndItsDependencies("liblapack-dev");
        graph.markPackageAndItsDependencies("liblapacke-dev");
        graph.markPackageAndItsDependencies("libmotif-dev");
        graph.markPackageAndItsDependencies("libosmesa6-dev");
        graph.markPackageAndItsDependencies("mdadm"); // Needed on systems with hardware/BIOS RAID volumes
        graph.markPackageAndItsDependencies("make");
        graph.markPackageAndItsDependencies("manpages");
        graph.markPackageAndItsDependencies("manpages-dev");
        graph.markPackageAndItsDependencies("nasm");
        graph.markPackageAndItsDependencies("yasm");
        graph.markPackageAndItsDependencies("git");
        graph.markPackageAndItsDependencies("gdb");
        graph.markPackageAndItsDependencies("libboost-regex1.74.0"); // Needed by gdb
        graph.markPackageAndItsDependencies("gdbserver");
        graph.markPackageAndItsDependencies("gource");
        graph.markPackageAndItsDependencies("python3-pip");
        graph.markPackageAndItsDependencies("strace");
        graph.markPackageAndItsDependencies("subversion");
        graph.markPackageAndItsDependencies("valgrind");
        graph.markPackageAndItsDependencies("libcfitsio-doc");

	// X11 / GL dev
        graph.markPackageAndItsDependencies("freeglut3-dev");
        graph.markPackageAndItsDependencies("x11proto-input-dev");
        graph.markPackageAndItsDependencies("x11proto-kb-dev");
        graph.markPackageAndItsDependencies("x11proto-render-dev");
        graph.markPackageAndItsDependencies("webp");
        graph.markPackageAndItsDependencies("libxpm-dev"); // Used by rpk
        graph.markPackageAndItsDependencies("x11proto-print-dev"); // Used by rpk
        graph.markPackageAndItsDependencies("libglm-dev"); // Window kit replacing glut and being used by newer OpenGL and Vulkan apps
	graph.markPackageAndItsDependencies("libglfw3-dev");

        // 32-bit development environment (test with rpk/Renderpark project)
        graph.markPackageAndItsDependencies("g++-multilib");
        graph.markPackageAndItsDependencies("freeglut3:i386");
        graph.markPackageAndItsDependencies("libstdc++-7-dev:i386"); // review this (dev?)
        graph.markPackageAndItsDependencies("libosmesa6:i386");
        graph.markPackageAndItsDependencies("libxm4:i386");
        graph.markPackageAndItsDependencies("libxft2:i386");
        graph.markPackageAndItsDependencies("libtiff5:i386");

        // Media dev
        graph.markPackageAndItsDependencies("libexif-doc");
	
        // Profilers & debuggers
	graph.markPackageAndItsDependencies("linux-tools-common"); // Contains perf
	
        // Python3 development (can build tensorflow)
	graph.markPackageAndItsDependencies("gitsome"); // For gh command
        graph.markPackageAndItsDependencies("python3-pip");
        graph.markPackageAndItsDependencies("python3-dev");
        graph.markPackageAndItsDependencies("python3-jinja2");
        graph.markPackageAndItsDependencies("git");

        // Can build dcraw
	graph.markPackageAndItsDependencies("liblcms2-dev");

        // Other commonly used
        graph.markPackageAndItsDependencies("libraw-dev"); // DCraw derived library to import raw images from digital cameras
        graph.markPackageAndItsDependencies("arduino");
        graph.markPackageAndItsDependencies("docker-ce");
        graph.markPackageAndItsDependencies("docker-ce-rootless-extras");
        graph.markPackageAndItsDependencies("docker-compose-plugin");
        graph.markPackageAndItsDependencies("docker-scan-plugin");
        graph.markPackageAndItsDependencies("docker-compose");
        graph.markPackageAndItsDependencies("clang-tidy");
    }

    private void markKernelDevelopment() {
        // Can compile kernel
        //graph.markPackageAndItsDependencies("linux-generic"); // Not true after kernel v4
        graph.markPackageAndItsDependencies("kernel-package");
        graph.markPackageAndItsDependencies("libncurses5-dev");
        graph.markPackageAndItsDependencies("flex");
        graph.markPackageAndItsDependencies("bison");
        graph.markPackageAndItsDependencies("libssl-dev");
        graph.markPackageAndItsDependencies("libelf-dev");
        graph.markPackageAndItsDependencies("rsync");
        graph.markPackageAndItsDependencies("debhelper");
        graph.markPackageAndItsDependencies("pkg-config");
        graph.markPackageAndItsDependencies("liborc-0.4-dev-bin");
        graph.markPackageAndItsDependencies("gtk+-2.0"); // make gconfig
        graph.markPackageAndItsDependencies("gmodule-2.0");
        graph.markPackageAndItsDependencies("libglade2-0");
        graph.markPackageAndItsDependencies("libglade2-dev");

        //graph.markPackageAndItsDependencies("libqt4-dev"); // For X11 menu, should include qt devel
        //graph.markPackageAndItsDependencies("qt4-dev-tools"); // make xconfig
    }
    
    private void markGtkApps() {
        // Pending to locate
        graph.markPackageAndItsDependencies("fonts-inconsolata"); // unlinked dependency!
        graph.markPackageAndItsDependencies("fonts-cantarell"); // unlinked dependency!

        // GTK apps
        graph.markPackageAndItsDependencies("lshw-gtk"); // List hardware elements
        graph.markPackageAndItsDependencies("soundmodem"); // TCP/IP stack over audio :)
        graph.markPackageAndItsDependencies("baobab"); // disk usage!
        graph.markPackageAndItsDependencies("aisleriot"); // /usr/games/sol
        graph.markPackageAndItsDependencies("pavucontrol");
        graph.markPackageAndItsDependencies("ghex");
        graph.markPackageAndItsDependencies("gparted");
        graph.markPackageAndItsDependencies("gtkterm"); // Serial, minicom like program with GUI
        graph.markPackageAndItsDependencies("inkscape");
        graph.markPackageAndItsDependencies("view3dscene");
        //graph.markPackageAndItsDependencies("libmagic-dev"); // needed by inkscape (export files)
        //graph.markPackageAndItsDependencies("python-pip"); // Python2, needed by inkscape
        // For inkscape: pip install lxml scour unicode libmagic file
        graph.markPackageAndItsDependencies("emacs");
        graph.markPackageAndItsDependencies("emacs25-el");
        graph.markPackageAndItsDependencies("firefox");
        graph.markPackageAndItsDependencies("firefox-locale-en");
        graph.markPackageAndItsDependencies("imagemagick");
        graph.markPackageAndItsDependencies("chafa"); // Image processing - downscaler, dithering, ASCII art
        graph.markPackageAndItsDependencies("wireshark-gtk"); // Packet analizer / sniffer
        graph.markPackageAndItsDependencies("wireshark"); 
        graph.markPackageAndItsDependencies("tshark"); 
        //graph.markPackageAndItsDependencies("wireshark-qt"); // can not be used along wx version
        graph.markPackageAndItsDependencies("xsane"); // Scanner software, not compatible with Canon Pixma TS9551C
	graph.markPackageAndItsDependencies("xpra"); // Efficient remote X11 connections
        graph.markPackageAndItsDependencies("xdg-utils"); // Used by google chrome
	graph.markPackageAndItsDependencies("baobab"); // Similar to WinDirStat (visualization of disk space)
	graph.markPackageAndItsDependencies("celestia"); // Extra repo
	graph.markPackageAndItsDependencies("code"); // Visual Studio code editor
	
        // GTK themes
        graph.markPackageAndItsDependencies("adwaita-icon-theme-full");
        graph.markPackageAndItsDependencies("yaru-theme-gnome-shell"); // Default Look and Feel for Gnome
        graph.markPackageAndItsDependencies("yaru-theme-icon");
        graph.markPackageAndItsDependencies("fonts-ubuntu"); // Contains fonts used by yaru theme
        graph.markPackageAndItsDependencies("gnome-icon-theme");
	
	// GTK + Python2.7 based apps
        graph.markPackageAndItsDependencies("gimp");
        graph.markPackageAndItsDependencies("gimp-ufraw");
        graph.markPackageAndItsDependencies("dia");
        graph.markPackageAndItsDependencies("dia-shapes");
        graph.markPackageAndItsDependencies("zenmap"); // nmapfe - packet prober
        graph.markPackageAndItsDependencies("nmap");

        // Custom repositories
        graph.markPackageAndItsDependencies("google-earth-pro-stable");
        graph.markPackageAndItsDependencies("awsvpnclient");
        graph.markPackageAndItsDependencies("code");
    }
    
    private void markOtherDependencies() {
        // Povray 3.7 dependencies
        graph.markPackageAndItsDependencies("libz-dev");
        graph.markPackageAndItsDependencies("libpng-dev");
        graph.markPackageAndItsDependencies("libjpeg-dev");
        graph.markPackageAndItsDependencies("libtiff-dev");
        graph.markPackageAndItsDependencies("libboost-dev");
        graph.markPackageAndItsDependencies("libopenexr-dev");	
        graph.markPackageAndItsDependencies("libsdl1.2-dev");
        //graph.markPackageAndItsDependencies("libtiff5-dev");
        //graph.markPackageAndItsDependencies("libsdl-dev");
        //graph.markPackageAndItsDependencies("libboost-date-time-dev");

        // JED development dependencies
        graph.markPackageAndItsDependencies("libmotif-dev");
}

    private void markOpenCvDependencies() {
        // Requires to have install custom:
	// - Java
	// - Ant
	// - Cuda + Nvidia drivers + cudnn
        graph.markPackageAndItsDependencies("gdb");
        graph.markPackageAndItsDependencies("g++-7");
        graph.markPackageAndItsDependencies("python3-bs4");
        graph.markPackageAndItsDependencies("libblas-dev");
        graph.markPackageAndItsDependencies("doxygen");
        graph.markPackageAndItsDependencies("libfreetype6-dev");
        graph.markPackageAndItsDependencies("libharfbuzz-dev");
        graph.markPackageAndItsDependencies("libhdf5-dev");
        graph.markPackageAndItsDependencies("libgflags-dev");
        graph.markPackageAndItsDependencies("libjpeg-dev");
        graph.markPackageAndItsDependencies("libjpeg8-dev");
        graph.markPackageAndItsDependencies("libopenjp2-7-dev");
        graph.markPackageAndItsDependencies("libjpeg-turbo8-dev");
        graph.markPackageAndItsDependencies("libavcodec-dev");
        graph.markPackageAndItsDependencies("libavformat-dev");
        graph.markPackageAndItsDependencies("libavresample-dev");
        graph.markPackageAndItsDependencies("libavutil-dev");
        graph.markPackageAndItsDependencies("libdc1394-22-dev");
        graph.markPackageAndItsDependencies("libgphoto2-dev");
        graph.markPackageAndItsDependencies("libswscale-dev");
        graph.markPackageAndItsDependencies("libopenexr-dev");
        graph.markPackageAndItsDependencies("libpng-dev");
        graph.markPackageAndItsDependencies("libprotobuf-dev");
        graph.markPackageAndItsDependencies("libtesseract-dev");
        graph.markPackageAndItsDependencies("libtiff-dev");
        graph.markPackageAndItsDependencies("libwebp-dev");
        graph.markPackageAndItsDependencies("libpixman-1-dev");
        graph.markPackageAndItsDependencies("zlib1g-dev");
        graph.markPackageAndItsDependencies("libopenjp2-7-dev");
        graph.markPackageAndItsDependencies("libxine2-dev");
        graph.markPackageAndItsDependencies("libgdal-dev");
        graph.markPackageAndItsDependencies("libv4l-dev");
        graph.markPackageAndItsDependencies("libpthread-stubs0-dev");
        graph.markPackageAndItsDependencies("libeigen3-dev");
	graph.markPackageAndItsDependencies("libatlas-base-dev");
        graph.markPackageAndItsDependencies("libgtk-3-dev");
	graph.markPackageAndItsDependencies("libgtkglext1-dev");
        graph.markPackageAndItsDependencies("libqt4-opengl-dev");
        graph.markPackageAndItsDependencies("libgstreamer-plugins-base1.0-dev");
        graph.markPackageAndItsDependencies("libhdf5-mpich-dev");

        //graph.markPackageAndItsDependencies("libz-dev");
        //graph.markPackageAndItsDependencies("libgtk2.0-dev");

        // Custom repositories
	graph.markPackageAndItsDependencies("cuda");
	graph.markPackageAndItsDependencies("cuda-repo-ubuntu2204-12-2-local");
	graph.markPackageAndItsDependencies("cudnn-local-repo-ubuntu2204-8.9.3.28");
        graph.markPackageAndItsDependencies("libcudnn7-dev");
        graph.markPackageAndItsDependencies("cuda-cudart-dev-10-0");
	graph.markPackageAndItsDependencies("gnupg1");
	graph.markPackageAndItsDependencies("gnupg2");

    	// NVIDIA
        graph.markPackageAndItsDependencies("nvidia-prime");
        graph.markPackageAndItsDependencies("nvidia-modprobe");
        graph.markPackageAndItsDependencies("libcudnn7-doc");
        graph.markPackageAndItsDependencies("libnvinfer-dev");
        graph.markPackageAndItsDependencies("cuda-tools-10-0");
        graph.markPackageAndItsDependencies("cuda-documentation-10-0");
        graph.markPackageAndItsDependencies("nvidia-driver-410");
        graph.markPackageAndItsDependencies("cuda-libraries-dev-10-0");
        graph.markPackageAndItsDependencies("cuda-libraries-10-0");
        graph.markPackageAndItsDependencies("cuda-compiler-10-0");
        graph.markPackageAndItsDependencies("cuda-nvprune-10-0");
        graph.markPackageAndItsDependencies("cuda-demo-suite-10-0");

        // Vulkan
        graph.markPackageAndItsDependencies("libvulkan-dev");
        graph.markPackageAndItsDependencies("vulkan-utils");
        graph.markPackageAndItsDependencies("vulkan-sdk");
        graph.markPackageAndItsDependencies("dxc-dev");
    }

    private void markLinuxIntermediate() {
        // Perl applications
	graph.markPackageAndItsDependencies("arp-scan"); // Network tool, used by nmap
	graph.markPackageAndItsDependencies("wakeonlan");
	graph.markPackageAndItsDependencies("irssi");
        graph.markPackageAndItsDependencies("findimagedupes");
        graph.markPackageAndItsDependencies("needrestart"); // Helper to restart services after library updates

	// Python3x applications
        graph.markPackageAndItsDependencies("youtube-dl");
        graph.markPackageAndItsDependencies("command-not-found");

        // Intermediate dependencies (gs, pango, cairo, gtk, cups, samba)
        graph.markPackageAndItsDependencies("graphviz");
        graph.markPackageAndItsDependencies("cups-client"); // lp command
        graph.markPackageAndItsDependencies("tesseract-ocr"); // Not still tested!
        graph.markPackageAndItsDependencies("tesseract-ocr-spa");
	graph.markPackageAndItsDependencies("nginx");
	graph.markPackageAndItsDependencies("certbot"); // Creates SSL certificates for Nginx! 
	graph.markPackageAndItsDependencies("python3-certbot-nginx");
        graph.markPackageAndItsDependencies("samba");
        graph.markPackageAndItsDependencies("samba-vfs-modules");
        graph.markPackageAndItsDependencies("smbclient");
        graph.markPackageAndItsDependencies("samba-client"); // deprecated?
        graph.markPackageAndItsDependencies("cups"); // Pending to research how to use without Gnome
        graph.markPackageAndItsDependencies("pstoedit");

        // TK based X11 apps
        graph.markPackageAndItsDependencies("tkcvs"); // tkdiff
        graph.markPackageAndItsDependencies("stopwatch");

	// Custom repositories
        graph.markPackageAndItsDependencies("google-chrome-stable");
        graph.markPackageAndItsDependencies("cnijfilter2"); // Driver Canon Pixma TS9551C
        graph.markPackageAndItsDependencies("scangearmp2"); // Canon Pixma TS9551C scanner util
        graph.markPackageAndItsDependencies("libpango1.0-0"); // Needed by scangearmp2
    }

    private void markQtApps() {
	// QT apps
        graph.markPackageAndItsDependencies("qgis-plugin-grass");
	graph.markPackageAndItsDependencies("qt5-image-formats-plugin-pdf");
        graph.markPackageAndItsDependencies("cmake-qt-gui");
        graph.markPackageAndItsDependencies("virtualbox-qt");
        graph.markPackageAndItsDependencies("vlc");
        graph.markPackageAndItsDependencies("vlc-plugin-skins2");
        graph.markPackageAndItsDependencies("vlc-plugin-visualization");
        graph.markPackageAndItsDependencies("vlc-plugin-video-splitter");
        graph.markPackageAndItsDependencies("vlc-plugin-notify");
        graph.markPackageAndItsDependencies("vlc-plugin-samba");
        graph.markPackageAndItsDependencies("vlc-plugin-access-extra");
        graph.markPackageAndItsDependencies("vlc-l10n");
        graph.markPackageAndItsDependencies("teamviewer");
        graph.markPackageAndItsDependencies("sqlitebrowser"); // GUI for sqlite, useful to browse iphone's image database

	// Custom repositories
        graph.markPackageAndItsDependencies("obs-studio");
        graph.markPackageAndItsDependencies("obs-plugins");

        // QT development
        graph.markPackageAndItsDependencies("libqt4-dev");
        graph.markPackageAndItsDependencies("qt4-dev-tools"); // make xconfig
    }

    private void markWxApps() {
        // WX apps
        graph.markPackageAndItsDependencies("audacity");
    }

    private void markKde() {
        graph.markPackageAndItsDependencies("k3b");
        graph.markPackageAndItsDependencies("kwin");
    }

    private void markGnome() {
        // Gnome applications
        graph.markPackageAndItsDependencies("gnome-keyring"); // seahorse app, similar to MacOS keyring
        graph.markPackageAndItsDependencies("activity-log-manager");
        graph.markPackageAndItsDependencies("bluetooth"); // Bluetooth audio
        graph.markPackageAndItsDependencies("cheese"); // Camera access
        graph.markPackageAndItsDependencies("gnome-calculator");
        graph.markPackageAndItsDependencies("dbus-user-session"); // Needed by gnome-terminal
        graph.markPackageAndItsDependencies("eog"); // Photo viewer
        graph.markPackageAndItsDependencies("evince"); // PDF viewer
        graph.markPackageAndItsDependencies("file-roller"); // Gnome compressed file manager
        graph.markPackageAndItsDependencies("gedit"); // Text editor
        graph.markPackageAndItsDependencies("gir1.2-gsound-1.0"); // used by pitivi
        graph.markPackageAndItsDependencies("gnome-calendar");
        graph.markPackageAndItsDependencies("gnome-disk-utility");
        graph.markPackageAndItsDependencies("gnome-font-viewer");
        graph.markPackageAndItsDependencies("gnome-tweak-tool"); // Desktop configuration
        graph.markPackageAndItsDependencies("gnome-tweaks"); // Desktop configuration
        graph.markPackageAndItsDependencies("gnome-mahjongg");
        graph.markPackageAndItsDependencies("gnome-mines");
        graph.markPackageAndItsDependencies("gnome-screenshot"); // Screenshot!
        graph.markPackageAndItsDependencies("gnome-sudoku");
        graph.markPackageAndItsDependencies("gnome-system-monitor");
        graph.markPackageAndItsDependencies("gnome-terminal");
        graph.markPackageAndItsDependencies("gthumb"); // Image viewer, supports camera raw
        graph.markPackageAndItsDependencies("nautilus"); // File explorer
        graph.markPackageAndItsDependencies("pavucontrol"); // Nice sound hardware util
        graph.markPackageAndItsDependencies("pitivi"); // video editor
        graph.markPackageAndItsDependencies("pulseaudio-module-bluetooth"); // Pair BT audio
        graph.markPackageAndItsDependencies("remmina"); // Remote desktop
        graph.markPackageAndItsDependencies("remmina-plugin-rdp");
        graph.markPackageAndItsDependencies("remmina-plugin-secret");
        graph.markPackageAndItsDependencies("remmina-plugin-vnc");
        graph.markPackageAndItsDependencies("rhythmbox-plugins");
        graph.markPackageAndItsDependencies("seahorse"); // Gnome keyring manager
        graph.markPackageAndItsDependencies("shotwell"); // Photo viewer
        graph.markPackageAndItsDependencies("system-config-printer"); // Printer installer
        graph.markPackageAndItsDependencies("totem-plugins");
        graph.markPackageAndItsDependencies("transmageddon"); // media transcoder
        graph.markPackageAndItsDependencies("transmission-gtk"); // torrent client
        graph.markPackageAndItsDependencies("ufraw"); // Raw image viewer for Sony camera
        graph.markPackageAndItsDependencies("usb-creator-gtk"); // Linux installer for usb boot pendrive
        graph.markPackageAndItsDependencies("woeusb-frontend-wxgtk"); // Create UEFI compatible Windows 10 installer pendrive
        graph.markPackageAndItsDependencies("yelp"); // Gnome desktop help system
        graph.markPackageAndItsDependencies("zeitgeist"); // Needed by activity-log-manager

        // Libreoffice
        //graph.markPackageAndItsDependencies("libreoffice-gnome");
        //graph.markPackageAndItsDependencies("libreoffice-help-en-us");
        //graph.markPackageAndItsDependencies("libreoffice-math");
        //graph.markPackageAndItsDependencies("libreoffice-ogltrans");
        //graph.markPackageAndItsDependencies("libreoffice-pdfimport");
        //graph.markPackageAndItsDependencies("libreoffice-style-breeze");
        //graph.markPackageAndItsDependencies("libreoffice-calc");
        //graph.markPackageAndItsDependencies("libreoffice-avmedia-backend-gstreamer");
        //graph.markPackageAndItsDependencies("python3-uno");

        // Gnome session (level 1: gnome panel = applets + indicators)
        graph.markPackageAndItsDependencies("indicator-session"); // Logout and settings
        graph.markPackageAndItsDependencies("indicator-datetime"); // Hour and date
        graph.markPackageAndItsDependencies("indicator-bluetooth"); // Bluetooth sync / audio
        graph.markPackageAndItsDependencies("indicator-keyboard"); // Change input language
        graph.markPackageAndItsDependencies("indicator-sound"); // Volume controls
        graph.markPackageAndItsDependencies("indicator-printers"); // UNTESTED
        graph.markPackageAndItsDependencies("indicator-application"); // UNTESTED
        graph.markPackageAndItsDependencies("indicator-applet-complete"); // UNTESTED
	graph.markPackageAndItsDependencies("indicator-appmenu"); // UNTESTED
	graph.markPackageAndItsDependencies("indicator-applet"); // UNTESTED
	graph.markPackageAndItsDependencies("indicator-messages"); // UNTESTED
	graph.markPackageAndItsDependencies("gir1.2-appindicator3-0.1"); // Used by xpra?
        graph.markPackageAndItsDependencies("unity-control-center"); // check if it is needed/wanted
        graph.markPackageAndItsDependencies("gnome-applets"); // To review
        graph.markPackageAndItsDependencies("gnome-control-center"); // System settings app
	graph.markPackageAndItsDependencies("cups-pk-helper"); // Needed by gnome-control-center to enable unlock button on printer settings tab
	graph.markPackageAndItsDependencies("python3-cffi-backend"); // needed by unity-control-center
        graph.markPackageAndItsDependencies("gnome-panel"); // If this fails, try: gnome-panel --replace &

        // Gnome session (level 2: gdm + window manager + theme + menus)
        graph.markPackageAndItsDependencies("gdm3"); // Gnome login manager (replaces lightdm, xdm, etc.)
        graph.markPackageAndItsDependencies("alacarte"); // Gnome menu editor
        graph.markPackageAndItsDependencies("gnome-session-flashback"); // Metacity based session start for gdm3 menu
        graph.markPackageAndItsDependencies("gnome-themes-extra");
        graph.markPackageAndItsDependencies("gnome-screensaver");
        graph.markPackageAndItsDependencies("xscreensaver");
        graph.markPackageAndItsDependencies("xscreensaver-gl");
        graph.markPackageAndItsDependencies("metacity"); // Gnome window manager, replaces mwm, twm, etc.
        graph.markPackageAndItsDependencies("ubuntu-artwork"); // Make session use Ubuntu colors and background
        graph.markPackageAndItsDependencies("ubuntu-settings");
        graph.markPackageAndItsDependencies("gnome-startup-applications"); // gnome-session-properties command

        graph.markPackageAndItsDependencies("ubuntu-wallpapers-jammy");

        // Third party apps
        graph.markPackageAndItsDependencies("github-desktop");
        graph.markPackageAndItsDependencies("gitkraken");

        // Other (unused) Gnome packages
        //graph.markPackageAndItsDependencies("gnome-user-guide");
        //graph.markPackageAndItsDependencies("thunderbird-gnome-support");
        //graph.markPackageAndItsDependencies("thunderbird"); // Email reader (like outlook)
        //graph.markPackageAndItsDependencies("thunderbird-locale-en-us");
    }

    void markAwsEC2() {
	graph.markPackageAndItsDependencies("linux-aws");
	graph.markPackageAndItsDependencies("linux-headers-5.19.0-1025-aws");
	graph.markPackageAndItsDependencies("linux-modules-5.19.0-1025-aws");
	graph.markPackageAndItsDependencies("ec2-hibinit-agent");
	graph.markPackageAndItsDependencies("ec2-instance-connect");
	graph.markPackageAndItsDependencies("cloud-init");
	graph.markPackageAndItsDependencies("chrony");
    }

    void markJedilinkCustom() {
	graph.markPackageAndItsDependencies("*renderpark");
	graph.markPackageAndItsDependencies("*sdkman");
	graph.markPackageAndItsDependencies("*nvidia_driver");
	graph.markPackageAndItsDependencies("*povray");
	graph.markPackageAndItsDependencies("*eureka");
	graph.markPackageAndItsDependencies("*stalker");
	graph.markPackageAndItsDependencies("*vk_raytracing_tutorial_KHR");
	graph.markPackageAndItsDependencies("*snap-postman");
	graph.markPackageAndItsDependencies("*snap-code");
	graph.markPackageAndItsDependencies("*snap-clion");
	graph.markPackageAndItsDependencies("*snap-intellij-idea-community");
	graph.markPackageAndItsDependencies("*davinci-resolve");
	graph.markPackageAndItsDependencies("*opencv-4.x");
	graph.markPackageAndItsDependencies("*darknet");
	graph.markPackageAndItsDependencies("*studio3t");
	graph.markPackageAndItsDependencies("*chromium-test-kit");
	graph.markPackageAndItsDependencies("*cde"); // Original CDE from git clone https://git.code.sf.net/p/cdesktopenv/code cde
	graph.markPackageAndItsDependencies("*nscde"); // CDE like desktop :)
	graph.markPackageAndItsDependencies("*lsix"); // Thumbnail image generator for console DEC sixel format https://github.com/hackerb9/lsix
	graph.markPackageAndItsDependencies("*eureka");
	graph.markPackageAndItsDependencies("*vulkan-tutorial");
	graph.markPackageAndItsDependencies("*apitrace");
	graph.markPackageAndItsDependencies("awsvpnclient");
	graph.markPackageAndItsDependencies("zerotier-one"); // Free VPN :)
	graph.markPackageAndItsDependencies("sendanywhere"); // Multi platform file transfer
        graph.markPackageAndItsDependencies("resilio-sync"); // Sync files with mobile device :) better than "SendAnywhere"
	graph.markPackageAndItsDependencies("darktable"); // Adobe Lightroom replacement :)
    }

    void markRiskVCrossEnvironment() {
	graph.markPackageAndItsDependencies("qemu");
	graph.markPackageAndItsDependencies("qemu-user-static");
	graph.markPackageAndItsDependencies("dpkg-cross");
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
