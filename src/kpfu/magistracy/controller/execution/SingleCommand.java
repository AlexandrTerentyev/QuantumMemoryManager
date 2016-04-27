package kpfu.magistracy.controller.execution;

public class SingleCommand {

    private CommandTypes mCommandType;

    private Double mCommandParam;

    private Long mQubit_1;
    private Long mQubit_2;
    private Long mQubit_3;

    public enum CommandTypes {
        CQET, QET, PHASE
    }

    public static class Builder {

        private SingleCommand singleCommand;

        public Builder() {
            singleCommand = new SingleCommand();
        }

        public Builder setCommand(CommandTypes commandType) {
            singleCommand.mCommandType = commandType;
            return this;
        }

        public Builder setCommandParam(Double commandParam) {
            singleCommand.mCommandParam = commandParam;
            return this;
        }

        public Builder setFirstQubit(Long qubitAddress) {
            singleCommand.mQubit_1 = qubitAddress;
            return this;
        }

        public Builder setSecondQubit(Long qubitAddress) {
            singleCommand.mQubit_2 = qubitAddress;
            return this;
        }

        public Builder setThirdQubit(Long qubitAddress) {
            singleCommand.mQubit_3 = qubitAddress;
            return this;
        }

        public SingleCommand build() {
            return singleCommand;
        }
    }
}


