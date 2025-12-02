/** Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: Main use for the server build */
package ch.heig;

import ch.heig.cli.ServerCliUtils;
import ch.heig.core.render.GameRender;
import ch.heig.network.coreVariant.ServerGame;
import ch.heig.network.socket.GameSocket;
import ch.heig.network.socket.ServerGameSocket;
import ch.heig.other.Arena;
import picocli.CommandLine;

@CommandLine.Command(name = "Bubble", version = "Bubble 1.0", mixinStandardHelpOptions = true)
public class ServerMain implements Runnable {

  @CommandLine.Option(
      names = {"-port", "--p"},
      description = "listen port")
  int initPort = GameSocket.PORT;

  public static void launchServer(int port) {
    ServerCliUtils.startServerStartMessage(port);
    ServerGame gameServer = new ServerGame(false);
    gameServer.start();
    gameServer.setGameSocket(new ServerGameSocket());

    // load the map

    // set arenna
    Arena.position.set(GameRender.WIDTH / 2, GameRender.HEIGHT / 2);
    Arena.radiuse = 375f;

    // make a quick way to interact with the server

    ServerCliUtils.serverStartMessage(gameServer.getGameSocket().getListenPort());
    try {
      gameServer.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    ServerCliUtils.serverStatue(gameServer);
  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new ServerMain()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public void run() {
    launchServer(initPort);
  }
}
