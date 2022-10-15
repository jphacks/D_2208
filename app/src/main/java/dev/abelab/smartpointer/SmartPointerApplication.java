package dev.abelab.smartpointer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(nameGenerator = SmartPointerApplication.FQCNBeanNameGenerator.class)
public class SmartPointerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartPointerApplication.class, args);
    }

    static class FQCNBeanNameGenerator extends AnnotationBeanNameGenerator {

        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            return definition.getBeanClassName();
        }

    }

}
