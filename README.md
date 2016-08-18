# EmissorBoletos-2.0
Servidor JAVA REST para geração de boletos bancários, Com e Sem registro.

#Uso
Após implantado em um servidor (Glassfish, por exemplo), deve-se configurar a conexão com o banco de dados (descrita no Persistence.xml) e
criar a estrutura e alguns registros nas tabelas previstas na pasta MODEL deste projeto. Após isso, as requisições podem ser feitas via GET.
Para <strong>criar</strong> um boleto, por exemplo, pode-se requisitar a URL: localhost:8080/api/boleto/create/TITULOID, onde TITULOID corresponde
ao ID da class Titulo, que deve existir no Banco de Dados. O retorno do método será um RESPONSE do próprio Boleto (Em PDF).
Há ainda mais dois métodos: <strong>getPath</strong> -> que retornará o local onde foi/será gravado o PDF do boleto. E <strong>open</strong> ->
Que tem o comportamento parecido com o <strong>create</strong> mas apenas retorna o boleto se existente (sem tentar criá-lo).

#Framworks
MAVEN<BR>
JPA<BR>
JAVAX-WS<BR>

#Biblioteca para geração de Boletos
<a href="http://jrimum.org/bopepo/">Jrimum - Bopepo</a>

#Dúvidas Frequentes
<a href="http://jrimum.org/bopepo/wiki/Componente/BancosSuportados"> Bancos Suportados (Jrimum)</a><BR>
<a href="http://jrimum.org/bopepo/wiki/Componente/AcervoDeTemplates"> Templates (Jrimum)</a><BR>
<a href="http://jrimum.org/bopepo/wiki/Componente/Documentacao/Tutoriais"> Tutoriais (Jrimum)</a><BR>


