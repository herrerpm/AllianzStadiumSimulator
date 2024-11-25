// File: AbstractTransactionManager.java
package Managers;

import Agents.AbstractAgent;
import Handlers.AbstractAgentHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * AbstractTransactionManager provides a generic framework for handling transactions between two types of agents.
 *
 * @param <Requester> The type of agent initiating the transaction.
 * @param <Responder> The type of agent responding to the transaction.
 */
public abstract class AbstractTransactionManager<Requester extends AbstractAgent<?>, Responder extends AbstractAgent<?>> {

    protected BlockingQueue<Responder> availableResponders;

    /**
     * Constructor initializes the queue of available responders.
     */
    public AbstractTransactionManager() {
        this.availableResponders = new LinkedBlockingQueue<>();
    }

    /**
     * Configures the TransactionManager with handlers for requesters and responders, and any other necessary parameters.
     *
     * @param requesterHandler Handler managing the Requester agents.
     * @param responderHandler Handler managing the Responder agents.
     * @param transactionTime  The time in milliseconds to simulate a transaction.
     */
    public abstract void configure(AbstractAgentHandler<?, Requester> requesterHandler,
                                   AbstractAgentHandler<?, Responder> responderHandler,
                                   int transactionTime);

    /**
     * Handles a transaction initiated by a requester agent.
     *
     * @param requester The agent initiating the transaction.
     */
    public abstract void handleTransaction(Requester requester);

    /**
     * Completes the transaction between a requester and a responder agent.
     *
     * @param requester The agent initiating the transaction.
     * @param responder The agent responding to the transaction.
     */
    protected abstract void completeTransaction(Requester requester, Responder responder);

    /**
     * Shuts down the TransactionManager, performing any necessary cleanup.
     */
    public abstract void shutdown();
}
