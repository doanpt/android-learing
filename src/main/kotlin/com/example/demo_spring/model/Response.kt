package com.example.demo_spring.model

data class Response(
        var fileName: String,
        var fileDownloadUri: String,
        var fileType: String,
        var size: Long = 0
)
