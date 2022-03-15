package com.game.base.state;

/**
 * @author zheng
 */
public class ProfessioinNal extends CardState {
    public ProfessioinNal() {
    }

    public ProfessioinNal(CardState cardState) {
        super();
        this.cardPlay = cardState.cardPlay;
    }

    @Override
    void play() {
        System.out.println("professional play");
    }

    @Override
    void doubleScore() {
        System.out.println("professional double score");
        cardPlay.setBalance(cardPlay.getBalance()*2);
        stateCheck();
    }

    @Override
    void handCards() {
        System.out.println("professional hand cards");
    }

    @Override
    void peekCards() {
        System.out.println("professional peekCards ");
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
