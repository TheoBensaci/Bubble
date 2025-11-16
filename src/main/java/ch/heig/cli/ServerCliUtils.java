package ch.heig.cli;

import ch.heig.core.Game;

import java.net.InetAddress;

public class ServerCliUtils {

    public static void playerJoinMessage(String username, InetAddress addr, int port){
        System.out.println(
                "\b\b"+CliUtils.GREEN+"[JOIN]"+CliUtils.RESET+" -> "+"Player ("+CliUtils.BLUE+username+" "+addressPortString(addr,port)+")"
        );
        serverNewLine();
    }

    public static void playerExitMessage(String username, InetAddress addr, int port){
        System.out.println(
                "\b\b"+CliUtils.RED+"[EXIT]"+CliUtils.RESET+" -> "+"Player ("+CliUtils.BLUE+username+" "+addressPortString(addr,port)+")"
        );
        serverNewLine();
    }

    public static String addressPortString(InetAddress addr, int port){
        return CliUtils.YELLOW+"[ address : "+addr+"| port : "+port+"]"+CliUtils.RESET;
    }

    public static void startServerStartMessage(int port){
        System.out.println(
                CliUtils.CYAN+"[TRY TO START SERVER]"+CliUtils.RESET+" with "+CliUtils.YELLOW+"[port : "+port+"]"+CliUtils.RESET
        );
    }

    public static void serverStartMessage(int port){
        System.out.println(
                CliUtils.GREEN+"[SERVER START]"+CliUtils.RESET+" with "+CliUtils.YELLOW+"[port : "+port+"]"+CliUtils.RESET
        );
    }

    public static void serverStatue(Game game){
        if(game.isRunning()){
            System.out.println(
                    "Server is "+CliUtils.GREEN+"[RUNNING]"+CliUtils.RESET
            );
        }
        else{
            System.out.println(
                    "Server is "+CliUtils.RED+"[CLOSE]"+CliUtils.RESET
            );
        }
    }

    public static void serverCommandMessage(){
        String[] commandList=new String[]{
                "exit | close the server"
        };
        System.out.println("Command list : ");
        for (String s : commandList){
            System.out.println(" - "+s);
        }
    }

    public static void serverNewLine(){
        System.out.print("> ");
    }


}
