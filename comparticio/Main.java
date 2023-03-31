
package comparticio;

import IA.Comparticion.Usuarios;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {

    public static void main(String[] args){
        // Parametres: nusuaris ndrivers seed heuristica solIni
        // hill_annealing steps stiter k lambda
        // imprimirAccions imprimirRes

        assert(args.length == 14);

        int nusuaris = Integer.parseInt(args[1]);
        int ndrivers =  Integer.parseInt(args[2]);
        int seed =  Integer.parseInt(args[3]);
        int heuristica =  Integer.parseInt(args[4]);
        int solIni =  Integer.parseInt(args[5]);
        int hill_annealing =  Integer.parseInt(args[6]);
        int steps = Integer.parseInt(args[7]);
        int stiter =  Integer.parseInt(args[8]);
        int k =  Integer.parseInt(args[9]);
        double lambda = Double.parseDouble(args[10]);
        boolean imprimirAccions = Integer.parseInt(args[11]) != 0;
        boolean imprimirRes = Integer.parseInt(args[12]) != 0;
        boolean imprimirIni = Integer.parseInt(args[13]) != 0;

        Usuarios users = new Usuarios(nusuaris, ndrivers, seed);
        State initialState = new State(users, solIni);
        HeuristicFunction[] heuristics = { new Heuristic(),
                new Heuristic2(initialState) };

        Search[] searches = { new HillClimbingSearch(),
                new SimulatedAnnealingSearch(steps, stiter, k, lambda)};

        HeuristicFunction heuristic = heuristics[heuristica];

        Problem problem = new Problem(initialState, new SuccesorsHC(), new GoalState(), heuristic);
        Search search = searches[hill_annealing];

        if (imprimirIni) {
            System.out.println("Initial solution " + solIni);
            System.out.println("Path distance: " + initialState.getTotalDistance());
            System.out.println("Drivers: " + initialState.getDrivers());
            System.out.println();
        }

        try {

            long startTime = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem, search);
            State goal = (State) search.getGoalState();
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            if (imprimirAccions) {
                int s = 0;
                int m = 0;
                int u = 0;
                for (Object act : agent.getActions()) {
                    if (act.equals("S")) ++s;
                    else if (act.equals("M")) ++m;
                    else if (act.equals("U")) ++u;
                }

                System.out.println("Swaps: " + s);
                System.out.println("Moves: " + m);
                System.out.println("Unassigns: " + u);
            }

            if (imprimirRes) {
                System.out.println("Solution found");
                System.out.println("Time taken: " + (duration / 1000000));
                System.out.println("Path distance: " + goal.getTotalDistance());
                System.out.println("Drivers: " + goal.getDrivers());
                System.out.println();
            }
        } catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
