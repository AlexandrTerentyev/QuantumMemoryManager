package kpfu.magistracy.service_for_controller;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import kpfu.magistracy.controller.QuantumMemoryOperator;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.magistracy.controller.execution.commands.CommandTypes;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressFromClient;
import kpfu.magistracy.service_for_controller.commands.CommandsFromClientDTO;
import kpfu.magistracy.service_for_controller.commands.LogicalAddressingCommandFromClient;
import kpfu.magistracy.service_for_controller.results.TopLevelResult;

import java.util.*;

public class ServiceManager {

    private static final String exceptionStringJsonIsNotValid = "Json is not valid";
    private static final String exceptionStringQubitCount = "This machine does not support creation of such qubit count";

    private static ServiceManager mServiceManager;
    private QuantumMemoryOperator mQuantumMemoryOperator;
    private HashMap<OwnerData, List<LogicalAddressingCommand>> mCommandsForControllerMap;
    private LinkedHashMap<LogicalQubitAddressForController, LogicalQubitAddressFromClient> addressesCorrespondenceMap;
    private List<OwnerData> mOwnerDataList;
    private Thread threadForCommandsExecuting;

    private ServiceManager() {
        mCommandsForControllerMap = new HashMap<OwnerData, List<LogicalAddressingCommand>>();
        addressesCorrespondenceMap = new LinkedHashMap<LogicalQubitAddressForController, LogicalQubitAddressFromClient>();
        mOwnerDataList = new LinkedList<OwnerData>();
        mQuantumMemoryOperator = QuantumMemoryOperator.getOperator();
    }

    public static ServiceManager getServiceManager() {
        if (mServiceManager == null) {
            mServiceManager = new ServiceManager();
        }
        return mServiceManager;
    }

    public void putCommandsToExecutionQueue(String userId, String commandsString) {
        try {
            OwnerData ownerData = new OwnerData(userId, System.currentTimeMillis());
            List<LogicalAddressingCommand> commandsListForController = checkCommandsAndTransformForController(ownerData, commandsString);
            mCommandsForControllerMap.put(ownerData, commandsListForController);
            mOwnerDataList.add(ownerData);
            //executeNextCommand();
        } catch (IllegalArgumentException e) {
            //todo сказать пользователю, что json составлен неверно
        }
    }

    private List<LogicalAddressingCommand> checkCommandsAndTransformForController(OwnerData ownerData, String commandsString) throws IllegalArgumentException {
        CommandsFromClientDTO commandsFromClientDTO;
        try {
            commandsFromClientDTO = new Gson().fromJson(commandsString, CommandsFromClientDTO.class);
        } catch (JsonSyntaxException exception) {
            throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
        }
        List<LogicalAddressingCommandFromClient> commandsFromClientList = commandsFromClientDTO.getLogicalAddressingCommandFromClientList();
        if (commandsFromClientDTO.getQubitCount() > mQuantumMemoryOperator.getQubitsMaxCount() && commandsFromClientList.size() > mQuantumMemoryOperator.getCommandsMaxCount()) {
            throw new IllegalArgumentException(exceptionStringQubitCount);
        }
        List<LogicalAddressingCommand> commandsListForController = new LinkedList<LogicalAddressingCommand>();
        for (LogicalAddressingCommandFromClient commandFromClient : commandsFromClientList) {
            if (!commandComposedRight(commandFromClient)) {
                throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
            } else {
                commandFromClient.getQubit_1().setOwnerData(ownerData);
                if (commandFromClient.getQubit_2() != null) {
                    commandFromClient.getQubit_2().setOwnerData(ownerData);
                }
                List<LogicalQubitAddressForController> addressesForController = getAddressesForControllerFromLocalClientAddress(commandFromClient.getQubit_1());
                LogicalAddressingCommand.Builder commandBuilder = new LogicalAddressingCommand.Builder();
                commandBuilder
                        .setCommand(commandFromClient.getCommandType())
                        .setCommandParam(commandFromClient.getCommandParam())
                        .setFirstQubit_Part1(addressesForController.get(0))
                        .setFirstQubit_Part2(addressesForController.get(1));
                if (commandFromClient.getCommandType() == CommandTypes.CQET) {
                    addressesForController = getAddressesForControllerFromLocalClientAddress(commandFromClient.getQubit_2());
                    commandBuilder.setSecondQubit_Part1(addressesForController.get(0));
                    commandBuilder.setSecondQubit_Part2(addressesForController.get(1));
                }
                commandsListForController.add(commandBuilder.build());
            }
        }
        return commandsListForController;
    }

    private boolean commandComposedRight(LogicalAddressingCommandFromClient commandFromClient) {
        if (commandFromClient.getQubit_1() == null) {
            return false;
        } else if (commandFromClient.getCommandType() == CommandTypes.CQET && commandFromClient.getQubit_2() == null) {
            return false;
        }
        return true;
    }

