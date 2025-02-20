package com.example.geektrust; 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Driver class for the actual execution
        Driver driver = new Driver(args[0]);
        driver.execute();
    }
}
