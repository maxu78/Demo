#################################################
springboot 配置html页面:
加入依赖：
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
配置thymeleaf模版.
html文件放在：resources/templates下.
如果需要模版校验不严格的话 加入依赖
<dependency>
    <groupId>net.sourceforge.nekohtml</groupId>
    <artifactId>nekohtml</artifactId>
    <version>1.9.21</version>
</dependency>
配置文件中增加:
spring.thymeleaf.cache=false 
spring.thymeleaf.mode = LEGACYHTML5
html,js,css, jpg等静态资源默认放在static下面(默认放置静态资源目录:META-INF/resources > resources > static > public)
springboot访问静态html的方式(非模版)
html文件放在静态资源文件夹下(例如放在:/static/index.html).	
页面可以直接通过/index.html访问;也可以通过controller中return "redirect:/index.html"访问;也可以通过controller中return "forward:/index.html"访问;

springboot访问jsp
添加依赖：
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
	<version>1.2</version>
</dependency>
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
</dependency>
jsp文件放在/webapp/WEB-INF/page/index.jsp
配置文件中添加配置:
spring.mvc.view.prefix= /WEB-INF/page/
spring.mvc.view.suffix=.jsp
正常写controller即可(return的页面不要和其它模版命名重复)

可以通过继承WebMvcConfigurerAdapter类,重写addResourceHandlers方法实现自定义配置静态资源文件

