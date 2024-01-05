package edu.uclm.esi.backJuegos.utils;

import java.util.ArrayList;
import java.util.List;

import edu.uclm.esi.backJuegos.constants.ConstantKeys;

public final class Utils {

	public static boolean isTie(String[][] board) {
		for (Integer y = 0; y < ConstantKeys.ROWS; y++) {
			for (Integer x = 0; x < ConstantKeys.COLUMNS; x++) {
				String currentCell = board[y][x];
				if (currentCell == ConstantKeys.CADENA_VACIA) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isWinner(String player, String[][] board) {
		for (Integer y = 0; y < ConstantKeys.ROWS; y++) {
			for (Integer x = 0; x < ConstantKeys.COLUMNS; x++) {
				Integer count = 0;
				count = countUp(x, y, player, board);
				if (count >= ConstantKeys.CONNECT)
					return true;
				count = countRight(x, y, player, board);
				if (count >= ConstantKeys.CONNECT)
					return true;
				count = countUpRight(x, y, player, board);
				if (count >= ConstantKeys.CONNECT)
					return true;
				count = countDownRight(x, y, player, board);
				if (count >= ConstantKeys.CONNECT)
					return true;
			}
		}
		return false;
	}

	private static Integer countDownRight(Integer x, Integer y, String player, String[][] board) {
		Integer endX = (x + ConstantKeys.CONNECT < ConstantKeys.COLUMNS) ? x + ConstantKeys.CONNECT - 1
				: ConstantKeys.COLUMNS - 1;
		Integer endY = (y + ConstantKeys.CONNECT < ConstantKeys.ROWS) ? y + ConstantKeys.CONNECT - 1
				: ConstantKeys.ROWS - 1;
		Integer counter = 0;
		while (x <= endX && y <= endY) {
			if (board[y][x].equals(player)) {
				counter++;
			} else {
				counter = 0;
			}
			x++;
			y++;
		}
		return counter;
	}

	private static Integer countUpRight(Integer x, Integer y, String player, String[][] board) {
		Integer endX = (x + ConstantKeys.CONNECT < ConstantKeys.COLUMNS) ? x + ConstantKeys.CONNECT - 1
				: ConstantKeys.COLUMNS - 1;
		Integer startY = (y - ConstantKeys.CONNECT >= 0) ? y - ConstantKeys.CONNECT + 1 : 0;
		Integer counter = 0;
		while (x <= endX && startY <= y) {
			if (board[y][x].equals(player)) {
				counter++;
			} else {
				counter = 0;
			}
			x++;
			y--;
		}
		return counter;
	}

	private static Integer countRight(Integer x, Integer y, String player, String[][] board) {
		Integer endX = (x + ConstantKeys.CONNECT < ConstantKeys.COLUMNS) ? x + ConstantKeys.CONNECT - 1
				: ConstantKeys.COLUMNS - 1;
		Integer counter = 0;
		for (; x <= endX; x++) {
			if (board[y][x].equals(player)) {
				counter++;
			} else {
				counter = 0;
			}
		}
		return counter;
	}

	private static Integer countUp(Integer x, Integer y, String player, String[][] board) {
		Integer startY = (y - ConstantKeys.CONNECT >= 0) ? y - ConstantKeys.CONNECT + 1 : 0;
		Integer counter = 0;

		for (; startY <= y; startY++) {
			if (board[startY][x].equals(player)) {
				counter++;
			} else {
				counter = 0;
			}
		}
		return counter;
	}

	public static List<Boolean> guessLetter(List<String> list) {

		List<Boolean> result = new ArrayList<>();

		if (list.size() != 2) {
			return result;
		}

		String letra = list.get(0);
		String palabra = list.get(1);

		for (int i = 0; i < palabra.length(); i++) {
			if (palabra.substring(i, i + 1).equals(letra)) {
				result.add(true);
			} else {
				result.add(false);
			}
		}

		return result;
	}

	public static Boolean guessWord(List<String> word) {

		boolean result = false;

		if (word.get(0).equals(word.get(1))) {
			result = true;
		}

		return result;
	}

}
