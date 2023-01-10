package com.ourlife.service;

import com.ourlife.dto.ourlife.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OurLifeService {

    public void save(CreateOurlifeRequest request, List<MultipartFile> multipartFiles, String token);

    public void update(UpdateOurlifeRequest request, List<MultipartFile> multipartFiles, String token);

    public void delete(DeleteOurlifeRequest request, String token);

    public List<GetOurlifeResponse> getOurlife(String token);

    public void ourlifeLike(OurlifeLikeRequest request, String token);

    public void ourlifeUnLike(OurlifeLikeRequest request, String token);


}
