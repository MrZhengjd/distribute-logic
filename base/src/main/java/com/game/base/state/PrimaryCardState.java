package com.game.base.state;

/**
 * @author zheng
 */
public class PrimaryCardState extends CardState {
    public PrimaryCardState() {
    }
    public PrimaryCardState(CardPlay cardPlay) {
        this.cardPlay = cardPlay;
    }
    public PrimaryCardState(CardState cardPlay) {
        this.cardPlay = cardPlay.cardPlay;
    }
    @Override
    void play() {

        System.out.println("play primary");
    }

    @Override
    void doubleScore() {
        System.out.println("double score primary ");
        cardPlay.setBalance(cardPlay.getBalance()*2);
        stateCheck();
    }

    @Override
    void handCards() {
        System.out.println("hand card primary ");
    }

    @Override
    void peekCards() {

    }
    private void stateCheck(){
        final double balance = cardPlay.getBalance();
        if (balance >= 100 && balance <= 200){
            cardPlay.setCardState(new SecondaryState(this));
        }else {
            cardPlay.setCardState(new ProfessioinNal(this));
        }
    }
}
