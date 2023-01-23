package com.atlxc.VulnScan.exception;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException() {
        super("账户不存在");
    }
}
