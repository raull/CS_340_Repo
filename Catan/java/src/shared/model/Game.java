package shared.model;

import shared.model.board.Map;
import shared.model.cards.Bank;

public class Game {
	
	private ScoreKeeper score;
	private TradeManager tradeManager;
	private TurnManager turnManager;
	private UserManager userManager;
	private Map map;
	private Bank bank;
}
