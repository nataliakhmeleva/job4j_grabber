package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.*;

public class HabrCareerDateTimeParserTest {
    @Test
    public void whenCorrectParseInDateTime() {
        DateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        String date = "2022-06-24T16:46:43+03:00";
        assertEquals(LocalDateTime.of(2022, Month.JUNE, 24, 16, 46, 43), dateTimeParser.parse(date));
        assertNotEquals(LocalDateTime.of(2022, Month.JUNE, 15, 16, 46, 43), dateTimeParser.parse(date));
    }

    @Test(expected = DateTimeParseException.class)
    public void whenUncorrectFormat() {
        DateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        String date = "2022/06/24";
        dateTimeParser.parse(date);
    }
}