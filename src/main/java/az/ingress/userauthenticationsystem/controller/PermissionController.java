package az.ingress.userauthenticationsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("Salam Admin!");
    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> getUserData() {
        return ResponseEntity.ok("Salam User!");
    }


    @GetMapping("/read")
    @PreAuthorize("hasAuthority('READ') and hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> readData() {
        return ResponseEntity.ok("Məlumatı oxumaq icazəniz var!");
    }
}
