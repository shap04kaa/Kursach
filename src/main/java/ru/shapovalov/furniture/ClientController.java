package ru.shapovalov.furniture;

public class ClientController {
    Client client;
    ClientScene clientScene;
    FurnitureScene furnitureScene;

    public ClientController(Client client, ClientScene clientScene){
        this.client = client;
        this.clientScene = clientScene;
    }
}
