package es.uji.ei1027.SgOVI;

import es.uji.ei1027.SgOVI.model.Rol;
import es.uji.ei1027.SgOVI.model.RolInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RolInterceptor(Rol.USUARIOOVI))
                .addPathPatterns("/usuarioOVI/**");

        registry.addInterceptor(new RolInterceptor(Rol.TECNICOOVI))
                .addPathPatterns("/tecnico/**");

        registry.addInterceptor(new RolInterceptor(Rol.ASISTENTE))
                .addPathPatterns("/asistentePersonal/**");

        registry.addInterceptor(new RolInterceptor(Rol.FORMADOR))
                .addPathPatterns("/formador/**");

        registry.addInterceptor(new RolInterceptor(Rol.USUARIOOVI))
                .addPathPatterns("/peticionAP/**");
    }
}