package com.game.base.state;

/**
 * @author zheng
 */
public class SecondaryState extends CardState {

    public SecondaryState(CardState cardState) {
        super();
        this.cardPlay = cardState.cardPlay;
    }

    @Override
    void play() {

    }

    @Override
    void doubleScore() {
        System.out.println("second double score  ");
        cardPlay.setBalance(cardPlay.getBalance()*2);
        stateCheck();
    }

    @Override
    void handCards() {

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
