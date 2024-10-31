package com.penaestrada.infra;

import java.time.format.DateTimeFormatter;

public class DefaultDateFormatter {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
}
