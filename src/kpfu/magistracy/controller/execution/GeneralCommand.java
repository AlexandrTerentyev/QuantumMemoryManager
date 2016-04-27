package kpfu.magistracy.controller.execution;

public class GeneralCommand {

    private long userId;

    private int userCommandNumber;

    public static class SingleCommand {

        private CommandTypes mCommandType;

        private double mCommandParam;

        private long mQubit_1;
        private long mQubit_2;
        private long mQubit_3;

    }

    public enum CommandTypes {
        CQET, QET, PHASE
    }

}
