package kpfu.magistracy.service_for_controller.commands;

import com.google.gson.annotations.SerializedName;
import kpfu.magistracy.controller.execution.commands.CommandTypes;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressFromClient;


public class LogicalAddressingCommandFromClient {
    @SerializedName("command_name")
    private CommandTypes mCommandType;
    @SerializedName("command_param")
    private Double mCommandParam;
    @SerializedName("qubit_1")
    private LogicalQubitAddressFromClient mQubit_1;
    @SerializedName("qubit_2")
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
