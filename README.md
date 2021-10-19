Vocabulary_WebService
=====================

Web Service for Vocabulary Desktop

### docker-compose.yml requirements
- "spring-config-server" to be on the same directory of "vocabulary-api"
- update env/*.env files properties(password, etc.)

### Certificates
- keytool -genkeypair -alias vocabularyapi -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore vocabulary-api.p12 -keypass <pass> -storepass <pass> -validity 3650 -dname "CN=vocabulary-api,OU=Aaron,O=Aaron,L=Singapore,ST=Singapore,C=PH" -ext SAN=dns:vocabulary-api,dns:spring-config-server,dns:localhost,ip:127.0.0.1
- keytool -export -alias vocabularyapi -file vocabulary-api.crt -keystore vocabulary-api.p12 -storepass <pass>
- keytool -import -alias springconfigserver -file spring-config-server.crt -keystore  vocabulary-api.p12 -storepass <pass>
