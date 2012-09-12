package org.jcrom;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

public class SessionFactoryImpl implements SessionFactory {

    private Repository repository;

    private String workspaceName;

    private Credentials credentials;

    private EventListenerDefinition eventListeners[] = new EventListenerDefinition[] {};

    /**
     * Default Constructor
     * Use this constructor if you can set repository, credentials by injection
     **/
    public SessionFactoryImpl() {
    }

    public SessionFactoryImpl(Repository repository) {
        this(repository, null, null);
    }

    public SessionFactoryImpl(Repository repository, Credentials credentials) {
        this(repository, credentials, null);
    }

    public SessionFactoryImpl(Repository repository, Credentials credentials, String workspaceName) {
        this.repository = repository;
        this.credentials = credentials;
        this.workspaceName = workspaceName;
    }

    @Override
    public Session getSession() throws RepositoryException {
        Session session = repository.login(credentials, workspaceName);
        return addListeners(session);
    }

    /**
     * Hook for adding listeners to the newly returned session. We have to treat exceptions manually and can't
     * reply on the template.
     * @param session JCR session
     * @return the listened session
     * @throws javax.jcr.RepositoryException
     */
    private Session addListeners(Session session) throws RepositoryException {
        if (getRepository() == null) {
            throw new IllegalArgumentException("repository is required");
        }

        if (eventListeners != null && eventListeners.length > 0) {
            if (!supportsObservation(getRepository())) {
                throw new IllegalArgumentException("repository " + getRepositoryInfo() + " does NOT support Observation; remove Listener definitions");
            }
            Workspace ws = session.getWorkspace();
            ObservationManager manager = ws.getObservationManager();

            for (EventListenerDefinition eventListener : eventListeners) {
                manager.addEventListener(eventListener.getListener(), eventListener.getEventTypes(), eventListener.getAbsPath(), eventListener.isDeep(), eventListener.getUuid(), eventListener.getNodeTypeName(), eventListener.isNoLocal());
            }
        }
        return session;
    }

    /**
     * @return Returns the repository.
     */
    public Repository getRepository() {
        return repository;
    }

    /**
     * @param repository The repository to set.
     */
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * @return Returns the workspaceName.
     */
    public String getWorkspaceName() {
        return workspaceName;
    }

    /**
     * @param workspaceName The workspaceName to set.
     */
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    /**
     * @return Returns the credentials.
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * @param credentials The credentials to set.
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * @return Returns the eventListenerDefinitions.
     */
    public EventListenerDefinition[] getEventListeners() {
        return eventListeners;
    }

    /**
     * @param eventListenerDefinitions The eventListenerDefinitions to set.
     */
    public void setEventListeners(EventListenerDefinition[] eventListenerDefinitions) {
        this.eventListeners = eventListenerDefinitions;
    }

    /**
     * A toString representation of the Repository.
     * @return
     */
    private String getRepositoryInfo() {
        if (getRepository() == null) {
            return "<N/A>";
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(getRepository().getDescriptor(Repository.REP_NAME_DESC));
        buffer.append(" ");
        buffer.append(getRepository().getDescriptor(Repository.REP_VERSION_DESC));
        return buffer.toString();
    }

    private static boolean supportsObservation(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_OBSERVATION_SUPPORTED));
    }
}
