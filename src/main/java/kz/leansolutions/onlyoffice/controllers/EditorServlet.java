package kz.leansolutions.onlyoffice.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.leansolutions.onlyoffice.entities.FileModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
//@RequestMapping(value = "EditorServlet")
public class EditorServlet {

    private static String filesLocationUrl = "http://localhost:8090/";
    private static String docFileLocation = "/var/lib/tomcat8/webapps/ROOT/";

    @GetMapping
    public String mainPage(@RequestParam(name = "fileName") String fileName,
                           Model model) {

        final String uuid = UUID.randomUUID().toString().replace("-", "");
        try {
            String copiedFileURL = getCopiedFileLocation(fileName);
            String fileUrl = filesLocationUrl + copiedFileURL;
            FileModel fileModel = new FileModel(fileName, "en", "5", "Nurbol", fileUrl);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(fileModel);
                System.out.println("ResultingJSONstring = " + json);
                System.out.println(json);
                model.addAttribute("fileModelString", json);
                model.addAttribute("docserviceApiUrl", "");
                model.addAttribute("fileModel", fileModel);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "index";
    }

    public String getCopiedFileLocation(String fileName)
            throws IOException {
        String rootPath="/var/lib/tomcat8/webapps/ROOT/";
        String originalFileURL = rootPath + fileName;
        Path copied = Paths.get(rootPath+"temp_copy_files/" + fileName);
        Path originalPath = Paths.get(originalFileURL);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        return "/temp_copy_files/" + fileName;
    }
}
