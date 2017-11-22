package com.tw.metadata.web;

import org.junit.Test;

public class ExceptionTest {

    @Test
    public void name() throws Exception2 {
        try {
            throw new Exception1("1....");
        } finally {
            throw new Exception2("2.....");
        }
    }
}

class Exception1 extends Exception {
    public Exception1(String message) {
        super(message);
    }
}

class Exception2 extends Exception {
    public Exception2(String message) {
        super(message);
    }
}
