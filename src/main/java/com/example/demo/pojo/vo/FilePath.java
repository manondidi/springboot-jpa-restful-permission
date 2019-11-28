package com.example.demo.pojo.vo;

import com.example.demo.config.property.FileServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;


@Data
public class FilePath {
    private String path;

    public static FilePath generate(String server,String path) {
        FilePath filePath = new FilePath();
        filePath.path = String.format("%s/%s", server, path);
        return filePath;

    }
}
