package kpfu.magistracy.controller.execution.commands;

import com.google.gson.annotations.SerializedName;

public enum CommandTypes {
    @SerializedName("CQET")
    CQET,
    @SerializedName("QET")
    QET,
    @SerializedName("PHASE")
    PHASE;
}