/** Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: Main use for the client build */
package ch.heig;

import ch.heig.cli.CliUtils;
import ch.heig.cli.ClientCliUtils;
import ch.heig.core.render.GameRender;
import ch.heig.network.coreVariant.ClientGame;
import ch.heig.network.packet.CommandPacket;
import ch.heig.network.packet.LoginPacket;
import ch.heig.network.socket.ClientGameSocket;
import ch.heig.network.socket.GameSocket;
import ch.heig.other.Arena;
import java.util.Scanner;
import picocli.CommandLine;

@CommandLine.Command(name = "Bubble", version = "Bubble 1.0", mixinStandardHelpOptions = true)
public class ClientMain implements Runnable {
  @CommandLine.Option(
      names = {"-address", "--a"},
      description = "server host address")
  String serverAddress = "localhost";

  @CommandLine.Option(
      names = {"-port", "--p"},
      description = "server host port")
  int serverPort = GameSocket.PORT;

  @CommandLine.Option(
      names = {"-listen", "--l"},
      description = "client listen port")
  int clientPort = 8001;

  public static void launchClient(String hostname, int hostPort, int listenPort) {
    ClientCliUtils.printClientStartMessage(hostname, hostPort, listenPort);

    ClientGameSocket clientSocket = new ClientGameSocket(hostname, hostPort, listenPort);
    clientSocket.start();

    // ping phase
    clientSocket.startPing();
    CliUtils.loadingMessage("PING SEVER", 0);
    while (clientSocket.pingStatue()) {
      clientSocket.pingRoutine();
    }
    clientSocket.stopPing();

    // init player input
    Scanner scan = new Scanner(System.in);
    String line;

    // select color
    int playerColor;
    do {
      ClientCliUtils.printColorSelectionMessage();
      line = scan.nextLine();
      playerColor = ClientCliUtils.colorSelectionLineValidation(line);
      System.out.print("\n");
    } while (playerColor < 0);

    ClientCliUtils.printColorSelectedMessage(playerColor);

    // login phase
    // ASK USERNAME
    LoginPacket loginData;

    do {
      ClientCliUtils.printAskUsernameMessage();
      line = scan.nextLine();
      clientSocket.login(line, playerColor);

      while (clientSocket.onLoginWait()) {
        clientSocket.loginRoutine();
      }

      loginData = clientSocket.getActualLoginPacket();

      if (loginData.id < 0) {
        ClientCliUtils.printErrorInvalidUsername();
      }
    } while (loginData.id < 0);

    // launch game
    ClientGame game = new ClientGame(loginData);
    game.start();
    game.setGameSocket(clientSocket);

    // set arena
    Arena.position.set((float) GameRender.WIDTH / 2, (float) GameRender.HEIGHT / 2);
    Arena.radiuse = 375f;

    // command routine

    while (game.isRunning()) {
      CliUtils.askUserInputMessage();
      line = scan.nextLine();
      CommandPacket commandPacket = ClientCliUtils.stringToCommand(line);
      if (commandPacket == null || !game.isRunning()) {
        ClientCliUtils.printErrorCommandUnknown();
        continue;
      }
      clientSocket.sendCommand(commandPacket.commandType, commandPacket.arg);
      while (game.isRunning() && !clientSocket.getLastCommandStatue()) {
        clientSocket.commandRoutine();
      }
      commandPacket = clientSocket.getActualCommand();
      if (commandPacket.commandType == CommandPacket.Command.error) {
        System.out.println(CliUtils.RED_BRIGHT + commandPacket.arg + CliUtils.RESET);
      } else {
        System.out.println(commandPacket.arg);
      }
    }
    try {
      game.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    System.out.println("Game close, bye bye :]");
  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new ClientMain()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public void run() {
    launchClient(serverAddress, serverPort, clientPort);
  }
}
