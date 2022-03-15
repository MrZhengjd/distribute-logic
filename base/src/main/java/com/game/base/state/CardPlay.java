package com.game.base.state;

/**
 * @author zheng
 */
public class CardPlay {
    private double balance = 0;
    private CardState cardState;
    private String name;

    public CardPlay() {
        this.cardState = new PrimaryCardState(this);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public CardState getCardState() {
        return cardState;
    }

    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void doubleScore(){
        cardState.doubleScore();
    }
}
