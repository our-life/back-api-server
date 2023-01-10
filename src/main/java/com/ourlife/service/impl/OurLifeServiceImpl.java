package com.ourlife.service.impl;

import com.ourlife.dto.ourlife.*;
import com.ourlife.entity.*;
import com.ourlife.exception.OurLifeNotFoundException;
import com.ourlife.exception.UserNotFoundException;
import com.ourlife.repository.*;
import com.ourlife.service.AwsService;
import com.ourlife.service.OurLifeService;
import com.ourlife.utils.Impl.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OurLifeServiceImpl implements OurLifeService {

    //TODO: 수정 삭제 -> 좋아요 -> 조회 순서로 만들기!
    private final JwtTokenUtils jwtTokenUtils;
    private final AwsService awsService;
    private final OurlifeRepository ourlifeRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ImgsRepository imgsRepository;
    private final OurlifeLikeRepository ourlifeLikeRepository;

    @Override
    public List<GetOurlifeResponse> getOurlife(String token) {
        User user = parseJwtToken(token);
        //팔로잉만
        List<Follow> userFollowList = followRepository.findAllByFromUser(user);
        List<User> followingUserList = new ArrayList<>();
        List<GetOurlifeResponse> responses = new ArrayList<>();

        for (Follow item: userFollowList) {
            followingUserList.add(item.getToUser());
        }
        for (User followingUser: followingUserList) {
            List<OurLife> ourLifeList = ourlifeRepository.findAllById(followingUser.getId());
            if(ourLifeList.isEmpty()) continue;
            else{

            }

        }

        //아라 구해서? 좋아요 굴리고? ?.????? 개오래 걸릴꺼 같은데...?
        return responses;
    }



    @Override
    public void save(CreateOurlifeRequest request, List<MultipartFile> multipartFiles, String token) {
        User user = parseJwtToken(token);
        List<String> ImgsName;
        Imgs imgs = null;

        if(multipartFiles.get(0).getSize() != 0){
            ImgsName = awsService.uploadFiles(multipartFiles);
            imgs= Imgs.createImgs1(ImgsName);
        }

        OurLife ourLife = OurLife.createOurlife(request, user, imgs);
        ourlifeRepository.save(ourLife);



    }

    //http://localhost:8080/h2-console/
    @Override
    public void update(UpdateOurlifeRequest request, List<MultipartFile> multipartFiles, String token) {
        User user = parseJwtToken(token);
        Imgs imgs = null;

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("개시글이 없습니다."));

        if(!user.getId().equals(ourLife.getUser().getId())){
            throw new UserNotFoundException("해당 유저의 글이 아닙니다.");
        }


        if(multipartFiles.get(0).getSize() != 0){
            Imgs repoImgs = imgsRepository.findByOurLifeId(ourLife.getId())
                    .orElseThrow(() -> new OurLifeNotFoundException("이미지가 존재하지 않습니다."));
            // 오류 수정필요
            String[] imgUrl = repoImgs.getImgUrl().split(",");
            for (String img: imgUrl) {
                awsService.deleteFile(img);
            }
            List<String> ImgsName;
            ImgsName = awsService.uploadFiles(multipartFiles);
            imgs = Imgs.updateImgs(repoImgs, ImgsName);
            imgsRepository.save(imgs);
        }

        OurLife updateOurlife = OurLife.updateOurlife(ourLife, request);
        ourlifeRepository.save(updateOurlife);

        //메서드로 빼보기

    }

    @Override
    public void delete(DeleteOurlifeRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("개시글이 없습니다."));

        if(!user.getId().equals(ourLife.getUser().getId())){
            throw new UserNotFoundException("해당 유저의 글이 아닙니다.");
        }

        Imgs imgs = imgsRepository.findByOurLifeId(ourLife.getId())
                .orElseGet(() -> null);
        if(imgs != null){
            String[] imgUrl = imgs.getImgUrl().split(",");
            for (String url: imgUrl) {
                awsService.deleteFile(url);
            }
        }


        ourlifeRepository.delete(ourLife);

    }

    @Override
    public void ourlifeLike(OurlifeLikeRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("개시글이 없습니다."));

        OurlifeLike araLike = OurlifeLike.createAraLike(user, ourLife);

        if(ourlifeLikeRepository.existsByOurLifeIdAndUserId(ourLife.getId(), user.getId())){
            throw new OurLifeNotFoundException("이미 좋아요를 누르셨습니다.");
        }

        ourlifeLikeRepository.save(araLike);
    }

    @Override
    public void ourlifeUnLike(OurlifeLikeRequest request, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = ourlifeRepository.findById(request.getAraId())
                .orElseThrow(() -> new OurLifeNotFoundException("개시글이 없습니다."));

        OurlifeLike ourlifeLike = ourlifeLikeRepository.findByOurLifeIdAndUserId(ourLife.getId(), user.getId())
                .orElseThrow(() -> new OurLifeNotFoundException("좋아요를 하지시 않았습니다."));

        ourlifeLikeRepository.delete(ourlifeLike);
    }

    //공통 메서드

    private int CountOurLifeLike(OurLife ourLife){
        return ourlifeLikeRepository.countByOurLifeId(ourLife.getId());
    }

    private User parseJwtToken(String token){
        Long userId = jwtTokenUtils.parseUserIdFrom(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저의 아이디가 없습니다."));

        return user;
    }
}


/*    @Override
    public void save(CreateOurlifeRequest request, List<MultipartFile> multipartFiles, String token) {
        User user = parseJwtToken(token);

        OurLife ourLife = OurLife.createOurlife(request, user);
        ourlifeRepository.save(ourLife);
        List<String> ImgsName;

        if(multipartFiles.get(0).getSize() != 0){
            ImgsName = awsService.uploadFiles(multipartFiles);
            imgsRepository.save(Imgs.createImgs(ourLife, ImgsName));
        }

    }*/
