package com.ourlife.controller;

import com.ourlife.argumentResolver.ValidateToken;
import com.ourlife.dto.ourlife.*;
import com.ourlife.service.OurLifeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "아라", description = "아라 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
public class OurLifeController {

    private final OurLifeService ourLifeService;

    @Operation(summary = "글(리스트) 조회 ", description = "팔로잉 한 유저들의 글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @GetMapping("/ourlifes/lists")
    public ResponseEntity<List<GetOurlifeResponse>> getOurlifes(@ValidateToken String token) {
        return ResponseEntity.ok().body(ourLifeService.getOurlifes(token));
    }

    @Operation(summary = "글(하나) 조회 ", description = "글 상세 내용을 조회합니다 (필요할지 몰라서 만들어 놓음).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @GetMapping("/ourlifes")
    public ResponseEntity<?> getOurlife(@RequestBody GetOurlifeRequest request,
                                        @ValidateToken String token){
        return ResponseEntity.ok().body(ourLifeService.getOurlife(request, token));
    }

    @GetMapping("/ourlifes/mylifes")
    public ResponseEntity<?> getMyOurlifes(@ValidateToken String token){
        return ResponseEntity.ok().body(ourLifeService.getMyOurlifes(token));
    }

    @Operation(summary = "글쓰기 ", description = "글쓰기 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
    })
    @PostMapping("/ourlifes")
    public ResponseEntity<OurlifeResponse> createOurlife(@RequestPart(name = "request") CreateOurlifeRequest request,
                                                         @RequestPart(name = "file") List<MultipartFile> multipartFiles,
                                                         @ValidateToken String token) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ourLifeService.save(request, multipartFiles, token));
    }

    @Operation(summary = "글 수정 ", description = "글수정 (Form-data : request(json) / file 멀티파트파일 )")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "해당 유저의 글이 아닙니다")
    })
    @PatchMapping("/ourlifes")
    public ResponseEntity<OurlifeResponse> updateOurLife(@RequestPart(name = "request") UpdateOurlifeRequest request,
                                                         @RequestPart(name = "file") List<MultipartFile> multipartFiles,
                                                         @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.update(request, multipartFiles, token));
    }

    @Operation(summary = "글 삭제 ", description = "글 삭제 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "글이 없습니다")
    })
    @DeleteMapping("/ourlifes")
    public ResponseEntity<OurlifeResponse> deleteOurLife(@RequestBody DeleteOurlifeRequest deleteOurlifeRequest,
                                                         @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.delete(deleteOurlifeRequest, token));
    }

    @Operation(summary = "좋아요 ", description = "글 좋아요 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "글이 없습니다"),
            @ApiResponse(responseCode = "404", description = "이미 좋아요를 누르셨습니다."),
    })
    @PostMapping("/ourlifes/likes")
    public ResponseEntity<?> likeOurLife(@RequestBody OurlifeLikeRequest request,
                                         @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.ourlifeLike(request, token));
    }

    @Operation(summary = "좋아요 취소", description = "글 좋아요 취소입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "글이 없습니다"),
            @ApiResponse(responseCode = "404", description = "좋아요를 하지 않으셨습니다."),
    })
    @DeleteMapping("/ourlifes/likes")
    public ResponseEntity<?> unlikeOurLife(@RequestBody OurlifeLikeRequest request,
                                           @ValidateToken String token) {

        return ResponseEntity.ok().body(ourLifeService.ourlifeUnLike(request, token));
    }

    @Operation(summary = "좋아요를 누른 사람들의 닉네임 리스트 조회", description = "ㅁㄴㅇㄹ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "글이 없습니다")
    })
    @GetMapping("/ourlifes/likes/lists")
    public ResponseEntity<?> ListToOurlifeLike(@RequestBody GetOurlifeRequest request,
                                               @ValidateToken String token){
        return ResponseEntity.ok().body(ourLifeService.getOurlifeLike(request, token));
    }


}
