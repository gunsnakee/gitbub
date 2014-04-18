package org.jiawu.image;

import java.io.File;
import java.io.IOException;

public class OcrTest {

    public static void main(String[] args) {
        String path = "/data/temp/captcha.png";
        System.out.println("ORC Test Begin......");
        try {
            String valCode = new OCR().recognizeText(new File(path), "jpg");
            System.out.println(valCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ORC Test End......");
    }

}
