package com.atlxc.VulnScan.exception;

public class UserNameExistException extends RuntimeException{
    public UserNameExistException() {
        super("用户名存在");
    }
}
