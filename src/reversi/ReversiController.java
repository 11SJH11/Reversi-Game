package reversi;

import java.awt.Color;

import javax.swing.JPanel;

public class ReversiController implements IController{
	
	
	IModel model;
	IView view;

	@Override
	public void initialise(IModel model, IView view) {
		this.model = model;
		this.view = view;
		
		
	}

	@Override
	public void startup() {
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		for ( int x = 0 ; x < width ; x++ )
			for ( int y = 0 ; y < height ; y++ )
				model.setBoardContents(x, y, 0);
		
		model.setPlayer(1);
		
		model.setBoardContents(3, 3, 1);
		model.setBoardContents(4, 4, 1);
		model.setBoardContents(3, 4, 2);
		model.setBoardContents(4, 3, 2);
		
		
		
		view.feedbackToUser((model.getPlayer() == 1) ? 1 : 2, "White player - choose where to put your piece");
		view.feedbackToUser((model.getPlayer() == 2) ? 1 : 2, "Black player - not your turn");
		view.refreshView();
		update();
		
	}

	@Override
	public void update() {
	    if (isBoardFull()) {
	        model.setFinished(true);
	        endGame();
	        return;
	    } else if (isBoardEmpty()) {
	        model.setFinished(true);
	        endGame();
	        return;
	    } else if (!isBoardOne()) { 
	        model.setFinished(true);
	        endGame();
	        return;
	    }

	    model.setFinished(false);

	    view.feedbackToUser(1, (model.getPlayer() == 2) ? "White player - not your turn" : "White player - choose where to put your piece");
	    view.feedbackToUser(2, (model.getPlayer() == 1) ? "Black player - not your turn" : "Black player - choose where to put your piece");
	    view.refreshView();
	}
	
	private boolean isBoardEmpty() {
	    int boardWidth = model.getBoardWidth();
	    int boardHeight = model.getBoardHeight();

	    for (int x = 0; x < boardWidth; x++) {
	        for (int y = 0; y < boardHeight; y++) {
	            if (model.getBoardContents(x, y) != 0) {
	                return false; 
	            }
	        }
	    }

	    return true;
	}
	
	
	private boolean isBoardFull() {
	    int boardWidth = model.getBoardWidth();
	    int boardHeight = model.getBoardHeight();
	    
	    for (int x = 0; x < boardWidth; x++) {
	        for (int y = 0; y < boardHeight; y++) {
	            if (model.getBoardContents(x, y) == 0) {
	                return false;
	            }
	        }
	    }
	    return true;
	}
	
	private boolean isBoardOne() {
	    int boardWidth = model.getBoardWidth();
	    int boardHeight = model.getBoardHeight();
	    boolean hasNonWhite = false;
	    boolean hasNonBlack = false;

	    
	    for (int x = 0; x < boardWidth; x++) {
	        for (int y = 0; y < boardHeight; y++) {
	            if (model.getBoardContents(x, y) == 1) {
	                hasNonWhite = true; 
	            } else if (model.getBoardContents(x, y) == 2) {
	                hasNonBlack = true; 
	            }
	        }
	    }
	    return (hasNonWhite == true && hasNonBlack == true);
	}
	
	@Override
	public void squareSelected(int player, int x, int y) {
	    if (!hasValidMoves(model.getPlayer())) {
	        if (!hasValidMoves((model.getPlayer() == 1) ? 2 : 1)) {
	            endGame();
	        } else {
	            model.setPlayer((model.getPlayer() == 1) ? 2 : 1);
	            update();
	        }
	    }

	    if (player != model.getPlayer()) {
	        view.feedbackToUser(model.getPlayer(), (player == 1) ? "White player - It is not your turn!" : "Black player - It is not your turn!");
	        return;
	    }

	    if (model.getBoardContents(x, y) != 0) {
	        return;
	    }

	    if (!isValidMove(player, x, y)) {
	        return;
	    }

	    model.setBoardContents(x, y, player);
	    flipChips(player, x, y);
	    model.setPlayer((player == 1) ? 2 : 1);

	    view.refreshView();
	    update();
	}

		

	@Override
	public void doAutomatedMove(int player) {
	    int bestX = -1;
	    int bestY = -1;
	    int maxFlips = 0;
	    
	    
	    if (!hasValidMoves(model.getPlayer())) {
	        if (!hasValidMoves((model.getPlayer() == 1) ? 2 : 1)) { 
	            endGame();
	            return;
	        }
	    }
	    
	    
	    if (player != model.getPlayer()) {
	        view.feedbackToUser(player, "It is not your turn!");
	        return;
	    }
	    	
	    
	    
	    for (int x = 0; x < model.getBoardWidth(); x++) {
	        for (int y = 0; y < model.getBoardHeight(); y++) {
	            if (model.getBoardContents(x, y) == 0 && isValidMove(player, x, y)) {
	                int flips = countFlips(player, x, y);
	                if (flips > maxFlips) {
	                    maxFlips = flips;
	                    bestX = x;
	                    bestY = y;
	                }
	            }
	        }
	    }

	    if (bestX != -1 && bestY != -1) {
	        model.setBoardContents(bestX, bestY, player);
	        flipChips(player, bestX, bestY);
	        model.setPlayer((player == 1) ? 2 : 1);
	        view.refreshView();
	        update();
	    }
	    
	    if (!hasValidMoves(model.getPlayer())) { 
	        if (!hasValidMoves((model.getPlayer() == 1) ? 2 : 1)) { 
	            endGame(); 
	            return;
	        }
	    }
	}

