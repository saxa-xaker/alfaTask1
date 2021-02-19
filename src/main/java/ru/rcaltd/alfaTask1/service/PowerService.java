package ru.rcaltd.alfaTask1.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PowerService {

    public void alfatask1Restart() throws IOException {
        Runtime.getRuntime().exec("systemctl restart alfatask1");
    }

    public void alfatask1Shutdown() throws IOException {
        Runtime.getRuntime().exec("systemctl stop alfatask1");
    }

    public void systemRestart() throws IOException {
        Runtime.getRuntime().exec("reboot");
    }

    public void systemShutdown() throws IOException {
        Runtime.getRuntime().exec("shutdown -P now");
    }
}
