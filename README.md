# Install Linux

## Dependencies

To build and run a Qoober blockchain node, you need to install Java 8 or later (Java 11 is recommended), and Git.

```shell
$ sudo apt install default-jdk git
```

## Get the latest version

To get the latest version of the Qoober blockchain node, you need to clone the repository from GitHub:

```shell
$ git clone git://github.com/Qoober/qoober-node.git ./
```
    
## How to build

Compilation the source code into class files is performed using the `compile.sh` script. The script must have execute permissions:

```shell
$ chmod 777 compile.sh
$ ./compile.sh
```

Compilation into a jar archive is performed using the `jar.sh` script. The script also must have execution permissions:

```shell
$ chmod 777 jar.sh
$ ./jar.sh 
```  

The resulting file is `qoober.jar`.

## Launching

Starting `qoober.jar` is done with the command:

```shell
$ java -jar qoober.jar
```

# Install Windows

## Installation

To install the Qoober blockchain node on Windows, download the distribution as a zip archive with an integrated Java machine.
Select the version corresponding to your Windows version:
- For 32-bit Windows OS (with Java 8) [Download](https://qoober.space/files/qoober-win-32.zip)
- For 64-bit Windows XP (with Java 8) [Download](https://qoober.space/files/qoober-winxp-64.zip)
- For other 64-bit Windows OS (from Java 11) [Download](https://qoober.space/files/qoober-win-64.zip)

To install, you need to unpack the archive into a folder on your PC.

### Installation on Windows XP

When installing on Windows XP, the path where the archive is unpacked must contain only Latin characters.
Qoober wallet does not support the standard Windows XP browser - Internet Explorer 8. You must update or use a different browser.

## Launching

To run a node of the Qoober blockchain, use the file `qoober.exe`.
Use the file `wallet.url` to start the wallet.

