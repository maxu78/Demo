#################################################
springboot ����htmlҳ��:
����������
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
����thymeleafģ��.
html�ļ����ڣ�resources/templates��.
�����Ҫģ��У�鲻�ϸ�Ļ� ��������
<dependency>
    <groupId>net.sourceforge.nekohtml</groupId>
    <artifactId>nekohtml</artifactId>
    <version>1.9.21</version>
</dependency>
�����ļ�������:
spring.thymeleaf.cache=false 
spring.thymeleaf.mode = LEGACYHTML5
html,js,css, jpg�Ⱦ�̬��ԴĬ�Ϸ���static����(Ĭ�Ϸ��þ�̬��ԴĿ¼:META-INF/resources > resources > static > public)
springboot���ʾ�̬html�ķ�ʽ(��ģ��)
html�ļ����ھ�̬��Դ�ļ�����(�������:/static/index.html).	
ҳ�����ֱ��ͨ��/index.html����;Ҳ����ͨ��controller��return "redirect:/index.html"����;Ҳ����ͨ��controller��return "forward:/index.html"����;

springboot����jsp
����������
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
	<version>1.2</version>
</dependency>
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
</dependency>
jsp�ļ�����/webapp/WEB-INF/page/index.jsp
�����ļ�����������:
spring.mvc.view.prefix= /WEB-INF/page/
spring.mvc.view.suffix=.jsp
����дcontroller����(return��ҳ�治Ҫ������ģ�������ظ�)