    private List<LogicalQubitAddressForController> getAddressesForControllerFromLocalClientAddress(LogicalQubitAddressFromClient qubitAddressFromClient) {
        List<LogicalQubitAddressForController> addressesForController = getGlobalLogicalQubitAddressFromLocalClient(qubitAddressFromClient);
        if (addressesForController.isEmpty()) {
            Long currentTimeInMillis = System.currentTimeMillis();
            addressesForController.add(new LogicalQubitAddressForController(currentTimeInMillis, 0));
            addressesForController.add(new LogicalQubitAddressForController(currentTimeInMillis, 1));
        }
        addressesCorrespondenceMap.put(addressesForController.get(0), qubitAddressFromClient);
        addressesCorrespondenceMap.put(addressesForController.get(1), qubitAddressFromClient);
        return addressesForController;
    }

    private List<LogicalQubitAddressForController> getGlobalLogicalQubitAddressFromLocalClient(LogicalQubitAddressFromClient qubitAddressFromClient) {
        List<LogicalQubitAddressForController> logicalQubitAddressForControllers = new LinkedList<LogicalQubitAddressForController>();
        for (LogicalQubitAddressForController key : addressesCorrespondenceMap.keySet()) {
            if (addressesCorrespondenceMap.get(key).equals(qubitAddressFromClient)) {
                if (key.getMemoryPart() == 0) {
                    logicalQubitAddressForControllers.add(0, key);
                } else {
                    logicalQubitAddressForControllers.add(key);
                }
            }
        }
        return logicalQubitAddressForControllers;
    }

    public void executeNextCommand() {
        if (threadForCommandsExecuting == null) {
            threadForCommandsExecuting = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!mCommandsForControllerMap.isEmpty()) {
                        int commandsMaxCount = mQuantumMemoryOperator.getCommandsMaxCount();
                        Map<OwnerData, List<LogicalAddressingCommand>> dataForController = new HashMap<OwnerData, List<LogicalAddressingCommand>>();
                        int commandsCount = 0;
                        for (OwnerData ownerData : mOwnerDataList) {
                            List<LogicalAddressingCommand> commandsList = mCommandsForControllerMap.get(ownerData);
                            if (commandsList != null) {
                                commandsCount += commandsList.size();
                                if (commandsCount <= commandsMaxCount) {
                                    dataForController.put(ownerData, commandsList);
                                } else {
                                    break;
                                }
                            }
                        }
                        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> results = mQuantumMemoryOperator.executeCommands(dataForController);
                        for (OwnerData ownerData : results.keySet()) {
                            Map<LogicalQubitAddressForController, Boolean> measureResultsWithLogicalAddresses = results.get(ownerData);
                            Map<LogicalQubitAddressFromClient, TopLevelResult> measureResultsWithClientAddresses = new HashMap<LogicalQubitAddressFromClient, TopLevelResult>();
                            for (LogicalQubitAddressForController logicalQubitAddressForController : measureResultsWithLogicalAddresses.keySet()) {
                                LogicalQubitAddressFromClient logicalQubitAddressFromClient = addressesCorrespondenceMap.get(logicalQubitAddressForController);
                                if (logicalQubitAddressFromClient != null) {
                                    TopLevelResult oneLoqicalQubitResult = measureResultsWithClientAddresses.get(logicalQubitAddressFromClient);
                                    if (oneLoqicalQubitResult == null) {
                                        oneLoqicalQubitResult = new TopLevelResult();
                                        measureResultsWithClientAddresses.put(logicalQubitAddressFromClient, oneLoqicalQubitResult);
                                    }
                                    //todo проверить: сначала положил в мар, потом изменяю значение полей одного из значений
                                    if (logicalQubitAddressForController.getMemoryPart() == 0) {
                                        oneLoqicalQubitResult.setQubit_1MeasureResult(measureResultsWithLogicalAddresses.get(logicalQubitAddressForController));
                                    } else {
                                        oneLoqicalQubitResult.setQubit_2MeasureResult(measureResultsWithLogicalAddresses.get(logicalQubitAddressForController));
                                    }
                                }
                            }
                            LinkedHashMap<LogicalQubitAddressFromClient, Integer> resultsForSending = new LinkedHashMap<LogicalQubitAddressFromClient, Integer>();
                            for (LogicalQubitAddressFromClient addressFromClient : measureResultsWithClientAddresses.keySet()) {
                                TopLevelResult result = measureResultsWithClientAddresses.get(addressFromClient);
                                if (result.getQubit_1MeasureResult() == false && result.getQubit_2MeasureResult() == true) {
                                    resultsForSending.put(addressFromClient, 0);
                                } else if (result.getQubit_1MeasureResult() == true && result.getQubit_2MeasureResult() == false) {
                                    resultsForSending.put(addressFromClient, 1);
                                } else {
                                    //оба физических кубита дали при измерении 0 или 1 - ошибка
                                }
                            }
                            printResults(ownerData, resultsForSending);
                            //todo вернуть пользователю по socket с помощью OwnerData
                            mCommandsForControllerMap.remove(ownerData);
                            mOwnerDataList.remove(ownerData);
                        }
                    }
                    threadForCommandsExecuting = null;
                }
            });
            threadForCommandsExecuting.run();
        }
    }

    private void printResults(OwnerData ownerData, LinkedHashMap<LogicalQubitAddressFromClient, Integer> results) {
        System.out.println("UserId = " + ownerData.getUserId());
        for (LogicalQubitAddressFromClient addressFromClient : results.keySet()) {
            System.out.println("AddressFromClient = " + addressFromClient.getLogicalQubitAddress() + " , result = " + results.get(addressFromClient));
        }
    }
}
