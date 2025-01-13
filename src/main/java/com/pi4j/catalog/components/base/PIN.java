package com.pi4j.catalog.components.base;

/**
 * Helper Class, used as Raspberry-Pi pin-numbering. Is helpful to see, which pin can act as what I/O provider
 */
public enum PIN {
    SDA1(2),
    SCL1(2),
    TXD(14),
    RXD(15),
    D4(4),
    D5(5),
    D6(6),
    D11(11),
    D12(12),
    D13(13),
    D16(16),
    D17(17),
    D20(20),
    D21(21),
    D22(22),
    D23(23),
    D24(24),
    D25(25),
    D26(26),
    D27(27),
    MOSI(10),
    MISO(9),
    CEO(8),
    CE1(7),
    PWM18(18),
    PWM19(19);

    private final int pin;

    PIN(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
