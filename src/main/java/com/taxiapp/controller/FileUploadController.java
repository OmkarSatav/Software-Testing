package com.taxiapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

	private static final long MAX_FILE_SIZE = 1024 * 1024;
	private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");

	@PostMapping("/id-proof")
	public ResponseEntity<?> uploadIdProof(@RequestParam("file") MultipartFile file, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		if (file.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(createErrorResponse("No file uploaded"));
		}

		if (file.getSize() > MAX_FILE_SIZE) {
			return ResponseEntity.badRequest()
					.body(createErrorResponse("File size exceeds 1MB limit"));
		}

		if (!ALLOWED_TYPES.contains(file.getContentType())) {
			return ResponseEntity.badRequest()
					.body(createErrorResponse("Only JPG and PNG files are allowed"));
		}

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("filename", file.getOriginalFilename());
		response.put("size", file.getSize());
		response.put("message", "File uploaded successfully");

		return ResponseEntity.ok(response);
	}

	private Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", message);
		return error;
	}
}

