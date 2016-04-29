package kpfu.magistracy.controller.execution.commands;

import com.sun.istack.internal.NotNull;
import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

/**
 * Created by Danila on 29.04.2016.
 */
public class MeasureCommand extends PhysicalAddressingCommand {

    public MeasureCommand(@NotNull GlobalQubitAddress qubitForMeasure) {
        setCommandType(CommandTypes.MEASURE);
        setFirstQubit_Part1(qubitForMeasure);
    }
}
