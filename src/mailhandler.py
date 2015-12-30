import config
import smtplib

def send(subject, body):
    smtp = smtplib.SMTP(config.SMTP_SERVER, config.SMTP_PORT)
    smtp.starttls()
    smtp.login(config.SMTP_USERNAME, config.SMTP_PASSWORD)
    smtp.sendmail(config.MAIL_FROM, config.MAIL_TO, "Subject: " +  subject + "\n" + body)
    smtp.quit()

if __name__ == "__main__":
    send("ACS Logging Test - Status", "Test message from ACS Logging Test Mailhandler")
