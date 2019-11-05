package com.cipher.cipher.controller;

import com.cipher.cipher.dao.SentenceDao;
import com.cipher.cipher.entity.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import java.io.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Controller
public class SentenceController {

    @Autowired
    private SentenceDao dao;
    private SecretKey key;
    private String actualText = "";
    private String actualCode = "";

    @RequestMapping("/index")
    public String indexPage() {
        return "index";
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.GET)
    public String indexPage(Model m) {
        m.addAttribute("sentence", new Sentence());
        return "result";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST) //szyfrowanie tekstu z pliku
    public String uploadPage(@ModelAttribute(value = "sentence") Sentence sentence, MultipartFile file)
            throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {

        String chooseFile = file.getOriginalFilename();
        String originalPath = "c:/Users/Olcia/Desktop/" + chooseFile;

        readFile(originalPath); //wpisuje tekst do actualCode
        sentence.setText(actualCode);

        String cipherPath = "c:/Users/Olcia/Desktop/szyfrogram.cfr";
        String decryptPath = "c:/Users/Olcia/Desktop/odszyfrowany.txt";

        generateKey();
        encrypt(originalPath, cipherPath);
        decrypt(cipherPath, decryptPath);
        readFile(cipherPath); //wpisuje tekst do actualCode
        sentence.setCode(actualCode); //tu wpisac zakodowane
        readFile(decryptPath);
        sentence.setEncode(actualCode);
        dao.save(sentence); //zapis bazy

        return "result";
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST) //szyfrowanie tekstu z pola
    public String indexPagePOST(@ModelAttribute(value = "sentence") Sentence sentence) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {

        actualText = sentence.getText();
        saveToFile(); //zapisuje do pliku ciag actualText
        String originalPath = "c:/Users/Olcia/Desktop/strona.txt";
        String cipherPath = "c:/Users/Olcia/Desktop/szyfrogram.cfr";
        String decryptPath = "c:/Users/Olcia/Desktop/odszyfrowany.txt";

        generateKey();
        encrypt(originalPath, cipherPath); //tworzy plik do szyfrowania z szyfrem
        decrypt(cipherPath, decryptPath); //tworzy plik zdeszyfrowany z odkodowanym tektem
        readFile(cipherPath); //do actualCode wpisuje ciag zaszyfrowany
        sentence.setCode(actualCode); //wpisuje zaszyfrowane do bazy
        readFile(decryptPath); //do actualCode wpisuje ciag odszyfrowany
        sentence.setEncode(actualCode); //wpisuje odszyfrowane do bazy
        dao.save(sentence); //zapis bazy

        return "result";
    }

    private void generateKey() throws NoSuchAlgorithmException {

        KeyGenerator keygen;
        keygen = KeyGenerator.getInstance("DESede");
        key = keygen.generateKey();
    }

    private void saveToFile() throws FileNotFoundException {

        PrintWriter save = new PrintWriter("c:/Users/Olcia/Desktop/strona.txt");
        save.println(actualText);
        save.close();
    }

    private void readFile(String cipherPath) {
        actualCode = "";
        try {
            FileReader fileReader = new FileReader(cipherPath);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                //System.out.println(line);
                actualCode += line;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void encrypt(String originalPath, String cipherPath) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher c;
        FileInputStream in = new FileInputStream(originalPath);

        c = Cipher.getInstance("DESede");
        c.init(Cipher.ENCRYPT_MODE, key);

        FileOutputStream out = new FileOutputStream(cipherPath); //do zapisu
        CipherOutputStream cos = new CipherOutputStream(out, c); //utworzenie strumienia szyfrującego
        byte[] bufor = new byte[4096];
        int bajty_odczytane;
        while ((bajty_odczytane = in.read(bufor)) != -1) {
            cos.write(bufor, 0, bajty_odczytane);
        }
        out.write(c.doFinal());
    }

    private void decrypt(String cipherPath, String decryptPath) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c;
        FileInputStream in = new FileInputStream(cipherPath); //utworzenie i inicjalizacja silnika algorytmu deszyfrującego
        c = Cipher.getInstance("DESede");
        c.init(Cipher.DECRYPT_MODE, key);
        FileOutputStream out = new FileOutputStream(decryptPath); //Odszyfrowanie i zapis tekstu jawnego do pliku
        byte[] bufor = new byte[4096];
        int bajty_odczytane;
        while ((bajty_odczytane = in.read(bufor)) != -1) {
            out.write(c.update(bufor, 0, bajty_odczytane));
        }
        out.write(c.doFinal());
    }
}
