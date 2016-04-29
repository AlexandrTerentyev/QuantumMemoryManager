package kpfu.magistracy.service_for_controller.commands;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommandsFromClientDTO {

    @SerializedName("qubit_count")
    private Integer mQubitCount;

    @SerializedName("commands")
    private List<LogicalAddressingCommandFromClient> mLogicalAddressingCommandFromClientList;

    public Integer getQubitCount() {
        return mQubitCount;
    }

    public List<LogicalAddressingCommandFromClient> getLogicalAddressingCommandFromClientList() {
        return mLogicalAddressingCommandFromClientList;
    }
}
