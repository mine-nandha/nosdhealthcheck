package com.mine.nosd.healthcheck.controller;

import com.mine.nosd.healthcheck.model.Configuration;
import com.mine.nosd.healthcheck.model.Roles;
import com.mine.nosd.healthcheck.model.Table;
import com.mine.nosd.healthcheck.utils.ConfigHandler;
import com.mine.nosd.healthcheck.utils.EmailSender;
import com.mine.nosd.healthcheck.utils.ExcelFileHandler;
import com.mine.nosd.healthcheck.utils.RolesHandler;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    RolesHandler rolesHandler;
    Authentication authentication;
    ConfigHandler configHandler;
    ExcelFileHandler excelFileHandler;
    @Value("${MAIL_ID}")
    String mailId;
    @Value("${MAIL_PASSWORD}")
    String mailPass;

    public HomeController(ConfigHandler configHandler, ExcelFileHandler excelFileHandler, RolesHandler rolesHandler) {
        this.excelFileHandler = excelFileHandler;
        this.configHandler = configHandler;
        this.rolesHandler = rolesHandler;
    }

    @GetMapping("/upload-excel")
    public String getExcel(Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!rolesHandler.findRolesByName(authentication.getName()).getRole().contains("ADMIN")) {
            return "403";
        }
        model.addAttribute("name", authentication.getName().split(" ")[0]);
        return "getexcel";
    }

    @PostMapping("/upload-excel")
    public String postExcel(@RequestPart MultipartFile file) {
        excelFileHandler.saveExcelFile(file);
        return "redirect:/";
    }

    @GetMapping("/configuration")
    public String getConfiguration(Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!rolesHandler.findRolesByName(authentication.getName()).getRole().contains("ADMIN")) {
            return "403";
        }
        model.addAttribute("name", authentication.getName().split(" ")[0]);
        model.addAttribute("config", configHandler.getConfig());
        return "configuration";
    }

    @PostMapping("/configuration")
    public String postConfiguration(@Valid Configuration configuration, BindingResult result) {
        configuration.setId("config");
        if (configuration.getPassword() == null) {
            configuration.setUsername(mailId);
            configuration.setPassword(mailPass);
        }
        configHandler.updateConfig(configuration);
        return "redirect:/configuration";
    }

    @GetMapping("/preview")
    public String getPreview(HttpSession session, Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!rolesHandler.findRolesByName(authentication.getName()).getRole().contains("ADMIN")) {
            return "403";
        }
        Table previewTable = (Table) session.getAttribute("previewTable");
        if (previewTable == null) {
            return "redirect:/";
        }
        model.addAttribute("name", authentication.getName().split(" ")[0]);
        model.addAttribute("thead", previewTable.thead);
        model.addAttribute("tbody", previewTable.tbody);
        return "preview";
    }

    @PostMapping("/preview")
    public String postPreview(String htmlContent, String signature, String issues, Model model, RedirectAttributes redirectAttributes) {
        String decodedHtml = URLDecoder.decode(htmlContent, StandardCharsets.UTF_8);
        String decodedSign = URLDecoder.decode(signature, StandardCharsets.UTF_8);
        Configuration configuration = configHandler.getConfig();
        EmailSender.send(configuration.username, configuration.password, configuration.to, configuration.cc, issues, configuration.firstName, decodedHtml, decodedSign);
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/";
    }

    @GetMapping("/")
    public String checklist(String success, Model model, HttpSession session) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (rolesHandler.findRolesByName(authentication.getName()) == null) {
            rolesHandler.updateRole(new Roles(authentication.getName(), "USER"));
        }
        try {
            excelFileHandler.retrieveExcelFromDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (excelFileHandler.isExcelExists()) {
            Table table = excelFileHandler.readExcelFile();
            model.addAttribute("thead", table.thead);
            model.addAttribute("tbody", table.tbody);
            model.addAttribute("name", authentication.getName().split(" ")[0]);
            session.setMaxInactiveInterval(1800);
            session.setAttribute("rowCount", table.getRowCount());
            session.setAttribute("colCount", table.getColCount());
            if (success != null) {
                model.addAttribute("success", "Successfully Submitted!");
            }
            return "checklist";
        } else {
            return "redirect:/upload-excel";
        }
    }

    @PostMapping("/")
    public String checklist(@RequestParam Map<String, String> parameters, HttpSession session) {
        List<String> thead = new ArrayList<>();
        List<List<String>> tbody = new ArrayList<>();
        int row = (int) session.getAttribute("rowCount");
        int col = (int) session.getAttribute("colCount");
        for (int i = 0; i < row; i++) {
            List<String> rows = new ArrayList<>();
            for (int j = 0; j < col; j++) {
                if (i != 0) {
                    rows.add(parameters.get("r" + i + "c" + j));
                } else {
                    thead.add(parameters.get("r" + i + "c" + j));
                }
            }
            if (i != 0) {
                tbody.add(rows);
            }
        }
        Table previewTable = new Table();
        previewTable.setTbody(tbody);
        previewTable.setThead(thead);
        excelFileHandler.updateExcelFile(previewTable);
        session.setAttribute("previewTable", previewTable);

        return "redirect:/preview";
    }
}
