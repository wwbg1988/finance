package com.blackbeard.common.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.blackbeard.common.dto.ImageInfoDto;

public interface ICreateImageService {

	public Map<String, Object> createImage(ImageInfoDto image,
			@RequestParam("image") MultipartFile myfile,
			HttpServletRequest request, HttpServletResponse response);

	public void showPicture(HttpServletResponse response, String imageUrl)
			throws Exception;
}
