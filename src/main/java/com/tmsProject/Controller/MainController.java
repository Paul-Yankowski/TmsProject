package com.tmsProject.Controller;

import com.tmsProject.Entity.Message;
import com.tmsProject.Repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;


    @GetMapping("/hello")
    public String first(Map<String,Object> model){
        return "hello";
    }

    @GetMapping
    public String showMessage(Map<String,Object> model)
    {
        Iterable<Message> messages=messageRepo.findAll();
        model.put("messages",messages);
        return "main";
    }
    @PostMapping
    public String addMessage(@RequestParam String text, @RequestParam String city, Map<String,Object> model)
    {
        Message message=new Message(text,city);
        messageRepo.save(message);
        Iterable<Message> messages=messageRepo.findAll();
        model.put("messages",messages);
        return "main";
    }

}
