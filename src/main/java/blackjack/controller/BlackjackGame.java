package blackjack.controller;

import blackjack.domain.GameBoard;
import blackjack.domain.Dealer;
import blackjack.domain.participants.GameParticipant;
import blackjack.domain.participants.Name;
import blackjack.domain.participants.Player;
import blackjack.domain.participants.Players;
import blackjack.dto.ParticipantDto;
import blackjack.view.InputView;
import blackjack.view.OutputView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlackjackGame {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        Players players = createPlayers();
        Dealer dealer = new Dealer();
        GameBoard gameBoard = new GameBoard(dealer, players);
        play(gameBoard);
    }

    private Players createPlayers() {
        List<String> names = inputView.readNames();
        List<Player> playerList = names.stream()
                .map(name -> new Player(new Name(name)))
                .toList();
        return new Players(playerList);
    }

    private void play(GameBoard gameBoard) {
        startSetting(gameBoard);
        proceedPlayerTurn(gameBoard);
        proceedDealerTurn(gameBoard);
        handleResult(gameBoard);
        handleVictory(gameBoard);
    }

    private void startSetting(GameBoard gameBoard) {
        gameBoard.distributeInitialHands();
        GameParticipant dealer = gameBoard.getDealer();
        ParticipantDto dealerDto = ParticipantDto.from(dealer);
        List<ParticipantDto> playersDto = gameBoard.getPlayers()
                .getPlayers()
                .stream()
                .map(ParticipantDto::from)
                .toList();
        outputView.printSetting(dealerDto, playersDto);
        outputView.printNewLine();
    }

    private void proceedPlayerTurn(GameBoard gameBoard) {
        List<Player> players = gameBoard.getPlayers().getPlayers();
        for (Player player : players) {
            proceedOnePlayerTurn(gameBoard, player);
        }
    }

    private void proceedDealerTurn(GameBoard gameBoard) {
        outputView.printNewLine();
        while (gameBoard.isDealerNotOver()) {
            gameBoard.addCardToDealer();
            outputView.printDealerOneMoreCard();
        }
        outputView.printNewLine();
    }

    private void proceedOnePlayerTurn(GameBoard gameBoard, Player player) {
        while (player.canHit() &&
                inputView.readCommand(player.getName().getName())) {
            gameBoard.addCardToPlayer(player);
            outputView.printCurrentCard(ParticipantDto.from(player));
            outputView.printNewLine();
        }
    }

    private void handleResult(GameBoard gameBoard) {
        GameParticipant dealer = gameBoard.getDealer();
        ParticipantDto dealerDto = ParticipantDto.from(dealer);
        List<ParticipantDto> playersDto = gameBoard.getPlayers()
                .getPlayers()
                .stream()
                .map(ParticipantDto::from)
                .toList();
        outputView.printScoreResult(dealerDto, playersDto);
    }

    private void handleVictory(GameBoard gameBoard) {
        Map<Player, Boolean> playerVictory = gameBoard.calculateWinOrLose();
        Map<String, Boolean> playerNameVictory = new LinkedHashMap<>();
        playerVictory.forEach(
                (key, value) -> playerNameVictory.put(key.getName().getName(), playerVictory.get(key)));
        outputView.printResult(playerNameVictory);
    }
}