	private int countFlips(int player, int x, int y) {
	    int totalFlips = 0;

	    for (int dx = -1; dx <= 1; dx++) {
	        for (int dy = -1; dy <= 1; dy++) {
	            if (dx == 0 && dy == 0) {
	                continue;
	            }
	            if (isValidDirection(player, x, y, dx, dy)) {
	                totalFlips += countFlipsInDirection(player, x, y, dx, dy);
	            }
	        }
	    }

	    return totalFlips;
	}

	private int countFlipsInDirection(int player, int x, int y, int dx, int dy) {
	    int opponent = (player == 1) ? 2 : 1;
	    int flips = 0;
	    x += dx;
	    y += dy;

	    while (x >= 0 && x < model.getBoardWidth() && y >= 0 && y < model.getBoardHeight()) {
	        if (model.getBoardContents(x, y) == opponent) {
	            flips++;
	            x += dx;
	            y += dy;
	        } else if (model.getBoardContents(x, y) == player) {
	            return flips;
	        } else {
	            return 0;
	        }
	    }

	    return 0;
	}

	private boolean hasValidMoves(int player) {
	    int boardWidth = model.getBoardWidth();
	    int boardHeight = model.getBoardHeight();
	    
	    
	    for (int x = 0; x < boardWidth; x++) {
	        for (int y = 0; y < boardHeight; y++) {
	            if (model.getBoardContents(x, y) == 0) {
	                if (isValidMove(player, x, y)) {
	                    return true; 
	                }
	            }
	        }
	    }
	    
	    return false; 
	}
	
	private boolean isValidMove(int player, int x, int y) {
	    if (model.getBoardContents(x, y) != 0) {
	        return false; // Square is not empty
	    }
	    for (int dx = -1; dx <= 1; dx++) {
	        for (int dy = -1; dy <= 1; dy++) {
	            if (dx == 0 && dy == 0) {
	                continue;
	            }
	            if (isValidDirection(player, x, y, dx, dy)) {
	                return true; 
	            }
	        }
	    }
	    return false; 
	}

	private boolean isValidDirection(int player, int x, int y, int dx, int dy) {
	    int opponent = (player == 1) ? 2 : 1;
	    boolean foundOpponent = false;
	    x += dx;
	    y += dy;

	    while (x >= 0 && x < model.getBoardWidth() && y >= 0 && y < model.getBoardHeight()) {
	        if (model.getBoardContents(x, y) == opponent) {
	            foundOpponent = true;
	            x += dx;
	            y += dy;
	        } else if (model.getBoardContents(x, y) == player) {
	            return foundOpponent; 
	        } else {
	            break;
	        }
	    }
	    return false; 
	}
	
	private void flipChips(int player, int x, int y) {
	    for (int dx = -1; dx <= 1; dx++) {
	        for (int dy = -1; dy <= 1; dy++) {
	            if (dx == 0 && dy == 0) {
	                continue;
	            }
	            if (isValidDirection(player, x, y, dx, dy)) {
	                flipChipsInDirection(player, x, y, dx, dy);
	            }
	        }
	    }
	}

	private void flipChipsInDirection(int player, int x, int y, int dx, int dy) {
	    int opponent = (player == 1) ? 2 : 1;
	    boolean foundOpponent = false;
	    x += dx;
	    y += dy;
	    while (x >= 0 && x < model.getBoardWidth() && y >= 0 && y < model.getBoardHeight()) {
	        if (model.getBoardContents(x, y) == opponent) {
	            foundOpponent = true;
	            model.setBoardContents(x, y, player);
	            x += dx;
	            y += dy;
	        } else if (model.getBoardContents(x, y) == player) {
	            if (foundOpponent) {
	                break;
	            }
	        } else {
	            break;
	        }
	    }
	}

	
	private void endGame() {
		int boardWidth = model.getBoardWidth();
	    int boardHeight = model.getBoardHeight();
	    
	    int whiteCount = 0;
	    int blackCount = 0;
	    
		for (int x = 0; x < boardWidth; x++) {
	        for (int y = 0; y < boardHeight; y++) {
	            if (model.getBoardContents(x, y) == 1) {
	                whiteCount += 1;
	            }else if (model.getBoardContents(x, y) == 2) {
	                blackCount += 1;
	            }
	        }
	    }
		
		if (whiteCount == blackCount) {
			view.feedbackToUser(1,  String.format("Draw. Both players ended with %d pieces. Reset game to replay.", whiteCount));
			view.feedbackToUser(2,  String.format("Draw. Both players ended with %d pieces. Reset game to replay.", blackCount));
		}else {

		view.feedbackToUser(1, (whiteCount > blackCount) ?  String.format("White won. White %d to Black %d. Reset the game to replay.", whiteCount, blackCount):String.format("Black won. Black %d to White %d. Reset the game to replay.", blackCount, whiteCount) );
		view.feedbackToUser(2, (whiteCount > blackCount) ?  String.format("White won. White %d to Black %d. Reset the game to replay.", whiteCount, blackCount):String.format("Black won. Black %d to White %d. Reset the game to replay.", blackCount, whiteCount) );
		}
	}
	
}
