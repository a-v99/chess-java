package boardgame;

import java.util.ArrayList;
import java.util.List;

import chess.ChessPiece;
import chess.Color;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Error creating board: There must be at least 1 row and 1 column");
		}

		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}

		return pieces[row][column];
	}

	public Piece piece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}

		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}

		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}

	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}

	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}

	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}

	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}

		return piece(position) != null;
	}

	public List<Piece> getPieces(Color color) {
		List<Piece> activePieces = new ArrayList<>();

		for (int row = 0; row < pieces.length; row++) {
			for (int col = 0; col < pieces[row].length; col++) {
				Piece piece = pieces[row][col];
				if ((ChessPiece) piece != null && ((ChessPiece) piece).getColor() == color) {
					activePieces.add(piece);
				}
			}
		}

		return activePieces;
	}

	public boolean isUnderAttack(Position position, Color color) {
		Color opponentColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
		List<Piece> opponentPieces = getPieces(opponentColor);
		for (Piece piece : opponentPieces) {
			boolean[][] possibleMoves = piece.possibleMoves();
			if (possibleMoves[position.getRow()][position.getColumn()]) {
				return true;
			}
		}

		return false;
	}
}