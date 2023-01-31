package com.qr.controller;

import com.google.zxing.WriterException;
import com.qr.services.QRCodeGenerator;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

import javax.imageio.ImageIO;

@Controller
public class MainController {

   private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/QRCode.png";

       
//    url // http://localhost:8000/qrcode?url=https://github.com/DarkSharkAsh
//    @GetMapping("/{url}")
   @GetMapping("/qrcode")
    @ResponseBody
//    public   ResponseEntity<byte[]>  getQRCode1(@PathVariable("url") String url) throws IOException{
    public   ResponseEntity<byte[]>  getQRCode1(@RequestParam(name = "url" ) String url) throws IOException{
    	String medium=url;
        String github=url;

        byte[] image = new byte[0];	
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(medium,250,250);

            // Generate and Save Qr Code Image in static/image folder
            QRCodeGenerator.generateQRCodeImage(github,250,250,QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage bufferedImage = ImageIO.read(in);
        File outputFile = new File("image.jpg");
        ImageIO.write(bufferedImage, "jpg", outputFile);
        

        File file = new File("image.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.toString());
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        
        //return Files.readAllBytes(file.toPath());
        return new ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);

        
    }
    
    
    
    
    
}
