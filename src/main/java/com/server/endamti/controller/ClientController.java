package com.server.endamti.controller;

import com.google.common.collect.Iterables;
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
    private ArrayList<Client> search(@RequestBody Params params) {
        ArrayList<Client> clients = new ArrayList<>();
        clientRepo.findAll().forEach(client -> {
            boolean isMatch = true;

            if (params.isHideInactive() != null && params.isHideInactive() && !client.getActive()) {
                isMatch = false;
            }

            if (params.getCompany() != null && !client.getCompany().toLowerCase().contains(params.getCompany().toLowerCase())) {
                isMatch = false;
            }

            if (params.getFirstname() != null && !client.getFirstname().toLowerCase().contains(params.getFirstname().toLowerCase())) {
                isMatch = false;
            }

            if (params.getLastname() != null && !client.getLastname().toLowerCase().contains(params.getLastname().toLowerCase())) {
                isMatch = false;
            }

            if (isMatch) {
                clients.add(client);
            }
        });
        return clients;
    }

}

class Params {
    private Boolean hideInactive;
    private String firstname;
    private String lastname;
    private String company;

    public Boolean isHideInactive() {
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
