/**
 * Autheur: Theo Bensaci | Date: 07:04 26.11.2025 | Description: packet use to manage client command
 * on the server Command packet use to manage client command on the server When a command is
 * accepted, the server send this packet with commandType = "log" and arg = result message If the
 * command is not accepted, the server send this packet with commandType = "error" command with arg
 * = Error message In any case, the packet will have the same id
 */
package ch.heig.network.packet;

public class CommandPacket extends Packet {
  public static final String CommandOpHelp =
      "game\n"
          + "\t- start : Start the game\n"
          + "\t- restart : Restart the game\n"
          + "\t- stop : Stop the game\n"
          + "server\n"
          + "\t- stop : Stop the server\n"
          + "kick <playername> : Kick player named <playername>\n"
          + "op <playername> : Make a player named <playername> a operator\n"
          + "players : List all player\n"
          + "help : List of command";
  public static final String CommandHelp = "players : List all player\n" + "Help : list of command";

  public enum Command {
    none,
    startGame,
    restartGame,
    cancelGame,
    kickPlayer,
    stopServer,
    players,
    op,
    help,
    log,
    error
  }

  public CommandPacket(String username, Command commandType, String arg, int index) {
    this(commandType, arg);
    this.username = username;
    this.index = index;
  }

  public CommandPacket(Command commandType, String arg) {
    this.type = PacketType.command;
    this.commandType = commandType;
    this.arg = arg;
  }

  public String username; // username of the sender, "" if from the server
  public Command commandType; // type of command packet
  public String arg; // argument of the command, can be use as a log value or error value
  public int index; // index of the command packet
}
