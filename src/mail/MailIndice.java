package mail;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pragas.bo.ContagemBO;
import util.Private;

public class MailIndice {
	Properties props;
	Session session;
	public MailIndice() {
		props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("gabriel@varaschinagro.com.br", Private.getStringEmail());
			}
		});

	}

	public void configuraMsg(List<ContagemBO> contBO, char praga) {
		int i = 0;
		String quadrasEIndice = "";
		String especie = getTipoPragaString(praga);

		quadrasEIndice = "Data  |  Quadra  |  Índice  <br />";
		do {

			String indice = String.valueOf(contBO.get(i).getIndiceFinal());
			String quadra = String.valueOf(contBO.get(i).frasco.quadra.getNumero());
			String data = contBO.get(i).data.toString("dd/MM/yy");
			
			quadrasEIndice += "<br />"+ data + " | " + quadra + " | " + indice;

			i++;

		} while (i < contBO.size());

		String mensagem = "Atualização do índice de quadras | Espécie = " + especie + "<br /><br />" + quadrasEIndice + "<br /> <br />"
				+ "Esse e-mail é gerado automaticamente, favor não responder. <br />"
				+ "Varaschin Software - v1.1 - Licença Especial";
		mandaEmail(especie, mensagem);
	}

	private void mandaEmail(String especie, String mensagem) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("Gabriel Zulian <gabriel@varaschinagro.com.br>")); //Remetente

			Address[] toUser = InternetAddress //Destinatário(s)
					.parse("gabriel@varaschinagro.com.br, ermanojr@varaschinagro.com.br");
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Atualização Índice Pomar - " + especie + " | Varaschin Software");//Assunto
			message.setContent(mensagem, "text/html; charset=utf-8");
			message.saveChanges();
			Transport.send(message);
			System.out.println("Feito!!!");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getTipoPragaString(char praga) {
		switch (praga) {
		case 'G': return "Grapholita";
		case 'B': return "Bonagota";
		case 'M': return "Mosca da fruta";
		default: return "";
		}
	}
}
