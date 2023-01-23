package com.ourlife.service;

import com.ourlife.dto.ourlife.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OurLifeService {

    public OurlifeResponse save(CreateOurlifeRequest request, List<MultipartFile> multipartFiles, String token);

    public OurlifeResponse update(UpdateOurlifeRequest request, List<MultipartFile> multipartFiles, String token);

    public OurlifeResponse delete(DeleteOurlifeRequest request, String token);

    public List<GetOurlifeResponse> getOurlifes(String token);

    public GetOurlifeResponse getOurlife(GetOurlifeRequest request, String token);

    public OurlifeResponse ourlifeLike(OurlifeLikeRequest request, String token);

    public OurlifeResponse ourlifeUnLike(OurlifeLikeRequest request, String token);

    public GetOurlifeLikeResponse getOurlifeLike(GetOurlifeRequest request, String token);


}
