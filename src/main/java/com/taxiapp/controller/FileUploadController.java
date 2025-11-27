package com.taxiapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
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

		// Server-side validation removed for bypass testing demonstration
		// Accept any file regardless of size, type, or content
		// All validation is now client-side only

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("filename", file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown");
		response.put("size", file.getSize());
		response.put("contentType", file.getContentType());
		response.put("message", "File uploaded successfully (bypass testing mode)");

		return ResponseEntity.ok(response);
	}

	private Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", message);
		return error;
	}
}

