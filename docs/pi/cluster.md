# Shopping List

You will need:
  
  * At least 3 Raspberry Pi 3 Model B
  * As many 8Gb SD-cards (Class 10) as you have Pis
  * Ethernet Switch with as many ports as you have Pis
  * 60W USB power supply with as many ports as you have Pis
  * Stackable Pi Case (or build your own with Lego ;-)
  * As many 1ft USB cables as you have Pis
  * As many 1ft Ethernet cables as you have Pis

# Installing Raspian and Docker on the Raspberry Pis

  * If you bought blank SD cards:
    * Download [Raspian Lite](https://www.raspberrypi.org/downloads/raspbian/)
    * [Install](https://www.raspberrypi.org/documentation/installation/installing-images/README.md) the operating system onto your SD Card
  * Plug in an HDMI and Ethernet cable and boot the Pi
  * Connect to the Pi via SSH
    * One of the last messages output on the boot log will be the Pis IP address
	* `ssh pi@PI_IP_ADDRESS` (password defaults to 'raspberry')
  * Change the password
    * `passwd`
  * Change the hostname from `raspberrypi` to `piswarm1` (incrementing the number for each successive Pi)
    * `sudo nano /etc/hosts`
	* `sudo nano /etc/hostname`
  * It's a good idea to label the board with the hostname of the Pi
  * Reduce the GPU memory to 16Mb since these machines with be headless
    * `sudo nano /boot/config.txt` and add `gpu_mem=16` at the end
  * Install Docker
    * `curl -sSL get.docker.com | sh`
  * Set Docker to auto start
    * `sudo systemctl enable docker`
  * It can be useful to enable the Docker client for debugging purposes
    * `sudo usermod -aG docker pi`
  * Reboot the Pi to check things work on reboot
    * `sudo reboot`
  * Reconnect via SSH
    * `ssh pi@PI_IP_ADDRESS`
  * Test the installation of Docker
    * `docker run -ti hypriot/armhf-busybox`
    * After pulling the image you shuold be in a busybox container shell
	* `hostname` should return a value other than 'piswarm1'
	
# Setting up the Network

This section is based on an excellent [Ada Fruit
tutorial](https://learn.adafruit.com/setting-up-a-raspberry-pi-as-a-wifi-access-point/install-software). if
things don't work for you then check there for more details.

One of our Pis will act as a bridge between our Internet and our
cluster. You can either connect this Pi to the Internet using Ethernet
or you can add a second Wifi adapter (USB) and have it bridge across
the clusters wireless network and the additional adapter.

In my setup I have opted for the second wireless adapter, this is
because I want to be able to demo my cluster away from my usual
network connections. By using a second Wifi adapter I can configure
that to connect to my phones hotspot and thus have a conncetion almost
anywhere.

The below will also work using a wired connection on eth0.

My setup is therefore:

### piswarm-1

wlan0 and eth0: Internet connection
wlan1: Cluster wireless
Configured as an access point on wlan1
SSID of Access Point: piswarm

### piswarm-2, piswarm-3, piswarm-4 etc.

wlan0: Connects to piswarm wifi network 

## Setting up the Access Point

This sectin is informed by https://learn.adafruit.com/setting-up-a-raspberry-pi-as-a-wifi-access-point/overview

```bash
sudo apt-get update
sudo apt-get install hostapd isc-dhcp-server
sudo apt-get install iptables-persistent
```

### /etc/dhcp/dhcp.conf

```
sudo nano /etc/dhcp/dhcpd.conf
```

Comment out the following lines:

```
#option domain-name "example.org";
#option domain-name-servers ns1.example.org, ns2.example.org;
```

Uncomment `authoratative`:

```
# If this DHCP server is the official DHCP server for the local
# network, the authoritative directive should be uncommented.
authoritative;
```

Add the following to the end of the file:

```
subnet 192.168.42.0 netmask 255.255.255.0 {
       range 192.168.42.10 192.168.42.50;
       option broadcast-address 192.168.42.255;
       option routers 192.168.42.1;
       default-lease-time 600;
       max-lease-time 7200;
       option domain-name "piswarm";
       option domain-name-servers 8.8.8.8, 8.8.4.4;
}
```

### /etc/default/isc-dhcp-server

```
sudo nano /etc/default/isc-dhcp-server
```

Add the desired wlan interface to the INTERACES paramter. In my setup
I am using wlan1 as the internal network and wlan0 as the external
network. If you only have one wifi connector you will use wlan0 below.

```
INTERFACES="wlan1"

### /etc/network/interfaces

```
sudo ifdown wlan1
```

Don't worry if the interfaces have not been configured yet, we will do
that below. This step is a precaution to ensure there is no active
interface.

```
sudo nano /etc/network/interfaces
```

Make the content after the initial comments:

```
source-directory /etc/network/interfaces.d

auto lo
iface lo inet loopback

iface etho0 inet manual

allow-hotplug wlan0
iface wlan0 inet manual
  wpa-conf /etc/wpa_supplicant/wpa_supplicant.conf

allow-hotplug wlan1
iface wlan1 inet static
  address 192.168.42.1
  netmask 255.255.255.0
```

Now we'll assing the IP to wlan1:

```
sudo ifconfig wlan0 192.168.42.1
```

### /etc/hostapd/hostapd.conf

```
sudo nano /etc/hostapd/hostapd.conf
```

Ensure the file contains the following content:

```
interface=wlan1
ssid=PiSwarm
country_code=US
hw_mode=g
channel=6
macaddr_acl=0
auth_algs=1
ignore_broadcast_ssid=0
wpa=2
wpa_passphrase=Raspberry
wpa_key_mgmt=WPA-PSK
wpa_pairwise=CCMP
wpa_group_rekey=86400
ieee80211n=1
wme_enabled=1
```

### /etc/default/hostapd

```
sudo nano /etc/default/hostapd
```

Uncomment the line starting with `#DAEMON_CONF` and ensure it says:

```
DAEMON_CONF="/etc/hostapd/hostapd.conf"
```

### /etc/init.d/hostapd 

```
sudo nano /etc/init.d/hostapd 
```

Uncomment the line starting with `#DAEMON_CONF` and ensure it says:

```
DAEMON_CONF="/etc/hostapd/hostapd.conf"
```

### /etc/sysctl.conf

```
sudo nano /etc/sysctl.conf
```

Add the following at the end of the file:

```
net.ipv4.ip_forward=1
```

Run:

```
sudo sh -c "echo 1 > /proc/sys/net/ipv4/ip_forward"
```

### IP Tables

```
sudo iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
sudo iptables -A FORWARD -i eth0 -o wlan1 -m state --state RELATED,ESTABLISHED -j ACCEPT
sudo iptables -A FORWARD -i wlan1 -o eth0 -j ACCEPT

sudo iptables -t nat -A POSTROUTING -o wlan0 -j MASQUERADE
sudo iptables -A FORWARD -i wlan0 -o wlan1 -m state --state RELATED,ESTABLISHED -j ACCEPT
sudo iptables -A FORWARD -i wlan1 -o wlan0 -j ACCEPT

sudo sh -c "iptables-save > /etc/iptables/rules.v4"
```

### Finalizing

```
sudo mv /usr/share/dbus-1/system-services/fi.epitest.hostap.WPASupplicant.service ~/
sudo service hostapd start 
sudo service isc-dhcp-server start
sudo update-rc.d hostapd enable 
sudo update-rc.d isc-dhcp-server enable 
```

This will start the Access Point, you should be able to connect to it
from any device and, assuming you are conencted to the internet, you
will be able to browse the web.

## Configure The Other Pis

We now need to configure each of the other Pis to conncet to the
"PiSwarm" network. On each of the machines do the following:

```
sudo nano /etc/wpa_supplicant/wpa_supplicant.conf 
```

At the bottom of this file add the following:

```
network={
    ssid="PiSwarm"
    proto=RSN
    key_mgmt=WPA-PSK
    pairwise=CCMP
    psk="raspberry"
}
```

## Connecting to your Agents

In order to SSH into your cluster you can now connect to the "PiSwarm"
wifi network and use:

```
ssh pi@192.168.42.1
```

If you want to connect to one of the other Pis you will 