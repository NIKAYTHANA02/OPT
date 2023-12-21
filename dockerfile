FROM 109420162169.dkr.ecr.ap-south-1.amazonaws.com/dropline-mobileapp-base-image-prod:prod
MAINTAINER narendran.a@kgisl.com
WORKDIR /opt/jboss-eap-7.2/bin/
RUN useradd jboss; chown -R jboss:jboss /opt/jboss-eap-7.2; yum install openssl -y;
USER jboss
CMD ./standalone.sh
ENV JAVA_HOME=/opt/jdk1.8.0_202/
ENV PATH=$PATH;/opt/jdk1.8.0_202/bin
ADD env/modules.tar.gz /opt/jboss-eap-7.2/
COPY env/standalone.xml /opt/jboss-eap-7.2/standalone/configuration/standalone.xml
COPY env/secrets.my.prod.enc /opt/secrets.my.prod.enc
COPY env/secrets.pos.prod.enc /opt/secrets.pos.prod.enc
RUN sed -i "s|MYSQL_SECRET|$(openssl aes-256-cbc -d -k CFvyw1mA5kDfFRHpsQE -a -pbkdf2 -in /opt/secrets.my.prod.enc)|g" /opt/jboss-eap-7.2/standalone/configuration/standalone.xml
RUN sed -i "s|POSTGRES_SECRET|$(openssl aes-256-cbc -d -k CFvyw1mA5kDfFRHpsQE -a -pbkdf2 -in /opt/secrets.pos.prod.enc)|g" /opt/jboss-eap-7.2/standalone/configuration/standalone.xml
#ENTRYPOINT cat /opt/jboss-eap-7.2/standalone/configuration/standalone.xml
COPY OTP-Service.war /opt/jboss-eap-7.2/standalone/deployments/
EXPOSE 8080
