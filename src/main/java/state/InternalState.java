package state;

public class InternalState implements IHockeyState {

    private HockeyContext hockeyContext;
    private ISimulateState simulateState;

    public InternalState(HockeyContext hockeyContext){
        this.hockeyContext = hockeyContext;
        simulateState = new TradeState();
    }

    @Override
    public void entry() {
        System.out.println("Simulating Season");
    }

    @Override
    public void process() {
        do{
            simulateState = simulateState.action();
        }while(simulateState!=null);
    }

    @Override
    public IHockeyState exit() {
        System.out.println("Exiting App!");
        return null;
    }
}