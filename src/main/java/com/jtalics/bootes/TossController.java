package com.jtalics.bootes;

import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.net.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import javax.validation.Valid;
import com.fasterxml.jackson.databind.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.*;
import org.springframework.validation.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest; 

@Controller
public class TossController {

    // inject via application.properties
    @Value("${welcome.message}")
    private String message;
    
    /**
     * 
     */
    @RequestMapping(value={"/dashboard","/"})
    public String dashboard(Model model) {
        
        model.addAttribute("message",message);
        return "dashboard"; // the view
    }

    /**
     * 
     */
    @RequestMapping("/toss/help")
    public String help(Model model) {
        
        model.addAttribute("message",message);
        return "help"; // the view
    }

    /**
     * 
     */
    @GetMapping("/toss")
    public String toss(Model model) throws IOException {
        
        User user = new User();
        user.setFirstName("Captain");
        user.setLastName("Obvious");
        user.setEmailAddress("talafours@gmail.com");
        user.setComment("Dear TOSS:  I am interested in getting a few storage lockers emptied at $20 hour.  Thank you.");
        
        model.addAttribute(user);
        return "home";
    }
    
    /**
     * 
     */
    @PostMapping(path = "/toss/confirm")
    public String tossConfirm(@ModelAttribute User user) throws IOException{

        String response = sendPostRequest("https://www.google.com/recaptcha/api/siteverify",
            "response="+user.getRecaptchaResponse() + 
            "&secret=6LebYrIUAAAAAJS4keEdp-QBTuxTLWfOJxWAYvVX");

        ObjectMapper mapper = new ObjectMapper();
	    RecaptchaResponse recaptchaResponse = mapper.readValue(response, RecaptchaResponse.class);

        System.out.println("Score: " + recaptchaResponse.getScore());
        System.out.println("JSON: "+response);
        if (50>recaptchaResponse.getScore()) {
            return "confirm";
        }
        else {
            return "spammer";
        }
    }

    /**
     * 
     */
    @PostMapping(path = "/toss/submit")
    public String tossSubmit(@ModelAttribute User user) {

        //User user = (User)model.asMap().get("user");
        System.out.println("user: " + user);
        //System.out.println("User email address: " + user.getEmailAddress());
        System.out.println("<Form was submitted and received User. The recaptchaResponse = " + user.getRecaptchaResponse()+">");

        // Replace sender@example.com with your "From" address.
        // This address must be verified with Amazon SES.
        String FROM = "thompsonorganizationaldev@gmail.com";

        // Replace recipient@example.com with a "To" address. If your account
        // is still in the sandbox, this address must be verified.
        String TO = user.getEmailAddress();

        // The subject line for the email.
        String SUBJECT = "TOSS follow up for " + user.getFirstName();
  
        // The HTML body for the email.
        String thankyou = user.getFirstName() + " " 
            + user.getLastName()+", thank you for your interest in TOSS";
        String comment = "Your comment: " + user.getComment();
        String contactInfo = "Contact TOSS at (423) 293-5891 or ThompsonOrganizational@yahoo.com";
        String HTMLBODY = "<b>"+thankyou+"</b><p>"+comment+"<p>"+contactInfo + "<p>";

          // The email body for recipients with non-HTML email clients.
        String TEXTBODY = thankyou + "\n "+comment +"\n" + contactInfo;

        try {
            sendEmail(user,FROM,TO,SUBJECT,HTMLBODY,TEXTBODY);
        }
        catch(Exception e) {
            //model.addAttribute("exception",e.getMessage());
            return "failure";
            //"The email was not sent to User: " + user.toString()
            //    +" Error message: " + e.getMessage();
        }
        return "success"; //"SUCCESS. Email was sent to User: " + user.toString();
    }

    /**
     * 
     */
    private String readFromResource(String content) throws IOException {

        InputStream is = TossController.class.getResourceAsStream(content);
        if (is == null) {
            throw new IOException("Cannot read from: " + content);
        }
        else {
            System.out.println("<Reading content from file: " + content+">");
        }

        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    
    /**
     * 
     */
    private void sendEmail(User user, String FROM, String TO, String SUBJECT, String HTMLBODY, String TEXTBODY) {
//            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
//           + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" 
//            + "AWS SDK for Java</a>";

        // The configuration set to use for this email. If you do not want to use a
        // configuration set, comment the following variable and the 
        // .withConfigurationSetName(CONFIGSET); argument below.
        //String CONFIGSET = "ConfigSet";

        AmazonSimpleEmailService client = 
            AmazonSimpleEmailServiceClientBuilder.standard()
            // Replace US_WEST_2 with the AWS Region you're using for Amazon SES.
                .withRegion(Regions.US_WEST_2).build();
            SendEmailRequest request = new SendEmailRequest().withDestination(
                new Destination().withToAddresses(TO)).withMessage(
                    new Message().withBody(new Body().withHtml(
                        new Content().withCharset("UTF-8").withData(HTMLBODY))
                            .withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT))).withSource(FROM)
                            // Comment or remove the next line if you are not using a configuration set
                            //.withConfigurationSetName(CONFIGSET)
                            ;
            client.sendEmail(request);
    }

    /**
     * 
     */
    private String sendPostRequest(String urlString, String urlParameters) throws IOException {
        
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        URL myurl = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }

        StringBuilder content;

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }
        return content.toString();
    }
}
