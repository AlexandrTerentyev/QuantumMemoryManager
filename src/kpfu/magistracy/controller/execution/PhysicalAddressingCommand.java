package kpfu.magistracy.controller.execution;

import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

public class PhysicalAddressingCommand {

    private CommandTypes mCommandType;

    private Double mCommandParam;

    private QuantumMemoryAddress mQubit_1;
    private QuantumMemoryAddress mQubit_2;
    private QuantumMemoryAddress mQubit_3;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public QuantumMemoryAddress getQubit_1() {
        return mQubit_1;
    }

    public QuantumMemoryAddress getQubit_2() {
        return mQubit_2;
    }

    public QuantumMemoryAddress getQubit_3() {
        return mQubit_3;
    }
}
