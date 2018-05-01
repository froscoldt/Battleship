package g11;

import battleship.interfaces.BattleshipsPlayer;
import tournament.player.PlayerFactory;

public class G11 implements PlayerFactory<BattleshipsPlayer> {

    
    public G11(){}
    
    
    @Override
    public BattleshipsPlayer getNewInstance()
    {
        return new RandomPlayer();
    }

    @Override
    public String getID()
    {
        return "G11";
    }

    @Override
    public String getName()
    {
        return "Random player";
    }

    @Override
    public String[] getAuthors()
    {
        String[] res = {"Mark Dyrby Denner","Mads","Damjan"};
        return res;
    }

}