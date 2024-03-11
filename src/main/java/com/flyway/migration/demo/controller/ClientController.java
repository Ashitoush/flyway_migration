package com.flyway.migration.demo.controller;

import com.flyway.migration.demo.entity.commondb.ClientMaster;
import com.flyway.migration.demo.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ClientMaster clientMaster) {
        return ResponseEntity.ok().body(clientService.createClient(clientMaster));
    }
}
