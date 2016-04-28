package kpfu.magistracy.controller.addresses;

public class LogicalQubitAddress {
    Integer logicalQubitNumber;
    // номер физического кубита внутри логического,
    //принимает значения 0 и 1
    Integer physicalQubitNumberInLogical;
    //todo for Andrey
    private long address;

    private int memoryPart;

    public long getAddress() {
        return address;
    }

    public int getMemoryPart() {
        return memoryPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalQubitAddress)) return false;

        LogicalQubitAddress that = (LogicalQubitAddress) o;

        return getAddress() == that.getAddress() && getMemoryPart() == that.getMemoryPart();

    }

}
