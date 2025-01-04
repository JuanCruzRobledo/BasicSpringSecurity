package org.juanrobledo.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Retorna un SecurityFilterChain es una interfaz que usa spring para configurar la seguridad
    //Aqui se configura nuestra aplicacion
    //Configuracion uno
    //@Bean
    /*public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf()//Cross-Site Request Forgery Es una vulnerabilidad que intercepta una request para poder realizar una accion en un endpoint como cambiar una contraseña
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("v1/index2").permitAll() //Las peticiones hacia los endpoint matcheados se permite el acesso y no necesita estar autenticado
                    .anyRequest().authenticated() //Cualquier otra peticion a otro endpoint necesita estar autenticado
                .and()
                .formLogin().permitAll() //Le permite a todos acceder al formulario
                .and()
                .build();
    }*/

    //Utilizada
    //Configuracion dos
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf ->{
                })
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("v1/index2").permitAll(); //Cualquier request al los endpoint mandados() pueden ser ingresados por todos sin autenticacion previa
                    auth.anyRequest().authenticated(); //Cualquier otra request necesita que esté autenticado
                })
                .formLogin(form ->{
                    form.failureUrl("/login?error=true");
                    form.permitAll(); //Permite el formulario de seguridad a todos
                    form.successHandler(successHandler()); //Url que se redirige despues de iniciar sesion en el form
                })
                .sessionManagement(session ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS); //crea una session para poder guardar informacion : ALWAYS - IF-REQUIRED - NEVER - STATELESS
                    session.invalidSessionUrl("/login?error=true"); //Si la session es invalida se redirige
                    session.maximumSessions(1); //Cuantas sessiones puede tener cada usuario
                    session.sessionFixation() //Es otra vulnerabilidad donde se apropian del id de la session
                            .migrateSession(); //Nos proteje y cuando se detecta un ataque de fijacion de session, genera otro id y migra los datos de la session
                            //.newSession(); //Nos proteje pero crea una nueva session
                            //.none(); //Nos quita la proteccion
                    //session.sessionRegistry(sessionRegistry());
                })
                .httpBasic(httpBasic ->{ //Es una autenticacion basica donde en la peticion GET PUT ...  debemos enviar las credenciales
                                        //Si no nos pedira iniciar session
                })
                .build();
    }

    public AuthenticationSuccessHandler successHandler(){
        return (request, response, auth) -> {
          response.sendRedirect("/v1/index");
        };
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}
