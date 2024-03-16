package com.mine.nosd.healthcheck.controller;

import com.mine.nosd.healthcheck.model.Roles;
import com.mine.nosd.healthcheck.utils.RolesHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Roles")
public class RolesController {

    RolesHandler rolesHandler;
    Authentication authentication;

    public RolesController(RolesHandler rolesHandler) {
        this.rolesHandler = rolesHandler;
    }

    @GetMapping("/")
    public String roles(Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!rolesHandler.findRolesByName(authentication.getName()).getRole().contains("OWNER")) {
            return "403";
        }
        model.addAttribute("rolesList", rolesHandler.listAllRoles());
        model.addAttribute("name", authentication.getName().split(" ")[0]);
        return "listroles";
    }

    @GetMapping("/updateroles")
    public String updateRoles(String name, Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!rolesHandler.findRolesByName(authentication.getName()).getRole().contains("OWNER")) {
            return "403";
        }
        model.addAttribute("roles", rolesHandler.findRolesByName(name));
        return "updateroles";
    }

    @PostMapping("/updateroles")
    public String updateRoles(@ModelAttribute("roles") Roles roles) {
        rolesHandler.updateRole(roles);
        return "redirect:/";
    }

    @GetMapping("/deleteroles")
    public String deleteRoles(String name) {
        rolesHandler.deleteRolesByName(name);
        return "redirect:/";
    }
}
