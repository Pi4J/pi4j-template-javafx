package com.pi4j.catalog.components.helpers;

/**
 * Helper Class, used as Raspberry-Pi pin-numbering. Is helpful to see, which pin can act as what I/O provider
 */
public enum PIN {
    SDA1(2),
    SCL1(2),
    D4(4),
    TXD(14),
    RXD(15),
    D17(17),
    PWM18(18),
    D27(27),
    D22(22),
    D23(23),
    D24(24),
    MOSI(10),
    MISO(9),
    D25(25),
    D11(11),
    CEO(8),
    CE1(7),
    D5(5),
    D6(6),
    D16(16),
    D26(26),
    D20(20),
    D21(21),
    PWM12(12),
    PWM13(13),
    PWM19(19);

    private final int pin;

    PIN(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
