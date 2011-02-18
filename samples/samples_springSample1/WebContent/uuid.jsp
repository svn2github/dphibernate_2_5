<%@ page import="java.util.UUID"%>

<HTML>
 <HEAD>
  <TITLE>UUID Generation</TITLE>
 </HEAD>
 <BODY>
  <H1>UUID Generation</H1>
  UUID: <%= UUID.randomUUID().toString() %>
 </BODY>
</HTML>