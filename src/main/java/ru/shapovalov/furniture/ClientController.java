package ru.shapovalov.furniture;

public class ClientController {
    Client client;
    ClientScene clientScene;

    public ClientController(Client client, ClientScene clientScene){
        this.client = client;
        this.clientScene = clientScene;
    }
}
