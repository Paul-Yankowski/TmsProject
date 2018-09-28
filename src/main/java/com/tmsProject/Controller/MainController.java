package com.tmsProject.Controller;

import com.tmsProject.Entity.Message;
import com.tmsProject.Entity.User;
import com.tmsProject.Repository.MessageRepo;
import com.tmsProject.Repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserDetailsRepo userRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String hello( Map<String,Object> model)
    {
        return "hello";
    }
    @GetMapping("/enter")
    public String login()
    {
        return "enter";
    }

    @PostMapping("/enter")
    public String login(@RequestParam String login,@RequestParam String password)
    {
        User users=userRepo.findUserByEmail(login);
        if (users.getEmail()!=null&& users.getPassword().equals(password))
        {
            return "/main";
        }

        return "redirect:/";
    }
    @GetMapping("/main")
    public String showMessage(@AuthenticationPrincipal User user, Map<String,Object> model)
    {

        Iterable<Message> messages=messageRepo.findAll();
        model.put("messages",messages);
        model.put("users",user);
        return "main";
    }
    @PostMapping("/main")
    public String addMessage(@AuthenticationPrincipal User user,
                             @RequestParam("file") MultipartFile multipartFile,
                             @RequestParam String text,
                             @RequestParam String city, Map<String,Object> model) throws IOException

    {
        Message message=new Message(text,city,user.getName());
        if (multipartFile!=null){
            File upload=new File(uploadPath);
            if (!upload.exists()){
                upload.mkdir();
            }

           String uuidFilename=UUID.randomUUID().toString();
           String newFilename=uuidFilename+"."+multipartFile.getOriginalFilename();
           multipartFile.transferTo(new File(uploadPath+"/"+newFilename));
            message.setFile_name(newFilename);
        }
        messageRepo.save(message);
        Iterable<Message> messages=messageRepo.findAll();
        model.put("messages",messages);
        return "main";
    }

    @PostMapping("search")
    public String find(@RequestParam String search,Map<String,Object>model){
        Iterable<Message> messages;
        if(search!=null&&!search.isEmpty()) {
            messages = messageRepo.findByCity(search);
        }else {
            messages=messageRepo.findAll();
        }
        model.put("messages",messages);
        return "main";
    }

    
}
