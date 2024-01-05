package edu.uclm.esi.backJuegos.services;

import org.springframework.stereotype.Service;

import edu.uclm.esi.backJuegos.constants.ConstantKeys;

@Service
public class CuatroEnRayaService {
	
	public boolean isTie(String [][] board) {
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
	
	public boolean isWinner(String player, String [][] board) {
		//String [][] invertida = invertirFilas(board);
	    for (Integer y = 0; y < ConstantKeys.ROWS; y++) {
	        for (Integer x = 0; x < ConstantKeys.COLUMNS; x++) {
	            Integer count = 0;
	            count = countUp(x, y, player, board);
	            if (count >= ConstantKeys.CONNECT) return true;
	            count = countRight(x, y, player, board);
	            if (count >= ConstantKeys.CONNECT) return true;
	            count = countUpRight(x, y, player, board);
	            if (count >= ConstantKeys.CONNECT) return true;
	            count = countDownRight(x, y, player, board);
	            if (count >= ConstantKeys.CONNECT) return true;
	        }
	    }
	    return false;
	}
	
	/*public String[][] invertirFilas(String[][] matriz) {
	    int filas = matriz.length;
	    int columnas = matriz[0].length;
	    String[][] matrizInvertida = new String[filas][columnas];

	    for (int fila = 0; fila < filas; fila++) {
	        for (int columna = 0; columna < columnas; columna++) {
	            matrizInvertida[filas - 1 - fila][columna] = matriz[fila][columna];
	        }
	    }

	    return matrizInvertida;
	}*/

	private Integer countDownRight(Integer x, Integer y, String player, String [][] board) {
	    Integer endX = (x + ConstantKeys.CONNECT < ConstantKeys.COLUMNS) ? x + ConstantKeys.CONNECT - 1 : ConstantKeys.COLUMNS - 1;
	    Integer endY = (y + ConstantKeys.CONNECT < ConstantKeys.ROWS) ? y + ConstantKeys.CONNECT - 1 : ConstantKeys.ROWS - 1;
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

	private Integer countUpRight(Integer x, Integer y, String player, String [][] board) {
	    Integer endX = (x + ConstantKeys.CONNECT < ConstantKeys.COLUMNS) ? x + ConstantKeys.CONNECT - 1 : ConstantKeys.COLUMNS - 1;
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

	private Integer countRight(Integer x, Integer y, String player, String [][] board) {
	    Integer endX = (x + ConstantKeys.CONNECT < ConstantKeys.COLUMNS) ? x + ConstantKeys.CONNECT - 1 : ConstantKeys.COLUMNS - 1;
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

	private Integer countUp(Integer x, Integer y, String player, String [][] board) {
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

}
