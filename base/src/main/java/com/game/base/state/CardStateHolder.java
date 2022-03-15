package com.game.base.state;

/**
 * @author zheng
 */
public enum CardStateHolder {
    PROFESSIONAL(new ProfessioinNal());
    private CardState cardState;

    CardStateHolder(CardState cardState) {
        this.cardState = cardState;
    }
}
