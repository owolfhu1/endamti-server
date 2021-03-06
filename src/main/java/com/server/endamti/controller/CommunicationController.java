package com.server.endamti.controller;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import com.server.endamti.model.Communication;
import com.server.endamti.model.Template;
import com.server.endamti.repository.CommunicationRepository;
import com.server.endamti.repository.TemplateRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/communication")
public class CommunicationController {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${spring.sendgrid.sender-email}")
    private String emailHost;

    @Value("${spring.twilio.sid}")
    private String twilioSID;

    @Value("${spring.twilio.token}")
    private String twilioToken;

    @Value("${spring.twilio.number}")
    private String twilioNumber;

    @Autowired
    private CommunicationRepository comRepo;

    @Autowired
    private TemplateRepository tempRepo;

    @PostMapping("/email")
    private String sendEmail(@RequestBody EmailDTO email) throws IOException {
        Email from = new Email(emailHost);
        Email to = new Email(email.getTo());
        Content content = new Content("text/html", email.getBody());
        Mail mail = new Mail(from, email.getSubject(), to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        boolean success = (response.getStatusCode() + "").charAt(0) == '2';

        if (success) {
            Communication com = new Communication();
            com.setType("EMAIL");
            com.setBody(email.getBody());
            com.setClientId(email.getClientId());
            com.setDate(email.getDate());
            com.setUsername(email.getUsername());
            com.setSubject(email.getSubject());
            com.setDestination(email.getTo());
            comRepo.save(com);
            return "Email sent.";
        }
        return "Something went wrong, message not sent.";
    }

    @PostMapping("/sms")
    private String sendSMS(@RequestBody SMSDTO sms) {
        String number = sms.getTo().replaceAll("[^\\d]", "");

        if(number.length() != 10) {
            return "Invalid phone Number, message not sent.";
        }

        Twilio.init(twilioSID, twilioToken);
        Message.creator(
                new PhoneNumber("+1" + number),
                new PhoneNumber(twilioNumber),
                sms.getBody()
        ).create();

        Communication com = new Communication();
        com.setType("SMS");
        com.setBody(sms.getBody());
        com.setClientId(sms.getClientId());
        com.setDate(sms.getDate());
        com.setUsername(sms.getUsername());
        com.setDestination(sms.getTo());
        comRepo.save(com);
        return "Message sent.";
    }


    @PostMapping("/search")
    private ArrayList<Communication> search(@RequestBody CommunicationSearchParams params) {
        ArrayList<Communication> communications = new ArrayList<>();
        comRepo.findAll().forEach(communication -> {
            boolean isMatch = true;

            if (!params.getType().equals("ANY") && !params.getType().equals("ANY") && !params.getType().equals(communication.getType())) {
                isMatch = false;
            }

            if (params.getSentBy() != null && !communication.getUsername().toLowerCase().contains(params.getSentBy().toLowerCase())) {
                isMatch = false;
            }

            if (params.getDestination() != null && !communication.getDestination().toLowerCase().contains(params.getDestination().toLowerCase())) {
                isMatch = false;
            }

            if (isMatch) {
                communications.add(communication);
            }
        });
        return communications;
    }

    @PostMapping("/template")
    private boolean addTemplate(@RequestBody Template template) {
        tempRepo.save(template);
        return true;
    }

    @PutMapping("/template")
    private boolean updateTemplate(@RequestBody Template template) {
        tempRepo.save(template);
        return true;
    }

    @GetMapping("/template")
    private Iterable<Template> getTemplates() {
        return tempRepo.findAll();
    }

    @DeleteMapping("/template")
    private boolean deleteTemplate(@RequestParam int id) {
        if (tempRepo.existsById(id)) {
            this.tempRepo.delete(tempRepo.findById(id).get());
            return true;
        } return false;
    }
}

class CommunicationSearchParams {
    private String sentBy;
    private String destination;
    private String type;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class EmailDTO {
    private String username;
    private int clientId;
    private String subject;
    private String body;
    private String date;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

class SMSDTO {
    private String username;
    private int clientId;
    private String body;
    private String date;
    private String to;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
