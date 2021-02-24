package mapelites.search;

import benchmarks.PlayRinascimento;
import benchmarks.RinascimentoEnv;
import com.google.gson.JsonObject;
import game.Parameters;
import game.adapters.WonAdapter;
import game.budget.ActionsBudget;
import game.budget.Budget;
import game.log.RinascimentoEventDispatcher;
import game.log.converters.EventIdConverter;
import game.log.converters.SimpleConverter;
import log.LogGroup;
import mapelites.BehavioursFunction;
import mapelites.FitnessFunction;
import mapelites.Search;
import mapelites.behaviours.*;
import mapelites.core.BehaviourSpace;
import mapelites.core.binning.Binning;
import mapelites.core.binning.LinearBinning;
import mapelites.core.summary.SummariseSolution;
import mapelites.interfaces.SolutionSpace;
import players.BasePlayerInterface;
import players.ai.explicit.RandomPlayer;
import statistics.player.PlayerNumericalStatistic;
import statistics.player.WinGameStats;
import utils.AgentsConfig;

import java.util.ArrayList;

public abstract class SearchCreator {

    // ENV
    protected String gameVersion = "assets/defaultx2/";
    private Budget budget = new ActionsBudget(1000);
    private int resampling;
    private String behavioursFile;
    private ArrayList<BasePlayerInterface> opponents = new ArrayList<BasePlayerInterface>(){{
        add(new RandomPlayer());
    }};

    // EVENT Logging
    private EventIdConverter converter = new SimpleConverter();

    public String[] behaviourNames;
    public PlayerNumericalStatistic[] featuresStats;
    public Binning[] statsBinning;

    public PlayerNumericalStatistic[] supportStats;
    public String[] supportNames;

    private LogGroup lg;

    public void init(JsonObject mapArgs, LogGroup lg){
        this.lg = lg;

        // game/version [path]
        // game/opponentfile [path]
        // game/opponentindex [0-N]
        // search/resampling [positive int]
        // search/behaviourFile [path]

        gameVersion = mapArgs.get("game/version").getAsString();
        opponents = new ArrayList<BasePlayerInterface>(){{
            add(PlayRinascimento.decodePlayers(
                    AgentsConfig.readJson(mapArgs.get("game/opponentfile").getAsString()),
                    Parameters.load(gameVersion))[mapArgs.get("game/opponentindex").getAsInt()]);
        }};

        resampling = mapArgs.get("search/resampling").getAsInt();
        behavioursFile = mapArgs.get("search/behaviours").getAsString();

        lg.add("opponents",opponents.toArray());
    }

   private void manageBehaviours(RinascimentoEventDispatcher dispatcher){
        String playerName = FitnessFunction.playerName;
        BehaviourReader br = new BehaviourReader(behavioursFile);
        int b_size = br.size();
        int metric_size = br.metricsCount();
        int bIndex = 0;

       featuresStats = new PlayerNumericalStatistic[metric_size];
       statsBinning = new Binning[metric_size];
       behaviourNames = new String[metric_size];

       supportStats = new PlayerNumericalStatistic[b_size-metric_size];
       supportNames = new String[b_size-metric_size];

       for(int i=0; i<b_size; i++){
           PlayerNumericalStatistic stat = pickStatistic(br.getCode(i), br.getSaveHistory(i));
           if(br.getIsMetric(i)) {
               featuresStats[bIndex] = stat;
               statsBinning[bIndex] = new LinearBinning(br.getMin(i), br.getMax(i), br.getBreaks(i));
               behaviourNames[bIndex] = br.getName(i);
               bIndex++;
           }else{
               supportStats[i-bIndex] = stat;
               supportNames[i-bIndex] = br.getName(i);
           }
           if (stat instanceof LoggingStatistic) {
               LoggingStatistic ls = (LoggingStatistic) stat;
               dispatcher.addLogger(ls.getPlayerLoggerName(playerName), ls.getLoggerInstance(playerName));
           }
       }

    }

    private PlayerNumericalStatistic pickStatistic(String code, boolean keepHistory){
        PlayerNumericalStatistic stat;
        switch (code){
                case "CardCount": stat = new CardCount();break;
                case "Nobles": stat = new Nobles();break;
                case "TotalCoins": stat = new TotalCoins();break;
                case "ReservedCards": stat = new ReservedCards();break;
                case "CardBoughtDeck": stat = new CardBoughtDeck();break;
                case "CardCost": stat = new CardCost();break;
                case "GameDuration": stat = new GameDuration();break;
                case "Points": stat = new Points(); break;
            default: stat = null;
        }

        if(keepHistory){
            stat = (PlayerNumericalStatistic) stat.keepHistory();
        }

        return stat;
    }

    public Search create(){
        RinascimentoEventDispatcher logger = new RinascimentoEventDispatcher();

        manageBehaviours(logger);

        lg.add("bins",statsBinning);
        lg.add("behaviours",behaviourNames);
        lg.add("support", supportNames);

        FitnessFunction rff = new FitnessFunction(
                gameVersion,
                new WinGameStats(new WonAdapter()),
                featuresStats,
                supportStats
        );

        rff.setExperiments(resampling);
        rff.getEnvironment().setPlayersBudget(budget);
        rff.getEnvironment().setDispatcher(logger);
        rff.opponents = opponents;

        BehavioursFunction rbf = rff.getBehaviourFunction();

        BehaviourSpace bs = new BehaviourSpace(statsBinning);
        bs.setNames(behaviourNames);

        SolutionSpace ss = getSolutionSpace();

        RinascimentoEnv.VERBOSE = false;

        return new Search(ss,rff,bs,rbf);
    }

    public String summary(){
        StringBuilder builder = new StringBuilder();
        builder.append("[game version: "+gameVersion+"]\n");
        builder.append("[opponent: "+ opponents.get(0).getClass().getSimpleName() +"]\n");
        return builder.toString();
    }

    public void setConverter(EventIdConverter converter) {
        this.converter = converter;
    }
    public EventIdConverter getConverter(){
        return converter;
    }

    public abstract SolutionSpace getSolutionSpace();

    public abstract SummariseSolution summariseSolution();
}
