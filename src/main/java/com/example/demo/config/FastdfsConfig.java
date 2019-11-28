package com.example.demo.config;
import com.github.tobato.fastdfs.FdfsClientConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@Data
public class FastdfsConfig {
    private String fileServe;
}

