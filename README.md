
<p align="center">
    <img align="center" src="/other/mockup/templogo.png"  width="80%" alt="mini gameplay 2">
</p>

# Bubble
Bubble is a twin stick shooter online where space is a ressource, it's just a prototype use to implement some networking

<p align="center">
    <img align="center" src="/other/Gamplay1.gif"  width="80%" alt="mini gameplay">
</p>

This project is due (except for PicoCLI) in full java vanilla
<p align="center">
    <img align="center" src="/other/Gamplay2.gif"  width="80%" alt="mini gameplay 2">
</p>

**WARNING** : this project is still in WIP

## Installation
1. Download the latest [release](https://github.com/TheoBensaci/BinaryWav/releases).
2. Download what you need (server or client)
3. You'r good to go
### Or
1. Clone the repo 
```
git clone https://github.com/TheoBensaci/Bubble
```
1. In the repo, download the dependencyes
```
./mvnw dependency:go-offline
```
1. Build the .jar
  - Server : `./mvnw clean package `
  - Client : `./mvnw -f pomClient.xml clean package `
You should have in the `./target` folder a file named `Bubble-x.x-SNAPSHOT.jar`.
**WARNING** There is only on target output, so if you build server for example, `Bubble-x.x-SNAPSHOT.jar` will be jar for the server and if you build the client then `Bubble-x.x-SNAPSHOT.jar` will be jar for the client

### WSL
You can technically install it with WSL and launch it with it, but, WSL is limited and not perfect when it comme to GUI, so you will experience sever performance issue.

## Usage
Depending of what kind of jar you have, the usage will defer.
### Server usage
Run the command `java -jar Bubble-x.x-SNAPSHOT.jar` where `Bubble-x.x-SNAPSHOT.jar` is located
By default it will launch the server bind to the port 8000, but you can specify a port by using `--p` or `-port`
Example : `java -jar Bubble-x.x-SNAPSHOT.jar --p 8000`

Once launch, the server will be up and only display LOG.



### Client usage
Run the command `java -jar Bubble-x.x-SNAPSHOT.jar -address [server address] -port [server port]` where `Bubble-x.x-SNAPSHOT.jar` is located.
This will start the client and connecting it to the sever specify.
If you want to change the port use by the client, you can add `-listen [port]` to change it

Once start, the client will ping the server, once answer, you can start login in to it.
1) select you color : the prompt will propose you to select your color, chose what you like
2) enter a username : the prompt will propose you to enter a UNIQUE username (without space) enter what you like
3) if you username is ok, normaly you will see a graphical interface and you will be good to go
#### Control
Move : {W, A, S, D}
Dash : {Right click}
Fire : {Left click}

#### Command
After connecting you can still use the CLI to send command to the server, type `help` to see which command a available.


#### Operator
The first client connected to the sever will have operator privilege granted by default. If this client log out and there is no body with operator privilege left on the sever, then a random player will have operator privilege granted.

## Credit / Auteur
- Theo Bensaci : [@TheoBensaci](https://github.com/TheoBensaci), [@Me-Theo](https://github.com/Me-Theo)
  - ART - DEVLOPEMENT - NETWORKING - DESIGN - DOC - DOCKER
- Gasmi Yasser : [@yss-g5](https://github.com/yss-g5)
    - ?

## Documentation
All the documentation is on the [documentation](/other/documentation/README.md) folder


## Contribution
If you want to contribute, go a head, i made this project for fun and i will be happy to see it grow.
Just, there is a [naming convention](/other/naming_convention.md) so please follow it if you want to contribute to the code base, thx :]

## AI usage
No AI as been use to make this project :]
