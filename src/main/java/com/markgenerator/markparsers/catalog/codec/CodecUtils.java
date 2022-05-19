package com.markgenerator.markparsers.catalog.codec;

public class CodecUtils {

    private static BaseNCoder base80Coder;

    public static BaseNCoder getBase80Coder() {
        if (base80Coder == null) {
            base80Coder = new BaseNCoder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"%&\'*+-./_,:;=<>?");
        }
        return base80Coder;
    }
}
