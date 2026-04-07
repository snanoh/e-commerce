package com.snanoh.e_commerce.image.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class ImageService {
    private val uploadPath: Path = Paths.get("image").toAbsolutePath().normalize()

    init {
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
    }

    fun uploadImage(file: MultipartFile): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("Cannot upload empty file")
        }

        val originalFilename = file.originalFilename ?: "unknown"
        val extension = originalFilename.substringAfterLast('.', "")
        val fileName = UUID.randomUUID().toString() + if (extension.isNotEmpty()) ".$extension" else ""

        val targetLocation = uploadPath.resolve(fileName)
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        return "$baseUrl/image/$fileName"
    }
}
