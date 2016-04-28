package kpfu.magistracy.service_for_controller;

import kpfu.magistracy.controller.execution.CommandTypes;


public class LogicalAddressingCommandFromClient {
    private CommandTypes mCommandType;

    private Double mCommandParam;

    private LogicalQubitAddressFromClient mQubit_1;
    private LogicalQubitAddressFromClient mQubit_2;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public LogicalQubitAddressFromClient getQubit_1() {
        return mQubit_1;
    }

    public LogicalQubitAddressFromClient getQubit_2() {
        return mQubit_2;
    }

}
