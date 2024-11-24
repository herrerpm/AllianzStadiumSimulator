package Tables;

import Agents.AbstractAgent;
import Handlers.AbstractAgentHandler;

/**
 * AgentTableFactory is responsible for creating AgentTable instances for different agent types,
 * using the names of their managing threads.
 */
public class AgentTableFactory {

    /**
     * Creates a default AgentTable for agents extending AbstractAgent with "Thread Name" and "State" columns.
     *
     * @param title The title of the JFrame.
     * @param <S>   Enum type representing the state of the agent.
     * @param <A>   Type of agent extending AbstractAgent<S>.
     * @return A new instance of AgentTable<S, A> with default columns.
     */
    public static <S extends Enum<S>, A extends AbstractAgent<S>> AgentTable<S, A> createDefaultAgentTable(
            String title
    ) {
        String[] defaultColumns = {"Thread Name", "State"};
        return new AgentTable<>(title, defaultColumns);
    }

    /**
     * Creates an AgentTable based on an AbstractAgentHandler.
     *
     * @param handler The AbstractAgentHandler managing the agents.
     * @param title   The title of the JFrame.
     * @param <S>     Enum type representing the state of the agent.
     * @param <A>     Type of agent extending AbstractAgent<S>.
     * @return A new instance of AgentTable<S, A>.
     */
    public static <S extends Enum<S>, A extends AbstractAgent<S>> AgentTable<S, A> createAgentTableFromHandler(
            AbstractAgentHandler<S, A> handler,
            String title
    ) {
        AgentTable<S, A> agentTable = createDefaultAgentTable(title);
        // Initial population of the table
        agentTable.updateTable(handler.getAgents(), handler.getAgentThreads());

        // Optionally, you can set up observers or listeners to update the table dynamically
        // based on changes in the handler's agents.

        return agentTable;
    }
}
