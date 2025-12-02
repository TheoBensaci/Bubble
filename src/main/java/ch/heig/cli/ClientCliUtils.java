package ch.heig.cli;

import ch.heig.network.packet.CommandPacket;

import java.security.KeyPair;
import java.util.*;

public class ClientCliUtils {

    public static void printColorSelectionMessage(){
        StringBuilder str = new StringBuilder("Select ship color : \n[ ");
        for (int i = 0; i < 7; i++) {
            str.append(getPlayerColorChatColor(i)).append(i).append(" ■").append(CliUtils.RESET).append("| ");
        }
        str.append(CliUtils.RESET + "\b\b ]");
        System.out.println(str);
        CliUtils.askUserInputMessage();
    }

    public static int colorSelectionLineValidation(String line){
        for (int i = 0; i < 7; i++) {
            if(line.matches("\s*"+i+"\s*")){
                return i;
            }
        }
        printErrorColorValidation();
        return -1;
    }

    public static void printColorSelectedMessage(int color){
        System.out.println(getPlayerColorChatColor(color)+"[ ■ SELECTED COLOR ■ ]"+CliUtils.RESET);
    }

    public static void printErrorColorValidation(){
        System.out.println(CliUtils.RED_BRIGHT+"SELECTED COLOR INVALID"+CliUtils.RESET);
    }

    public static void printErrorInvalidUsername(){
        System.out.println(CliUtils.RED_BRIGHT+"USERNAME INVALID"+CliUtils.RESET);
    }

    public static void printErrorCommandUnknown(){
        System.out.println(CliUtils.RED_BRIGHT+"COMMAND UNKNOWN"+CliUtils.RESET);
    }

    public static void printAskUsernameMessage(){
        System.out.println("Enter a username : ");
        CliUtils.askUserInputMessage();
    }

    public static String getPlayerColorChatColor(int color){
        return switch (color){
            case 0 -> CliUtils.WHITE;
            case 1 -> CliUtils.RED;
            case 2 -> CliUtils.GREEN;
            case 3 -> CliUtils.YELLOW;
            case 4 -> CliUtils.PURPLE;
            case 5 -> CliUtils.BLUE;
            case 6 -> CliUtils.BLACK_BRIGHT;
            default -> CliUtils.RESET;
        };
    }

    public static CommandPacket stringToCommand(String command){
        List<String> tokens = new ArrayList<>(List.of(command.split("\s", 10)));
        tokens.removeIf(String::isEmpty);

        if(tokens.isEmpty())return null;
        switch (tokens.size()){
            case 1 :
                if(tokens.getFirst().equals("help") || tokens.getFirst().equals("command")){
                    return new CommandPacket(CommandPacket.Command.help,"");
                }

                if(tokens.getFirst().equals("players")){
                    return new CommandPacket(CommandPacket.Command.players,"");
                }
                return null;
            case 2 :
                if(tokens.getFirst().equals("game")){
                    switch (tokens.get(1)){
                        case "start":
                            return new CommandPacket(CommandPacket.Command.startGame,"");
                        case "stop":
                            return new CommandPacket(CommandPacket.Command.cancelGame,"");
                        case "restart":
                            return new CommandPacket(CommandPacket.Command.restarGame,"");
                        default:
                            return null;
                    }
                }
                if(tokens.getFirst().equals("kick")){
                    return new CommandPacket(CommandPacket.Command.kickPlayer,tokens.get(1));
                }
                if(tokens.getFirst().equals("op")){
                    return new CommandPacket(CommandPacket.Command.op,tokens.get(1));
                }

                if(tokens.getFirst().equals("server") && tokens.get(1).equals("stop")){
                    return new CommandPacket(CommandPacket.Command.stopServer,"");
                }
                return null;

            default:
                return null;
        }
    }
}
