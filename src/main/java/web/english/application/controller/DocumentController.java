package web.english.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.DocumentDAO;
import web.english.application.dao.UserDAO;
import web.english.application.entity.Document;
import web.english.application.entity.user.Users;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.Utils;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class DocumentController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DocumentDAO documentDAO;

    private JwtHelper jwtHelper=new JwtHelper();

    private Utils utils=new Utils();

    @GetMapping("/document")
    public String getDocumentPage(HttpServletRequest httpServletRequest, Model model){
        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Users user = null;
        if(httpServletRequest.getCookies() == null){
            return "redirect:/login";
        }
        if(token != ""){
            user = userDAO.getUserFromToken(token);
        }

        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("users",user);

        List<Document> documents=documentDAO.findAllDocument(token);

        model.addAttribute("documents",documents);

        return "admin/document/document";
    }

    @GetMapping("/adddocument")
    public String getUploadFilePage(HttpServletRequest httpServletRequest,Model model){

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Users users = userDAO.getUserFromToken(token);
        model.addAttribute("users",users);

        return "admin/document/adddocument";
    }

    @PostMapping("/document/add")
    public String uploadFile(HttpServletRequest httpServletRequest,Model model, @RequestPart(value = "file") MultipartFile file, @RequestParam String name, @RequestParam String description){
//        log.info(name);
//        log.info(description);
//        log.info(file.getOriginalFilename());

        if(!utils.checkMaxLength(name)){
            model.addAttribute("errorName", Utils.maxLengthRequire);
            return  "admin/document/adddocument";
        }
        if(!utils.checkMaxLength(description)){
            model.addAttribute("errorDescription", Utils.maxLengthRequire);
            return  "admin/document/adddocument";
        }

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);

        Document document=documentDAO.uploadFile(token,file,name,description);

        return "redirect:/admin/document";
    }

    @GetMapping("/document/delete/{id}")
    public String deleteFile(@PathVariable int id, RedirectAttributes redirectAttributes){
        String message=documentDAO.deleteFile(id);
        if(message.equalsIgnoreCase("Delete success!"))
            redirectAttributes.addFlashAttribute("msg","Xóa tập tin thành công!");
        else
            redirectAttributes.addFlashAttribute("msg","Xóa không thành công!");

        return "redirect:/admin/document";
    }

    @PostMapping("/document/search")
    public String searchDocument(HttpServletRequest httpServletRequest, Model model,@RequestParam String id,@RequestParam String name){
        log.info(id);
        log.info(name);

        String jwt=jwtHelper.getJwtFromCookie(httpServletRequest);
        String token=jwtHelper.createToken(jwt);
        Users users = userDAO.getUserFromToken(token);
        model.addAttribute("users",users);

        List<Document> documents=documentDAO.searchDocument(token,id,name);
        model.addAttribute("documents",documents);

        return "admin/document/document";
    }
}