package com.arthur.redcup;

import com.arthur.redcup.Model.EventData;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Double.valueOf;
import static org.junit.Assert.*;

public class EventDataUnitTest {

    @Test
    public void shouldValidateYear(){
        EventData eventData = new EventData();
        eventData.setYear("2000");
        assertEquals("2000", eventData.getYear());
    }

    @Test
    public void shouldValidateMonth(){
        EventData eventData = new EventData();
        eventData.setMonth("August");
        assertEquals("August", eventData.getMonth());
    }

    @Test
    public void shouldValidateDay(){
        EventData eventData = new EventData();
        eventData.setDay("day");
        assertEquals("day", eventData.getDay());
    }

    @Test
    public void shouldValidateHour(){
        EventData eventData = new EventData();
        eventData.setHour("20");
        assertEquals("20", eventData.getHour());
    }

    @Test
    public void shouldValidateMinute(){
        EventData eventData = new EventData();
        eventData.setMinute("200");
        assertEquals("200", eventData.getMinute());
    }

    @Test
    public void shouldValidateSecond(){
        EventData eventData = new EventData();
        eventData.setSecond("2");
        assertEquals("2", eventData.getSecond());
    }
}
