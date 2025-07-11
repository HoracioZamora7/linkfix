package com.linkfix.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linkfix.dto.UsuarioDTO;

public final class SesionUtils {

    private SesionUtils() {}

    public static boolean sesionIsValid(HttpSession httpSession)
    {
        return httpSession.getAttribute("logueado") != null; //si es null la sesion es invalida
    }

    public static String handleSesionInvalida(RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("error", "Sesión inválida");
        return "redirect:/index";
    }

    public static String handleErrorToIndex(RedirectAttributes redirectAttributes, String mensaje)
    {
        redirectAttributes.addFlashAttribute("error", mensaje);
        return "redirect:/index";
    }

    public static boolean isAdmin(HttpSession session) 
    {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        return usuarioDTO != null && usuarioDTO.getRoles().contains(1); //si contiene el rol 1 (admin)
    }
}
