package com.moviegrep.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.lang.ref.Reference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moviegrep.manager.UploadFileMananger;

@RestController

public class MoviegrepSubtitleUploadController {
	@Autowired
	UploadFileMananger ufManager;

	@GetMapping("/home")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping("/UploadFile")
	public ResponseEntity<String> uploadSubTitleFile(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		String response = ufManager.uploadFile(file);
		if (response == null)
			return ResponseEntity.ok("File uploaded successfully!!!");
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
