package com.rick.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    //metodo responsavel por fazer o upload do arquivo que eu passar aqui
    public URI uploadFile(MultipartFile multipartFile)  {
        try {
            String fileName = multipartFile.getOriginalFilename(); //Pegar o nome do arquivo
            InputStream is = multipartFile.getInputStream(); //Encapsula o processamento de leitura a partir de uma origem "arquivo"
            String contentType = multipartFile.getContentType(); //Pegar o tipo do arquivo que foi enviado
            return uploadFile(is, fileName, contentType);
        } catch (IOException e) {
            throw new RuntimeException("Erro de IO: " + e.getMessage());
        }
    }

    public URI uploadFile(InputStream is, String fileName, String contentType) {
        try {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        LOG.info("Iniciando upload");
        s3client.putObject(bucketName, fileName, is, metadata);
        LOG.info("Upload finalizado");

        return s3client.getUrl(bucketName, fileName).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao converter URL para URI!");
        }
    }
}
