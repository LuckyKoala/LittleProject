package telnetchat.command;

import telnetchat.client.Client;

public interface Command {
    boolean canHandle(String label);
    boolean processInput(Client client, String label, String[] args);
}
