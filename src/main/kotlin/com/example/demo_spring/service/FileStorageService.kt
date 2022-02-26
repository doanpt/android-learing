package com.example.demo_spring.service

import com.example.demo_spring.exception.FileNotFoundException
import com.example.demo_spring.exception.FileStorageException
import com.example.demo_spring.property.FileStorageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService @Autowired constructor(fileStorageProperties: FileStorageProperties) {
    private val fileStorageLocation: Path

    init {
        fileStorageLocation = Paths.get(fileStorageProperties.uploadDir).toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: IOException) {
            throw FileStorageException("Could not create the directory where the uploaded files will be stored.", e)
        }
    }

    fun storeFile(file: MultipartFile): String {
        val fileName = StringUtils.cleanPath(file.originalFilename!!)
        return try {
            if (fileName.contains("..")) {
                throw FileStorageException("Sorry! Filename contains invalid path sequence $fileName")
            }
            val targetLocation = fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        } catch (e: IOException) {
            throw FileStorageException("Could not store file $fileName. Please try again!", e)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw FileNotFoundException("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw FileNotFoundException("File not found $fileName", ex)
        }
    }
}