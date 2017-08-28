package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"demo",""})

public class DemoApplication {
	public FileReader getFileReader() {
		return fileReader;
	}

	static FileReader fileReader = new FileReader("file.txt");
	static FileReader fileReader2 = new FileReader("errorFile.txt");
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
        new FileWriter("f1.txt").write(fileReader);
        new FileWriter("f2.txt").write(fileReader2);

    }
	public String example(){
		return "ex	22";
	}




}
