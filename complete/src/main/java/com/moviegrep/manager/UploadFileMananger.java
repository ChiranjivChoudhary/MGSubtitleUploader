package com.moviegrep.manager;

import java.io.File;
import java.io.FileOutputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.client.ApiClient;
import io.swagger.client.api.MediaApi;

@Component
public class UploadFileMananger {

	ApiClient apiClient;
	MediaApi mediaApi;

	@PostConstruct
	private void intialise() {

		String basePath = "http://localhost:49994";

		apiClient = new ApiClient();
		apiClient.setBasePath(basePath);
		apiClient.setConnectTimeout(990000);
		mediaApi = new MediaApi();
		mediaApi.setApiClient(apiClient);
	}

	public String uploadFile(MultipartFile fileUpload) {

		String response = null;

		try {

			String fileName = StringUtils.stripFilenameExtension(fileUpload.getOriginalFilename());

			File convFile = new File(fileUpload.getOriginalFilename());
			convFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(fileUpload.getBytes());
			fos.close();

			String media = getMediaObjectAsJsonString(fileName);

			mediaApi.postMedia("1", media, convFile);

		} catch (Exception e) {
			response = e.getMessage();
		}
		return response;
	}

	public String uploadFiles(String directoryPath) {
		File dir = new File(directoryPath);
		File[] directoryListing = dir.listFiles();
		String response = null;
		if (directoryListing != null) {
			for (File child : directoryListing) {

				try {
					String fileName = StringUtils.stripFilenameExtension(child.getName());

					String media = getMediaObjectAsJsonString(fileName);

					mediaApi.postMedia("1", media, child);

				} catch (

				Exception e) {
					response = e.getMessage();
				}
			}
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

	// private String getMediaMetaDataFromFileName(String fileName) throws
	// JsonProcessingException {
	// String[] splitMediaMetadata = StringUtils.split(fileName, "-");
	//
	// String movieName = splitMediaMetadata[0].replaceAll("_", " ");
	//
	// String movieYear = splitMediaMetadata[1].split("_")[0];
	// //
	// // String movieLang = splitMediaMetadata[1].split("_")[1];
	//
	// Media media = new Media();
	//
	// media.setName(movieName);
	// media.setDescription(String.format("%s is a fantastic movie.", movieName));
	//
	// // DateTime releaseDate = new DateTime(Integer.parseInt(movieYear), 1, 1, 0,
	// 0);
	// //
	// // DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	// //
	// // DateTime parseDateTime =
	// dateFormatter.parseDateTime(releaseDate.toString());
	//
	// DateTime parseDateTime = DateTime.now();
	// media.setReleaseDate(parseDateTime);
	//
	// // Object to Json String
	// ObjectMapper mapper = new ObjectMapper();
	// mapper.setSerializationInclusion(Include.NON_NULL);
	// mapper.setSerializationInclusion(Include.NON_EMPTY);
	// // Object to JSON in file
	// String mediaJsonInString = mapper.writeValueAsString(media);
	//
	// return mediaJsonInString;
	// }

	private String getMediaObjectAsJsonString(String fileName) {
		String[] splitMediaMetadata = StringUtils.split(fileName, "-");

		String movieName = splitMediaMetadata[0].replaceAll("_", " ");

		String movieYear = splitMediaMetadata[1].split("_")[0];

		// String movieLang = splitMediaMetadata[1].split("_")[1];

		// {"Name":"Alien", "Description":"Thriller", "ReleaseDate":"1/1/1979"}

		String mediaJson = String.format(
				"{\"Name\":\"%s\", \"Description\":\"%s is a awesome movie\", \"ReleaseDate\":\"1/1/%s\"}", movieName,
				movieName, movieYear);
		return mediaJson;

	}

}
