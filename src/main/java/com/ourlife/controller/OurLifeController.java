package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.ourlife.CreateOurlifeRequest;
import com.ourlife.dto.ourlife.DeleteOurlifeRequest;
import com.ourlife.dto.ourlife.OurlifeLikeRequest;
import com.ourlife.dto.ourlife.UpdateOurlifeRequest;
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
    public ResponseEntity<?> createOurlife(@RequestPart(name = "request") CreateOurlifeRequest request,
                                           @RequestPart(name = "file") List<MultipartFile> multipartFiles,
                                           @ValidateToken String token) {
        ourLifeService.save(request, multipartFiles, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/ourlifes")
    public ResponseEntity<?> updateOurLife(@RequestPart(name = "request") UpdateOurlifeRequest request,
                                           @RequestPart(name = "file") List<MultipartFile> multipartFiles,
                                           @ValidateToken String token) {
        ourLifeService.update(request, multipartFiles, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ourlifes")
    public ResponseEntity<?> deleteOurLife(@RequestBody DeleteOurlifeRequest deleteOurlifeRequest, @ValidateToken String token) {
        ourLifeService.delete(deleteOurlifeRequest, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ourlifes/likes")
    public ResponseEntity<?> likeOurLife(@RequestBody OurlifeLikeRequest request, @ValidateToken String token) {
        ourLifeService.ourlifeLike(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ourlifes/likes")
    public ResponseEntity<?> unlikeOurLife(@RequestBody OurlifeLikeRequest request, @ValidateToken String token) {
        ourLifeService.ourlifeUnLike(request, token);
        return ResponseEntity.ok().build();
    }


}
