package bcco.wWar.game;

/**
 * @author Cristofer Oswald
 * @since 29/01/17
 */

public enum GameStates{
    INICIO("Inicio"),
    JOGANDO("Jogando");

    String nome_;

    GameStates(String nome){
        nome_ = nome;
    }
}