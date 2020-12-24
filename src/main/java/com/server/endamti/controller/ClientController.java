package com.server.endamti.controller;

import com.server.endamti.model.Client;
import com.server.endamti.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepo;

    @PostMapping("/add")
    private Client add(@RequestBody Client client) {
        return clientRepo.save(client);
    }

    @PostMapping("/search")
    private ArrayList<Client> search(@RequestBody ClientSearchParams params) {
        ArrayList<Client> clients = new ArrayList<>();
        clientRepo.findAll().forEach(client -> {
            boolean isMatch = true;

            if (params.getHideInactive() != null && params.getHideInactive() && !client.getActive()) {
                isMatch = false;
            }

            if (params.getOrganization() != null && (client.getOrganization() == null || !client.getOrganization().toLowerCase().contains(params.getOrganization().toLowerCase()))) {
                isMatch = false;
            }

            if (params.getFirstname() != null && (client.getFirstname() == null || !client.getFirstname().toLowerCase().contains(params.getFirstname().toLowerCase()))) {
                isMatch = false;
            }

            if (params.getLastname() != null && (client.getLastname() == null || !client.getLastname().toLowerCase().contains(params.getLastname().toLowerCase()))) {
                isMatch = false;
            }

            if (params.getStatus() != null && !client.getStatus().equals(params.getStatus())) {
                isMatch = false;
            }

            if (isMatch) {
                clients.add(client);
            }
        });
        return clients;
    }

    @PutMapping("/update")
    private Client update(@RequestBody Client client) {
        return clientRepo.save(client);
    }

    @DeleteMapping("/delete")
    private void delete(@RequestParam Integer id) {
        clientRepo.deleteById(id);
    }

    @PutMapping("/activate")
    private boolean activate(@RequestParam int id) {
        if (clientRepo.existsById(id)) {
            Client client = clientRepo.findById(id).get();
            client.setActive(!client.getActive());
            clientRepo.save(client);
            return true;
        }
        return false;
    }

    @GetMapping("/emails")
    private ArrayList<Client> emails() {
        ArrayList<Client> clients = new ArrayList<>();
        clientRepo.findAll().forEach(client -> {
            if (client.getEmail() != null) {
                clients.add(client);
            }
        });
        return clients;
    }

    @GetMapping("/sms")
    private ArrayList<Client> sms() {
        ArrayList<Client> clients = new ArrayList<>();
        clientRepo.findAll().forEach(client -> {
            if (
                (client.getPhone() != null || client.getOtherPhone() != null)
                && client.getCanSms()
            ) {
                clients.add(client);
            }
        });
        return clients;
    }

    @GetMapping("/get")
    private Client get(@RequestParam int id) {
        if (clientRepo.existsById(id)) {
            return clientRepo.findById(id).get();
        }
        return null;
    }
}

class ClientSearchParams {
    private Boolean hideInactive;
    private String firstname;
    private String lastname;
    private String organization;
    private String status;
    private String from;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getHideInactive() {
        return hideInactive;
    }

    public void setHideInactive(Boolean hideInactive) {
        this.hideInactive = hideInactive;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
