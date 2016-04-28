package kpfu.magistracy.service_for_controller;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

public class ServiceManager {

    //ключ - id пользователя, значение - набор его команд
    private static HashMap<String, CommandsFromClientDTO> commandsMap;
    private static final String exceptionStringJsonIsNotValid = "Json is not valid";
    private static final String exceptionStringQubitCount = "This machine does not support creation of such qubit count";

    public static void putCommandsToExecutionQueue(String commandsString) {
        CommandsFromClientDTO commandsFromClientDTO;
        try {
            commandsFromClientDTO = checkCommands(commandsString);
            //todo где-то взять id пользователя
            //commandsMap.put(, commandsFromClientDTO);
        } catch (IllegalArgumentException e) {
            //todo сказать пользователю, что json составлен неверно
        }
    }

    private static CommandsFromClientDTO checkCommands(String commandsString) throws IllegalArgumentException {
        CommandsFromClientDTO commandsFromClientDTO;
        try {
            commandsFromClientDTO = new Gson().fromJson(commandsString, CommandsFromClientDTO.class);
        } catch (JsonSyntaxException exception) {
            throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
        }
        if (commandsFromClientDTO.getQubitCount() > 50/*todo спросить у памяти*/) {
            throw new IllegalArgumentException(exceptionStringQubitCount);
        }
        for (LogicalAddressingCommandFromClient commandFromClient : commandsFromClientDTO.getLogicalAddressingCommandFromClientList()) {
            //todo проверить на корректность каждую команду
//            if () {
//                throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
//            }
        }
        return commandsFromClientDTO;
    }

    private void executeNextCommand() {
       // if()
    }
}
