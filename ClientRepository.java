package Models;

import java.util.ArrayList;

public class ClientRepository {

    private ArrayList<Client> clients = new ArrayList<>();

    public void addClient(Client client) {
        clients.add(client);
    }

    public ArrayList<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    public void updateClient(Client updatedClient) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == updatedClient.getId()) {
                clients.set(i, updatedClient);
                return;
            }
        }
    }

    public void removeClient(int id) {
        clients.removeIf(client -> client.getId() == id);
    }
}
