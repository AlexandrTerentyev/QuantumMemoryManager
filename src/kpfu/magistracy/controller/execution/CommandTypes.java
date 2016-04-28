package kpfu.magistracy.controller.execution;

import com.google.gson.annotations.SerializedName;

public enum CommandTypes {
    @SerializedName("CQET")
    CQET,
    @SerializedName("QET")
    QET,
    @SerializedName("PHASE")
    PHASE;
}