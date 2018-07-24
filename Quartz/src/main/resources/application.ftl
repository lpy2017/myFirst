server.context-path=/quartz
server.port=${port?if_exists }

masterRest=http://${masterip?if_exists}:5091/masterl/ws

logging.level.tk.mybatis=TRACE
druid.url=jdbc:mysql://${dbip?if_exists}:${dbport?if_exists}/${dbname?if_exists}?useUnicode=true&characterEncoding=UTF8
druid.driver-class=com.mysql.jdbc.Driver
druid.username=${dbuser?if_exists}
druid.password=${dbpassword?if_exists}

druid.initial-size=5
druid.min-idle=10
druid.max-active=50
druid.test-on-borrow=true

mybatis.mappersLocation=classpath:mapper/*.xml
mybatis.typeAliasesPackage=com.dc.appengine.quartz.entity
spring.http.multipart.maxFileSize=100Mb
spring.http.multipart.maxRequestSize=1000Mb

rollback.threadPoolSize=100