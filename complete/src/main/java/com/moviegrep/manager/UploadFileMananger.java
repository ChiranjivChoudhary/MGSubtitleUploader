package com.moviegrep.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.client.ApiClient;
import io.swagger.client.api.MediaApi;

@Component
public class UploadFileMananger {

	public String uploadFile(MultipartFile fileUpload) {

		String response=null;
		ApiClient apiClient = new ApiClient();
		String basePath = "http://localhost:49994";
		apiClient.setBasePath(basePath);
		try {
			String fileName = StringUtils.stripFilenameExtension(fileUpload.getOriginalFilename());

			File convFile = new File(fileUpload.getOriginalFilename());
			convFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(fileUpload.getBytes());
			fos.close();
			Map<String, String> media = getMediaMetaDataFromFileName(fileName);

			MediaApi mediaApi = new MediaApi();
			mediaApi.setApiClient(apiClient);
			mediaApi.postMedia("1", media, convFile);

		} catch (Exception e) {
			response=e.getMessage();
			
		}
		return response;
	}

	// Media class properties 
	// Name
	// Description
	// Season
	// Episode
	// Type
	// Image
	// Keywords
	// Status
	// ExternalLink
	// ReleaseDate
	// Created
	// Modified

	private Map<String, String> getMediaMetaDataFromFileName(String fileName) {
		String[] splitMediaMetadata = StringUtils.split(fileName, "-");

		String movieName = splitMediaMetadata[0].replaceAll("_", " ");

		// String movieYear = splitMediaMetadata[1].split("_")[0];
		//
		// String movieLang = splitMediaMetadata[1].split("_")[1];

		Map<String, String> mapMedia = new HashMap<>();

		mapMedia.put("Id", String.valueOf(DateTime.now().getMillis()));
		mapMedia.put("Name", movieName);

		DateTime releaseDate = new DateTime();

		mapMedia.put("ReleaseDate", releaseDate.now().toString());
		mapMedia.put("Created", releaseDate.now().toString());
		mapMedia.put("Modified", releaseDate.now().toString());
		return mapMedia;
	}
}
