package com.reddit.reddit.service;

import com.reddit.reddit.exeption.SpringRedditException;
import com.reddit.reddit.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
  private final JavaMailSender mailSender;
  private final MailContentBuilder mailContentBuilder;
  public void sendMail(NotificationEmail notificationEmail){
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setFrom("springreddit@gmail.com");
      messageHelper.setTo(notificationEmail.getRecipient());
      messageHelper.setSubject(notificationEmail.getSubject());
      messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
    };
    try {
      mailSender.send(messagePreparator);
      log.info("Activation email sent!!");
    } catch (MailException e){
      throw new SpringRedditException("Exception occured when sending mail to ");
    }
  }
}
