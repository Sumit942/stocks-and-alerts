package com.stock.demo.test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

interface function {
	public void run(String person);
}

public class Test {

	public static void main(String[] args) {
		System.out.println(-1+3+1);
	}
	
	public static LocalDate getMovingAverageFromDate(int minusDays) {
		int minusWeekDays = minusDays;
		LocalDate today = LocalDate.now(),temp = null;
		for (int i = 0; i < minusWeekDays; i++) {
			temp = today.minusDays(i);
			if ( temp.getDayOfWeek().equals(DayOfWeek.SATURDAY) || temp.getDayOfWeek().equals(DayOfWeek.SUNDAY))
				minusWeekDays++;
		}
		
		return temp;
	}
}
