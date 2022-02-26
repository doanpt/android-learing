package com.example.demo_spring.controller

import com.example.demo_spring.model.Response
import com.example.demo_spring.service.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class FileUploadController {
    @Autowired
    lateinit var fileStorageService: FileStorageService

    @PostMapping("/uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile): Response {
        val fileName: String = fileStorageService.storeFile(file)
        val fileDownloadUri: String = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString()
        return Response(fileName, fileDownloadUri, file.contentType!!, file.size)
    }

    @PostMapping("/uploadMultipleFiles")
    fun uploadMultipleFiles(@RequestParam("files") files: Array<MultipartFile>): List<Response> {
        val responses = arrayListOf<Response>()
        for (file in files) {
            val response = uploadFile(file)
            responses.add(response)
        }
        return responses
    }
}