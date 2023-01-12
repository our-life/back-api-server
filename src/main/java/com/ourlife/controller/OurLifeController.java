package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.ourlife.*;
import com.ourlife.service.OurLifeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OurLifeController {

    private final OurLifeService ourLifeService;


    @GetMapping("/ourlifes")
    public ResponseEntity<?> getOurLife(@ValidateToken String token) {
        return null;
    }


    @PostMapping("/ourlifes")
    public ResponseEntity<OurlifeResponse> createOurlife(@RequestPart(name = "request") CreateOurlifeRequest request,
                                                         @RequestPart(name = "file") List<MultipartFile> multipartFiles,
                                                         @ValidateToken String token) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ourLifeService.save(request, multipartFiles, token));
    }

    @PatchMapping("/ourlifes")
    public ResponseEntity<OurlifeResponse> updateOurLife(@RequestPart(name = "request") UpdateOurlifeRequest request,
                                                         @RequestPart(name = "file") List<MultipartFile> multipartFiles,
                                                         @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.update(request, multipartFiles, token));
    }

    @DeleteMapping("/ourlifes")
    public ResponseEntity<OurlifeResponse> deleteOurLife(@RequestBody DeleteOurlifeRequest deleteOurlifeRequest,
                                                         @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.delete(deleteOurlifeRequest, token));
    }

    @PostMapping("/ourlifes/likes")
    public ResponseEntity<?> likeOurLife(@RequestBody OurlifeLikeRequest request,
                                         @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.ourlifeLike(request, token));
    }

    @DeleteMapping("/ourlifes/likes")
    public ResponseEntity<?> unlikeOurLife(@RequestBody OurlifeLikeRequest request,
                                           @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.ourlifeUnLike(request, token));
    }


}
