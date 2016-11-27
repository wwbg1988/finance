package com.blackbeard.common.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blackbeard.common.constant.StatusConstants;
import com.blackbeard.common.dto.ImageInfoDto;
import com.blackbeard.common.pojo.ImageInfo;
import com.blackbeard.common.service.ICreateImageService;
import com.ssic.util.BeanUtils;
import com.ssic.util.FileUtils;
import com.ssic.util.PropertiesUtils;
import com.ssic.util.model.Response;

@Service
public class CreateImageServiceImpl implements ICreateImageService {

	protected static final Log logger = LogFactory
			.getLog(CreateImageServiceImpl.class);

	// 上传图片

	public Map<String, Object> createImage(ImageInfoDto image,
			MultipartFile myfile, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("upload pic : " + image);

		Map<String, Object> map = new HashMap<String, Object>();

		if (!myfile.isEmpty()) {
			// 上传的必须是图片
			String[] fileType = myfile.getOriginalFilename().split("[.]");
			String fType = fileType[1];

			if (!"jpg".equals(fType) && !"gif".equals(fType)
					&& !"jpeg".equals(fType) && !"png".equals(fType)
					&& !"JPG".equals(fType) && !"JEPG".equals(fType)
					&& !"PNG".equals(fType) && !"GIF".equals(fType)) {
				map.put("message", "图片格式必须是：jpg,gif,jpeg,png,JPG,GIF,JPEG,PNG");
				map.put("success", false);
				return map;
			}

			printFileInfo(myfile);
			try {
				uploadImage(image, myfile, request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			map.put("message", "图片不能为空");
			map.put("success", false);
			return map;
		}

		create(image);

		System.out.println(image.getUrl());

		map.put("message", "图片保存成功");
		map.put("success", true);
		map.put("image_url", image.getUrl());

		return map;
	}

	private void printFileInfo(MultipartFile myfile) {
		logger.info("文件长度: " + myfile.getSize());
		logger.info("文件类型: " + myfile.getContentType());
		logger.info("文件名称: " + myfile.getName());
		logger.info("文件原名: " + myfile.getOriginalFilename());
		logger.info("========================================");
	}

	private void uploadImage(ImageInfoDto image, MultipartFile myfile,
			HttpServletRequest request) throws IOException {
		// 如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解
		// 如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解
		// String context = "upload";
		// String ngix_ip = "103.36.132.7";
		// String realPath = "/upload";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String context = format.format(new Date());
		String realPath = PropertiesUtils.getProperty("web.upload.userInfo.url");
		String fileName = FileUtils.getUploadFileName(myfile
				.getOriginalFilename());
		FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(
				realPath + "/" + context, fileName));
		logger.info("上传目录：  " + realPath + "/" + context);
		image.setUrl("/upload/images/" + context + "/" + fileName);
		logger.info("上传目录2：  " + "/upload/images/" + context + "/" + fileName);
	}

	public void create(ImageInfoDto imageInfo) {
		ImageInfo image = BeanUtils.createBeanByTarget(imageInfo,
				ImageInfo.class);
		// imageInfoDao.insert(image);
	}

	// 读取本地的图片
	public void showPicture(HttpServletResponse response, String imageUrl)
			throws Exception {

		// String picId = getRequest().getParameter("picId");
		// String pic_path = pointCardApplyManager.findPicturePath(picId);
		// System.out.println(pic_path);
		// String pic_path = "D:/upLoad/upload/"+pid+".jpg";
		String realPath = PropertiesUtils.getProperty("web.upload.userInfo.url");
		imageUrl = realPath + "/" + imageUrl;
		System.out.println("imageUrl---" + imageUrl);
		Response<String> result = new Response<String>();
		logger.info("pic_path=" + imageUrl);
		FileInputStream is = new FileInputStream(imageUrl);
		int i = is.available(); // 得到文件大小
		byte data[] = new byte[i];
		is.read(data); // 读数据
		is.close();
		response.setContentType("image/*"); // 设置返回的文件类型
		OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
		toClient.write(data); // 输出数据
		toClient.close();
		result.setStatus(StatusConstants.RETURN_SUCCESS);
		result.setMessage("读取图片成功");
		// return result;
	}
}