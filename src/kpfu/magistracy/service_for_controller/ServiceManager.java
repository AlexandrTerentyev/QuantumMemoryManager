package kpfu.magistracy.service_for_controller;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import kpfu.magistracy.controller.execution.commands.CommandTypes;

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
            executeNextCommand();
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
            if (!checkCommand(commandFromClient)) {
                throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
            }
            //todo формировать List<TopLevelCommandKeeper>
        }
        return commandsFromClientDTO;
    }

    private static boolean checkCommand(LogicalAddressingCommandFromClient commandFromClient) {        //todo сделать более подробную проверку
        if (commandFromClient.getQubit_1() == null) {
            return false;
        } else {
            if (commandFromClient.getCommandType() == CommandTypes.CQET && commandFromClient.getQubit_2() == null) {
                return false;
            }
        }
        return true;
    }

    private static void executeNextCommand() {
        // todo достать самый старый commandsFromClientDTO, проверить, есть ли место для исполнения в квантовой памяти
        //todo если есть, то отправить на исполнение

        //QuantumMemoryOperator.executeCommands()
    }

    public static void returnMeasuredQubits(){
        //todo подумать про параметры функции
        //todo возвратить значение пользователю
    }
}
